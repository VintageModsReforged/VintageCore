package mods.vintage.core.platform.lang.components;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

@SuppressWarnings("all")
public interface IChatComponent extends Iterable {
    IChatComponent setChatStyle(ChatStyle style);

    ChatStyle getChatStyle();

    /**
     * Appends the given text to the end of this component.
     */
    IChatComponent appendText(String text);

    /**
     * Appends the given component to the end of this one.
     */
    IChatComponent appendSibling(IChatComponent component);

    /**
     * Gets the text of this component, without any special formatting codes added, for chat.
     */
    String getUnformattedTextForChat();

    /**
     * Gets the text of this component, without any special formatting codes added.
     */
    String getUnformattedText();

    /**
     * Gets the text of this component, with formatting codes added for rendering.
     */
    String getFormattedText();

    /**
     * Gets the sibling components of this one.
     */
    List getSiblings();

    /**
     * Creates a copy of this component.  Almost a deep copy, except the style is shallow-copied.
     */
    IChatComponent createCopy();

    public static class Serializer implements JsonDeserializer, JsonSerializer {
        private static final Gson GSON;
        private static final String __OBFID = "CL_00001263";

        public IChatComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
            if (jsonElement.isJsonPrimitive()) {
                return new ChatComponentText(jsonElement.getAsString());
            } else if (!jsonElement.isJsonObject()) {
                if (jsonElement.isJsonArray()) {
                    JsonArray jsonarray1 = jsonElement.getAsJsonArray();
                    IChatComponent ichatcomponent = null;
                    Iterator iterator = jsonarray1.iterator();

                    while (iterator.hasNext()) {
                        JsonElement jsonelement1 = (JsonElement) iterator.next();
                        IChatComponent ichatcomponent1 = this.deserialize(jsonelement1, jsonelement1.getClass(), context);

                        if (ichatcomponent == null) {
                            ichatcomponent = ichatcomponent1;
                        } else {
                            ichatcomponent.appendSibling(ichatcomponent1);
                        }
                    }

                    return ichatcomponent;
                } else {
                    throw new JsonParseException("Don\'t know how to turn " + jsonElement.toString() + " into a Component");
                }
            } else {
                JsonObject jsonobject = jsonElement.getAsJsonObject();
                Object object;

                if (jsonobject.has("text")) {
                    object = new ChatComponentText(jsonobject.get("text").getAsString());
                } else {
                    if (!jsonobject.has("translate")) {
                        throw new JsonParseException("Don\'t know how to turn " + jsonElement.toString() + " into a Component");
                    }

                    String s = jsonobject.get("translate").getAsString();

                    if (jsonobject.has("with")) {
                        JsonArray jsonarray = jsonobject.getAsJsonArray("with");
                        Object[] aobject = new Object[jsonarray.size()];

                        for (int i = 0; i < aobject.length; ++i) {
                            aobject[i] = this.deserialize(jsonarray.get(i), type, context);

                            if (aobject[i] instanceof ChatComponentText) {
                                ChatComponentText chatcomponenttext = (ChatComponentText) aobject[i];

                                if (chatcomponenttext.getChatStyle().isEmpty() && chatcomponenttext.getSiblings().isEmpty()) {
                                    aobject[i] = chatcomponenttext.getUnformattedTextForChat();
                                }
                            }
                        }

                        object = new ChatComponentTranslation(s, aobject);
                    } else {
                        object = new ChatComponentTranslation(s, new Object[0]);
                    }
                }

                if (jsonobject.has("extra")) {
                    JsonArray jsonarray2 = jsonobject.getAsJsonArray("extra");

                    if (jsonarray2.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }

                    for (int j = 0; j < jsonarray2.size(); ++j) {
                        ((IChatComponent) object).appendSibling(this.deserialize(jsonarray2.get(j), type, context));
                    }
                }

                ((IChatComponent) object).setChatStyle((ChatStyle) context.deserialize(jsonElement, ChatStyle.class));
                return (IChatComponent) object;
            }
        }

        private void serializeChatStyle(ChatStyle style, JsonObject object, JsonSerializationContext ctx) {
            JsonElement jsonelement = ctx.serialize(style);

            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject1 = (JsonObject) jsonelement;
                Iterator iterator = jsonobject1.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    object.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
            }
        }

        public JsonElement serialize(IChatComponent chatComponent, Type type, JsonSerializationContext context) {
            if (chatComponent instanceof ChatComponentText && chatComponent.getChatStyle().isEmpty() && chatComponent.getSiblings().isEmpty()) {
                return new JsonPrimitive(((ChatComponentText) chatComponent).getUnformattedTextForChat());
            } else {
                JsonObject jsonobject = new JsonObject();

                if (!chatComponent.getChatStyle().isEmpty()) {
                    this.serializeChatStyle(chatComponent.getChatStyle(), jsonobject, context);
                }

                if (!chatComponent.getSiblings().isEmpty()) {
                    JsonArray jsonarray = new JsonArray();
                    Iterator iterator = chatComponent.getSiblings().iterator();

                    while (iterator.hasNext()) {
                        IChatComponent ichatcomponent1 = (IChatComponent) iterator.next();
                        jsonarray.add(this.serialize(ichatcomponent1, ichatcomponent1.getClass(), context));
                    }

                    jsonobject.add("extra", jsonarray);
                }

                if (chatComponent instanceof ChatComponentText) {
                    jsonobject.addProperty("text", ((ChatComponentText) chatComponent).getUnformattedTextForChat());
                } else {
                    if (!(chatComponent instanceof ChatComponentTranslation)) {
                        throw new IllegalArgumentException("Don\'t know how to serialize " + chatComponent + " as a Component");
                    }

                    ChatComponentTranslation chatcomponenttranslation = (ChatComponentTranslation) chatComponent;
                    jsonobject.addProperty("translate", chatcomponenttranslation.getKey());

                    if (chatcomponenttranslation.getFormatArgs() != null && chatcomponenttranslation.getFormatArgs().length > 0) {
                        JsonArray jsonarray1 = new JsonArray();
                        Object[] aobject = chatcomponenttranslation.getFormatArgs();
                        int i = aobject.length;

                        for (int j = 0; j < i; ++j) {
                            Object object = aobject[j];

                            if (object instanceof IChatComponent) {
                                jsonarray1.add(this.serialize((IChatComponent) object, object.getClass(), context));
                            } else {
                                jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
                            }
                        }

                        jsonobject.add("with", jsonarray1);
                    }
                }

                return jsonobject;
            }
        }

        public static String componentToJson(IChatComponent component) {
            return GSON.toJson(component);
        }

        public static IChatComponent jsonToComponent(String json) {
            return (IChatComponent) GSON.fromJson(json, IChatComponent.class);
        }

        public JsonElement serialize(Object object, Type type, JsonSerializationContext context) {
            return this.serialize((IChatComponent) object, type, context);
        }

        static {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeHierarchyAdapter(IChatComponent.class, new Serializer());
            gsonBuilder.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
            gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
            GSON = gsonBuilder.create();
        }
    }

    class EnumTypeAdapterFactory implements TypeAdapterFactory {
        private static final String __OBFID = "CL_00001494";

        public TypeAdapter create(Gson gson, TypeToken typeToken) {
            Class oclass = typeToken.getRawType();

            if (!oclass.isEnum()) {
                return null;
            } else {
                final HashMap hashmap = new HashMap();
                Object[] aobject = oclass.getEnumConstants();
                int i = aobject.length;

                for (int j = 0; j < i; ++j) {
                    Object object = aobject[j];
                    hashmap.put(this.getName(object), object);
                }

                return new TypeAdapter() {
                    private static final String __OBFID = "CL_00001495";

                    public void write(JsonWriter jsonWriter, Object object) throws IOException {
                        if (object == null) {
                            jsonWriter.nullValue();
                        } else {
                            jsonWriter.value(EnumTypeAdapterFactory.this.getName(object));
                        }
                    }

                    public Object read(JsonReader jsonReader) throws IOException {
                        if (jsonReader.peek() == JsonToken.NULL) {
                            jsonReader.nextNull();
                            return null;
                        } else {
                            return hashmap.get(jsonReader.nextString());
                        }
                    }
                };
            }
        }

        private String getName(Object object) {
            return object instanceof Enum ? ((Enum) object).name().toLowerCase(Locale.US) : object.toString().toLowerCase(Locale.US);
        }
    }
}
