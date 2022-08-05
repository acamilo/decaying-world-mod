package net.acamilo.decayingworldmod.block.custom;

import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.block.entity.ModBlockEntities;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ProtectionBlock extends BaseEntityBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    private static final Logger LOGGER = LogUtils.getLogger();
    public ProtectionBlock(Properties properties) {
        super(properties);
    }




    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(LIT);

    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
        if(!level.isClientSide()) {
            ProtectionBlockEntity.removePosition(blockPos, level);
        }
        return super.onDestroyedByPlayer(state, level, blockPos, player, willHarvest, fluid);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        super.onPlace(blockState, level, blockPos, blockState1, b);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ProtectionBlockEntity) {
                ((ProtectionBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof ProtectionBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (ProtectionBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ProtectionBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.PROTECTION_BLOCK_ENTITY.get(),
                ProtectionBlockEntity::tick);

    }
    /*

    When protection block is placed, it flags blocks around it.
    when the decay block is consuming a neighbor, it checks for this flag, if found it skips.

    When a protection block is destroyed, it un-flags blocks.

     */
}
