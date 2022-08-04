package net.acamilo.decayingworldmod.block.custom;

import net.acamilo.decayingworldmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class DecayBlock extends Block {

    public DecayBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState state, boolean b) {
        level.scheduleTick(pos, this, this.spreadDelay());
    }
    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos pos, RandomSource source) {
        BlockPos neighbors[] = {
                pos.above(),
                pos.below(),
                pos.east(),
                pos.west(),
                pos.north(),
                pos.south()
        };
        serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        for (BlockPos b : neighbors){
            BlockState block = serverLevel.getBlockState(b);
            if (block.isAir()==false){
                BlockState bs = ModBlocks.DECAY_BLOCK.get().defaultBlockState();
                serverLevel.setBlockAndUpdate(b,bs);
            }
        }
    }
    protected int spreadDelay() {
        return 20+(int)(Math.random()*50);
    }
}
