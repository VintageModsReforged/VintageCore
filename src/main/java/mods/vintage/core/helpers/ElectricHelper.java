package mods.vintage.core.helpers;

import mods.vintage.core.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.util.List;

public class ElectricHelper {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addChargeVariants(Item item, List list) {
        list.add(getCharged(item, 0));
        list.add(getCharged(item, Integer.MAX_VALUE));
    }

    public static ItemStack getCharged(Item item, int charge) {
        try {
            if (!Utils.instanceOf(item, "ic2.api.item.IElectricItem")) {
                throw new IllegalArgumentException(item + " must be an instanceof IElectricItem!");
            } else {
                ItemStack ret = new ItemStack(item);
                Class<?> electricItemClass = Class.forName("ic2.api.item.ElectricItem");
                // Get static field "manager"
                Object manager = electricItemClass.getField("manager").get(null);
                // Find the charge method
                // signature: charge(ItemStack, int, int, boolean, boolean)
                Class<?> managerClass = manager.getClass();
                Method chargeMethod = managerClass.getMethod(
                        "charge",
                        ItemStack.class, int.class, int.class, boolean.class, boolean.class
                );
                chargeMethod.invoke(manager, ret, charge, Integer.MAX_VALUE, true, false);

                return ret;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static int getCharge(ItemStack stack) {
        NBTTagCompound tag = StackHelper.getOrCreateTag(stack);
        return tag.getInteger("charge");
    }
}
