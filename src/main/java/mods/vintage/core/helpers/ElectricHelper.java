package mods.vintage.core.helpers;

import mods.vintage.core.platform.lang.Translator;
import mods.vintage.core.utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class ElectricHelper {

    public static String energyTooltip(int current, int max, int tier) {
        DecimalFormat formatter = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ROOT));
        return Translator.AQUA.format("info.energy", formatter.format(current), formatter.format(max), Translator.DARK_GRAY.format("info.energy.tier", Translator.YELLOW.literal(getTierForDisplay(tier))));
    }

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

    public static String getTierForDisplay(int tier) {
        String suffix;
        Translator formatter;

        switch (tier) {
            case 0:  suffix = "ulv"; formatter = Translator.DARK_GRAY; break;
            case 1:  suffix = "lv";  formatter = Translator.GRAY; break;
            case 2:  suffix = "mv";  formatter = Translator.AQUA; break;
            case 3:  suffix = "hv";  formatter = Translator.GOLD; break;
            case 4:  suffix = "ev";  formatter = Translator.DARK_PURPLE; break;
            case 5:  suffix = "iv";  formatter = Translator.BLUE; break;
            case 6:  suffix = "luv"; formatter = Translator.LIGHT_PURPLE; break;
            case 7:  suffix = "zpm"; formatter = Translator.RED; break;
            case 8:  suffix = "uv";  formatter = Translator.DARK_AQUA; break;
            case 9:  suffix = "uhv"; formatter = Translator.WHITE; break;
            case 10: suffix = "uev"; formatter = Translator.WHITE; break;
            case 11: suffix = "uiv"; formatter = Translator.WHITE; break;
            case 12: suffix = "umv"; formatter = Translator.WHITE; break;
            case 13: suffix = "uxv"; formatter = Translator.WHITE; break;
            case 14: suffix = "max"; formatter = Translator.WHITE; break;
            default:
                return Translator.RED.literal("ERROR, please report!");
        }
        return formatter.format("info.tier." + suffix);
    }

    public static int getTierFromEU(int value) {
        return value <= 8 ? 0 : (int) Math.ceil(Math.log((double) value * 0.125) * (1.0 / Math.log(4.0)));
    }

    public static int getMaxInputFromTier(int tier) {
        return 8 << Math.min(tier, 13) * 2;
    }

    public static Translator getTextColor(int value) {
        Translator colorCode = Translator.WHITE; // white
        if (value >= 90) {
            colorCode = Translator.GREEN; // green
        }
        if ((value <= 90) && (value > 75)) {
            colorCode = Translator.YELLOW; // yellow
        }
        if ((value <= 75) && (value > 50)) {
            colorCode = Translator.GOLD; // gold
        }
        if ((value <= 50) && (value > 35)) {
            colorCode = Translator.RED; // red
        }
        if (value <= 35) {
            colorCode = Translator.DARK_RED; // dark_red
        }

        return colorCode;
    }
}
