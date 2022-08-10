package net.acamilo.decayingworldmod.block;

import net.acamilo.decayingworldmod.DecayingWorldMod;
import net.acamilo.decayingworldmod.block.custom.DecayBlock;
import net.acamilo.decayingworldmod.block.custom.DecaySandBlock;
import net.acamilo.decayingworldmod.block.custom.FastDecayBlock;
import net.acamilo.decayingworldmod.block.custom.ProtectionBlock;
import net.acamilo.decayingworldmod.item.ModCreativeModeTab;
import net.acamilo.decayingworldmod.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DecayingWorldMod.MOD_ID);
    public static final RegistryObject<Block> AETHER_ROSE = registerBlock("aether_rose",
            () -> new FlowerBlock(MobEffects.LEVITATION, 8,
                    BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion()), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);

    public static final RegistryObject<Block> POTTED_AETHER_ROSE = registerBlockWithoutBlockItem("potted_aether_rose",
            () -> new FlowerPotBlock(null, ModBlocks.AETHER_ROSE,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion()));
    public static final RegistryObject<Block> AETHER_ORE = registerBlock("aether_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(9f).requiresCorrectToolForDrops()),ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);

    public static final RegistryObject<Block> DEEPSLATE_AETHER_ORE = registerBlock("deepslate_aether_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(9f).requiresCorrectToolForDrops()),ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);

    public static final RegistryObject<Block> NETHER_AETHER_ORE = registerBlock("nether_aether_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(9f).requiresCorrectToolForDrops()),ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);

    public static final RegistryObject<Block> DECAY_BLOCK = registerBlock("decay_block",
            () -> new DecayBlock(BlockBehaviour.Properties.of(Material.STONE).randomTicks()), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);
    public static final RegistryObject<Block> FAST_DECAY_BLOCK = registerBlock("fast_decay_block",
            () -> new FastDecayBlock(BlockBehaviour.Properties.of(Material.STONE).randomTicks()), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);

    public static final RegistryObject<Block> DECAY_SAND_BLOCK = registerBlock("decay_sand_block",
            () -> new DecaySandBlock(BlockBehaviour.Properties.of(Material.SAND).randomTicks()), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);



    public static final RegistryObject<Block> PROTECTION_BLOCK = registerBlock("protection_block",
            () -> new ProtectionBlock(BlockBehaviour.Properties
                        .of(Material.STONE).lightLevel(state -> state.getValue(ProtectionBlock.LIT) ? 15:0)), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name,toReturn,tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties().tab(tab)));
    }
    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
