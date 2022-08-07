package net.acamilo.decayingworldmod.item;

import net.acamilo.decayingworldmod.DecayingWorldMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DecayingWorldMod.MOD_ID);

    public static final RegistryObject<Item> AETHER_CRYSTAL = ITEMS.register("aether_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.DECAYING_WORLD_MOD_TAB)));

    public static final RegistryObject<Item> AETHER_DUST = ITEMS.register("aether_dust",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.DECAYING_WORLD_MOD_TAB)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
