package dev.matthe815.hunterarmorupgrades.blocks;

import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockArmorSphere extends Block {
    public BlockArmorSphere() {
        super(Properties.create(Material.IRON).setRequiresTool().harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3));
        this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere_ore"));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        int random = new Random().nextInt(10);
        List<ItemStack> generatedLoot = new ArrayList<>();

        if (random > 2) {
            generatedLoot.add(new ItemStack(Registration.ARMOR_SPHERE, new Random().nextInt( 1) + 1));
        }
        if (random < 3) {
            generatedLoot.add(new ItemStack(Registration.ARMOR_SPHERE2, 1));
        }
        return generatedLoot;
    }

    public static class ItemBlockArmorCrafter extends BlockItem {
        public ItemBlockArmorCrafter(Block blockIn) {
            super(blockIn, new Properties().group(Registration.HUNTER_ARMOR));
            this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere_ore"));
        }
    }
}
