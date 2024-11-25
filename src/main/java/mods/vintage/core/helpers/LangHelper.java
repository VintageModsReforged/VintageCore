package mods.vintage.core.helpers;

import net.minecraft.util.StatCollector;

public class LangHelper {

    public static String format(String key, Object... args) {
        return StatCollector.translateToLocalFormatted(key, args);
    }

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }
}
