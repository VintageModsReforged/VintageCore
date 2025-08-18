package mods.vintage.core.platform.lang.component;

import mods.vintage.core.platform.lang.component.contents.ComponentContents;
import mods.vintage.core.platform.lang.component.contents.LiteralContents;
import mods.vintage.core.platform.lang.component.contents.TranslatableContents;
import mods.vintage.core.platform.lang.component.interfaces.Component;
import mods.vintage.core.platform.lang.component.interfaces.FormattedCharSequence;
import mods.vintage.core.platform.lang.component.interfaces.FormattedText;
import mods.vintage.core.platform.lang.component.style.ChatFormatting;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.Optional;
import mods.vintage.core.utils.function.Function;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MutableComponent implements Component {

    private final ComponentContents contents;
    private final List<Component> siblings;
    private Style style;
    private final FormattedCharSequence visualOrderText = FormattedCharSequence.EMPTY;

    MutableComponent(ComponentContents contents, List<Component> siblings, Style style) {
        this.siblings = siblings;
        this.style = style;
        this.contents = contents;
    }

    public static MutableComponent create(ComponentContents contents) {
        return new MutableComponent(contents, new ArrayList<Component>(), Style.EMPTY);
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public ComponentContents getContents() {
        return this.contents;
    }

    @Override
    public List<Component> getSiblings() {
        return this.siblings;
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return this.visualOrderText;
    }

    public MutableComponent setStyle(Style style) {
        this.style = style;
        return this;
    }

    public MutableComponent append(String string) {
        return append(literal(string));
    }

    public MutableComponent append(Component component) {
        this.siblings.add(component);
        return this;
    }

    public MutableComponent withStyle(Function<Style, Style> styleFunction) {
        this.setStyle(styleFunction.apply(this.getStyle()));
        return this;
    }

    public MutableComponent withStyle(Style style) {
        this.setStyle(style.applyTo(this.getStyle()));
        return this;
    }

    public MutableComponent withStyle(ChatFormatting... chatFormattings) {
        this.setStyle(this.getStyle().applyFormats(chatFormattings));
        return this;
    }

    public MutableComponent withStyle(ChatFormatting chatFormatting) {
        this.setStyle(this.getStyle().applyFormat(chatFormatting));
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MutableComponent)) {
            return false;
        } else {
            MutableComponent mutablecomponent = (MutableComponent) object;
            return this.contents.equals(mutablecomponent.contents) && this.style.equals(mutablecomponent.style) && this.siblings.equals(mutablecomponent.siblings);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.contents, this.style, this.siblings});
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder(this.contents.toString());
        boolean flag = !this.style.isEmpty();
        boolean flag1 = !this.siblings.isEmpty();
        if (flag || flag1) {
            stringbuilder.append('[');
            if (flag) {
                stringbuilder.append("style=");
                stringbuilder.append(this.style);
            }

            if (flag && flag1) {
                stringbuilder.append(", ");
            }

            if (flag1) {
                stringbuilder.append("siblings=");
                stringbuilder.append((Object) this.siblings);
            }

            stringbuilder.append(']');
        }

        return stringbuilder.toString();
    }

    @Override
    public <T> Optional<T> visit(ContentConsumer<T> contentConsumer) {
        Optional<T> optional = this.getContents().visit(contentConsumer);
        if (optional.isPresent()) {
            return optional;
        } else {
            for (Component component : this.getSiblings()) {
                Optional<T> optional1 = component.visit(contentConsumer);
                if (optional1.isPresent()) {
                    return optional1;
                }
            }

            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> visit(StyledContentConsumer<T> styledContentConsumer, Style styleArg) {
        Style style = this.getStyle().applyTo(styleArg);
        Optional<T> optional = this.getContents().visit(styledContentConsumer, style);
        if (optional.isPresent()) {
            return optional;
        } else {
            for (Component component : this.getSiblings()) {
                Optional<T> optional1 = component.visit(styledContentConsumer, style);
                if (optional1.isPresent()) {
                    return optional1;
                }
            }

            return Optional.empty();
        }
    }

    @Override
    public String getString() {
        final StringBuilder stringbuilder = new StringBuilder();
        this.visit(new ContentConsumer<Object>() {
            @Override
            public Optional<Object> accept(String string) {
                stringbuilder.append(string);
                return Optional.empty();
            }
        });
        return stringbuilder.toString();
    }

    public String getFormattedString() {
        final StringBuilder builder = new StringBuilder();
        this.visit(new FormattedText.StyledContentConsumer<Object>() {
            public Optional<Object> accept(Style style, String string) {
                if (style.getColor() != null) {
                    ChatFormatting legacy = style.getColor().toLegacyFormat();
                    if (legacy != null) {
                        builder.append(legacy);
                    }
                }
                builder.append(string);
                return Optional.empty();
            }
        }, Style.EMPTY);
        return builder.toString();
    }

    public static Component nullToEmpty(@Nullable String string) {
        return (Component) (string != null ? literal(string) : Component.EMPTY);
    }

    public static MutableComponent literal(String string) {
        return MutableComponent.create(new LiteralContents(string));
    }

    public static MutableComponent translatable(String string) {
        return MutableComponent.create(new TranslatableContents(string));
    }

    public static MutableComponent translatable(String key, Object... args) {
        return MutableComponent.create(new TranslatableContents(key, args));
    }

    public static MutableComponent empty() {
        return MutableComponent.create(ComponentContents.EMPTY);
    }
}
