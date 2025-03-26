package mods.vintage.core.helpers;

import mods.vintage.core.helpers.pos.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet14BlockDig;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class ToolHelper {

    public static boolean harvestBlock(World world, int x, int y, int z, EntityPlayer player) {
        return harvestAndDrop(world, x, y, z, player, simpleHarvest());
    }

    private static IDropCallback simpleHarvest() {
        return new IDropCallback() {
            @Override
            public void handleServer(World world, Block block, int x, int y, int z, EntityPlayer player) {
                int blockMetadata = world.getBlockMetadata(x, y, z);
                block.harvestBlock(world, player, x, y, z, blockMetadata);
            }

            @Override
            public void handleClient(World world, Block block, int x, int y, int z, EntityPlayer player) {
                // NO-OP
            }
        };
    }

    public static boolean harvestAndDrop(World world, int x, int y, int z, EntityPlayer player, IDropCallback callback) {
        if (world.isAirBlock(x, y, z)) {
            return false;
        } else {
            EntityPlayerMP playerMP = null;
            if (player instanceof EntityPlayerMP) {
                playerMP = (EntityPlayerMP)player;
            }
            Block block = Block.blocksList[world.getBlockId(x, y, z)];
            int blockMetadata = world.getBlockMetadata(x, y, z);
            if (!ForgeHooks.canHarvestBlock(block, player, blockMetadata)) {
                return false;
            } else {
                if (player.capabilities.isCreativeMode) {
                    if (!world.isRemote) {
                        block.onBlockHarvested(world, x, y, z, blockMetadata, player);
                    } else {
                        world.playAuxSFX(2001, x, y, z, world.getBlockId(x, y, z) | blockMetadata << 12);
                    }

                    if (block.removeBlockByPlayer(world, player, x, y, z)) {
                        block.onBlockDestroyedByPlayer(world, x, y, z, blockMetadata);
                    }

                    if (!world.isRemote) {
                        assert playerMP != null;

                        playerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange());
                    } else {
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet14BlockDig());
                    }
                } else {
                    world.playAuxSFXAtEntity(player, 2001, x, y, z, world.getBlockId(x, y, z) | blockMetadata << 12);
                    if (!world.isRemote) {
                        block.onBlockHarvested(world, x, y, z, blockMetadata, player);
                        if (block.removeBlockByPlayer(world, player, x, y, z)) {
                            block.onBlockDestroyedByPlayer(world, x, y, z, blockMetadata);
                            callback.handleServer(world, block, x, y, z, player);
                        }
                        assert playerMP != null;
                        playerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange());
                    } else {
                        if (block.removeBlockByPlayer(world, player, x, y, z)) {
                            block.onBlockDestroyedByPlayer(world, x, y, z, blockMetadata);
                        }
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet14BlockDig());
                        callback.handleClient(world, block, x, y, z, player);
                    }
                }

                return true;
            }
        }
    }

    public interface IDropCallback {
        void handleServer(World world, Block block, int x, int y, int z, EntityPlayer player);
        void handleClient(World world, Block block, int x, int y, int z, EntityPlayer player);
    }

    public static List<BlockPos> getAOE(EntityPlayer player, BlockPos pos, int radius) {
        World world = player.worldObj;
        MovingObjectPosition mop = BlockHelper.raytraceFromEntity(world, player, false, 4.5D);
        int xRange = radius, yRange = radius, zRange = radius;
        if (mop == null) { // cancel when rayTrace fails
            return new ArrayList<BlockPos>();
        }
        switch (mop.sideHit) {
            case 0:
            case 1:
                yRange = 0;
                break;
            case 2:
            case 3:
                zRange = 0;
                break;
            case 4:
            case 5:
                xRange = 0;
                break;
        }
        return BlockPos.getAllInBox(pos.add(-xRange, -yRange, -zRange), pos.add(xRange, yRange, zRange));
    }
}
