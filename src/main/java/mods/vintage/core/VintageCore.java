package mods.vintage.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.vintage.core.helpers.ConfigHelper;
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

    public VintageCore() {
        LOGGER.setParent(FMLLog.getLogger());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        CONFIG = ConfigHelper.getConfigFor(Refs.ID);
        CONFIG.load();
        LANGS = ConfigHelper.getLocalizations(CONFIG, new String[] { "en_US", "ru_RU" }, Refs.ID);
        if (CONFIG.hasChanged()) CONFIG.save();
        TickRegistry.registerTickHandler(new ClientTickEvent(), Side.CLIENT);
        LangManager.THIS.registerLangProvider(this);
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
