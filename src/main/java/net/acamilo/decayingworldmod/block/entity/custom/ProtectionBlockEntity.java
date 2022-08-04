package net.acamilo.decayingworldmod.block.entity.custom;

import net.acamilo.decayingworldmod.block.custom.ProtectionBlock;
import net.acamilo.decayingworldmod.block.entity.ModBlockEntities;
import net.acamilo.decayingworldmod.screen.ProtectionBlockMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class ProtectionBlockEntity extends BlockEntity implements MenuProvider {
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();


    public ProtectionBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.PROTECTION_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                switch(index){
                    case 0: return ProtectionBlockEntity.this.progress;
                    case 1: return ProtectionBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0:  ProtectionBlockEntity.this.progress = value; break;
                    case 1:  ProtectionBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }


    @Override
    public Component getDisplayName() {
        return Component.literal("Protection Block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inventory, Player player) {
        return new ProtectionBlockMenu(pContainerId,inventory,this, this.data);
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);

        if(!this.level.isClientSide()) {
            ProtectionBlock.registerPosition(this.worldPosition,this.level);
        }
    }

    @Override
    public void onChunkUnloaded() {
        if(!this.level.isClientSide()) {
            ProtectionBlock.removePosition(this.worldPosition,this.level);
        }
        super.onChunkUnloaded();
    }

    @Override
    public void invalidateCaps()  {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("protection_block.progress",progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("protection_block.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, ProtectionBlockEntity pBlockEntity) {
        pBlockEntity.progress++;
        setChanged(pLevel,pPos,pState);
        if (pBlockEntity.progress > pBlockEntity.maxProgress){
            pBlockEntity.progress=0;
        }
    }

    private static void craftItem(ProtectionBlockEntity entity) {
        entity.itemHandler.extractItem(0, 1, false);
        /*
        entity.itemHandler.extractItem(1, 1, false);
        entity.itemHandler.getStackInSlot(2).hurt(1, new Random(), null);

        entity.itemHandler.setStackInSlot(3, new ItemStack(ModItems.CITRINE.get(),
                entity.itemHandler.getStackInSlot(3).getCount() + 1));

         */
    }

    private static boolean hasRecipe(ProtectionBlockEntity entity) {
        //boolean hasItemInWaterSlot = PotionUtils.getPotion(entity.itemHandler.getStackInSlot(0)) == Potions.WATER;
        //boolean hasItemInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.RAW_CITRINE.get();
        //boolean hasItemInSecondSlot = entity.itemHandler.getStackInSlot(2).getItem() == ModItems.GEM_CUTTER_TOOL.get();

        //return hasItemInWaterSlot && hasItemInFirstSlot && hasItemInSecondSlot;
        return false;
    }

    private static boolean hasNotReachedStackLimit(ProtectionBlockEntity entity) {
        return entity.itemHandler.getStackInSlot(1).getCount() < entity.itemHandler.getStackInSlot(1).getMaxStackSize();
    }
}
