package dev.matthe815.hunterarmorupgrades.blocks;

import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.Registration;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Arrays;
import java.util.List;

public class BlockArmorSphere extends Block {
    public BlockArmorSphere() {
        super(Properties.create(Material.IRON));
        this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere_ore"));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return Arrays.asList(new ItemStack[] { new ItemStack(Registration.ARMOR_SPHERE) });
    }

    public static class ItemBlockArmorCrafter extends BlockItem {
        public ItemBlockArmorCrafter(Block blockIn) {
            super(blockIn, new Properties().group(Registration.HUNTER_ARMOR));
            this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere_ore"));
        }
    }
}