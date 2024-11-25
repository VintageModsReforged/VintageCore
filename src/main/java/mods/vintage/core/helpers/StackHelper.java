package mods.vintage.core.helpers;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class StackHelper {

    public static boolean areStacksEqual(ItemStack aStack, ItemStack bStack) {
        return aStack != null && bStack != null &&
                aStack.itemID == bStack.itemID &&
                (aStack.getTagCompound() == null == (bStack.getTagCompound() == null)) &&
                (aStack.getTagCompound() == null || aStack.getTagCompound().equals(bStack.getTagCompound()))
                && (aStack.getItemDamage() == bStack.getItemDamage() || aStack.getItemDamage() == 32767 || bStack.getItemDamage() == 32767);
    }

    public static List<ItemStack> getStackFromOre(String prefix) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (String name : OreDictionary.getOreNames()) {
            if (name.startsWith(prefix)) {
                List<ItemStack> oreDictList = OreDictionary.getOres(name);
                stacks.addAll(oreDictList);
            }
        }
        return stacks;
    }

    public static void dropAsEntity(World world, int x, int y, int z, ItemStack stack) {
        if (stack != null) {
            double offset = 0.7;
            double xPos = (double)world.rand.nextFloat() * offset + (1.0 - offset) * 0.5;
            double yPos = (double)world.rand.nextFloat() * offset + (1.0 - offset) * 0.5;
            double zPos = (double)world.rand.nextFloat() * offset + (1.0 - offset) * 0.5;
            EntityItem drop = new EntityItem(world, (double)x + xPos, (double)y + yPos, (double)z + zPos, stack.copy());
            drop.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(drop);
        }
    }
}
