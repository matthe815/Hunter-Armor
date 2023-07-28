package dev.matthe815.hunterarmorupgrades.network;

import com.google.common.collect.Multimap;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
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

            CompoundNBT nbt = item.getTag();

            if (nbt == null) nbt = new CompoundNBT();
            int currentLevel = nbt.getInt("upgrade_level");

            nbt.putInt("upgrade_level", currentLevel + 1);

            item.addAttributeModifier(Attributes.ARMOR,
                    new AttributeModifier("Armor modifier", ((ArmorItem) item.getItem()).getDamageReduceAmount() / 2, AttributeModifier.Operation.ADDITION), ((ArmorItem)item.getItem()).getEquipmentSlot());

            item.setDisplayName(new StringTextComponent(item.getItem().getName().getString() + " +" + currentLevel));

            item.setTag(nbt);
        }
    }
}
