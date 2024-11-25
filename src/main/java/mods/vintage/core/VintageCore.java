package mods.vintage.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;

import java.util.logging.Logger;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION)
public class VintageCore {

    public static final Logger LOGGER = Logger.getLogger(Refs.LOG_NAME);

    public VintageCore() {
        LOGGER.setParent(FMLLog.getLogger());
    }
}
