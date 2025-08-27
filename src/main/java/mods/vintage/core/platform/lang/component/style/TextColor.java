package mods.vintage.core.platform.lang.component.style;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TextColor {

    private static final String CUSTOM_COLOR_PREFIX = "#";

    private static final Map<ChatFormatting, TextColor> LEGACY_FORMAT_TO_COLOR;
    private static final Map<String, TextColor> NAMED_COLORS;

    static {
        Map<ChatFormatting, TextColor> legacyTmp = new HashMap<ChatFormatting, TextColor>();
        for (ChatFormatting format : ChatFormatting.values()) {
            if (format.isColor()) {
                TextColor color = new TextColor(format.getColor(), format.getName());
                legacyTmp.put(format, color);
            }
        }
        LEGACY_FORMAT_TO_COLOR = Collections.unmodifiableMap(legacyTmp);

        Map<String, TextColor> namedTmp = new HashMap<String, TextColor>();
        for (TextColor color : LEGACY_FORMAT_TO_COLOR.values()) {
            namedTmp.put(color.name, color);
        }
        NAMED_COLORS = Collections.unmodifiableMap(namedTmp);
    }

    private final int value;
    @Nullable
    private final String name;

    private TextColor(int value, String name) {
        this.value = value;
        this.name = name;
    }

    private TextColor(int value) {
        this.value = value;
        this.name = null;
    }

    public String serialize() {
        return this.name != null ? this.name : this.formatValue();
    }

    private String formatValue() {
        return String.format(Locale.ROOT, "#%06X", this.value);
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            TextColor textcolor = (TextColor)other;
            return this.value == textcolor.value;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        Object[] objects = new Object[] {this.value, this.name};
        return Arrays.hashCode(objects);
    }

    @Override
    public String toString() {
        return this.name != null ? this.name : this.formatValue();
    }

    public static TextColor fromLegacyFormat(ChatFormatting chatFormatting) {
        return LEGACY_FORMAT_TO_COLOR.get(chatFormatting);
    }

    public static TextColor fromRgb(int value) {
        return new TextColor(value);
    }

    public static TextColor parseColor(String name) {
        if (name.startsWith(CUSTOM_COLOR_PREFIX)) {
            try {
                int i = Integer.parseInt(name.substring(1), 16);
                return fromRgb(i);
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        } else {
            return NAMED_COLORS.get(name);
        }
    }

    @Nullable
    public ChatFormatting toLegacyFormat() {
        for (Map.Entry<ChatFormatting, TextColor> entry : LEGACY_FORMAT_TO_COLOR.entrySet()) {
            if (this.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null; // no corresponding legacy formatting
    }
}
