package mods.vintage.core.platform.config;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageConfig extends Configuration {

    public static AutoID testItemID = new AutoID(AutoID.Mode.ITEM, "testItem", 4000);
    public static AutoID testBlockID = new AutoID(AutoID.Mode.BLOCK, "testBlock", 800);

    public VintageConfig(File file) {
        super(file);
        load();
        this.addCustomCategoryComment("item", "Test Comment for item id section");
        this.addCustomCategoryComment("block", "Test Comment for block id section");
    }
}
