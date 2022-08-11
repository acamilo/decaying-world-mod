package net.acamilo.decayingworldmod;


import com.mojang.logging.LogUtils;
import net.acamilo.decayingworldmod.block.ModBlocks;
import net.acamilo.decayingworldmod.block.entity.custom.ProtectionBlockEntity;
import net.acamilo.decayingworldmod.item.ModItems;
import net.acamilo.decayingworldmod.utility.DimensionAwareBlockPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import org.slf4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class DecaySpawnEventHandler
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private boolean spawnCourruption = DecayingWorldOptionsHolder.COMMON.SPAWN_COURRUPTION_ENABLE.get();
    private int COURRUPTION_RADIUS = DecayingWorldOptionsHolder.COMMON.DECAY_SPAWN_PLAYER_RADIUS.get();
    private int SAFE_RADIUS = DecayingWorldOptionsHolder.COMMON.DECAY_SPAWN_SAFE_RADIUS.get();
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

    @SubscribeEvent
    public void blockPlacedEvent(BlockEvent.EntityPlaceEvent event){
        if (event.getEntity() instanceof ServerPlayer == false) return;



        ServerPlayer player = (ServerPlayer) event.getEntity();
        BlockPos spawn = player.level.getSharedSpawnPos();
        BlockPos player_pos = player.getOnPos();
        // if in safe zone, can place beds
        if (getDistance(spawn,player_pos)<DecayingWorldOptionsHolder.COMMON.DECAY_SPAWN_SAFE_RADIUS.get()) return;

        Block block = event.getPlacedBlock().getBlock();


        if (block instanceof BedBlock && DecayingWorldOptionsHolder.COMMON.BEDS_EXPLODE.get()){
            player.level.setBlock(event.getPos(), Blocks.AIR.defaultBlockState(),0);
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level, player) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            player.level.explode(player, player.getX(), player.getY(), player.getZ(), 3.0F, explosion$blockinteraction);
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.CHARRED_PILLOW.get()));
        }
        LOGGER.info("Bed placed by player");


    }
    private static int counter = 0;
    @SubscribeEvent
    public void onWorldTickEvent(TickEvent.PlayerTickEvent event){
        if (event.player.level.isClientSide)
            return;
        if (counter>0) {
            counter--;
            return;
        }

        counter = DecayingWorldOptionsHolder.COMMON.DECAY_SPAWN_RATE.get();
        Player player = event.player;
        BlockPos playerbock = player.getOnPos();
        Level world = player.getLevel();
        // If we're not in the courruptable area, return
        if (getDistance(playerbock,world.getSharedSpawnPos())>SAFE_RADIUS) {
            LOGGER.info("Outside Courruptable area");
            return;
        }

        if (spawnCourruption==false) return;

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


        // if we use the isProtected function it stops working on server. idk why
        if (state!=null){
            if (!state.isAir()) {
                LOGGER.info(target+" not air");
                boolean safe=false;
                for (DimensionAwareBlockPosition prot : ProtectionBlockEntity.PROTECTED_BLOCKS) {
                    if (ProtectionBlockEntity.getDistance(target, prot.position) < DecayingWorldOptionsHolder.COMMON.PROTECTION_BLOCK_PROTECTION_RADIUS.get() && prot.level.equals(world)) {
                       safe=true;
                    }
                }
                if (safe==false){
                    LOGGER.info("Courrupting " + target);
                    player.getLevel().setBlockAndUpdate(new BlockPos(rx, ry, rz), ModBlocks.FAST_DECAY_BLOCK.get().defaultBlockState());
                }
            } else {
                LOGGER.info(target+" is air");
            }
        } else {
            LOGGER.info(target+" is null");


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

    private static double getDistance(BlockPos a, BlockPos b){
        double deltaX = a.getX() - b.getX();
        double deltaY = a.getY() - b.getY();
        double deltaZ = a.getZ() - b.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }
}
