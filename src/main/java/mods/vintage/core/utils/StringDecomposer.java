package mods.vintage.core.utils;

import mods.vintage.core.platform.lang.component.interfaces.FormattedCharSink;
import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.ChatFormatting;
import mods.vintage.core.platform.lang.component.style.Style;

public class StringDecomposer {

    private static final char REPLACEMENT_CHAR = '\ufffd';
    private static final Optional<Object> STOP_ITERATION = Optional.empty();

    private static boolean feedChar(Style style, FormattedCharSink formattedCharSink, int index, char c) {
        return Utils.isSurrogate(c) ? formattedCharSink.accept(index, style, 65533) : formattedCharSink.accept(index, style, c);
    }

    public static boolean iterate(String string, Style style, FormattedCharSink formattedCharSink) {
        int i = string.length();

        for(int j = 0; j < i; ++j) {
            char c0 = string.charAt(j);
            if (Character.isHighSurrogate(c0)) {
                if (j + 1 >= i) {
                    if (!formattedCharSink.accept(j, style, 65533)) {
                        return false;
                    }
                    break;
                }

                char c1 = string.charAt(j + 1);
                if (Character.isLowSurrogate(c1)) {
                    if (!formattedCharSink.accept(j, style, Character.toCodePoint(c0, c1))) {
                        return false;
                    }

                    ++j;
                } else if (!formattedCharSink.accept(j, style, 65533)) {
                    return false;
                }
            } else if (!feedChar(style, formattedCharSink, j, c0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean iterateBackwards(String string, Style style, FormattedCharSink formattedCharSink) {
        int i = string.length();

        for(int j = i - 1; j >= 0; --j) {
            char c0 = string.charAt(j);
            if (Character.isLowSurrogate(c0)) {
                if (j - 1 < 0) {
                    if (!formattedCharSink.accept(0, style, 65533)) {
                        return false;
                    }
                    break;
                }

                char c1 = string.charAt(j - 1);
                if (Character.isHighSurrogate(c1)) {
                    --j;
                    if (!formattedCharSink.accept(j, style, Character.toCodePoint(c1, c0))) {
                        return false;
                    }
                } else if (!formattedCharSink.accept(j, style, 65533)) {
                    return false;
                }
            } else if (!feedChar(style, formattedCharSink, j, c0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean iterateFormatted(String p_14347_, Style style, FormattedCharSink formattedCharSink) {
        return iterateFormatted(p_14347_, 0, style, formattedCharSink);
    }

    public static boolean iterateFormatted(String string, int size, Style style, FormattedCharSink formattedCharSink) {
        return iterateFormatted(string, size, style, style, formattedCharSink);
    }

    public static boolean iterateFormatted(String string, int size, Style style1, Style style2, FormattedCharSink formattedCharSink) {
        int i = string.length();
        Style style = style1;

        for(int j = size; j < i; ++j) {
            char c0 = string.charAt(j);
            if (c0 == 167) {
                if (j + 1 >= i) {
                    break;
                }

                char c1 = string.charAt(j + 1);
                ChatFormatting chatformatting = ChatFormatting.getByCode(c1);
                if (chatformatting != null) {
                    style = chatformatting == ChatFormatting.RESET ? style2 : style.applyLegacyFormat(chatformatting);
                }

                ++j;
            } else if (Character.isHighSurrogate(c0)) {
                if (j + 1 >= i) {
                    if (!formattedCharSink.accept(j, style, 65533)) {
                        return false;
                    }
                    break;
                }

                char c2 = string.charAt(j + 1);
                if (Character.isLowSurrogate(c2)) {
                    if (!formattedCharSink.accept(j, style, Character.toCodePoint(c0, c2))) {
                        return false;
                    }

                    ++j;
                } else if (!formattedCharSink.accept(j, style, 65533)) {
                    return false;
                }
            } else if (!feedChar(style, formattedCharSink, j, c0)) {
                return false;
            }
        }

        return true;
    }

    public static boolean iterateFormatted(FormattedText formattedText, Style style, final FormattedCharSink formattedCharSink) {
        return !formattedText.visit(new FormattedText.StyledContentConsumer<Object>() {
            @Override
            public Optional<Object> accept(Style style, String string) {
                return iterateFormatted(string, 0, style, formattedCharSink) ? Optional.empty() : STOP_ITERATION;
            }
        }, style).isPresent();
    }

    public static String filterBrokenSurrogates(String string) {
        final StringBuilder stringbuilder = new StringBuilder();
        iterate(string, Style.EMPTY, new FormattedCharSink() {
            @Override
            public boolean accept(int index, Style style, int width) {
                stringbuilder.appendCodePoint(width);
                return true;
            }
        });
        return stringbuilder.toString();
    }

    public static String getPlainText(FormattedText formattedText) {
        final StringBuilder stringbuilder = new StringBuilder();
        iterateFormatted(formattedText, Style.EMPTY, new FormattedCharSink() {
            @Override
            public boolean accept(int index, Style style, int width) {
                stringbuilder.appendCodePoint(width);
                return true;
            }
        });
        return stringbuilder.toString();
    }
}
