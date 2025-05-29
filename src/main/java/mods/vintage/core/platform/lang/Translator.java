package mods.vintage.core.platform.lang;

public class Translator {

    public static String format(String key, Object... args) {
        return FormattedTranslator.GRAY.format(key, args);
    }

    public static String literal(String literal) {
        return FormattedTranslator.GRAY.literal(literal);
    }

    public static String format(boolean b) {
        return literal(b ? "true" : "false");
    }

    public static String formattedBoolean(boolean bool) {
        return (bool ? FormattedTranslator.GREEN : FormattedTranslator.RED).literal(bool ? "true" : "false");
    }
}
