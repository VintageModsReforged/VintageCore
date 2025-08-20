package mods.vintage.core.utils;

import mods.vintage.core.helpers.pos.BlockPos;
import net.minecraft.block.Block;

public interface IBlockAction {
    boolean actionPerformed(BlockPos pos, Block block, boolean isRightBlock);
}
