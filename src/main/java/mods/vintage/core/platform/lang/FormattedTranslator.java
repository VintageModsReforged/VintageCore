package mods.vintage.core.platform.lang;

import mods.vintage.core.platform.lang.components.ChatComponentText;
import mods.vintage.core.platform.lang.components.ChatComponentTranslation;
import mods.vintage.core.platform.lang.components.ChatStyle;

public enum FormattedTranslator {
    BLACK,
    DARK_BLUE,
    DARK_GREEN,
    DARK_AQUA,
    DARK_RED,
    DARK_PURPLE,
    GOLD,
    GRAY,
    DARK_GRAY,
    BLUE,
    GREEN,
    AQUA,
    RED,
    LIGHT_PURPLE,
    YELLOW,
    WHITE,
    OBFUSCATED,
    BOLD,
    STRIKETHROUGH,
    UNDERLINE,
    ITALIC;

    final ChatFormatting FORMAT;

    FormattedTranslator() {
        this.FORMAT = ChatFormatting.valueOf(name());
    }

    public String format(String translatable) {
       return new ChatComponentTranslation(translatable).setChatStyle(new ChatStyle().setColor(this.FORMAT)).getFormattedText();
    }

    public String format(String translatable, Object... args) {
        return new ChatComponentTranslation(translatable, args).setChatStyle(new ChatStyle().setColor(this.FORMAT)).getFormattedText();
    }

    public String literal(String literal) {
        return new ChatComponentText(literal).setChatStyle(new ChatStyle().setColor(this.FORMAT)).getFormattedText();
    }
}
