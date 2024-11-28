package mods.vintage.core.platform.lang.components;

import java.util.Iterator;

@SuppressWarnings("all")
public class ChatComponentText extends ChatComponentStyle {
    private final String text;
    private static final String __OBFID = "CL_00001269";

    public ChatComponentText(String msg) {
        this.text = msg;
    }

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.
     */
    public String getUnformattedTextForChat() {
        return this.text;
    }

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    public ChatComponentText createCopy() {
        ChatComponentText chatcomponenttext = new ChatComponentText(this.text);
        chatcomponenttext.setChatStyle(this.getChatStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent) iterator.next();
            chatcomponenttext.appendSibling(ichatcomponent.createCopy());
        }

        return chatcomponenttext;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatComponentText)) {
            return false;
        } else {
            ChatComponentText chatcomponenttext = (ChatComponentText) object;
            return this.text.equals(chatcomponenttext.getUnformattedTextForChat()) && super.equals(object);
        }
    }

    public String toString() {
        return "Component{text=\'" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getChatStyle() + '}';
    }
}
