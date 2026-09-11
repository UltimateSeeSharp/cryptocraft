package net.ultimateseesharp.cryptocraft;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Placeholder spec so the mod registers a config. The real settings are step 2.3
 * ({@code docs/MINING.md} §6); only the disable switch is wired up so far.
 */
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DISABLE_REAL_MINING = BUILDER
            .comment("Disable real mining entirely. Pack and server switch; overrides all in-game controls.")
            .define("disableRealMining", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
