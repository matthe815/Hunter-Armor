package dev.matthe815.hunterarmorupgrades.upgrades;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class Upgrade {
    public ItemStack[] ingredients;
    public ItemStack[] results;

    public Upgrade (ItemStack[] ingredients, @Nullable ItemStack[] results) {
        this.ingredients = ingredients;
        this.results = results;
    }

    public List<ItemStack> asList () {
        return Arrays.asList(ingredients);
    }
}
