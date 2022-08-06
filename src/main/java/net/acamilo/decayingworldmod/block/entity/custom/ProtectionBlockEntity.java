package net.acamilo.decayingworldmod.block.entity.custom;

import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.block.entity.ModBlockEntities;
import net.acamilo.decayingworldmod.item.ModItems;
import net.acamilo.decayingworldmod.screen.ProtectionBlockMenu;
import net.acamilo.decayingworldmod.utility.DimensionAwareBlockPosition;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashSet;

public class ProtectionBlockEntity extends BlockEntity implements MenuProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    // Position of all beacons.
    public static HashSet<DimensionAwareBlockPosition> PROTECTED_BLOCKS = new HashSet<DimensionAwareBlockPosition>();
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 300;//3600*20;

    private boolean protectionEventFired = false;
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

    public static void registerPosition(DimensionAwareBlockPosition b){
        PROTECTED_BLOCKS.add(b);
        LOGGER.debug("Added position  (" + PROTECTED_BLOCKS.size() + ")");
    }

    public static void removePosition(DimensionAwareBlockPosition b){
        PROTECTED_BLOCKS.remove(b);
        LOGGER.debug("Removed position  (" + PROTECTED_BLOCKS.size() + ")");
    }

    public static boolean isProtected(BlockPos b){
        for (DimensionAwareBlockPosition prot : PROTECTED_BLOCKS){

            if (getDistance(b,prot.position)<32){
                return true;
            }
        }
        return false;
    }

    private static double getDistance(BlockPos a, BlockPos b){
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double deltaZ = a.getZ() - b.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
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
            registerPosition(new DimensionAwareBlockPosition(this.worldPosition,this.level.dimensionType()));
        }
    }

    @Override
    public void onChunkUnloaded() {
        if(!this.level.isClientSide()) {
            removePosition(new DimensionAwareBlockPosition(this.worldPosition,this.level.dimensionType()));
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

        if (pBlockEntity.progress==0 && pBlockEntity.protectionEventFired==false)
            if(!pBlockEntity.level.isClientSide()) {
                removePosition(new DimensionAwareBlockPosition(pPos,pLevel.dimensionType()));
                pBlockEntity.protectionEventFired=true;
            }

        if (pBlockEntity.progress>0){
            pBlockEntity.progress--;
            setChanged(pLevel,pPos,pState);
        }
        if (burnItem(pBlockEntity))
            if(!pBlockEntity.level.isClientSide()) {
                registerPosition(new DimensionAwareBlockPosition(pPos,pLevel.dimensionType()));
                pBlockEntity.protectionEventFired=false;
            }


    }

    private static boolean burnItem(ProtectionBlockEntity entity){
        boolean hasItemInSlot = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.AETHER_DUST.get();
        if (hasItemInSlot && entity.progress==0){
            entity.itemHandler.extractItem(0, 1, false);
            entity.progress= entity.maxProgress;
            return true;
        }
    return false;

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
