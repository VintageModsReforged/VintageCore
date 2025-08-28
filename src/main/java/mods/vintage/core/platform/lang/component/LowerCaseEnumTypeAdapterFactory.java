package mods.vintage.core.platform.lang.component;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {

    @Nullable
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> oclass = (Class<T>)type.getRawType();
        if (!oclass.isEnum()) {
            return null;
        } else {
            final Map<String, T> map = Maps.newHashMap();

            for(T t : oclass.getEnumConstants()) {
                map.put(this.toLowercase(t), t);
            }

            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter jsonWriter, T value) throws IOException {
                    if (value == null) {
                        jsonWriter.nullValue();
                    } else {
                        jsonWriter.value(LowerCaseEnumTypeAdapterFactory.this.toLowercase(value));
                    }

                }

                @Nullable
                @Override
                public T read(JsonReader jsonReader) throws IOException {
                    if (jsonReader.peek() == JsonToken.NULL) {
                        jsonReader.nextNull();
                        return (T)null;
                    } else {
                        return map.get(jsonReader.nextString());
                    }
                }
            };
        }
    }

    String toLowercase(Object object) {
        return object instanceof Enum ? ((Enum)object).name().toLowerCase(Locale.ROOT) : object.toString().toLowerCase(Locale.ROOT);
    }
}
