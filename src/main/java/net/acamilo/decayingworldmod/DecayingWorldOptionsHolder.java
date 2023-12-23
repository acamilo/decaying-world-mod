package net.acamilo.decayingworldmod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DecayingWorldOptionsHolder
{

    public static class Common
{
    private static final int defaultInt1 = 37;
    private static final boolean defaultBool1 = true;

    private static int default_DECAY_BLOCK_SPREAD_DELAY = 100;
    public final ForgeConfigSpec.ConfigValue<Integer> DECAY_BLOCK_SPREAD_DELAY;
    private static int default_FAST_DECAY_BLOCK_SPREAD_DELAY = 5;
    public final ForgeConfigSpec.ConfigValue<Integer> FAST_DECAY_BLOCK_SPREAD_DELAY;

    private static double default_DECAY_BLOCK_SAND_CHANCE = 0.05;
    public final ForgeConfigSpec.ConfigValue<Double> DECAY_BLOCK_SAND_CHANCE;
    private static double default_FAST_DECAY_BLOCK_SPAWN_CHANCE = 0.75;
    public final ForgeConfigSpec.ConfigValue<Double> FAST_DECAY_BLOCK_SPAWN_CHANCE;

    private static int default_PROTECTION_RESOURCE_BURN_TIME = 18900;
    public final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_RESOURCE_BURN_TIME;

    private static int default_PROTECTION_BLOCK_PROTECTION_RADIUS = 25;
    public final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_BLOCK_PROTECTION_RADIUS;
    private static int default_DECAY_SPAWN_PLAYER_RADIUS = 100;
    public final ForgeConfigSpec.ConfigValue<Integer> DECAY_SPAWN_PLAYER_RADIUS;
    private static int default_DECAY_SPAWN_SAFE_RADIUS = 1000;
    public final ForgeConfigSpec.ConfigValue<Integer> DECAY_SPAWN_SAFE_RADIUS;


    private static int DEFAULT_SPAWN_ZONE_RADIUS = 500;

    private static boolean DEFAULT_BEDS_EXPLODE = true;
    public final ForgeConfigSpec.ConfigValue<Boolean> BEDS_EXPLODE;

    private static boolean DEFAULT_ENABLE_DECAY = false;
    public final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_DECAY;
    //
    private static int default_DECAY_SPAWN_RATE = 240;
    public final ForgeConfigSpec.ConfigValue<Integer> DECAY_SPAWN_RATE;

    public final ForgeConfigSpec.ConfigValue<Boolean> SPAWN_COURRUPTION_ENABLE;
    public Common(ForgeConfigSpec.Builder builder)
    {
        builder.push("decay_blocks");
        this.DECAY_BLOCK_SPREAD_DELAY = builder.comment(
                        "Maximum delay in ticks before decay block spreads. Multiplied by random double in range 0.0-1.0")
                .worldRestart()
                .defineInRange("Decay block spread delay", default_DECAY_BLOCK_SPREAD_DELAY, 1, 10000);
        this.FAST_DECAY_BLOCK_SPREAD_DELAY = builder.comment(
                        "Maximum delay in ticks before a fast decay block spreads.")
                .worldRestart()
                .defineInRange("Fast decay block spread delay", default_FAST_DECAY_BLOCK_SPREAD_DELAY, 1, 10000);
        this.DECAY_BLOCK_SAND_CHANCE = builder.comment(
                        "Chance that a Decay_Block will decay into decay sand")
                .worldRestart()
                .defineInRange("Decay block sand chance", default_DECAY_BLOCK_SAND_CHANCE, 0.0, 1.0);
        this.FAST_DECAY_BLOCK_SPAWN_CHANCE = builder.comment(
                        "Chance a Fast Decay Block will spawn another Fast Decay Block")
                .worldRestart()
                .defineInRange("Fast decay block spawn chance", default_FAST_DECAY_BLOCK_SPAWN_CHANCE, 0.0, 1.0);

        this.PROTECTION_RESOURCE_BURN_TIME = builder.comment(
                        "Number of ticks it takes for a protection block to consume a crystal")
                .worldRestart()
                .defineInRange("Protection Resource Burn Time", default_PROTECTION_RESOURCE_BURN_TIME, 1, 1000000);

        this.PROTECTION_BLOCK_PROTECTION_RADIUS = builder.comment(
                        "Number of ticks it takes for a protection block to consume a crystal")
                .worldRestart()
                .defineInRange("Protection block protection radius", default_PROTECTION_BLOCK_PROTECTION_RADIUS, 1, 1024);
        this.DECAY_SPAWN_RATE = builder.comment(
                        "Number of ticks between attempts to spawn decay around player")
                .worldRestart()
                .defineInRange("Decay spawn rate", default_DECAY_SPAWN_RATE, 1, 10000);

        this.DECAY_SPAWN_PLAYER_RADIUS = builder.comment(
                        "Radius around player in which to select a random block to courrupt")
                .worldRestart()
                .defineInRange("Decay spawn player radius", default_DECAY_SPAWN_PLAYER_RADIUS, 1, 256);
        this.DECAY_SPAWN_SAFE_RADIUS = builder.comment(
                        "Radius outside of which no couruption will spawn, but where beds can not be placed.")
                .worldRestart()
                .defineInRange("No Decay Threshold radius", default_DECAY_SPAWN_SAFE_RADIUS, 1, 10000);

        this.SPAWN_COURRUPTION_ENABLE = builder.comment("Enable spawning of courruption")
                        .worldRestart()
                                .define("Spawn Courruption Enabled", false);
        this.BEDS_EXPLODE = builder.comment("Enable exploding of beds outside courruption radius")
                .worldRestart()
                .define("Exploding Beds Enabled", true);
        this.ENABLE_DECAY = builder.comment("Enable decay spread")
                .worldRestart()
                .define("Decay Enabled", DEFAULT_ENABLE_DECAY);
        builder.pop();
    }
}

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static //constructor
    {
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = commonSpecPair.getLeft();
        COMMON_SPEC = commonSpecPair.getRight();
    }
}