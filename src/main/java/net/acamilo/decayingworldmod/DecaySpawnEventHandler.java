package net.acamilo.decayingworldmod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.HashSet;

public class DecaySpawnEventHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static HashSet<DimensionAwareChunkPosition> LOADED_CHUNKS = new HashSet<DimensionAwareChunkPosition>();
    public static void addChunkPos(DimensionAwareChunkPosition p){
        LOADED_CHUNKS.add(p);
        LOGGER.debug("Added Chunk Position ("+p.position+":"+p.level.effectsLocation().getPath()+") to pool ("+LOADED_CHUNKS.size()+")");
    }
    public static void removeChunkPos(DimensionAwareChunkPosition p){
        LOADED_CHUNKS.remove(p);
        LOGGER.debug("Removed Chunk Position ("+p.position+":"+p.level.effectsLocation().getPath()+") to pool ("+LOADED_CHUNKS.size()+")");
    }

    private class DimensionAwareChunkPosition {
        public ChunkPos position;
        public DimensionType level;
        public DimensionAwareChunkPosition(ChunkPos p, DimensionType l){
            this.position = p;
            this.level = l;
        }

        @Override
        public int hashCode() {
            return this.position.hashCode() * this.level.hashCode();
        }
        @Override
        public boolean equals(Object o){
            if (o==null) return false;
            if(o instanceof DimensionAwareChunkPosition) {
                DimensionAwareChunkPosition cp = (DimensionAwareChunkPosition) o;
                if (cp.position.equals(this.position) && cp.level.equals(this.level))
                        return true;
            }
            return false;
        }


    }

    @SubscribeEvent
    public void onChunkLoaded(ChunkEvent.Load event){
        LevelAccessor level = event.getLevel();
        DimensionType d = level.dimensionType();
        if(!level.isClientSide())
            addChunkPos(new DimensionAwareChunkPosition(event.getChunk().getPos(),d));

    }

    @SubscribeEvent
    public void onChunkUnloaded(ChunkEvent.Unload event){
        LevelAccessor level = event.getLevel();
        DimensionType d = level.dimensionType();
        if(!level.isClientSide())
            removeChunkPos(new DimensionAwareChunkPosition(event.getChunk().getPos(),  d  ));
    }

    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.ServerTickEvent event){
        //
        //event.
        //LOGGER.debug("Server Tick Event!");
    }
}
