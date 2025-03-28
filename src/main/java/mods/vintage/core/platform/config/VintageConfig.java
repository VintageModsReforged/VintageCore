package mods.vintage.core.platform.config;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageConfig extends Configuration {

    public static ItemBlockID testItemID = ItemBlockID.ofItem("testItem", 1);
    public static ItemBlockID test2ItemID = ItemBlockID.ofItem("test2Item", 1);
    public static ItemBlockID testBlockID = ItemBlockID.ofBlock("testBlock", 1);
    public static ItemBlockID test2BlockID = ItemBlockID.ofBlock("test2Block", 1);

    public VintageConfig(File file) {
        super(file);
        load();
        this.addCustomCategoryComment("item", "Test Comment for item id section");
        this.addCustomCategoryComment("block", "Test Comment for block id section");
    }
}
