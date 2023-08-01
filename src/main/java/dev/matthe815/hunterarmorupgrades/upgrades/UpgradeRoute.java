package dev.matthe815.hunterarmorupgrades.upgrades;

import net.minecraft.item.ItemStack;

import java.util.List;

public class UpgradeRoute {
    public Upgrade[] ingredientsForUpgrade;

    public UpgradeRoute (Upgrade[] upgrades) {
        this.ingredientsForUpgrade = upgrades;
    }

    public List<ItemStack> getIngredientsForLevel (int level) {
        if (level >= ingredientsForUpgrade.length) return null;
        return ingredientsForUpgrade[level].asList();
    }

    public ItemStack[] getNextResults (int level) {
        return ingredientsForUpgrade[level].results;
    }

}
