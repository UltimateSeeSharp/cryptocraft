package net.ultimateseesharp.cryptocraft.miner.randomx;

import net.ultimateseesharp.cryptocraft.miner.ffm.Ffm;

/**
 * A RandomX virtual machine. Hashing is single-threaded per VM — give each worker thread
 * its own, sharing the {@link RandomXContext} that owns the cache and dataset.
 *
 * <p>Not thread-safe. Close it when done, before closing the context.
 */
public final class RandomXVm implements AutoCloseable {
    public static final int HASH_SIZE = 32;

    private final RandomXContext context;
    private final AutoCloseable arena;
    private final Object vm;
    private final Object output;
    private boolean closed;

    RandomXVm(RandomXContext context, Object vm) {
        this.context = context;
        this.arena = Ffm.sharedArena();
        this.vm = vm;
        this.output = Ffm.allocate(arena, HASH_SIZE);
    }

    /** Hashes {@code input} and returns the 32-byte result. */
    public byte[] hash(byte[] input) {
        byte[] result = new byte[HASH_SIZE];
        hash(input, result);
        return result;
    }

    /** Hashes {@code input} into {@code target}, which must be {@link #HASH_SIZE} bytes. */
    public void hash(byte[] input, byte[] target) {
        if (target.length != HASH_SIZE) {
            throw new IllegalArgumentException("Target must be " + HASH_SIZE + " bytes, was " + target.length);
        }
        if (closed) {
            throw new IllegalStateException("VM is closed");
        }
        AutoCloseable scratch = Ffm.confinedArena();
        try {
            Object in = Ffm.allocate(scratch, input.length);
            Ffm.copyToNative(input, 0, in, 0, input.length);
            context.natives().calculateHash.invoke(vm, in, (long) input.length, output);
            Ffm.copyFromNative(output, 0, target, 0, HASH_SIZE);
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        } finally {
            RandomXContext.closeQuietly(scratch);
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            context.natives().destroyVm.invoke(vm);
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        } finally {
            RandomXContext.closeQuietly(arena);
        }
    }
}
