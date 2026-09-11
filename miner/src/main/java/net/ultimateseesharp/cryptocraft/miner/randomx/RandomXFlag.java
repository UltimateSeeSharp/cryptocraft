package net.ultimateseesharp.cryptocraft.miner.randomx;

/** Mirrors {@code randomx_flags} in {@code randomx.h}. */
public enum RandomXFlag {
    DEFAULT(0),
    LARGE_PAGES(1),
    HARD_AES(2),
    FULL_MEM(4),
    JIT(8),
    SECURE(16),
    ARGON2_SSSE3(32),
    ARGON2_AVX2(64);

    private final int bits;

    RandomXFlag(int bits) {
        this.bits = bits;
    }

    public int bits() {
        return bits;
    }

    public boolean isSetIn(int flags) {
        return (flags & bits) == bits;
    }

    public static int combine(RandomXFlag... flags) {
        int bits = 0;
        for (RandomXFlag flag : flags) {
            bits |= flag.bits;
        }
        return bits;
    }
}
