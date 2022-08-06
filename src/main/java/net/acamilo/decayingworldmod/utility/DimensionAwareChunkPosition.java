package net.acamilo.decayingworldmod.utility;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionAwareChunkPosition {
    public ChunkPos position;
    public DimensionType level;

    public DimensionAwareChunkPosition(ChunkPos p, DimensionType l) {
        this.position = p;
        this.level = l;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() * this.level.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof DimensionAwareChunkPosition) {
            DimensionAwareChunkPosition cp = (DimensionAwareChunkPosition) o;
            if (cp.position.equals(this.position) && cp.level.equals(this.level))
                return true;
        }
        return false;
    }


}
