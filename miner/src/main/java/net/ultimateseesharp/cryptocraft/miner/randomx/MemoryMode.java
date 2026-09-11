package net.ultimateseesharp.cryptocraft.miner.randomx;

import java.lang.management.ManagementFactory;

/**
 * RandomX memory mode. Fast mode allocates a ~2080MB dataset and is roughly 5x faster;
 * light mode needs only the 256MB cache. {@code docs/MINING.md} §5.
 */
public enum MemoryMode {
    LIGHT,
    FAST;

    /** Dataset plus the cache it is built from. The dataset itself is 2080MB. */
    public static final long FAST_MODE_BYTES = 2336L * 1024 * 1024;

    /** Headroom left for Minecraft and the OS when choosing automatically. */
    public static final long RESERVED_BYTES = 4096L * 1024 * 1024;

    /**
     * Picks a mode from free <em>physical</em> memory — the dataset is a native
     * allocation, so the Java heap limit says nothing about whether it will fit.
     * Falls back to light mode when the platform will not report memory.
     */
    public static MemoryMode auto() {
        return auto(freePhysicalBytes());
    }

    static MemoryMode auto(long freePhysicalBytes) {
        return freePhysicalBytes >= FAST_MODE_BYTES + RESERVED_BYTES ? FAST : LIGHT;
    }

    /** Free physical memory, or -1 when the platform does not expose it. */
    static long freePhysicalBytes() {
        if (ManagementFactory.getOperatingSystemMXBean()
                instanceof com.sun.management.OperatingSystemMXBean os) {
            return os.getFreeMemorySize();
        }
        return -1;
    }

    public boolean isFast() {
        return this == FAST;
    }
}
