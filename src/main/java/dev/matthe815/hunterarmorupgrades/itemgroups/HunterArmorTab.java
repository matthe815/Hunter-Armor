package dev.matthe815.hunterarmorupgrades.itemgroups;

import dev.matthe815.hunterarmorupgrades.Registration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class HunterArmorTab extends ItemGroup {
    public HunterArmorTab() {
        super("hunter_armor");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Registration.ARMOR_SPHERE2);
    }
}
