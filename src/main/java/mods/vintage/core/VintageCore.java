package mods.vintage.core;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import mods.vintage.core.helpers.BlockHelper;
import mods.vintage.core.platform.commands.CommandGM;
import mods.vintage.core.platform.events.ClientTickEvent;
import mods.vintage.core.platform.lang.LangManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.logging.Logger;

@Mod(modid = Refs.ID, useMetadata = true)
public class VintageCore {

    public static final Logger LOGGER = Logger.getLogger(Refs.LOG_NAME);

    public VintageCore() {
        LOGGER.setParent(FMLLog.getLogger());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent e) {
        VintageConfig.init();
        TickRegistry.registerTickHandler(new ClientTickEvent(), Side.CLIENT);
        LangManager.INSTANCE.scanForLocalizationProviders(e.getAsmData());
    }

    @Mod.Init
    public void init(FMLInitializationEvent e) {
        LangManager.INSTANCE.processLocalizationProviders();
    }

    @Mod.ServerStarting
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandGM());
    }

    @ForgeSubscribe
    public void onRightClick(PlayerInteractEvent e) {
        if (VintageConfig.inspect_mode && e.entityPlayer.getHeldItem() != null) {
            if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.entityPlayer.getHeldItem().getItem() == Item.stick) {
                Block block = BlockHelper.getBlock(e.entity.worldObj, e.x, e.y, e.z);
                int metadata = e.entityPlayer.worldObj.getBlockMetadata(e.x, e.y, e.z);
                if (block != null) {
                    LOGGER.info("Block: " + block.getLocalizedName() + " | Class Name: " + block.getClass().getName());
                    LOGGER.info("Block Metadata: " + metadata);
                }
            }
        }
    }
}