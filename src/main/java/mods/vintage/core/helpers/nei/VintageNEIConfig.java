package mods.vintage.core.helpers.nei;

import codechicken.nei.MultiItemRange;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import mods.vintage.core.Refs;

import java.util.ArrayList;
import java.util.List;

public class VintageNEIConfig implements IConfigureNEI {

    public static final List<INEIProvider> NEI_PROVIDERS = new ArrayList<INEIProvider>();

    @Override
    public void loadConfig() {
        for (INEIProvider provider : NEI_PROVIDERS) {
            provider.loadNEI();
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

    public static void addProvider(INEIProvider provider) {
        NEI_PROVIDERS.add(provider);
    }

    public static void createAndAddItemRange(String name, Integer... ids) {
        MultiItemRange itemRange = new MultiItemRange();
        for (int id : ids) {
            itemRange.add(id);
        }
        API.addSetRange(name, itemRange);
    }
}
