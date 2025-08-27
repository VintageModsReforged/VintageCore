package mods.vintage.core.utils;

import java.lang.reflect.Array;

import static java.lang.Character.MAX_SURROGATE;
import static java.lang.Character.MIN_SURROGATE;

@SuppressWarnings("all")
public class Utils {

    public static boolean instanceOf(Object obj, String clazz) {
        if (obj == null)
            return false;
        try {
            Class<?> c = Class.forName(clazz);
            if (c.isInstance(obj))
                return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    // Java 8+ method
    public static <T> T[] add(final T[] array, final T element) {
        final Class<?> type;
        if (array != null) {
            type = array.getClass().getComponentType();
        } else if (element != null) {
            type = element.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        // type must be T
        final T[] newArray = (T[]) copyArrayGrow(array, type);
        newArray[newArray.length - 1] = element;
        return newArray;
    }

    private static Object copyArrayGrow(final Object array, final Class<?> newArrayComponentType) {
        if (array != null) {
            final int arrayLength = Array.getLength(array);
            final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
            System.arraycopy(array, 0, newArray, 0, arrayLength);
            return newArray;
        }
        return Array.newInstance(newArrayComponentType, 1);
    }

    public static boolean isSurrogate(char ch) {
        return ch >= MIN_SURROGATE && ch < (MAX_SURROGATE + 1);
    }
}
