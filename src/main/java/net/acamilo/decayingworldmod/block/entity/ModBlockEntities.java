package net.acamilo.decayingworldmod.block.entity;

import net.acamilo.decayingworldmod.DecayingWorldMod;
import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DecayingWorldMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ProtectionBlockEntity>> PROTECTION_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("protection_block_entity", () ->
                BlockEntityType.Builder.of(ProtectionBlockEntity::new,
                        ModBlocks.PROTECTION_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
