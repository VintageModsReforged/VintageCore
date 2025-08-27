package mods.vintage.core.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Iterator;

public class RecipeHelper {

    public static void removeRecipeByOutput(ItemStack stack) {
        if (stack != null) {
            ArrayList<IRecipe> recipeList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
            Iterator<IRecipe> iterator = recipeList.iterator();
            while (iterator.hasNext()) {
                IRecipe recipe = iterator.next();
                if (StackHelper.areStacksEqual(stack, recipe.getRecipeOutput())) {
                    iterator.remove();
                }
            }
        }
    }
}
