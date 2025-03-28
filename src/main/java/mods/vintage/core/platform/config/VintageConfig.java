package mods.vintage.core.platform.config;

import net.minecraftforge.common.Configuration;

import java.io.File;

public class VintageConfig extends Configuration {

    public static AutoID testItemID = new AutoID(mods.vintage.core.platform.config.AutoID.Mode.ITEM, "testItem", 400);
    public static AutoID testBlockID = new AutoID(mods.vintage.core.platform.config.AutoID.Mode.BLOCK, "testBlock", 800);

    public VintageConfig(File file) {
        super(file);
        load();
    }
}
