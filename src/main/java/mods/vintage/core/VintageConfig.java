package mods.vintage.core;

import mods.vintage.core.helpers.ConfigHelper;
import mods.vintage.core.platform.lang.LocalizationProvider;
import net.minecraftforge.common.Configuration;

@LocalizationProvider
public class VintageConfig {

    public static Configuration CONFIG;

    @LocalizationProvider.List(modId = Refs.ID)
    public static String[] langs;

    public static String[] logs;
    public static String[] leaves;
    public static boolean inspect_mode = false;
    public static int veinMaxCount;

    public static void init() {
        CONFIG = ConfigHelper.getConfigFor(Refs.ID);
        CONFIG.load();
        langs = ConfigHelper.getLocalizations(CONFIG, new String[] { "en_US", "ru_RU" }, Refs.ID);
        inspect_mode = ConfigHelper.getBoolean(CONFIG, "general", "enable_inspect_mode", inspect_mode, "Enable inspect mode. Helps identify block name, class and metadata.");
        logs = ConfigHelper.getStrings(CONFIG, "compat", "logs", new String[]{"thaumcraft.common.world.BlockMagicalLog"}, "Support for custom logs block that aren't instances of `BlockLog`. Enable inspect_mode and right click with a stick to get more info in the log. Used by TreeCapitator-providers (gravisuite, harvester).");
        leaves = ConfigHelper.getStrings(CONFIG, "compat", "leaves", new String[]{}, "Support for custom leaves block. This shouldn't be here, but just in case, for blocks that have their `isLeaves=false` for some reasons, but still are leaves... Enable inspect_mode and right click with a stick to get more info in the log. Used by TreeCapitator-providers (gravisuite, harvester).");
        veinMaxCount = ConfigHelper.getInt(CONFIG, "compat", "veinMaxCount", 1, Integer.MAX_VALUE, 256, "Max Block per both Vein Mining and Tree Chopping Actions.");
    }
}
