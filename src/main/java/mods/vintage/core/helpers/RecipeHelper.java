package mods.vintage.core.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;

public class RecipeHelper {

    public static void removeRecipeByOutput(ItemStack stack) {
        if (stack != null) {
            ArrayList<IRecipe> recipeList = (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < recipeList.size(); i++) {
                if (StackHelper.areStacksEqual(stack, recipeList.get(i).getRecipeOutput())) {
                    recipeList.remove(i);
                }
            }
        }
    }
}
