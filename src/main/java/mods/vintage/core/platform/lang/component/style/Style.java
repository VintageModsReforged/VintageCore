package mods.vintage.core.platform.lang.component.style;

import mods.vintage.core.utils.Optional;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class Style {

    public static final Style EMPTY = new Style(null, null, null, null, null, null, null);

    @Nullable
    final TextColor color;
    @Nullable
    final Boolean bold;
    @Nullable
    final Boolean italic;
    @Nullable
    final Boolean underlined;
    @Nullable
    final Boolean strikethrough;
    @Nullable
    final Boolean obfuscated;
    @Nullable
    final String insertion;

    private static Style create(Optional<TextColor> textColor, Optional<Boolean> bold, Optional<Boolean> italic, Optional<Boolean> underlined, Optional<Boolean> strikethrough, Optional<Boolean> obfuscated, Optional<String> insertion) {
        return new Style(textColor.orElse(null), bold.orElse(null), italic.orElse(null), underlined.orElse(null), strikethrough.orElse(null), obfuscated.orElse(null), insertion.orElse(null));
    }

    Style(@Nullable TextColor textColor, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated, @Nullable String insertion) {
        this.color = textColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.insertion = insertion;
    }

    @Nullable
    public TextColor getColor() {
        return this.color;
    }

    public boolean isBold() {
        return this.bold == Boolean.TRUE;
    }

    public boolean isItalic() {
        return this.italic == Boolean.TRUE;
    }

    public boolean isStrikethrough() {
        return this.strikethrough == Boolean.TRUE;
    }

    public boolean isUnderlined() {
        return this.underlined == Boolean.TRUE;
    }

    public boolean isObfuscated() {
        return this.obfuscated == Boolean.TRUE;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Nullable
    public String getInsertion() {
        return this.insertion;
    }

    public Style withColor(@Nullable TextColor textColor) {
        return new Style(textColor, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.insertion);
    }

    public Style withColor(@Nullable ChatFormatting chatFormatting) {
        return this.withColor(chatFormatting != null ? TextColor.fromLegacyFormat(chatFormatting) : null);
    }

    public Style withColor(int color) {
        return this.withColor(TextColor.fromRgb(color));
    }

    public Style withBold(@Nullable Boolean bold) {
        return new Style(this.color, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.insertion);
    }

    public Style withItalic(@Nullable Boolean italic) {
        return new Style(this.color, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated, this.insertion);
    }

    public Style withUnderlined(@Nullable Boolean underlined) {
        return new Style(this.color, this.bold, this.italic, underlined, this.strikethrough, this.obfuscated, this.insertion);
    }

    public Style withStrikethrough(@Nullable Boolean strikethrough) {
        return new Style(this.color, this.bold, this.italic, this.underlined, strikethrough, this.obfuscated, this.insertion);
    }

    public Style withObfuscated(@Nullable Boolean obfuscated) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, obfuscated, this.insertion);
    }

    public Style withInsertion(@Nullable String insertion) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, insertion);
    }

    public Style applyFormat(ChatFormatting chatFormatting) {
        TextColor textcolor = this.color;
        Boolean bold = this.bold;
        Boolean italic = this.italic;
        Boolean strikethrough = this.strikethrough;
        Boolean underlined = this.underlined;
        Boolean obfuscated = this.obfuscated;
        switch (chatFormatting) {
            case OBFUSCATED:
                obfuscated = true;
                break;
            case BOLD:
                bold = true;
                break;
            case STRIKETHROUGH:
                strikethrough = true;
                break;
            case UNDERLINE:
                underlined = true;
                break;
            case ITALIC:
                italic = true;
                break;
            case RESET:
                return EMPTY;
            default:
                textcolor = TextColor.fromLegacyFormat(chatFormatting);
        }

        return new Style(textcolor, bold, italic, underlined, strikethrough, obfuscated, this.insertion);
    }

    public Style applyLegacyFormat(ChatFormatting chatFormatting) {
        TextColor textcolor = this.color;
        Boolean bold = this.bold;
        Boolean italic = this.italic;
        Boolean strikethrough = this.strikethrough;
        Boolean underlined = this.underlined;
        Boolean obfuscated = this.obfuscated;
        switch (chatFormatting) {
            case OBFUSCATED:
                obfuscated = true;
                break;
            case BOLD:
                bold = true;
                break;
            case STRIKETHROUGH:
                strikethrough = true;
                break;
            case UNDERLINE:
                underlined = true;
                break;
            case ITALIC:
                italic = true;
                break;
            case RESET:
                return EMPTY;
            default:
                obfuscated = false;
                bold = false;
                strikethrough = false;
                underlined = false;
                italic = false;
                textcolor = TextColor.fromLegacyFormat(chatFormatting);
        }

        return new Style(textcolor, bold, italic, underlined, strikethrough, obfuscated, this.insertion);
    }

    public Style applyFormats(ChatFormatting... chatFormattings) {
        TextColor textcolor = this.color;
        Boolean bold = this.bold;
        Boolean italic = this.italic;
        Boolean strikethrough = this.strikethrough;
        Boolean underlined = this.underlined;
        Boolean obfuscated = this.obfuscated;

        for(ChatFormatting chatformatting : chatFormattings) {
            switch (chatformatting) {
                case OBFUSCATED:
                    obfuscated = true;
                    break;
                case BOLD:
                    bold = true;
                    break;
                case STRIKETHROUGH:
                    strikethrough = true;
                    break;
                case UNDERLINE:
                    underlined = true;
                    break;
                case ITALIC:
                    italic = true;
                    break;
                case RESET:
                    return EMPTY;
                default:
                    textcolor = TextColor.fromLegacyFormat(chatformatting);
            }
        }

        return new Style(textcolor, bold, italic, underlined, strikethrough, obfuscated, this.insertion);
    }

    public Style applyTo(Style style) {
        if (this == EMPTY) {
            return style;
        } else {
            return style == EMPTY ? this : new Style(this.color != null ? this.color : style.color, this.bold != null ? this.bold : style.bold, this.italic != null ? this.italic : style.italic, this.underlined != null ? this.underlined : style.underlined, this.strikethrough != null ? this.strikethrough : style.strikethrough, this.obfuscated != null ? this.obfuscated : style.obfuscated, this.insertion != null ? this.insertion : style.insertion);
        }
    }

    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("{");

        class Collector {
            private boolean isNotFirst;

            private void prependSeparator() {
                if (this.isNotFirst) {
                    stringbuilder.append(',');
                }

                this.isNotFirst = true;
            }

            void addFlagString(String string, @Nullable Boolean separator) {
                if (separator != null) {
                    this.prependSeparator();
                    if (!separator) {
                        stringbuilder.append('!');
                    }

                    stringbuilder.append(string);
                }

            }

            void addValueString(String string, @Nullable Object object) {
                if (object != null) {
                    this.prependSeparator();
                    stringbuilder.append(string);
                    stringbuilder.append('=');
                    stringbuilder.append(object);
                }

            }
        }

        Collector style$1collector = new Collector();
        style$1collector.addValueString("color", this.color);
        style$1collector.addFlagString("bold", this.bold);
        style$1collector.addFlagString("italic", this.italic);
        style$1collector.addFlagString("underlined", this.underlined);
        style$1collector.addFlagString("strikethrough", this.strikethrough);
        style$1collector.addFlagString("obfuscated", this.obfuscated);
        style$1collector.addValueString("insertion", this.insertion);
        stringbuilder.append("}");
        return stringbuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Style)) {
            return false;
        }

        Style style = (Style) obj;
        return this.isBold() == style.isBold()
                && this.isItalic() == style.isItalic()
                && this.isObfuscated() == style.isObfuscated()
                && this.isStrikethrough() == style.isStrikethrough()
                && this.isUnderlined() == style.isUnderlined()
                && safeEquals(this.getColor(), style.getColor())
                && safeEquals(this.getInsertion(), style.getInsertion());
    }

    private static boolean safeEquals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public int hashCode() {
        Object[] objects = new Object[] {this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.insertion};
        return Arrays.hashCode(objects);
    }
}
