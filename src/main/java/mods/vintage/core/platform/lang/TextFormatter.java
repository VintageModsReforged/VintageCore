package mods.vintage.core.platform.lang;

import mods.vintage.core.platform.lang.components.ChatComponentText;
import mods.vintage.core.platform.lang.components.ChatComponentTranslation;
import mods.vintage.core.platform.lang.components.ChatStyle;

public enum TextFormatter {
    BLACK("0"),
    DARK_BLUE("1"),
    DARK_GREEN("2"),
    DARK_AQUA("3"),
    DARK_RED("4"),
    DARK_PURPLE("5"),
    GOLD("6"),
    GRAY("7"), // default text color
    DARK_GRAY("8"),
    BLUE("9"),
    GREEN("a"),
    AQUA("b"),
    RED("c"),
    LIGHT_PURPLE("d"),
    YELLOW("e"),
    WHITE("f"),
    OBFUSCATED("k"),
    BOLD("l"),
    STRIKETHROUGH("m"),
    UNDERLINE("n"),
    ITALIC("o"),
    RESET("r");

    private final String colorCode;

    TextFormatter(String colorIndex) {
        this.colorCode = "\247" + colorIndex;
    }

    public String format(String translatable) {
        return new ChatComponentTranslation(translatable).setChatStyle(new ChatStyle().setColor(this)).getFormattedText();
    }

    public String format(String translatable, Object... args) {
        return new ChatComponentTranslation(translatable, args).setChatStyle(new ChatStyle().setColor(this)).getFormattedText();
    }

    public String literal(String literal) {
        return new ChatComponentText(literal).setChatStyle(new ChatStyle().setColor(this)).getFormattedText();
    }

    @Override
    public String toString() {
        return this.colorCode;
    }
}
