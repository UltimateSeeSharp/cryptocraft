package net.ultimateseesharp.cryptocraft.miner.randomx;

import net.ultimateseesharp.cryptocraft.miner.ffm.Ffm;

import java.lang.invoke.MethodHandle;

/**
 * Downcall handles for the {@code randomx.h} functions the miner uses. One instance owns
 * the handles for one loaded copy of the library.
 *
 * <p>Handles are erased to {@code Object} parameters so this code need not name FFM types;
 * see {@link Ffm}. Calls through them are ordinary {@code MethodHandle} invocations.
 */
final class RandomXNative {
    final MethodHandle getFlags;
    final MethodHandle allocCache;
    final MethodHandle initCache;
    final MethodHandle releaseCache;
    final MethodHandle allocDataset;
    final MethodHandle datasetItemCount;
    final MethodHandle initDataset;
    final MethodHandle releaseDataset;
    final MethodHandle createVm;
    final MethodHandle destroyVm;
    final MethodHandle calculateHash;

    RandomXNative(Object lookup) {
        Object addr = Ffm.ADDRESS;
        Object i32 = Ffm.JAVA_INT;
        Object i64 = Ffm.JAVA_LONG;

        getFlags = bind(lookup, "randomx_get_flags", Ffm.descriptorOf(i32));
        allocCache = bind(lookup, "randomx_alloc_cache", Ffm.descriptorOf(addr, i32));
        initCache = bind(lookup, "randomx_init_cache", Ffm.descriptorOfVoid(addr, addr, i64));
        releaseCache = bind(lookup, "randomx_release_cache", Ffm.descriptorOfVoid(addr));
        allocDataset = bind(lookup, "randomx_alloc_dataset", Ffm.descriptorOf(addr, i32));
        datasetItemCount = bind(lookup, "randomx_dataset_item_count", Ffm.descriptorOf(i64));
        initDataset = bind(lookup, "randomx_init_dataset", Ffm.descriptorOfVoid(addr, addr, i64, i64));
        releaseDataset = bind(lookup, "randomx_release_dataset", Ffm.descriptorOfVoid(addr));
        createVm = bind(lookup, "randomx_create_vm", Ffm.descriptorOf(addr, i32, addr, addr));
        destroyVm = bind(lookup, "randomx_destroy_vm", Ffm.descriptorOfVoid(addr));
        calculateHash = bind(lookup, "randomx_calculate_hash", Ffm.descriptorOfVoid(addr, addr, i64, addr));
    }

    private static MethodHandle bind(Object lookup, String name, Object descriptor) {
        Object symbol = Ffm.find(lookup, name)
                .orElseThrow(() -> new IllegalStateException("RandomX library exports no " + name));
        return Ffm.asObjectHandle(Ffm.downcallHandle(symbol, descriptor));
    }

    int getFlags() {
        try {
            return (int) getFlags.invoke();
        } catch (Throwable t) {
            throw sneaky(t);
        }
    }

    static RuntimeException sneaky(Throwable t) {
        if (t instanceof RuntimeException e) {
            return e;
        }
        if (t instanceof Error e) {
            throw e;
        }
        return new IllegalStateException(t);
    }
}
