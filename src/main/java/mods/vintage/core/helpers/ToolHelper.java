package mods.vintage.core.helpers;

import com.google.common.collect.ImmutableList;
import mods.vintage.core.helpers.pos.BlockPos;
import mods.vintage.core.utils.IHarvestCallback;
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

    public static IHarvestCallback simpleHarvest() {
        return new IHarvestCallback() {
            @Override
            public HarvestMode getMode(World world, Block block, int x, int y, int z, int meta, EntityPlayer player) {
                return HarvestMode.NORMAL;
            }

            @Override
            public void handleCustom(World world, Block block, int x, int y, int z, int meta, EntityPlayer player) {
                block.harvestBlock(world, player, x, y, z, meta);
            }
        };
    }

    public static boolean harvestAndDrop(World world, int x, int y, int z, EntityPlayer player, IHarvestCallback callback) {
        Block block = BlockHelper.getBlock(world, x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (block == null) return false;
        if (world.isAirBlock(x, y, z)) return false;

        if (!ForgeHooks.canHarvestBlock(block, player, meta)) return false;

        boolean isCreative = player.capabilities.isCreativeMode;
        EntityPlayerMP mp = player instanceof EntityPlayerMP ? (EntityPlayerMP) player : null;

        // --- CLIENT SIDE ---
        if (world.isRemote) {
            world.playAuxSFXAtEntity(player, 2001, x, y, z, world.getBlockId(x, y, z) | (meta << 12));
            if (block.removeBlockByPlayer(world, player, x, y, z)) {
                block.onBlockDestroyedByPlayer(world, x, y, z, meta);
            }
            Minecraft.getMinecraft().getSendQueue().addToSendQueue(new Packet14BlockDig());
            return true;
        }

        // --- SERVER SIDE ---
        block.onBlockHarvested(world, x, y, z, meta, player);

        if (!block.removeBlockByPlayer(world, player, x, y, z)) {
            return false;
        }
        block.onBlockDestroyedByPlayer(world, x, y, z, meta);

        if (!isCreative && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {

            IHarvestCallback.HarvestMode mode =
                    callback != null
                            ? callback.getMode(world, block, x, y, z, meta, player)
                            : IHarvestCallback.HarvestMode.NORMAL;

            switch (mode) {
                case NORMAL:
                    block.harvestBlock(world, player, x, y, z, meta);
                    break;

                case SMELT:
                    // custom drop, NO XP
                    callback.handleCustom(world, block, x, y, z, meta, player);
                    break;

                case CUSTOM:
                    callback.handleCustom(world, block, x, y, z, meta, player);
                    break;

                case VOID:
                    // nothing
                    break;
            }
        }

        if (mp != null) {
            mp.playerNetServerHandler.sendPacketToPlayer(
                    new Packet53BlockChange(x, y, z, world)
            );
        }

        return true;
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
