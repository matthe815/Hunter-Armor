package dev.matthe815.hunterarmorupgrades.utils;

import dev.matthe815.hunterarmorupgrades.Registration;
import dev.matthe815.hunterarmorupgrades.upgrades.Upgrade;
import dev.matthe815.hunterarmorupgrades.upgrades.UpgradeRoute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

    private static UpgradeRoute getDiamondIngredientList (ItemStack[] finalResult) {
        return new UpgradeRoute(new Upgrade[] {
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 2), new ItemStack(Registration.DIAMOND_PLATE, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.NETHER_STAR, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 5), new ItemStack(Registration.DIAMOND_RIVETS, 3) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 3), new ItemStack(Registration.DIAMOND_RIVETS, 3), new ItemStack(Items.FEATHER, 4) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 2), new ItemStack(Items.EMERALD, 1)  }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 6), new ItemStack(Registration.DIAMOND_RIVETS, 4), new ItemStack(Registration.ARMOR_SPHERE2) }, finalResult)
        }
        );
    }

    private static UpgradeRoute getIronIngredientList (ItemStack[] finalResult) {
        return new UpgradeRoute(new Upgrade[] {
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.GUNPOWDER, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 2), new ItemStack(Registration.IRON_PLATE, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 2), new ItemStack(Registration.IRON_RIVETS, 3) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 3), new ItemStack(Registration.IRON_RIVETS, 3), new ItemStack(Items.FEATHER, 4) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.DIAMOND, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 2), new ItemStack(Items.PUMPKIN, 3), new ItemStack(Items.CLAY, 2)  }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.IRON_PLATE, 3), new ItemStack(Registration.IRON_RIVETS, 2), new ItemStack(Registration.ARMOR_SPHERE2) }, finalResult)
            }
        );
    }

    private static UpgradeRoute getLeatherIngredientList (ItemStack[] finalResult) {
        return new UpgradeRoute(new Upgrade[] {
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 1), new ItemStack(Registration.LEATHER_STRAP, 1) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 2), new ItemStack(Registration.LEATHER_STRAP, 1), new ItemStack(Items.BONE, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 1), new ItemStack(Registration.PROCESSED_HIDE, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.PROCESSED_HIDE), new ItemStack(Items.FEATHER, 2) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 1), new ItemStack(Registration.LEATHER_STRAP, 1), new ItemStack(Items.IRON_INGOT, 2)}, null),
                new Upgrade(new ItemStack[] { new ItemStack(Items.LEATHER, 2), new ItemStack(Registration.PROCESSED_HIDE, 2), new ItemStack(Items.GUNPOWDER, 3) }, null),
                new Upgrade(new ItemStack[] { new ItemStack(Registration.PROCESSED_HIDE, 2), new ItemStack(Registration.ARMOR_SPHERE, 1) }, finalResult)
        }
        );
    }

    public static void init()
    {
        ingredientList.put(Items.DIAMOND_HELMET, getDiamondIngredientList(null));
        ingredientList.put(Items.DIAMOND_CHESTPLATE, getDiamondIngredientList(null));
        ingredientList.put(Items.DIAMOND_LEGGINGS, getDiamondIngredientList(null));
        ingredientList.put(Items.DIAMOND_BOOTS, getDiamondIngredientList(null));

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
        if (level >= ingredientList.get(item).ingredientsForUpgrade.length) return null;
        return ingredientList.get(item).ingredientsForUpgrade[level];
    }

    public static ItemStack getHeldItemStack (ServerPlayerEntity player, ItemStack stack)
    {
        ItemStack heldStack = null;
        for (ItemStack stackd : player.inventory.mainInventory) {
            if (stack.getItem().equals(stackd.getItem())) {
                heldStack = stackd;
                break;
            }
        }
        return heldStack;
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
        if (world.getRecipeManager().getRecipe(item.getItem().getRegistryName()).isPresent()) {
            NonNullList<Ingredient> items = world.getRecipeManager().getRecipe(item.getItem().getRegistryName()).get().getIngredients();
            return items;
        }
        return null;
    }

}
