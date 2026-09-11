package net.ultimateseesharp.cryptocraft;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(CryptoCraft.MODID)
public class CryptoCraft {
    public static final String MODID = "cryptocraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CryptoCraft(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
