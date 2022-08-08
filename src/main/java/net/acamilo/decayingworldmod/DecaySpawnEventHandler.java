package net.acamilo.decayingworldmod;

import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.acamilo.decayingworldmod.utility.DimensionAwareChunkPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class DecaySpawnEventHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int COURRUPTION_RADIUS = 100;
    /*
    public static HashSet<DimensionAwareChunkPosition> LOADED_CHUNKS = new HashSet<DimensionAwareChunkPosition>();
    public static void addChunkPos(DimensionAwareChunkPosition p){
        LOADED_CHUNKS.add(p);
        LOGGER.debug("Added Chunk Position ("+p.position+":"+p.level.effectsLocation().getPath()+") to pool ("+LOADED_CHUNKS.size()+")");
    }
    public static void removeChunkPos(DimensionAwareChunkPosition p){
        LOADED_CHUNKS.remove(p);
        LOGGER.debug("Removed Chunk Position ("+p.position+":"+p.level.effectsLocation().getPath()+") to pool ("+LOADED_CHUNKS.size()+")");
    }

    public static boolean inCourruptionArea(ChunkPos c){
        double deltaX = c.x*16;
        double deltaZ = c.z*16;

        double d = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));
        if (d < COURRUPTION_RADIUS) {
            //LOGGER.debug("d: "+d);
            return true;
        }

        return false;
    }
    @SubscribeEvent
    public void onChunkLoaded(ChunkEvent.Load event){
        LevelAccessor level = event.getLevel();
        DimensionType d = level.dimensionType();
        if(!level.isClientSide()) {
            ChunkAccess ch = event.getChunk();
            if (inCourruptionArea(ch.getPos())) {
                //LOGGER.debug("Chunk is in courruption area, Adding");
                addChunkPos(new DimensionAwareChunkPosition(ch.getPos(), d));
            }
        }

    }

    @SubscribeEvent
    public void onChunkUnloaded(ChunkEvent.Unload event){
        LevelAccessor level = event.getLevel();
        DimensionType d = level.dimensionType();

        if(!level.isClientSide()) {
            ChunkAccess ch = event.getChunk();
            if (inCourruptionArea(ch.getPos()))
                removeChunkPos(new DimensionAwareChunkPosition(event.getChunk().getPos(), d));
        }
    }
*/
    private static int counter = 0;
    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.PlayerTickEvent event){
        if (event.player.level.isClientSide)
            return;
        if (counter>0) {
            counter--;
            return;
        }

        counter = 20*60;
        Player player = event.player;
        BlockPos playerbock = player.getOnPos();

        LOGGER.debug("Protection list size:\t"+ProtectionBlockEntity.PROTECTED_BLOCKS.size());
        LOGGER.debug("IsClientSide:\t"+String.valueOf(event.player.level.isClientSide));
        LOGGER.debug("Player Level:\t"+player.level);
        LOGGER.debug("Player Dim:\t"+player.level.dimensionType());


        int rx = ThreadLocalRandom.current().nextInt(
                playerbock.getX()-COURRUPTION_RADIUS,
                playerbock.getX()+COURRUPTION_RADIUS);
        int ry = ThreadLocalRandom.current().nextInt(
                playerbock.getY()-COURRUPTION_RADIUS,
                playerbock.getY()+COURRUPTION_RADIUS);
        int rz = ThreadLocalRandom.current().nextInt(
                playerbock.getZ()-COURRUPTION_RADIUS,
                playerbock.getZ()+COURRUPTION_RADIUS);
        BlockPos target = new BlockPos(rx,ry,rz);
        BlockState state = player.level.getBlockState(target);
        if (state!=null && !state.isAir()){
            if (ProtectionBlockEntity.isProtected(target,player.level.dimension())){
                LOGGER.debug("Courrupting "+target);
                player.getLevel().setBlockAndUpdate(new BlockPos(rx,ry,rz), ModBlocks.FAST_DECAY_BLOCK.get().defaultBlockState());
            } else {
                LOGGER.debug(target+" in "+ player.level +" is protected");
            }
        } else {
            LOGGER.debug(target+" is air");

        }


        /*
        for(DimensionAwareChunkPosition cp : LOADED_CHUNKS){
            DimensionType l = cp.level;
            ChunkPos c =cp.position;

        }*/

        //
        //event.
        //LOGGER.debug("Server Tick Event!");
    }
}
