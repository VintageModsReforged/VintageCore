package mods.vintage.core.platform.lang.component;

import com.google.common.collect.ImmutableList;
import mods.vintage.core.platform.lang.component.interfaces.FormattedCharSequence;
import mods.vintage.core.platform.lang.component.interfaces.FormattedCharSink;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.StringDecomposer;

import java.util.List;

public class FormattedCharSequences {

    public static FormattedCharSequence codepoint(final int i, final Style style) {
        return new FormattedCharSequence() {
            @Override
            public boolean accept(FormattedCharSink formattedCharSink) {
                return formattedCharSink.accept(0, style, i);
            }
        };
    }

    public static FormattedCharSequence forward(final String string, final Style style) {
        return string.isEmpty() ? FormattedCharSequence.EMPTY : new FormattedCharSequence() {
            @Override
            public boolean accept(FormattedCharSink formattedCharSink) {
                return StringDecomposer.iterate(string, style, formattedCharSink);
            }
        };
    }

    public static FormattedCharSequence backward(final String string, final Style style) {
        return string.isEmpty() ? FormattedCharSequence.EMPTY : new FormattedCharSequence() {
            @Override
            public boolean accept(FormattedCharSink formattedCharSink) {
                return StringDecomposer.iterateFormatted(string, style, formattedCharSink);
            }
        };
    }

    public static FormattedCharSequence composite() {
        return FormattedCharSequence.EMPTY;
    }

    public static FormattedCharSequence composite(FormattedCharSequence formattedCharSequence) {
        return formattedCharSequence;
    }

    public static FormattedCharSequence composite(FormattedCharSequence formattedCharSequence, FormattedCharSequence formattedCharSequence1) {
        return fromPair(formattedCharSequence, formattedCharSequence1);
    }

    public static FormattedCharSequence composite(FormattedCharSequence... formattedCharSequences) {
        return fromList(ImmutableList.copyOf(formattedCharSequences));
    }

    public static FormattedCharSequence composite(List<FormattedCharSequence> formattedCharSequences) {
        int i = formattedCharSequences.size();
        switch (i) {
            case 0:
                return FormattedCharSequence.EMPTY;
            case 1:
                return formattedCharSequences.get(0);
            case 2:
                return fromPair(formattedCharSequences.get(0), formattedCharSequences.get(1));
            default:
                return fromList(ImmutableList.copyOf(formattedCharSequences));
        }
    }

    public static FormattedCharSequence fromPair(final FormattedCharSequence formattedCharSequence, final FormattedCharSequence formattedCharSequence1) {
        return new FormattedCharSequence() {
            @Override
            public boolean accept(FormattedCharSink formattedCharSink) {
                return formattedCharSequence.accept(formattedCharSink) && formattedCharSequence1.accept(formattedCharSink);
            }
        };
    }

    public static FormattedCharSequence fromList(final List<FormattedCharSequence> formattedCharSequences) {
        return new FormattedCharSequence() {
            @Override
            public boolean accept(FormattedCharSink formattedCharSink) {
                for (FormattedCharSequence formattedCharSequence : formattedCharSequences) {
                    if (!formattedCharSequence.accept(formattedCharSink)) return false;
                }
                return true;
            }
        };
    }
}
