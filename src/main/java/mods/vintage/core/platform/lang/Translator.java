package mods.vintage.core.platform.lang;

import mods.vintage.core.platform.lang.component.style.ChatFormatting;
import mods.vintage.core.platform.lang.component.MutableComponent;

public enum Translator {
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
    ITALIC,
    RESET;

    final ChatFormatting FORMAT;

    Translator() {
        this.FORMAT = ChatFormatting.valueOf(name());
    }

    public String format(String key) {
        return MutableComponent.translatable(key).withStyle(this.FORMAT).getFormattedString();
    }

    public String format(String key, Object... args) {
        return MutableComponent.translatable(key, args).withStyle(this.FORMAT).getFormattedString();
    }

    public String literal(String literal) {
        return MutableComponent.literal(literal).withStyle(this.FORMAT).getFormattedString();
    }

    public String format(boolean b) {
        return literal(b ? "true" : "false");
    }

    public String formattedBoolean(boolean bool) {
        return MutableComponent.literal(format(bool)).withStyle(bool ? ChatFormatting.GREEN : ChatFormatting.RED).getFormattedString();
    }
}
