package mods.vintage.core.platform.lang.component.contents;

import com.google.common.collect.ImmutableList;
import mods.vintage.core.platform.lang.component.FormattedTexts;
import mods.vintage.core.platform.lang.component.MutableComponent;
import mods.vintage.core.platform.lang.component.interfaces.Component;
import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;
import mods.vintage.core.utils.function.Consumer;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatableContents implements ComponentContents {

    private static final Object[] NO_ARGS = new Object[0];
    private static final FormattedText TEXT_PERCENT = FormattedTexts.of("%");
    private static final FormattedText TEXT_NULL = FormattedTexts.of("null");

    private final String key;
    private final Object[] args;

    private List<FormattedText> decomposedParts = ImmutableList.of();
    private static final Pattern FORMAT_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    public TranslatableContents(String key) {
        this.key = key;
        this.args = NO_ARGS;
    }

    public TranslatableContents(String key, Object... args) {
        this.key = key;
        this.args = args;
    }

    private void decompose() {
        String s = StatCollector.translateToLocal(this.key);

        try {
            final ImmutableList.Builder<FormattedText> builder = ImmutableList.builder();
            this.decomposeTemplate(s, new Consumer<FormattedText>() {
                @Override
                public void accept(FormattedText formattedText) {
                    builder.add(formattedText);
                }
            });
            this.decomposedParts = builder.build();
        } catch (TranslatableFormatException translatableformatexception) {
            this.decomposedParts = ImmutableList.of(FormattedTexts.of(s));
        }
    }

    private void decomposeTemplate(String key, Consumer<FormattedText> textConsumer) {
        Matcher matcher = FORMAT_PATTERN.matcher(key);

        try {
            int i = 0;

            int j;
            int l;
            for(j = 0; matcher.find(j); j = l) {
                int k = matcher.start();
                l = matcher.end();
                if (k > j) {
                    String s = key.substring(j, k);
                    if (s.indexOf(37) != -1) {
                        throw new IllegalArgumentException();
                    }

                    textConsumer.accept(FormattedTexts.of(s));
                }

                String s4 = matcher.group(2);
                String s1 = key.substring(k, l);
                if ("%".equals(s4) && "%%".equals(s1)) {
                    textConsumer.accept(TEXT_PERCENT);
                } else {
                    if (!"s".equals(s4)) {
                        throw new TranslatableFormatException(this, "Unsupported format: '" + s1 + "'");
                    }

                    String s2 = matcher.group(1);
                    int i1 = s2 != null ? Integer.parseInt(s2) - 1 : i++;
                    if (i1 < this.args.length) {
                        textConsumer.accept(this.getArgument(i1));
                    }
                }
            }

            if (j == 0) {
                j = handle(textConsumer, this.args, key);
            }

            if (j < key.length()) {
                String s3 = key.substring(j);
                if (s3.indexOf(37) != -1) {
                    throw new IllegalArgumentException();
                }

                textConsumer.accept(FormattedTexts.of(s3));
            }

        } catch (IllegalArgumentException illegalargumentexception) {
            throw new TranslatableFormatException(this, illegalargumentexception);
        }
    }

    private FormattedText getArgument(int i) {
        if (i >= this.args.length) {
            throw new TranslatableFormatException(this, i);
        } else {
            Object object = this.args[i];
            if (object instanceof Component) {
                return (Component) object;
            } else {
                return object == null ? TEXT_NULL : FormattedTexts.of(object.toString());
            }
        }
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
        this.decompose();

        for(FormattedText formattedtext : this.decomposedParts) {
            Optional<T> optional = formattedtext.visit(styledContentConsumer, style);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return Optional.empty();
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> styledContentConsumer) {
        this.decompose();

        for(FormattedText formattedtext : this.decomposedParts) {
            Optional<T> optional = formattedtext.visit(styledContentConsumer);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            if (object instanceof TranslatableContents) {
                TranslatableContents translatablecontents = (TranslatableContents) object;
                if (this.key.equals(translatablecontents.key) && Arrays.equals(this.args, translatablecontents.args)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public int hashCode() {
        int i = this.key.hashCode();
        return 31 * i + Arrays.hashCode(this.args);
    }

    @Override
    public String toString() {
        return "translation{key='" + this.key + "', args=" + Arrays.toString(this.args) + "}";
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public static int handle(final Consumer<FormattedText> addChild, final Object[] formatArgs, final String format) {
        try {
            final String formattedString = StatCollector.translateToLocalFormatted(format, formatArgs);

            if (format.indexOf('\'') != -1) {
                boolean onlyMissingQuotes = true;
                for (int i = 0; i < format.length(); i++) {
                    char ch = format.charAt(i);
                    if (formattedString.indexOf(ch) == -1 && ch != '\'') {
                        onlyMissingQuotes = false;
                        break;
                    }
                }
                if (onlyMissingQuotes) {
                    return 0;
                }
            }

            MutableComponent component = MutableComponent.literal(formattedString);
            addChild.accept(component);
            return format.length();
        } catch (IllegalArgumentException ex) {
            return 0;
        }
    }
}
