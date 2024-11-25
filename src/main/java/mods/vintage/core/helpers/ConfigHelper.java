package mods.vintage.core.helpers;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ConfigHelper {

    public static String getString(Configuration cfg, String cat, String tag, String defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = cfg.get(cat, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        return prop.getString();
    }

    public static int getId(Configuration cfg, String cat, String tag, int defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = cfg.get(cat, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        int value = prop.getInt(defaultValue);
        prop.set(Integer.toString(value));
        return value;
    }

    private static int getInt(Configuration cfg, String cat, String tag, int min, int max, int defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = cfg.get(cat, tag, defaultValue);
        prop.comment = comment + "Min: " + min + ", Max: " + max + ", Default: " + defaultValue;
        int value = prop.getInt(defaultValue);
        value = Math.max(value, min);
        value = Math.min(value, max);
        prop.set(Integer.toString(value));
        return value;
    }

    private static double getDouble(Configuration cfg, String cat, String tag, double min, double max, double defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = cfg.get(cat, tag, defaultValue);
        prop.comment = comment + "Min: " + min + ", Max: " + max + ", Default: " + defaultValue;
        double value = prop.getDouble(defaultValue);
        value = Math.max(value, min);
        value = Math.min(value, max);
        prop.set(Double.toString(value));
        return value;
    }

    private static boolean getBoolean(Configuration cfg, String cat, String tag, boolean defaultValue, String comment) {
        comment = comment.replace("{t}", tag) + "\n";
        Property prop = cfg.get(cat, tag, defaultValue);
        prop.comment = comment + "Default: " + defaultValue;
        return prop.getBoolean(defaultValue);
    }
}
