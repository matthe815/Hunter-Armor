package dev.matthe815.hunterarmorupgrades.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorEvents {
    // Reduce damage taken while wearing golden chestplate by half.
    @SubscribeEvent
    public void OnDamageTaken(final LivingHurtEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();

        if (!(player.inventory.armorInventory.get(2) != null && player.inventory.armorInventory.get(2).getItem().equals(Items.GOLDEN_CHESTPLATE))) return;
        event.setAmount(event.getAmount() / 2);
    }
}
