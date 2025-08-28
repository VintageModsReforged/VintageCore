package mods.vintage.core.platform.lang.component.interfaces;

import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;

public interface FormattedText {

    Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);

    FormattedText EMPTY = new FormattedText() {
        public <T> Optional<T> visit(ContentConsumer<T> consumer) {
            return Optional.empty();
        }
        public <T> Optional<T> visit(StyledContentConsumer<T> consumer, Style style) {
            return Optional.empty();
        }
    };

    <T> Optional<T> visit(ContentConsumer<T> contentConsumer);

    <T> Optional<T> visit(StyledContentConsumer<T> styledContentConsumer, Style style);

    interface ContentConsumer<T> {
        Optional<T> accept(String string);
    }

    interface StyledContentConsumer<T> {
        Optional<T> accept(Style style, String string);
    }

    enum Unit {
        INSTANCE;
    }
}
