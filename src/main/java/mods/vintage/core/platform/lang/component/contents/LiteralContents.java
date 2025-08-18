package mods.vintage.core.platform.lang.component.contents;

import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;

public class LiteralContents implements ComponentContents {

    String text;

    public LiteralContents(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> styledContentConsumer, Style style) {
        return styledContentConsumer.accept(style, this.text);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> styledContentConsumer) {
        return styledContentConsumer.accept(this.text);
    }
}
