package net.acamilo.decayingworldmod.event.loot;


import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

/*
public class ChestLootAdditionModifier extends LootModifier {
    //public static final Supplier<Codec<ChestLootAdditionModifier>> CODEC = Suppliers.memoize(() ->
    //        RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, ChestLootAdditionModifier::new)));
    public ChestLootAdditionModifier(LootItemCondition[] conditions, int numSeeds, Item itemCheck, Item reward) {
        super(conditions);
    }

    @Nonnull
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

        return generatedLoot;
    }
    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return null;
    }
}
*/