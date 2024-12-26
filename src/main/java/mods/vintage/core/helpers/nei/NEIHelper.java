package mods.vintage.core.helpers.nei;

import java.util.HashMap;
import java.util.Map;

public class NEIHelper {

    protected static final Map<String, Integer[]> MAPPED_CATEGORIES = new HashMap<String, Integer[]>();

    public static void addCategory(String name, Integer... ids) {
        MAPPED_CATEGORIES.put(name, ids);
    }
}
