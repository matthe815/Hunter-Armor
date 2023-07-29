package dev.matthe815.hunterarmorupgrades.network;

import com.google.common.collect.Multimap;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.mixin.ItemMixin;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PacketArmorUpgrade {
    public PacketArmorUpgrade() {}
    public static PacketArmorUpgrade decode(PacketBuffer buf) { return new PacketArmorUpgrade(); }
    public static void encode(PacketArmorUpgrade msg, PacketBuffer buf) {}

    public static class Handler
    {
        /**
         * Required for the packet to function, but performs no operation.
         */
        public static void handleServer(final PacketArmorUpgrade pkt, Supplier<NetworkEvent.Context> ctx) {
            ItemStack item = ((ContainerArmorCrafter)ctx.get().getSender().openContainer).crafterInventory.getStackInSlot(0);

            // Only process armors
            if (!(item.getItem() instanceof ArmorItem)) return;
            if (!checkUpgradeRequirements(ctx.get().getSender(), (ContainerArmorCrafter) ctx.get().getSender().openContainer)) return;
            takeUpgradeItems(ctx.get().getSender(), (ContainerArmorCrafter) ctx.get().getSender().openContainer);

            CompoundNBT nbt = item.getTag();

            if (nbt == null) nbt = new CompoundNBT();
            int currentLevel = nbt.getInt("upgrade_level") + 1;

            nbt.putInt("upgrade_level", currentLevel);

            if (currentLevel == 1) {
                item.addAttributeModifier(Attributes.ARMOR,
                        new AttributeModifier("Armor modifier", ((ArmorItem) item.getItem()).getDamageReduceAmount(), AttributeModifier.Operation.ADDITION), ((ArmorItem) item.getItem()).getEquipmentSlot());
            }

            item.addAttributeModifier(Attributes.ARMOR,
                    new AttributeModifier("Armor upgrade", ((ArmorItem) item.getItem()).getDamageReduceAmount() / 2, AttributeModifier.Operation.ADDITION), ((ArmorItem)item.getItem()).getEquipmentSlot());

            item.setTag(nbt);
        }

        private static void takeUpgradeItems(PlayerEntity player, ContainerArmorCrafter container) {
            List<ItemStack> ingredients = mergeIngredients(getUpgradeMaterials(((ContainerArmorCrafter)player.openContainer).crafterInventory.getStackInSlot(0), player.world));

            for (ItemStack ingredient: ingredients) {
                // Show as able if you have enough items
                ItemStack item = player.inventory.getStackInSlot(player.inventory.getSlotFor(ingredient));
                item.setCount(item.getCount() - ingredient.getCount());
            }

            return;
        }

        protected static List<ItemStack> mergeIngredients (NonNullList<Ingredient> ingredients) {
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

        protected static NonNullList<Ingredient> getUpgradeMaterials (ItemStack item, World world) {
            NonNullList<Ingredient> items = world.getRecipeManager().getRecipe(item.getItem().getRegistryName()).get().getIngredients();
            return items;
        }

        private static boolean checkUpgradeRequirements(PlayerEntity player, ContainerArmorCrafter container) {
            List<ItemStack> ingredients = mergeIngredients(getUpgradeMaterials(((ContainerArmorCrafter)player.openContainer).crafterInventory.getStackInSlot(0), player.world));

            boolean valid = true;

            for (ItemStack ingredient: ingredients) {
                // Show as able if you have enough items
                if (valid == true) valid = player.inventory.hasItemStack(ingredient) && player.inventory.getStackInSlot(player.inventory.getSlotFor(ingredient)).getCount() >= ingredient.getCount();
            }

            return valid;
        }
    }
}
