package net.acamilo.decayingworldmod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.acamilo.decayingworldmod.block.ModBlocks;

public class ModCreativeModeTab {
    public static final CreativeModeTab DECAYING_WORLD_MOD_TAB = new CreativeModeTab("decayingworldmodtab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.AETHER_DUST.get());
        }
    };
}