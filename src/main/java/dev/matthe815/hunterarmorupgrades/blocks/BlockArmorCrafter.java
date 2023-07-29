package dev.matthe815.hunterarmorupgrades.blocks;

import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockArmorCrafter extends Block {
    public BlockArmorCrafter() {
        super(Properties.create(Material.IRON));
        this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_crafter"));

    }

    public static class ItemBlockArmorCrafter extends BlockItem {
        public ItemBlockArmorCrafter(Block blockIn) {
            super(blockIn, new Properties());
            this.setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_crafter"));
        }
    }

    /**
     * Uses an inner class as an INamedContainerProvider.  This does two things:
     *   1) Provides a name used when displaying the container, and
     *   2) Creates an instance of container on the server which is linked to the ItemFlowerBag
     * You could use SimpleNamedContainerProvider with a lambda instead, but I find this method easier to understand
     * I've used a static inner class instead of a non-static inner class for the same reason
     */
    private static class ContainerProviderArmorCrafter implements INamedContainerProvider {
        public ContainerProviderArmorCrafter() {}

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("title.hunterarmorupgrades.armorcrafter");
        }

        /**
         * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
         */
        @Override
        public ContainerArmorCrafter createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            return new ContainerArmorCrafter(windowID, playerInventory);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        INamedContainerProvider provider = new ContainerProviderArmorCrafter();

        if (!worldIn.isRemote) NetworkHooks.openGui((ServerPlayerEntity) player, provider);

        return ActionResultType.SUCCESS;
    }
}
