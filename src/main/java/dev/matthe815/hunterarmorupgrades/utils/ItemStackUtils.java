package dev.matthe815.hunterarmorupgrades.utils;

import dev.matthe815.hunterarmorupgrades.Registration;
import dev.matthe815.hunterarmorupgrades.upgrades.Upgrade;
import dev.matthe815.hunterarmorupgrades.upgrades.UpgradeRoute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {
    public static Map<Item, UpgradeRoute> ingredientList = new HashMap<>();

    private static UpgradeRoute getIronIngredientList (ItemStack[] finalResult) {
        return new UpgradeRoute(new Upgrade[] {
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Registration.IRON_PLATE, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 2), new ItemStack(Items.NETHER_STAR, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 5), new ItemStack(Registration.IRON_RIVETS, 3) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 6) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 7) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 8) }, finalResult)
            }
        );
    }

    private static UpgradeRoute getLeatherIngredientList (ItemStack[] finalResult) {
        return new UpgradeRoute(new Upgrade[] {
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 3) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 4) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 5) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 6) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 7) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 8) }, finalResult)
        }
        );
    }

    public static void init()
    {
        ingredientList.put(Items.IRON_HELMET, getIronIngredientList(new ItemStack[] { new ItemStack(Items.DIAMOND_HELMET), new ItemStack(Items.GOLDEN_HELMET) }));
        ingredientList.put(Items.IRON_CHESTPLATE, getIronIngredientList(new ItemStack[] { new ItemStack(Items.DIAMOND_CHESTPLATE), new ItemStack(Items.GOLDEN_CHESTPLATE) }));
        ingredientList.put(Items.IRON_LEGGINGS, getIronIngredientList(new ItemStack[] { new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.GOLDEN_LEGGINGS) }));
        ingredientList.put(Items.IRON_BOOTS, getIronIngredientList(new ItemStack[] { new ItemStack(Items.DIAMOND_BOOTS), new ItemStack(Items.GOLDEN_BOOTS) }));

        ingredientList.put(Items.LEATHER_HELMET, getLeatherIngredientList(new ItemStack[] { new ItemStack(Items.IRON_HELMET) }));
        ingredientList.put(Items.LEATHER_CHESTPLATE, getLeatherIngredientList(new ItemStack[] { new ItemStack(Items.IRON_CHESTPLATE) }));
        ingredientList.put(Items.LEATHER_LEGGINGS, getLeatherIngredientList(new ItemStack[] { new ItemStack(Items.IRON_LEGGINGS) }));
        ingredientList.put(Items.LEATHER_BOOTS, getLeatherIngredientList(new ItemStack[] { new ItemStack(Items.IRON_BOOTS) }));
    }

    public static Upgrade getUpgradeForLevel (Item item, int level) {
        if (!ingredientList.containsKey(item)) return null;
        return ingredientList.get(item).ingredientsForUpgrade[level];
    }

    public static ItemStack getHeldItemStack (PlayerEntity player, ItemStack itemStack)
    {
        return player.inventory.getStackInSlot(player.inventory.getSlotFor(itemStack));
    }

    public static List<ItemStack> getIngredientList (ItemStack item, World world, int level)
    {
        if (ingredientList.containsKey(item.getItem()))
            return ingredientList.get(item.getItem()).getIngredientsForLevel(level);

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