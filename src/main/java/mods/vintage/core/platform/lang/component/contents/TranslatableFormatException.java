package mods.vintage.core.platform.lang.component.contents;

import java.util.Locale;

public class TranslatableFormatException extends IllegalArgumentException {
    public TranslatableFormatException(TranslatableContents contents, String key) {
        super(String.format(Locale.ROOT, "Error parsing: %s: %s", contents, key));
    }

    public TranslatableFormatException(TranslatableContents contents, int index) {
        super(String.format(Locale.ROOT, "Invalid index %d requested for %s", index, contents));
    }

    public TranslatableFormatException(TranslatableContents contents, Throwable throwable) {
        super(String.format(Locale.ROOT, "Error while parsing: %s", contents), throwable);
    }
}
