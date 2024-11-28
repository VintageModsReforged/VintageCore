package mods.vintage.core.platform.lang.components;

import com.google.gson.*;
import mods.vintage.core.platform.lang.ChatFormatting;

import java.lang.reflect.Type;

@SuppressWarnings("all")
public class ChatStyle {

    /**
     * The parent of this ChatStyle.  Used for looking up values that this instance does not override.
     */
    private ChatStyle parentStyle;
    private ChatFormatting color;
    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;
    /**
     * The base of the ChatStyle hierarchy.  All ChatStyle instances are implicitly children of this.
     */
    private static final ChatStyle rootStyle = new ChatStyle() {
        private static final String __OBFID = "CL_00001267";

        /**
         * Gets the effective color of this ChatStyle.
         */
        public ChatFormatting getColor() {
            return null;
        }

        /**
         * Whether or not text of this ChatStyle should be in bold.
         */
        public boolean getBold() {
            return false;
        }

        /**
         * Whether or not text of this ChatStyle should be italicized.
         */
        public boolean getItalic() {
            return false;
        }

        /**
         * Whether or not to format text of this ChatStyle using strikethrough.
         */
        public boolean getStrikethrough() {
            return false;
        }

        /**
         * Whether or not text of this ChatStyle should be underlined.
         */
        public boolean getUnderlined() {
            return false;
        }

        /**
         * Whether or not text of this ChatStyle should be obfuscated.
         */
        public boolean getObfuscated() {
            return false;
        }

        /**
         * Sets the color for this ChatStyle to the given value.  Only use color values for this; set other values using
         * the specific methods.
         */
        public ChatStyle setColor(ChatFormatting colorIn) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets whether or not text of this ChatStyle should be in bold.  Set to false if, e.g., the parent style is
         * bold and you want text of this style to be unbolded.
         */
        public ChatStyle setBold(Boolean bold) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets whether or not text of this ChatStyle should be italicized.  Set to false if, e.g., the parent style is
         * italicized and you want to override that for this style.
         */
        public ChatStyle setItalic(Boolean italicized) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets whether or not to format text of this ChatStyle using strikethrough.  Set to false if, e.g., the parent
         * style uses strikethrough and you want to override that for this style.
         */
        public ChatStyle setStrikethrough(Boolean strikethrough) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets whether or not text of this ChatStyle should be underlined.  Set to false if, e.g., the parent style is
         * underlined and you want to override that for this style.
         */
        public ChatStyle setUnderlined(Boolean underlined) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets whether or not text of this ChatStyle should be obfuscated.  Set to false if, e.g., the parent style is
         * obfuscated and you want to override that for this style.
         */
        public ChatStyle setObfuscated(Boolean obfuscated) {
            throw new UnsupportedOperationException();
        }

        /**
         * Sets the fallback ChatStyle to use if this ChatStyle does not override some value.  Without a parent, obvious
         * defaults are used (bold: false, underlined: false, etc).
         */
        public ChatStyle setParentStyle(ChatStyle parentStyle) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return "Style.ROOT";
        }

        /**
         * Creates a shallow copy of this style.  Changes to this instance's values will not be reflected in the copy,
         * but changes to the parent style's values WILL be reflected in both this instance and the copy, wherever
         * either does not override a value.
         */
        public ChatStyle createShallowCopy() {
            return this;
        }

        /**
         * Creates a deep copy of this style.  No changes to this instance or its parent style will be reflected in the
         * copy.
         */
        public ChatStyle createDeepCopy() {
            return this;
        }

        /**
         * Gets the equivalent text formatting code for this style, without the initial section sign (U+00A7) character.
         */
        public String getFormattingCode() {
            return "";
        }
    };
    private static final String __OBFID = "CL_00001266";

    /**
     * Gets the effective color of this ChatStyle.
     */
    public ChatFormatting getColor() {
        return this.color == null ? this.getParent().getColor() : this.color;
    }

    /**
     * Whether or not text of this ChatStyle should be in bold.
     */
    public boolean getBold() {
        return this.bold == null ? this.getParent().getBold() : this.bold.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be italicized.
     */
    public boolean getItalic() {
        return this.italic == null ? this.getParent().getItalic() : this.italic.booleanValue();
    }

    /**
     * Whether or not to format text of this ChatStyle using strikethrough.
     */
    public boolean getStrikethrough() {
        return this.strikethrough == null ? this.getParent().getStrikethrough() : this.strikethrough.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be underlined.
     */
    public boolean getUnderlined() {
        return this.underlined == null ? this.getParent().getUnderlined() : this.underlined.booleanValue();
    }

    /**
     * Whether or not text of this ChatStyle should be obfuscated.
     */
    public boolean getObfuscated() {
        return this.obfuscated == null ? this.getParent().getObfuscated() : this.obfuscated.booleanValue();
    }

    /**
     * Whether or not this style is empty (inherits everything from the parent).
     */
    public boolean isEmpty() {
        return this.bold == null && this.italic == null && this.strikethrough == null && this.underlined == null && this.obfuscated == null && this.color == null;
    }

    /**
     * Sets the color for this ChatStyle to the given value.  Only use color values for this; set other values using the
     * specific methods.
     */
    public ChatStyle setColor(ChatFormatting colorIn) {
        this.color = colorIn;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be in bold.  Set to false if, e.g., the parent style is bold
     * and you want text of this style to be unbolded.
     */
    public ChatStyle setBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be italicized.  Set to false if, e.g., the parent style is
     * italicized and you want to override that for this style.
     */
    public ChatStyle setItalic(Boolean italicized) {
        this.italic = italicized;
        return this;
    }

    /**
     * Sets whether or not to format text of this ChatStyle using strikethrough.  Set to false if, e.g., the parent
     * style uses strikethrough and you want to override that for this style.
     */
    public ChatStyle setStrikethrough(Boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be underlined.  Set to false if, e.g., the parent style is
     * underlined and you want to override that for this style.
     */
    public ChatStyle setUnderlined(Boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Sets whether or not text of this ChatStyle should be obfuscated.  Set to false if, e.g., the parent style is
     * obfuscated and you want to override that for this style.
     */
    public ChatStyle setObfuscated(Boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Sets the fallback ChatStyle to use if this ChatStyle does not override some value.  Without a parent, obvious
     * defaults are used (bold: false, underlined: false, etc).
     */
    public ChatStyle setParentStyle(ChatStyle parentStyle) {
        this.parentStyle = parentStyle;
        return this;
    }

    /**
     * Gets the equivalent text formatting code for this style, without the initial section sign (U+00A7) character.
     */

    public String getFormattingCode() {
        if (this.isEmpty()) {
            return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
        } else {
            StringBuilder stringbuilder = new StringBuilder();

            if (this.getColor() != null) {
                stringbuilder.append(this.getColor());
            }

            if (this.getBold()) {
                stringbuilder.append(ChatFormatting.BOLD);
            }

            if (this.getItalic()) {
                stringbuilder.append(ChatFormatting.ITALIC);
            }

            if (this.getUnderlined()) {
                stringbuilder.append(ChatFormatting.UNDERLINE);
            }

            if (this.getObfuscated()) {
                stringbuilder.append(ChatFormatting.OBFUSCATED);
            }

            if (this.getStrikethrough()) {
                stringbuilder.append(ChatFormatting.STRIKETHROUGH);
            }

            return stringbuilder.toString();
        }
    }

    /**
     * Gets the immediate parent of this ChatStyle.
     */
    private ChatStyle getParent() {
        return this.parentStyle == null ? rootStyle : this.parentStyle;
    }

    public String toString() {
        return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + '}';
    }

    public int hashCode() {
        int i = this.color.hashCode();
        i = 31 * i + this.bold.hashCode();
        i = 31 * i + this.italic.hashCode();
        i = 31 * i + this.underlined.hashCode();
        i = 31 * i + this.strikethrough.hashCode();
        i = 31 * i + this.obfuscated.hashCode();
        return i;
    }

    /**
     * Creates a shallow copy of this style.  Changes to this instance's values will not be reflected in the copy, but
     * changes to the parent style's values WILL be reflected in both this instance and the copy, wherever either does
     * not override a value.
     */
    public ChatStyle createShallowCopy() {
        ChatStyle chatstyle = new ChatStyle();
        chatstyle.bold = this.bold;
        chatstyle.italic = this.italic;
        chatstyle.strikethrough = this.strikethrough;
        chatstyle.underlined = this.underlined;
        chatstyle.obfuscated = this.obfuscated;
        chatstyle.color = this.color;
        chatstyle.parentStyle = this.parentStyle;
        return chatstyle;
    }

    /**
     * Creates a deep copy of this style.  No changes to this instance or its parent style will be reflected in the
     * copy.
     */
    public ChatStyle createDeepCopy() {
        ChatStyle chatstyle = new ChatStyle();
        chatstyle.setBold(Boolean.valueOf(this.getBold()));
        chatstyle.setItalic(Boolean.valueOf(this.getItalic()));
        chatstyle.setStrikethrough(Boolean.valueOf(this.getStrikethrough()));
        chatstyle.setUnderlined(Boolean.valueOf(this.getUnderlined()));
        chatstyle.setObfuscated(Boolean.valueOf(this.getObfuscated()));
        chatstyle.setColor(this.getColor());
        return chatstyle;
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer {
        private static final String __OBFID = "CL_00001268";

        public ChatStyle deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
            if (jsonElement.isJsonObject()) {
                ChatStyle chatstyle = new ChatStyle();
                JsonObject jsonobject = jsonElement.getAsJsonObject();

                if (jsonobject == null) {
                    return null;
                } else {
                    if (jsonobject.has("bold")) {
                        chatstyle.bold = Boolean.valueOf(jsonobject.get("bold").getAsBoolean());
                    }

                    if (jsonobject.has("italic")) {
                        chatstyle.italic = Boolean.valueOf(jsonobject.get("italic").getAsBoolean());
                    }

                    if (jsonobject.has("underlined")) {
                        chatstyle.underlined = Boolean.valueOf(jsonobject.get("underlined").getAsBoolean());
                    }

                    if (jsonobject.has("strikethrough")) {
                        chatstyle.strikethrough = Boolean.valueOf(jsonobject.get("strikethrough").getAsBoolean());
                    }

                    if (jsonobject.has("obfuscated")) {
                        chatstyle.obfuscated = Boolean.valueOf(jsonobject.get("obfuscated").getAsBoolean());
                    }

                    if (jsonobject.has("color")) {
                        chatstyle.color = (ChatFormatting) context.deserialize(jsonobject.get("color"), ChatFormatting.class);
                    }

                    return chatstyle;
                }
            } else {
                return null;
            }
        }

        public JsonElement serialize(ChatStyle chatStyle, Type type, JsonSerializationContext context) {
            if (chatStyle.isEmpty()) {
                return null;
            } else {
                JsonObject jsonobject = new JsonObject();

                if (chatStyle.bold != null) {
                    jsonobject.addProperty("bold", chatStyle.bold);
                }

                if (chatStyle.italic != null) {
                    jsonobject.addProperty("italic", chatStyle.italic);
                }

                if (chatStyle.underlined != null) {
                    jsonobject.addProperty("underlined", chatStyle.underlined);
                }

                if (chatStyle.strikethrough != null) {
                    jsonobject.addProperty("strikethrough", chatStyle.strikethrough);
                }

                if (chatStyle.obfuscated != null) {
                    jsonobject.addProperty("obfuscated", chatStyle.obfuscated);
                }

                if (chatStyle.color != null) {
                    jsonobject.add("color", context.serialize(chatStyle.color));
                }

                return jsonobject;
            }
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext context) {
            return this.serialize((ChatStyle) object, type, context);
        }
    }
}
