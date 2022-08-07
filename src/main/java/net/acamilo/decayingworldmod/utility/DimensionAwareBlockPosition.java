package net.acamilo.decayingworldmod.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionAwareBlockPosition {
    public BlockPos position;
    public ResourceKey<Level> level;

    public DimensionAwareBlockPosition(BlockPos p, ResourceKey<Level> l) {
        position = p;
        level = l;
    }

    @Override
    public int hashCode() {
        return position.hashCode() * level.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof DimensionAwareBlockPosition) {
            DimensionAwareBlockPosition cp = (DimensionAwareBlockPosition) o;
            if (cp.position.equals(position) && cp.level.equals(level))
                return true;
        }
        return false;
    }


}
