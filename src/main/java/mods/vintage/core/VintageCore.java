package mods.vintage.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.commands.CommandGM;
import mods.vintage.core.platform.events.ClientTickEvent;
import mods.vintage.core.platform.lang.LangManager;
import mods.vintage.core.platform.lang.LocalizationProvider;
import net.minecraftforge.common.Configuration;

import java.util.logging.Logger;

@LocalizationProvider
@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION)
public class VintageCore {

    public static final Logger LOGGER = Logger.getLogger(Refs.LOG_NAME);

    public static Configuration CONFIG;

    @LocalizationProvider.List(modId = Refs.ID)
    public static String[] LANGS;

    public VintageCore() {
        LOGGER.setParent(FMLLog.getLogger());
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        CONFIG = ConfigHelper.getConfigFor(Refs.ID);
        CONFIG.load();
        LANGS = ConfigHelper.getLocalizations(CONFIG, new String[] { "en_US", "ru_RU" }, Refs.ID);
        if (CONFIG.hasChanged()) CONFIG.save();
        TickRegistry.registerTickHandler(new ClientTickEvent(), Side.CLIENT);
        LangManager.INSTANCE.processLocalizationProviders(e.getAsmData());
    }

    @Mod.ServerStarting
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandGM());
    }
}
