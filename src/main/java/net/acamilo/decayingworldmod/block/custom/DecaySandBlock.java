package net.acamilo.decayingworldmod.block.custom;

import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DecaySandBlock extends FallingBlock {

    public DecaySandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState state1, FallingBlockEntity fallingBlockEntity) {
        super.onLand(level, pos, state, state1, fallingBlockEntity);
        // When the block lands, spawn more decay.
        if (ProtectionBlockEntity.isProtected(pos)){
            //serverLevel.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
            return;
        }
        level.setBlockAndUpdate(pos.below(), ModBlocks.DECAY_BLOCK.get().defaultBlockState());

    }
}
