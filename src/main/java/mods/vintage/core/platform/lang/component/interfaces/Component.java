package mods.vintage.core.platform.lang.component.interfaces;

import com.google.gson.*;
import mods.vintage.core.platform.lang.component.LowerCaseEnumTypeAdapterFactory;
import mods.vintage.core.platform.lang.component.MutableComponent;
import mods.vintage.core.platform.lang.component.contents.ComponentContents;
import mods.vintage.core.platform.lang.component.contents.LiteralContents;
import mods.vintage.core.platform.lang.component.contents.TranslatableContents;
import mods.vintage.core.platform.lang.component.style.Style;
import mods.vintage.core.utils.function.Supplier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface Component extends Message, FormattedText {

    Style getStyle();
    ComponentContents getContents();
    List<Component> getSiblings();
    FormattedCharSequence getVisualOrderText();

    class Serializer implements JsonDeserializer<MutableComponent>, JsonSerializer<Component> {
        private static final Gson GSON = new Supplier<Gson>() {
            @Override
            public Gson get() {
                GsonBuilder gsonbuilder = new GsonBuilder();
                gsonbuilder.disableHtmlEscaping();
                gsonbuilder.registerTypeHierarchyAdapter(Component.class, new Component.Serializer());
                gsonbuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
                gsonbuilder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
                return gsonbuilder.create();
            }
        }.get();

        @Override
        public MutableComponent deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (jsonElement.isJsonPrimitive()) {
                return MutableComponent.literal(jsonElement.getAsString());
            } else if (!jsonElement.isJsonObject()) {
                if (jsonElement.isJsonArray()) {
                    JsonArray jsonarray1 = jsonElement.getAsJsonArray();
                    MutableComponent mutablecomponent1 = null;

                    for(JsonElement jsonelement : jsonarray1) {
                        MutableComponent mutablecomponent2 = this.deserialize(jsonelement, jsonelement.getClass(), context);
                        if (mutablecomponent1 == null) {
                            mutablecomponent1 = mutablecomponent2;
                        } else {
                            mutablecomponent1.append(mutablecomponent2);
                        }
                    }

                    return mutablecomponent1;
                } else {
                    throw new JsonParseException("Don't know how to turn " + jsonElement + " into a Component");
                }
            } else {
                JsonObject jsonobject = jsonElement.getAsJsonObject();
                MutableComponent mutablecomponent = MutableComponent.empty();
                if (jsonobject.has("text")) {
                    String s = getAsString(jsonobject, "text");
                    mutablecomponent = s.isEmpty() ? MutableComponent.empty() : MutableComponent.literal(s);
                } else if (jsonobject.has("translate")) {
                    String s1 = getAsString(jsonobject, "translate");
                    if (jsonobject.has("with")) {
                        JsonArray jsonarray = getAsJsonArray(jsonobject, "with");
                        Object[] aobject = new Object[jsonarray.size()];

                        for(int i = 0; i < aobject.length; ++i) {
                            aobject[i] = unwrapTextArgument(this.deserialize(jsonarray.get(i), type, context));
                        }

                        mutablecomponent = MutableComponent.translatable(s1, aobject);
                    } else {
                        mutablecomponent = MutableComponent.translatable(s1);
                    }
                }

                if (jsonobject.has("extra")) {
                    JsonArray jsonarray2 = getAsJsonArray(jsonobject, "extra");
                    if (jsonarray2.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }

                    for(int j = 0; j < jsonarray2.size(); ++j) {
                        mutablecomponent.append(this.deserialize(jsonarray2.get(j), type, context));
                    }
                }

                mutablecomponent.setStyle((Style) context.deserialize(jsonElement, Style.class));
                return mutablecomponent;
            }
        }

        private static Object unwrapTextArgument(Object object) {
            if (object instanceof Component) {
                if (((Component) object).getStyle().isEmpty() && ((Component) object).getSiblings().isEmpty()) {
                    ComponentContents componentcontents = ((Component) object).getContents();
                    if (componentcontents instanceof LiteralContents) {
                        LiteralContents literalcontents = (LiteralContents)componentcontents;
                        return literalcontents.getText();
                    }
                }
            }

            return object;
        }

        private void serializeStyle(Style style, JsonObject jsonObject, JsonSerializationContext context) {
            JsonElement jsonelement = context.serialize(style);
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = (JsonObject)jsonelement;

                for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                    jsonObject.add(entry.getKey(), entry.getValue());
                }
            }

        }

        @Override
        public JsonElement serialize(Component src, Type type, JsonSerializationContext context) {
            JsonObject jsonobject = new JsonObject();
            if (!src.getStyle().isEmpty()) {
                this.serializeStyle(src.getStyle(), jsonobject, context);
            }

            if (!src.getSiblings().isEmpty()) {
                JsonArray jsonarray = new JsonArray();

                for(Component component : src.getSiblings()) {
                    jsonarray.add(this.serialize(component, Component.class, context));
                }

                jsonobject.add("extra", jsonarray);
            }

            ComponentContents componentcontents = src.getContents();
            if (componentcontents == ComponentContents.EMPTY) {
                jsonobject.addProperty("text", "");
            } else if (componentcontents instanceof LiteralContents) {
                LiteralContents literalcontents = (LiteralContents)componentcontents;
                jsonobject.addProperty("text", literalcontents.getText());
            } else if (componentcontents instanceof TranslatableContents) {
                TranslatableContents translatablecontents = (TranslatableContents)componentcontents;
                jsonobject.addProperty("translate", translatablecontents.getKey());
                if (translatablecontents.getArgs().length > 0) {
                    JsonArray jsonarray1 = new JsonArray();

                    for(Object object : translatablecontents.getArgs()) {
                        if (object instanceof Component) {
                            jsonarray1.add(this.serialize((Component)object, object.getClass(), context));
                        } else {
                            jsonarray1.add(new JsonPrimitive(String.valueOf(object)));
                        }
                    }

                    jsonobject.add("with", jsonarray1);
                }
            }

            return jsonobject;
        }

        public static String getAsString(JsonObject jsonObject, String tag) {
            if (jsonObject.has(tag)) {
                return convertToString(jsonObject.get(tag), tag);
            } else {
                throw new JsonSyntaxException("Missing " + tag + ", expected to find a string");
            }
        }

        public static String convertToString(JsonElement jsonElement, String tag) {
            if (jsonElement.isJsonPrimitive()) {
                return jsonElement.getAsString();
            } else {
                throw new JsonSyntaxException("Expected " + tag + " to be a string!");
            }
        }

        public static JsonArray getAsJsonArray(JsonObject jsonObject, String tag) {
            if (jsonObject.has(tag)) {
                return convertToJsonArray(jsonObject.get(tag), tag);
            } else {
                throw new JsonSyntaxException("Missing " + tag + ", expected to find a JsonArray");
            }
        }

        public static JsonArray convertToJsonArray(JsonElement jsonElement, String tag) {
            if (jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray();
            } else {
                throw new JsonSyntaxException("Expected " + tag + " to be a JsonArray!");
            }
        }

        public static String toJson(Component component) {
            return GSON.toJson(component);
        }

        @Nullable
        public static MutableComponent fromJson(String string) {
            return GSON.fromJson(string, MutableComponent.class);
        }

        @Nullable
        public static MutableComponent fromJson(JsonElement jsonElement) {
            return GSON.fromJson(jsonElement, MutableComponent.class);
        }
    }
}
