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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

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
            ItemStack repairItem = ((ArmorItem)container.crafterInventory.getStackInSlot(0).getItem()).getArmorMaterial().getRepairMaterial().getMatchingStacks()[0];
            if (!player.inventory.hasItemStack(repairItem)) return;
            int repairSlot = player.inventory.getSlotFor(repairItem);
            player.inventory.getStackInSlot(repairSlot).setCount(player.inventory.getStackInSlot(repairSlot).getCount() - 1);

            return;
        }

        private static boolean checkUpgradeRequirements(PlayerEntity player, ContainerArmorCrafter container) {
            ItemStack repairItem = ((ArmorItem)container.crafterInventory.getStackInSlot(0).getItem()).getArmorMaterial().getRepairMaterial().getMatchingStacks()[0];
            return player.inventory.hasItemStack(repairItem);
        }
    }
}
