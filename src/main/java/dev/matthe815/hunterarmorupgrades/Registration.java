package dev.matthe815.hunterarmorupgrades;

import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.itemgroups.HunterArmorTab;
import dev.matthe815.hunterarmorupgrades.loot.SimpleChestModifier;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

public class Registration {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HunterArmorUpgrades.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, HunterArmorUpgrades.MOD_ID);

    private static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, HunterArmorUpgrades.MOD_ID);
    public static final RegistryObject<SimpleChestModifier.Serializer> DUNGEON_MOD = GLM.register("dungeon_mod", SimpleChestModifier.Serializer::new);


    public static final RegistryObject<ContainerType<ContainerArmorCrafter>> ARMOR_CRAFTER = CONTAINERS.register("armor_crafter", () -> IForgeContainerType.create((windowId, inv, data) -> new ContainerArmorCrafter(windowId, inv)));

    public static final ItemGroup HUNTER_ARMOR = new HunterArmorTab();
    
    public static final Item LEATHER_STRAP = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "leather_straps"));
    public static final Item PROCESSED_HIDE = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "processed_hide"));
    public static final Item IRON_PLATE = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "iron_plate"));
    public static final Item IRON_RIVETS = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "iron_rivets"));
    public static final Item DIAMOND_PLATE = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "diamond_plate"));
    public static final Item DIAMOND_RIVETS = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "diamond_rivets"));
    public static final Item DIAMOND_SHARD = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "diamond_shard"));
    public static final Item ROTTEN_HEART = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "rotten_heart"));
    public static final Item ARMOR_SPHERE = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere"));
    public static final Item ARMOR_SPHERE2 = new Item(new Item.Properties().group(HUNTER_ARMOR)).setRegistryName(new ResourceLocation(HunterArmorUpgrades.MOD_ID, "armor_sphere2"));


    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
