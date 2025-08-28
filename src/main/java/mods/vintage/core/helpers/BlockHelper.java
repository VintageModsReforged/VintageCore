package mods.vintage.core.helpers;

import cpw.mods.fml.common.Loader;
import mods.vintage.core.VintageConfig;
import mods.vintage.core.helpers.pos.BlockPos;
import mods.vintage.core.utils.IBlockAction;
import mods.vintage.core.utils.Utils;
import mods.vintage.core.utils.VeinSearchResult;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockHelper {

    public static final int[][] SIDE_COORD_MOD = new int[][]{{0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}};
    public static final int[] SIDE_LEFT = new int[]{4, 5, 5, 4, 2, 3};
    public static final int[] SIDE_RIGHT = new int[]{5, 4, 4, 5, 3, 2};
    public static final int[] SIDE_OPPOSITE = new int[]{1, 0, 3, 2, 5, 4};

    public static int getRotateType(Block block) {
        if (block instanceof BlockChest) {
            return 9;
        }
        if (block instanceof BlockWood) {
            return 7;
        }
        if (block instanceof BlockDispenser || block instanceof BlockPistonBase) {
            return 2;
        }
        if (block instanceof  BlockRail) {
            return 3;
        }
        if (block instanceof BlockHalfSlab) {
            return 8;
        }
        if (block instanceof BlockStairs) {
            return 5;
        }
        if (block instanceof BlockFurnace) {
            return 1;
        }
        if (block instanceof BlockSign) {
            return 11;
        }
        if (block instanceof BlockLever) {
            return 10;
        }
        if (block instanceof BlockPumpkin) {
            return 4;
        }
        if (block instanceof BlockRedstoneRepeater) {
            return 6;
        }
        return 0;
    }

    public static int[] getAdjacentCoordinatesForSide(int x, int y, int z, int side) {
        return new int[]{x + SIDE_COORD_MOD[side][0], y + SIDE_COORD_MOD[side][1], z + SIDE_COORD_MOD[side][2]};
    }

    public static int rotate(World world, int block, int meta, int x, int y, int z) {
        int shift;
        switch (getRotateType(Block.blocksList[block])) {
            case 1:
                return SIDE_LEFT[meta];
            case 2:
                if (meta < 6) {
                    ++meta;
                    return meta % 6;
                }

                return meta;
            case 3:
                if (meta < 2) {
                    ++meta;
                    return meta % 2;
                }

                return meta;
            case 4:
                ++meta;
                return meta % 4;
            case 5:
                ++meta;
                return meta % 8;
            case 6:
                int upper = meta & 12;
                int lower = meta & 3;
                ++lower;
                return upper + lower % 4;
            case 7:
                return (meta + 4) % 12;
            case 8:
                return (meta + 8) % 16;
            case 9:
                for(shift = 2; shift < 6; ++shift) {
                    int[] coords;
                    coords = getAdjacentCoordinatesForSide(x, y, z, shift);
                    if (world.getBlockId(coords[0], coords[1], coords[2]) == block) {
                        world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
                        return SIDE_OPPOSITE[meta];
                    }
                }

                return SIDE_LEFT[meta];
            case 10:
                shift = 0;
                if (meta > 7) {
                    meta -= 8;
                    shift = 8;
                }

                if (meta == 5) {
                    return 6 + shift;
                } else if (meta == 6) {
                    return 5 + shift;
                } else if (meta == 7) {
                    return 0 + shift;
                } else {
                    if (meta == 0) {
                        return 7 + shift;
                    }

                    return meta + shift;
                }
            case 11:
                ++meta;
                return meta % 16;
            default:
                return meta;
        }
    }

    public static int rotateAlt(World world, int block, int meta, int x, int y, int z) {
        int shift;
        switch (getRotateType(Block.blocksList[block])) {
            case 1:
                return SIDE_RIGHT[meta];
            case 2:
                if (meta < 6) {
                    return (meta + 5) % 6;
                }

                return meta;
            case 3:
                if (meta < 2) {
                    ++meta;
                    return meta % 2;
                }

                return meta;
            case 4:
                return (meta + 3) % 4;
            case 5:
                return (meta + 7) % 8;
            case 6:
                int upper = meta & 12;
                int lower = meta & 3;
                return upper + (lower + 3) % 4;
            case 7:
                return (meta + 8) % 12;
            case 8:
                return (meta + 8) % 16;
            case 9:
                for(shift = 2; shift < 6; ++shift) {
                    int[] coords;
                    coords = getAdjacentCoordinatesForSide(x, y, z, shift);
                    if (world.getBlockId(coords[0], coords[1], coords[2]) == block) {
                        world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
                        return SIDE_OPPOSITE[meta];
                    }
                }

                return SIDE_RIGHT[meta];
            case 10:
                shift = 0;
                if (meta > 7) {
                    meta -= 8;
                    shift = 8;
                }

                if (meta == 5) {
                    return 6 + shift;
                } else if (meta == 6) {
                    return 5 + shift;
                } else if (meta == 7) {
                    return shift;
                } else if (meta == 0) {
                    return 7 + shift;
                }
            case 11:
                ++meta;
                return meta % 16;
            default:
                return meta;
        }
    }

    public static int getBlockId(World world, BlockPos pos) {
        return world.getBlockId(pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean isAir(World world, BlockPos pos) {
        return world.isAirBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean blockHasTileEntity(World world, BlockPos pos) {
        return world.blockHasTileEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    public static TileEntity getBlockTileEntity(World world, BlockPos pos) {
        return world.getBlockTileEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    public int blockGetRenderType(World world, BlockPos pos) {
        return world.blockGetRenderType(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean blockExists(World world, BlockPos pos) {
        return world.blockExists(pos.getX(), pos.getY(), pos.getZ());
    }

    public static boolean setBlock(World world, BlockPos pos, int id) {
        return world.setBlock(pos.getX(), pos.getY(), pos.getZ(), id);
    }

    public Material getBlockMaterial(World world, BlockPos pos) {
        return world.getBlockMaterial(pos.getX(), pos.getY(), pos.getZ());
    }

    public static int getBlockMetadata(World world, BlockPos pos) {
        return world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Sets the metadata value of a block at the given position and notifies the world accordingly.
     *
     * @param world    The world in which the block is located.
     * @param pos      The block position (x, y, z).
     * @param metadata The new metadata value to assign to the block (0–15).
     * @param flag     Bitmask flags controlling update behavior:
     *                 - 1: Notify neighboring blocks (e.g. for redstone updates)
     *                 - 2: Send block update to client (re-renders the block)
     *                 - 4: Prevent re-render (when combined with 2)
     *                 - 8: Suppress observer block updates
     *                 These flags can be combined using bitwise OR (e.g. 1 | 2 = 3).
     *
     * @return true if the metadata was successfully updated; false otherwise
     * <br/>
     * Example usage:
     * <br/>
     * setBlockMetadataWithNotify(world, pos, 3, 3); // Set metadata to 3, notify neighbors and client
     */
    public boolean setBlockMetadataWithNotify(World world, BlockPos pos, int metadata, int flag) {
        return world.setBlockMetadataWithNotify(pos.getX(), pos.getY(), pos.getZ(), metadata, flag);
    }

    public static boolean setBlockToAir(World world, BlockPos pos) {
        return world.setBlock(pos.getX(), pos.getY(), pos.getZ(), 0, 0, 3);
    }

    public boolean destroyBlock(World world, BlockPos pos, boolean drop) {
        return world.destroyBlock(pos.getX(), pos.getY(), pos.getZ(), drop);
    }

    public static void removeBlockTileEntity(World world, BlockPos pos) {
        world.removeBlockTileEntity(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void markBlockForUpdate(World world, BlockPos pos) {
        world.markBlockForUpdate(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void markBlockForRenderUpdate(World world, BlockPos pos) {
        world.markBlockForRenderUpdate(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Block getBlock(World world, BlockPos pos) {
        return getBlock(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static Block getBlock(World world, int x, int y, int z) {
        return Block.blocksList[world.getBlockId(x, y, z)];
    }

    public static Set<BlockPos> veinPos(World world, BlockPos origin, int maxVeinSize) {
        Block originBlock = getBlock(world, origin);
        Set<BlockPos> found = new LinkedHashSet<BlockPos>();
        Set<BlockPos> openSet = new LinkedHashSet<BlockPos>();
        openSet.add(origin); // add origin
        while (!openSet.isEmpty()) { // just in case check if it's not empty
            BlockPos blockPos = openSet.iterator().next();
            found.add(blockPos); // add blockPos to found list for return
            openSet.remove(blockPos); // remove it and continue
            if (found.size() > maxVeinSize) { // nah, too much
                return found;
            }
            for (BlockPos pos : BlockPos.getAllInBoxMutable(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                if (!found.contains(pos)) { // we check if it's not in the list already
                    int checkedBlockId = getBlockId(world, pos);
                    Block checkedBlock = getBlock(world, pos);
                    if (checkedBlockId != 0) {
                        if (originBlock == checkedBlock) {
                            openSet.add(pos.toImmutable()); // add to openSet so we add it later when !openSet.isEmpty()
                        }
                        if (originBlock == Block.oreRedstone || originBlock == Block.oreRedstoneGlowing) {
                            if (checkedBlock == Block.oreRedstone || checkedBlock == Block.oreRedstoneGlowing) {
                                openSet.add(pos.toImmutable());
                            }
                        }
                    }
                }
            }
        }
        return found;
    }

    public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean checkFluid, double range) {
        float f = 1.0F;
        float pitchRot = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float yawRot = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double posX = player.prevPosX + (player.posX - player.prevPosX) * f;
        double posY = player.prevPosY + (player.posY - player.prevPosY) * f;
        if ((!world.isRemote) && ((player instanceof EntityPlayer))) {
            posY += 1.62D;
        }
        double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3 vec3 = Vec3.createVectorHelper(posX, posY, posZ);
        float cosYaw = MathHelper.cos(-yawRot * 0.017453292F - 3.1415927F);
        float sinYaw = MathHelper.sin(-yawRot * 0.017453292F - 3.1415927F);
        float cosPitch = -MathHelper.cos(-pitchRot * 0.017453292F);
        float sinPitch = MathHelper.sin(-pitchRot * 0.017453292F);
        float f7 = sinYaw * cosPitch;
        float f8 = cosYaw * cosPitch;
        double reachDistance = range;
        if ((player instanceof EntityPlayerMP)) {
            reachDistance = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector(f7 * reachDistance, sinPitch * reachDistance, f8 * reachDistance);
        return world.rayTraceBlocks_do_do(vec3, vec31, checkFluid, !checkFluid);
    }

    public static MovingObjectPosition retraceBlock(World world, EntityLiving entity, int x, int y, int z) {
        Vec3 vec = Vec3.createVectorHelper(entity.posX, entity.posY + 1.62 - (double)entity.yOffset, entity.posZ);
        Vec3 lookVec = entity.getLook(1.0F);
        Vec3 combined = vec.addVector(lookVec.xCoord * 5.0, lookVec.yCoord * 5.0, lookVec.zCoord * 5.0);
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return block == null ? null : block.collisionRayTrace(world, x, y, x, vec, combined);
    }

    public static boolean isLog(Block block) {
        String[] logs = VintageConfig.logs;
        boolean configLogs = false;
        for (String log : logs) {
            if (Utils.instanceOf(block, log)) configLogs = true;
            break;
        }
        return block instanceof BlockLog || configLogs;
    }

    public static boolean isLeaves(World world, BlockPos pos) {
        Block block = BlockHelper.getBlock(world, pos);
        String[] leaves = VintageConfig.leaves;
        boolean configLeaves = false;
        for (String leave : leaves) {
            if (Utils.instanceOf(block, leave)) configLeaves = true;
            break;
        }
        return getBOPStatus(world, pos) || configLeaves;
    }

    private static boolean getBOPStatus(World world, BlockPos pos) {
        int meta = BlockHelper.getBlockMetadata(world, pos) | 8;
        Block block = BlockHelper.getBlock(world, pos);
        if (Loader.isModLoaded("BiomesOPlenty")) {
            if (Utils.instanceOf(block, "biomesoplenty.blocks.BlockBOPPetals") ||
                    Utils.instanceOf(block, "biomesoplenty.blocks.BlockBOPLeaves") ||
                    Utils.instanceOf(block, "biomesoplenty.blocks.BlockBOPColorizedLeaves") ||
                    Utils.instanceOf(block, "biomesoplenty.blocks.BlockBOPAppleLeaves")) {
                return meta >= 8 && meta <= 15;
            }
        }
        return false;
    }

    public static VeinSearchResult scanForTree(final World world, final BlockPos startPos) {
        Block block = getBlock(world, startPos);
        ItemStack blockStack = new ItemStack(block, 1, 32767);
        boolean isLog = false;
        List<ItemStack> logs = StackHelper.getStackFromOre("log");
        logs.addAll(StackHelper.getStackFromOre("wood"));
        for (ItemStack check : logs) {
            if (StackHelper.areStacksEqual(check, blockStack) || isLog(block)) {
                isLog = true;
                break;
            }
        }
        if (!isLog) {
            return VeinSearchResult.abort(VeinSearchResult.Type.ABORT_NULL);
        }
        final boolean[] leavesFound = new boolean[1];
        VeinSearchResult result = recursiveSearch(world, startPos, new IBlockAction() {
            @Override
            public boolean actionPerformed(BlockPos pos, Block block, boolean isRightBlock) {
                int metadata = getBlockMetadata(world, pos) | 8;
                boolean isLeave = metadata >= 8 && metadata <= 11;
                if (block.isLeaves(world, startPos.getX(), startPos.getY(), startPos.getZ()) && isLeave || isLeaves(world, pos)) leavesFound[0] = true;
                return true;
            }
        });
        if (result.getType() == VeinSearchResult.Type.ABORT_OVER_LIMIT) {
            return result;
        }
        if (!leavesFound[0]) {
            return VeinSearchResult.NO_LEAVES;
        }

        return result;
    }

    // Recursively scan 3x3x3 cubes while keeping track of already scanned blocks to avoid cycles.
    private static VeinSearchResult recursiveSearch(final World world, final BlockPos start, @Nullable final IBlockAction action) {
        Block wantedBlock = getBlock(world, start);
        final Set<BlockPos> visited = new HashSet<BlockPos>();
        final List<BlockPos> result = new ArrayList<BlockPos>();
        final Deque<BlockPos> queue = new ArrayDeque<BlockPos>();
        if (!isAir(world, start)) {
            visited.add(start.toImmutable());
            result.add(start);
            queue.push(start);
            if (action != null && !action.actionPerformed(start, wantedBlock, true)) {
                return VeinSearchResult.NO_LEAVES;
            }
        } else {
            return VeinSearchResult.NULL;
        }

        while (!queue.isEmpty()) {
            final BlockPos center = queue.pop();
            final int x0 = center.getX();
            final int y0 = center.getY();
            final int z0 = center.getZ();
            for (int z = z0 - 1; z <= z0 + 1; ++z) {
                for (int y = y0 - 1; y <= y0 + 1; ++y) {
                    for (int x = x0 - 1; x <= x0 + 1; ++x) {
                        final BlockPos pos = new BlockPos(x, y, z);
                        if (!visited.add(pos.toImmutable()) || isAir(world, pos)) continue;
                        Block checkBlock = getBlock(world, pos);
                        final boolean isRightBlock = checkBlock.blockID == wantedBlock.blockID;
                        if (isRightBlock) {
                            if (result.size() >= VintageConfig.veinMaxCount) {
                                return VeinSearchResult.OVER_LIMIT;
                            }
                            result.add(pos);
                            queue.push(pos);
                        }

                        if (action != null && !action.actionPerformed(pos, checkBlock, isRightBlock)) {
                            return VeinSearchResult.NO_LEAVES;
                        }
                    }
                }
            }
        }
        return VeinSearchResult.success(result);
    }
}
