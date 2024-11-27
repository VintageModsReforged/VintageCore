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
                    // when you remove an item from the list, the index of the subsequent elements decrease by one.
                    // In this loop where we’re incrementing i,
                    // this would cause it to skip the element that moves into the current index position after removal
                    i--;
                }
            }
        }
    }
}
