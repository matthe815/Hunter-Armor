package dev.matthe815.hunterarmorupgrades.network;

import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
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
            ContainerArmorCrafter container = ((ContainerArmorCrafter) Objects.requireNonNull(ctx.get().getSender()).openContainer);
            PlayerEntity player = Objects.requireNonNull(ctx.get().getSender());
            ItemStack item = container.getSlotItem();

            // Only process armors
            if (!(item.getItem() instanceof ArmorItem)) return;
            if (!checkUpgradeRequirements(player, container)) return;

            takeUpgradeItems(player, (ContainerArmorCrafter) ctx.get().getSender().openContainer);

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
            List<ItemStack> ingredients = ItemStackUtils.makeMergedList(container.getSlotItem(), player.world);

            for (ItemStack ingredient: ingredients) {
                // Show as able if you have enough items
                ItemStack item = ItemStackUtils.getHeldItemStack(player, ingredient);
                item.setCount(item.getCount() - ingredient.getCount());
            }

        }

        private static boolean checkUpgradeRequirements(PlayerEntity player, ContainerArmorCrafter container) {
            List<ItemStack> ingredients = ItemStackUtils.makeMergedList(container.getSlotItem(), player.world);

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
