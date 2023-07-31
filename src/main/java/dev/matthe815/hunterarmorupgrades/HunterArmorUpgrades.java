package dev.matthe815.hunterarmorupgrades;

import dev.matthe815.hunterarmorupgrades.blocks.BlockArmorCrafter;
import dev.matthe815.hunterarmorupgrades.blocks.BlockArmorSphere;
import dev.matthe815.hunterarmorupgrades.events.ArmorEvents;
import dev.matthe815.hunterarmorupgrades.gui.screens.ScreenArmorCrafter;
import dev.matthe815.hunterarmorupgrades.loot.SimpleChestModifier;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod("hunterarmorupgrades")
public class HunterArmorUpgrades {
    public static final String MOD_ID = "hunterarmorupgrades";
    private static final String PROTOCOL_VERSION = "1";
    public static final Block ARMOR_CRAFTER = new BlockArmorCrafter();
    public static final Block ARMOR_SPHERE_ORE = new BlockArmorSphere();
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "events"), () -> PROTOCOL_VERSION, s -> true, s -> true);

    public HunterArmorUpgrades() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Registration.init();
        ItemStackUtils.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(0, PacketArmorUpgrade.class, PacketArmorUpgrade::encode, PacketArmorUpgrade::decode, PacketArmorUpgrade.Handler::handleServer);
        MinecraftForge.EVENT_BUS.register(new ArmorEvents());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.ARMOR_CRAFTER.get(), ScreenArmorCrafter::new);
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

    private static class DataProvider extends GlobalLootModifierProvider
    {
        public DataProvider(DataGenerator gen, String modid)
        {
            super(gen, modid);
        }

        @Override
        protected void start()
        {
            add("dungeon_mod", Registration.DUNGEON_MOD.get(), new SimpleChestModifier(
                    new ILootCondition[] { LootTableIdCondition.builder(new ResourceLocation("chests/simple_dungeon")).build() })
            );
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void runData(GatherDataEvent event)
        {
            event.getGenerator().addProvider(new DataProvider(event.getGenerator(), MOD_ID));
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> blockRegistryEvent) {
            blockRegistryEvent.getRegistry().register(Registration.IRON_PLATE);
            blockRegistryEvent.getRegistry().register(Registration.IRON_RIVETS);
            blockRegistryEvent.getRegistry().register(Registration.LEATHER_STRAP);
            blockRegistryEvent.getRegistry().register(Registration.PROCESSED_HIDE);
            blockRegistryEvent.getRegistry().register(Registration.ARMOR_SPHERE);
            blockRegistryEvent.getRegistry().register(Registration.ARMOR_SPHERE2);
            blockRegistryEvent.getRegistry().register(Registration.DIAMOND_PLATE);
            blockRegistryEvent.getRegistry().register(Registration.DIAMOND_RIVETS);
            blockRegistryEvent.getRegistry().register(Registration.DIAMOND_SHARD);
            blockRegistryEvent.getRegistry().register(Registration.ROTTEN_HEART);
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            blockRegistryEvent.getRegistry().register(ARMOR_CRAFTER);
            blockRegistryEvent.getRegistry().register(ARMOR_SPHERE_ORE);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegisteryEvent) {
            itemRegisteryEvent.getRegistry().register(new BlockArmorCrafter.ItemBlockArmorCrafter(ARMOR_CRAFTER));
            itemRegisteryEvent.getRegistry().register(new BlockArmorSphere.ItemBlockArmorCrafter(ARMOR_SPHERE_ORE));
        }
    }
}
