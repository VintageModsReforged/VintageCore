package mods.vintage.core.helpers;

import com.google.common.collect.ImmutableList;
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

import java.util.Iterator;

public class ToolHelper {

    public static boolean harvestBlock(World world, int x, int y, int z, EntityPlayer player) {
        return harvestAndDrop(world, x, y, z, player, simpleHarvest());
    }

    private static IDropCallback simpleHarvest() {
        return new IDropCallback() {
            @Override
            public void handleServer(World world, Block block, int x, int y, int z, int metadata, EntityPlayer player) {
                block.harvestBlock(world, player, x, y, z, metadata);
            }

            @Override
            public void handleClient(World world, Block block, int x, int y, int z, int metadata, EntityPlayer player) {
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
            int metadata = world.getBlockMetadata(x, y, z);
            if (!ForgeHooks.canHarvestBlock(block, player, metadata)) {
                return false;
            } else {
                if (player.capabilities.isCreativeMode) {
                    if (!world.isRemote) {
                        block.onBlockHarvested(world, x, y, z, metadata, player);
                    } else {
                        world.playAuxSFX(2001, x, y, z, world.getBlockId(x, y, z) | metadata << 12);
                    }

                    if (block.removeBlockByPlayer(world, player, x, y, z)) {
                        block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
                    }

                    if (!world.isRemote) {
                        assert playerMP != null;

                        playerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange());
                    } else {
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet14BlockDig());
                    }
                } else {
                    world.playAuxSFXAtEntity(player, 2001, x, y, z, world.getBlockId(x, y, z) | metadata << 12);
                    if (!world.isRemote) {
                        block.onBlockHarvested(world, x, y, z, metadata, player);
                        if (block.removeBlockByPlayer(world, player, x, y, z)) {
                            block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
                            callback.handleServer(world, block, x, y, z, metadata, player);
                        }
                        assert playerMP != null;
                        playerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange());
                    } else {
                        if (block.removeBlockByPlayer(world, player, x, y, z)) {
                            block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
                        }
                        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet14BlockDig());
                        callback.handleClient(world, block, x, y, z, metadata, player);
                    }
                }

                return true;
            }
        }
    }

    public interface IDropCallback {
        void handleServer(World world, Block block, int x, int y, int z, int metadata, EntityPlayer player);
        void handleClient(World world, Block block, int x, int y, int z, int metadata, EntityPlayer player);
    }

    public static ImmutableList<BlockPos> getAOE(EntityPlayer player, BlockPos pos, int radius) {
        World world = player.worldObj;
        MovingObjectPosition mop = BlockHelper.raytraceFromEntity(world, player, false, 4.5D);
        int xRange = radius, yRange = radius, zRange = radius;
        if (mop == null) { // cancel when rayTrace fails
            return ImmutableList.of();
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

        ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
        Iterable<BlockPos> area = BlockPos.getAllInBox(pos.add(-xRange, -yRange, -zRange), pos.add(xRange, yRange, zRange));
        for (Iterator<BlockPos> it = area.iterator(); it.hasNext();) {
            builder.add(it.next().toImmutable());
        }
        return builder.build();
    }
}
