package dev.matthe815.hunterarmorupgrades.events;

import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
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

    public static final ConfiguredFeature<?, ?> armorSphereOre = Feature.ORE.withConfiguration(
                    new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, HunterArmorUpgrades.ARMOR_SPHERE_ORE.getDefaultState(), 2)) //vein size
            .range(40).square() //maximum y level where ore can spawn
            .count(30); //how many veins maximum per chunck

    @SubscribeEvent
    public void OnBiomeLoad(BiomeLoadingEvent e) {
        BiomeGenerationSettingsBuilder generation = e.getGeneration();
        if (e.getCategory() != Biome.Category.NETHER && e.getCategory() != Biome.Category.THEEND) {
            generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, armorSphereOre);
        }
    }
}
