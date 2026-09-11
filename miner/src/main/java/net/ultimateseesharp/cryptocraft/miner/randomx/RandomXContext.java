package net.ultimateseesharp.cryptocraft.miner.randomx;

import net.ultimateseesharp.cryptocraft.miner.ffm.Ffm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Owns the RandomX cache and, in fast mode, the dataset — the expensive state shared by
 * every hashing thread. Create one per key (for Monero, the seed hash of the current
 * block), then a {@link RandomXVm} per thread.
 *
 * <p>Close the VMs before closing the context.
 */
public final class RandomXContext implements AutoCloseable {
    private final AutoCloseable arena;
    private final RandomXNative natives;
    private final MemoryMode mode;
    private final int flags;
    private final Object cache;
    private final Object dataset;
    private boolean closed;

    private RandomXContext(AutoCloseable arena, RandomXNative natives, MemoryMode mode, int flags,
                           Object cache, Object dataset) {
        this.arena = arena;
        this.natives = natives;
        this.mode = mode;
        this.flags = flags;
        this.cache = cache;
        this.dataset = dataset;
    }

    /** Creates a context for {@code key}, choosing the memory mode automatically. */
    public static RandomXContext create(byte[] key) {
        return create(key, MemoryMode.auto());
    }

    /**
     * Creates a context for {@code key} in {@code mode}. Fast mode builds the ~2GB dataset
     * here, which takes seconds; light mode returns quickly.
     */
    public static RandomXContext create(byte[] key, MemoryMode mode) {
        AutoCloseable arena = Ffm.sharedArena();
        try {
            RandomXNative natives = new RandomXNative(NativeLibrary.load(arena));
            int flags = natives.getFlags() | (mode.isFast() ? RandomXFlag.FULL_MEM.bits() : 0);

            Object cache = allocCache(natives, flags);
            initCache(natives, cache, key);

            Object dataset = Ffm.NULL_SEGMENT;
            if (mode.isFast()) {
                dataset = allocDataset(natives, flags);
                initDataset(natives, dataset, cache);
            }
            return new RandomXContext(arena, natives, mode, flags, cache, dataset);
        } catch (RuntimeException | Error e) {
            closeQuietly(arena);
            throw e;
        }
    }

    /** A VM for one hashing thread. The caller closes it. */
    public RandomXVm newVm() {
        if (closed) {
            throw new IllegalStateException("Context is closed");
        }
        try {
            Object vm = natives.createVm.invoke(flags, cache, dataset);
            if (Ffm.isNull(vm)) {
                throw new IllegalStateException("randomx_create_vm returned NULL (flags=" + flags + ")");
            }
            return new RandomXVm(this, vm);
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        }
    }

    public MemoryMode mode() {
        return mode;
    }

    public int flags() {
        return flags;
    }

    RandomXNative natives() {
        return natives;
    }

    private static Object allocCache(RandomXNative natives, int flags) {
        try {
            Object cache = natives.allocCache.invoke(flags);
            if (Ffm.isNull(cache)) {
                throw new IllegalStateException("randomx_alloc_cache returned NULL (flags=" + flags + ")");
            }
            return cache;
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        }
    }

    private static void initCache(RandomXNative natives, Object cache, byte[] key) {
        AutoCloseable scratch = Ffm.confinedArena();
        try {
            Object keySegment = Ffm.allocate(scratch, key.length);
            Ffm.copyToNative(key, 0, keySegment, 0, key.length);
            natives.initCache.invoke(cache, keySegment, (long) key.length);
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        } finally {
            closeQuietly(scratch);
        }
    }

    private static Object allocDataset(RandomXNative natives, int flags) {
        try {
            Object dataset = natives.allocDataset.invoke(flags);
            if (Ffm.isNull(dataset)) {
                throw new IllegalStateException(
                        "randomx_alloc_dataset returned NULL — not enough memory for fast mode");
            }
            return dataset;
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        }
    }

    /** Builds the dataset across every available core; single-threaded it takes minutes. */
    private static void initDataset(RandomXNative natives, Object dataset, Object cache) {
        long itemCount;
        try {
            itemCount = (long) natives.datasetItemCount.invoke();
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        }

        int workers = Math.max(1, Runtime.getRuntime().availableProcessors());
        long perWorker = itemCount / workers;
        List<Thread> threads = new ArrayList<>(workers);
        List<Throwable> failures = new ArrayList<>();
        for (int i = 0; i < workers; i++) {
            long start = i * perWorker;
            long count = (i == workers - 1) ? itemCount - start : perWorker;
            threads.add(Thread.ofPlatform().name("randomx-dataset-" + i).start(() -> {
                try {
                    natives.initDataset.invoke(dataset, cache, start, count);
                } catch (Throwable t) {
                    synchronized (failures) {
                        failures.add(t);
                    }
                }
            }));
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted building the RandomX dataset", e);
            }
        }
        if (!failures.isEmpty()) {
            throw new IllegalStateException("Building the RandomX dataset failed", failures.get(0));
        }
    }

    /** Convenience for a string key, as the upstream test vectors use. */
    public static byte[] key(String key) {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    static void closeQuietly(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            throw RandomXNative.sneaky(e);
        }
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        try {
            if (!Ffm.isNull(dataset)) {
                natives.releaseDataset.invoke(dataset);
            }
            natives.releaseCache.invoke(cache);
        } catch (Throwable t) {
            throw RandomXNative.sneaky(t);
        } finally {
            closeQuietly(arena);
        }
    }
}
