package mods.vintage.core.platform.lang.component.contents;

import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;

public interface ComponentContents {
    ComponentContents EMPTY = new ComponentContents() {
        @Override
        public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
            return Optional.empty();
        }

        @Override
        public <T> Optional<T> visit(FormattedText.ContentConsumer<T> styledContentConsumer) {
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "empty";
        }
    };

    <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style);
    <T> Optional<T> visit(FormattedText.ContentConsumer<T> styledContentConsumer);
}
