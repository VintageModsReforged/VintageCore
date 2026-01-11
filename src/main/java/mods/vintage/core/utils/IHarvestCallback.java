package mods.vintage.core.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IHarvestCallback {
    HarvestMode getMode(World world, Block block, int x, int y, int z, int meta, EntityPlayer player);
    void handleCustom(World world, Block block, int x, int y, int z, int meta, EntityPlayer player);

    public enum HarvestMode {
        NORMAL,     // vanilla harvest
        SMELT,      // auto-smelt
        VOID,       // no drops
        CUSTOM      // handled by callback
    }
}
