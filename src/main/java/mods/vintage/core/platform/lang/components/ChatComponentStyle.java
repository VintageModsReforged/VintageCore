package mods.vintage.core.platform.lang.components;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import mods.vintage.core.platform.lang.ChatFormatting;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("all")
public abstract class ChatComponentStyle implements IChatComponent {
    /**
     * The later siblings of this component.  If this component turns the text bold, that will apply to all the siblings
     * until a later sibling turns the text something else.
     */
    protected List siblings = Lists.newArrayList();
    private ChatStyle style;
    private static final String __OBFID = "CL_00001257";

    /**
     * Appends the given component to the end of this one.
     */
    public IChatComponent appendSibling(IChatComponent component) {
        component.getChatStyle().setParentStyle(this.getChatStyle());
        this.siblings.add(component);
        return this;
    }

    /**
     * Gets the sibling components of this one.
     */
    public List getSiblings() {
        return this.siblings;
    }

    /**
     * Appends the given text to the end of this component.
     */
    public IChatComponent appendText(String text) {
        return this.appendSibling(new ChatComponentText(text));
    }

    public IChatComponent setChatStyle(ChatStyle style) {
        this.style = style;
        Iterator iterator = this.siblings.iterator();

        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent) iterator.next();
            ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
        }

        return this;
    }

    public ChatStyle getChatStyle() {
        if (this.style == null) {
            this.style = new ChatStyle();
            Iterator iterator = this.siblings.iterator();

            while (iterator.hasNext()) {
                IChatComponent ichatcomponent = (IChatComponent) iterator.next();
                ichatcomponent.getChatStyle().setParentStyle(this.style);
            }
        }

        return this.style;
    }

    public Iterator iterator() {
        return Iterators.concat(Iterators.forArray(new ChatComponentStyle[]{this}), createDeepCopyIterator(this.siblings));
    }

    /**
     * Gets the text of this component, without any special formatting codes added.
     */
    public final String getUnformattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent) iterator.next();
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
        }

        return stringbuilder.toString();
    }

    /**
     * Gets the text of this component, with formatting codes added for rendering.
     */

    public final String getFormattedText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent) iterator.next();
            stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
            stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
            stringbuilder.append(ChatFormatting.RESET);
        }

        return stringbuilder.toString();
    }

    /**
     * Creates an iterator that iterates over the given components, returning deep copies of each component in turn so
     * that the properties of the returned objects will remain externally consistent after being returned.
     */
    public static Iterator createDeepCopyIterator(Iterable components) {
        Iterator iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function() {
            private static final String __OBFID = "CL_00001258";

            public Iterator apply(IChatComponent chatComponent) {
                return chatComponent.iterator();
            }

            public Object apply(Object object) {
                return this.apply((IChatComponent) object);
            }
        }));
        iterator = Iterators.transform(iterator, new Function() {
            private static final String __OBFID = "CL_00001259";

            public IChatComponent apply(IChatComponent chatComponent) {
                IChatComponent ichatcomponent1 = chatComponent.createCopy();
                ichatcomponent1.setChatStyle(ichatcomponent1.getChatStyle().createDeepCopy());
                return ichatcomponent1;
            }

            public Object apply(Object object) {
                return this.apply((IChatComponent) object);
            }
        });
        return iterator;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatComponentStyle)) {
            return false;
        } else {
            ChatComponentStyle chatcomponentstyle = (ChatComponentStyle) object;
            return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
        }
    }

    public int hashCode() {
        return 31 * this.style.hashCode() + this.siblings.hashCode();
    }

    public String toString() {
        return "Component{style=" + this.style + ", siblings=" + this.siblings + '}';
    }
}
