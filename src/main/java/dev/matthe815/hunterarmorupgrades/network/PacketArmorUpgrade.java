package dev.matthe815.hunterarmorupgrades.network;

import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.upgrades.Upgrade;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class PacketArmorUpgrade {
    public int upgradeIndex = 0;

    public PacketArmorUpgrade(int upgradeIndex) {
        this.upgradeIndex = upgradeIndex;
    }

    public static PacketArmorUpgrade decode(PacketBuffer buf) {
        return new PacketArmorUpgrade(buf.readInt());
    }

    public static void encode(PacketArmorUpgrade msg, PacketBuffer buf) {
        buf.writeInt(msg.upgradeIndex);
    }

    public static class Handler
    {
        /**
         * Required for the packet to function, but performs no operation.
         */
        public static void handleServer(final PacketArmorUpgrade pkt, Supplier<NetworkEvent.Context> ctx) {
            ContainerArmorCrafter container = ((ContainerArmorCrafter) Objects.requireNonNull(ctx.get().getSender()).openContainer);
            PlayerEntity player = Objects.requireNonNull(ctx.get().getSender());
            ItemStack item = container.getSlotItem();
            ItemStack newItem = new ItemStack(container.getSlotItem().getItem());

            // Only process armors
            if (!(item.getItem() instanceof ArmorItem)) return;

            CompoundNBT nbt = item.getTag();

            if (nbt == null) nbt = new CompoundNBT();
            int currentLevel = nbt.getInt("upgrade_level");

            if (!checkUpgradeRequirements(player, container, currentLevel)) return;
            takeUpgradeItems(player, (ContainerArmorCrafter) ctx.get().getSender().openContainer, currentLevel);

            Upgrade route = ItemStackUtils.getUpgradeForLevel(item.getItem(), currentLevel);

            // Handle evolving the armor.
            if (route.results != null) {
                container.crafterInventory.setInventorySlotContents(0, route.results[pkt.upgradeIndex]);
                return;
            }

            currentLevel++;

            CompoundNBT newNbt = new CompoundNBT();
            newNbt.putInt("upgrade_level", currentLevel);

            item.getEnchantmentTagList().forEach(inbt -> {
                newItem.getEnchantmentTagList().add(inbt);
            });

            newItem.setTag(newNbt);

            newItem.addAttributeModifier(Attributes.ARMOR,
                    new AttributeModifier("Armor modifier", ((ArmorItem) item.getItem()).getDamageReduceAmount() + (((ArmorItem) item.getItem()).getDamageReduceAmount() / 2) * currentLevel, AttributeModifier.Operation.ADDITION), ((ArmorItem)item.getItem()).getEquipmentSlot());

            container.crafterInventory.setInventorySlotContents(0, newItem);
        }

        private static void takeUpgradeItems(PlayerEntity player, ContainerArmorCrafter container, int level) {
            List<ItemStack> ingredients = ItemStackUtils.getIngredientList(container.getSlotItem(), player.world, level);

            for (ItemStack ingredient: ingredients) {
                // Show as able if you have enough items
                ItemStack item = ItemStackUtils.getHeldItemStack(player, ingredient);
                item.setCount(item.getCount() - ingredient.getCount());
            }

        }

        private static boolean checkUpgradeRequirements(PlayerEntity player, ContainerArmorCrafter container, int level) {
            List<ItemStack> ingredients = ItemStackUtils.getIngredientList(container.getSlotItem(), player.world, level);

            boolean valid = true;

            for (ItemStack ingredient: ingredients) {
                // Show as able if you have enough items
                valid = container.hasIngredient(ingredient);
                if (!valid) break;
            }

            return valid;
        }
    }
}
