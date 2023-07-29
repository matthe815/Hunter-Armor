package dev.matthe815.hunterarmorupgrades.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtils {

    public static ItemStack getHeldItemStack (PlayerEntity player, ItemStack itemStack)
    {
        return player.inventory.getStackInSlot(player.inventory.getSlotFor(itemStack));
    }

    public static List<ItemStack> makeMergedList (ItemStack item, World world)
    {
        return mergeIngredients(getUpgradeMaterials(item, world));
    }

    public static List<ItemStack> mergeIngredients (NonNullList<Ingredient> ingredients) {
        List<ItemStack> items = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getMatchingStacks().length == 0) continue;

            boolean found = false;

            for (ItemStack item : items) {
                if (item.getItem().getRegistryName().equals(ingredient.getMatchingStacks()[0].getItem().getRegistryName())) {
                    item.setCount(item.getCount() + 1);
                    found = true;
                    break;
                }
            }

            if (found) continue;

            items.add(ingredient.getMatchingStacks()[0].copy());
        }

        return items;
    }

    public static NonNullList<Ingredient> getUpgradeMaterials (ItemStack item, World world) {
        NonNullList<Ingredient> items = world.getRecipeManager().getRecipe(item.getItem().getRegistryName()).get().getIngredients();
        return items;
    }

}
