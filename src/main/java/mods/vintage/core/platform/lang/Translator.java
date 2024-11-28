package mods.vintage.core.platform.lang;

import net.minecraft.util.StatCollector;

public class Translator {

    public static String format(String key, Object... args) {
        return StatCollector.translateToLocalFormatted(key, args);
    }

    public static String format(boolean b) {
        return format(b ? "true" : "false");
    }

    public static String formattedBoolean(boolean bool) {
        return (bool ? FormattedTranslator.GREEN : FormattedTranslator.RED).literal(bool ? "true" : "false");
    }
}
