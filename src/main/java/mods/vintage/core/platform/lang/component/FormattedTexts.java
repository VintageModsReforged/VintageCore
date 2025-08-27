package mods.vintage.core.platform.lang.component;

import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for working with {@link FormattedText} instances.
 * <p>
 * A {@link FormattedText} represents a piece of text together with its associated
 * formatting (color, bold, italic, etc). This class provides
 * helper methods to process, join, and render such texts without manually
 * traversing their style components.
 * </p>
 *
 * <h2>Key Responsibilities:</h2>
 * <ul>
 *   <li>Concatenating multiple {@link FormattedText} objects into a single one.</li>
 *   <li>Applying custom style transformations when combining texts.</li>
 *   <li>Converting raw strings or chat components into {@link FormattedText}.</li>
 *   <li>Extracting plain text or limited substrings with style awareness.</li>
 * </ul>
 *
 * <h2>Common Use Cases:</h2>
 * <ul>
 *   <li>Rendering styled text in GUIs.</li>
 *   <li>Building tooltips, chat messages, or HUD elements where multiple
 *       styles are combined.</li>
 *   <li>Safely truncating text while keeping formatting consistent.</li>
 * </ul>
 *
 */
public class FormattedTexts {

    public static FormattedText of(final String text) {
        return new FormattedText() {
            public <T> Optional<T> visit(ContentConsumer<T> consumer) {
                return consumer.accept(text);
            }
            public <T> Optional<T> visit(StyledContentConsumer<T> consumer, Style style) {
                return consumer.accept(style, text);
            }
        };
    }

    public static FormattedText of(final String text, final Style style) {
        return new FormattedText() {
            public <T> Optional<T> visit(ContentConsumer<T> consumer) {
                return consumer.accept(text);
            }
            public <T> Optional<T> visit(StyledContentConsumer<T> consumer, Style parentStyle) {
                return consumer.accept(style.applyTo(parentStyle), text);
            }
        };
    }

    public static FormattedText composite(final FormattedText... parts) {
        return composite(Arrays.asList(parts));
    }

    public static FormattedText composite(final List<? extends FormattedText> parts) {
        return new FormattedText() {
            public <T> Optional<T> visit(ContentConsumer<T> consumer) {
                for (FormattedText part : parts) {
                    Optional<T> result = part.visit(consumer);
                    if (result.isPresent()) {
                        return result;
                    }
                }
                return Optional.empty();
            }
            public <T> Optional<T> visit(StyledContentConsumer<T> consumer, Style style) {
                for (FormattedText part : parts) {
                    Optional<T> result = part.visit(consumer, style);
                    if (result.isPresent()) {
                        return result;
                    }
                }
                return Optional.empty();
            }
        };
    }
}
