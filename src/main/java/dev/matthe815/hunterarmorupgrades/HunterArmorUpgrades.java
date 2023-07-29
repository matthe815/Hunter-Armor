package dev.matthe815.hunterarmorupgrades;

import dev.matthe815.hunterarmorupgrades.blocks.BlockArmorCrafter;
import dev.matthe815.hunterarmorupgrades.gui.screens.ScreenArmorCrafter;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("hunterarmorupgrades")
public class HunterArmorUpgrades {
    public static final String MOD_ID = "hunterarmorupgrades";
    private static final String PROTOCOL_VERSION = "1";
    public static final Block ARMOR_CRAFTER = new BlockArmorCrafter();
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "events"), () -> PROTOCOL_VERSION, s -> true, s -> true);

    public HunterArmorUpgrades() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Registration.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(0, PacketArmorUpgrade.class, PacketArmorUpgrade::encode, PacketArmorUpgrade::decode, PacketArmorUpgrade.Handler::handleServer);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(Registration.ARMOR_CRAFTER.get(), ScreenArmorCrafter::new);
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            blockRegistryEvent.getRegistry().register(ARMOR_CRAFTER);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegisteryEvent) {
            itemRegisteryEvent.getRegistry().register(new BlockArmorCrafter.ItemBlockArmorCrafter(ARMOR_CRAFTER));
        }
    }
}
