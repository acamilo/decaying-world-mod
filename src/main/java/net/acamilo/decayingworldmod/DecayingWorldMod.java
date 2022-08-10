package net.acamilo.decayingworldmod;

import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.ModBlockEntities;
import net.acamilo.decayingworldmod.item.ModItems;
import net.acamilo.decayingworldmod.screen.ModMenuTypes;
import net.acamilo.decayingworldmod.screen.ProtectionBlockScreen;
import net.acamilo.decayingworldmod.world.feature.ModConfiguredFeatures;
import net.acamilo.decayingworldmod.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DecayingWorldMod.MOD_ID)
public class DecayingWorldMod
{
    public static final String MOD_ID = "decayingworldmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DecayingWorldMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DecayingWorldOptionsHolder.COMMON_SPEC);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);


        //MinecraftForge.EVENT_BUS.register(new DecayingWorldChunkModifier());

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        //modEventBus.addListener(this::clientSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLCommonSetupEvent event)
    {

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new DecaySpawnEventHandler());


        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.AETHER_ROSE.getId(), ModBlocks.POTTED_AETHER_ROSE);
        });




    }




    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.PROTECTION_BLOCK_MENU.get(), ProtectionBlockScreen::new);
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AETHER_ROSE.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_AETHER_ROSE.get(), RenderType.cutout());
        }
    }
}
