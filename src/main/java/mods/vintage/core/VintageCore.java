package mods.vintage.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.commands.CommandGM;
import mods.vintage.core.platform.config.ConfigHandler;
import mods.vintage.core.platform.config.VintageConfig;
import mods.vintage.core.platform.events.ClientTickEvent;
import mods.vintage.core.platform.lang.ILangProvider;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraftforge.common.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptedMinecraftVersions = Refs.MC_VERSION)
public class VintageCore implements ILangProvider {

    public static final Logger LOGGER = Logger.getLogger(Refs.LOG_NAME);

    public static Configuration CONFIG;
    public static String[] LANGS;
    public ConfigHandler configHandler = new ConfigHandler(Refs.ID);
    public VintageConfig config;

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
        LangManager.THIS.registerLangProvider(this);
        this.config = new VintageConfig(ConfigHelper.getConfigFileFor("TestVintageCore"));
        this.configHandler.initIDs(this.config);
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        this.configHandler.confirmIDs(this.config);
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent e) {
        this.configHandler.confirmOwnership(this.config);
    }

    @Mod.ServerStarting
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandGM());
    }

    @Override
    public String getModid() {
        return Refs.ID;
    }

    @Override
    public List<String> getLocalizationList() {
        return Arrays.asList(LANGS);
    }
}
