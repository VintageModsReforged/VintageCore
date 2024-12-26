package mods.vintage.core.helpers.nei;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import mods.vintage.core.Refs;

import java.util.Map;

public class NEIVintageConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        Map<String, Integer[]> MAPPED_IDS = NEIHelper.MAPPED_CATEGORIES;
        for (String category : MAPPED_IDS.keySet()) {
            createAndAddItemRange(category, MAPPED_IDS.get(category));
        }
    }

    @Override
    public String getName() {
        return Refs.NAME;
    }

    @Override
    public String getVersion() {
        return "1.5.2-1.0.0";
    }

    public static void createAndAddItemRange(String name, Integer... ids) {
        MultiItemRange itemRange = new MultiItemRange();
        for (int id : ids) {
            itemRange.add(id);
        }
        API.addSetRange(name, itemRange);
    }
}
