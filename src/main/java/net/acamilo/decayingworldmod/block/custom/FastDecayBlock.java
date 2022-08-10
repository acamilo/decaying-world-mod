package net.acamilo.decayingworldmod.block.custom;

import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.DecayingWorldOptionsHolder;
import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class FastDecayBlock extends Block {
    private static final Logger LOGGER = LogUtils.getLogger();
    public FastDecayBlock(Properties properties) {
        super(properties);

    }
    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState state, boolean b) {
        level.scheduleTick(pos, this, this.spreadDelay());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.randomTick(state, level, pos, randomSource);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos pos, RandomSource source) {
        LOGGER.debug("Fast decay block tick");
        BlockPos neighbors[] = {
                pos.above(),
                pos.below(),
                pos.east(),
                pos.west(),
                pos.north(),
                pos.south()
        };
        // do not spread if block is protected
        if (ProtectionBlockEntity.isProtected(pos,serverLevel.dimension())){
            serverLevel.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
            return;
        }

        // replace self with decay sand

        if (source.nextDouble()>0.05)
            serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        else
            serverLevel.setBlockAndUpdate(pos, ModBlocks.DECAY_SAND_BLOCK.get().defaultBlockState());


        for (BlockPos b : neighbors){
            BlockState block = serverLevel.getBlockState(b);
            if (block.isAir()==false){
                if(!(block.is(ModBlocks.DECAY_BLOCK.get()) || block.is(ModBlocks.FAST_DECAY_BLOCK.get()) || block.is(ModBlocks.DECAY_SAND_BLOCK.get()))) {

                        //LOGGER.info("Fast decay block place");
                        if (source.nextDouble()>DecayingWorldOptionsHolder.COMMON.FAST_DECAY_BLOCK_SPAWN_CHANCE.get())
                            serverLevel.setBlockAndUpdate(b, ModBlocks.DECAY_BLOCK.get().defaultBlockState());
                        else
                            serverLevel.setBlockAndUpdate(b, ModBlocks.FAST_DECAY_BLOCK.get().defaultBlockState());

                }
            }
        }
    }

    protected int spreadDelay() {
        return DecayingWorldOptionsHolder.COMMON.FAST_DECAY_BLOCK_SPREAD_DELAY.get();
    }



}
