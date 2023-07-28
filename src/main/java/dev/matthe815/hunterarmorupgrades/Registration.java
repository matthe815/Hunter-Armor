package dev.matthe815.hunterarmorupgrades;

import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, HunterArmorUpgrades.MOD_ID);

    public static final RegistryObject<ContainerType<ContainerArmorCrafter>> ARMOR_CRAFTER = CONTAINERS.register("armor_crafter", () -> IForgeContainerType.create((windowId, inv, data) -> {
       return new ContainerArmorCrafter(windowId, inv);
    }));

    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
