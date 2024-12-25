package mods.vintage.core.helpers;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabHelper {

    public static CreativeTabs create(final String modid, final String tabName, final ItemStack icon) {
        return create(modid, tabName, icon.getItem());
    }

    public static CreativeTabs create(final String modid, final String tabName, final Item icon) {
        return new CreativeTabs(modid) {{
                LanguageRegistry.instance().addStringLocalization("itemGroup." + modid, tabName);
            }

            @Override
            public Item getTabIconItem() {
                return icon;
            }
        };
    }
}
