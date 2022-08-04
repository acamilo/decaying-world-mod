package net.acamilo.decayingworldmod.block;

import net.acamilo.decayingworldmod.DecayingWorldMod;
import net.acamilo.decayingworldmod.block.custom.DecayBlock;
import net.acamilo.decayingworldmod.item.ModCreativeModeTab;
import net.acamilo.decayingworldmod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

    public static final RegistryObject<Block> DECAY_BLOCK = registerBlock("decay_block",
            () -> new DecayBlock(BlockBehaviour.Properties.of(Material.STONE)), ModCreativeModeTab.DECAYING_WORLD_MOD_TAB);
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name,toReturn,tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties().tab(tab)));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
