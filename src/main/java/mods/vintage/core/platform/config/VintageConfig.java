package mods.vintage.core.platform.config;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageConfig extends Configuration {

    public static ItemBlockID testItemID = new ItemBlockID(ItemBlockID.Mode.ITEM, "testItem", 1);
    public static ItemBlockID test2ItemID = new ItemBlockID(ItemBlockID.Mode.ITEM, "test2Item", 1);
    public static ItemBlockID testBlockID = new ItemBlockID(ItemBlockID.Mode.BLOCK, "testBlock", 1);
    public static ItemBlockID test2BlockID = new ItemBlockID(ItemBlockID.Mode.BLOCK, "test2Block", 1);

    public VintageConfig(File file) {
        super(file);
        load();
        this.addCustomCategoryComment("item", "Test Comment for item id section");
        this.addCustomCategoryComment("block", "Test Comment for block id section");
    }
}
