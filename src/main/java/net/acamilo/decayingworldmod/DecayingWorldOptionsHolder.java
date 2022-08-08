package net.acamilo.decayingworldmod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DecayingWorldOptionsHolder
{

    public static class Common
    {
        private static final int defaultInt1 = 37;
        private static final boolean defaultBool1 = true;

        private static int default_DECAY_BLOCK_SPREAD_DELAY = 1000;
        public final ForgeConfigSpec.ConfigValue<Integer> DECAY_BLOCK_SPREAD_DELAY;
        private static int default_FAST_DECAY_BLOCK_SPREAD_DELAY = 5;
        public final ForgeConfigSpec.ConfigValue<Integer> FAST_DECAY_BLOCK_SPREAD_DELAY;

        private static double default_DECAY_BLOCK_SAND_CHANCE = 0.05;
        public final ForgeConfigSpec.ConfigValue<Double> DECAY_BLOCK_SAND_CHANCE;
        private static double default_FAST_DECAY_BLOCK_SPAWN_CHANCE = 0.25;
        public final ForgeConfigSpec.ConfigValue<Double> FAST_DECAY_BLOCK_SPAWN_CHANCE;

        private static int default_PROTECTION_RESOURCE_BURN_TIME = 3600*20;
        public final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_RESOURCE_BURN_TIME;

        private static int default_PROTECTION_BLOCK_PROTECTION_RADIUS = 32;
        public final ForgeConfigSpec.ConfigValue<Integer> PROTECTION_BLOCK_PROTECTION_RADIUS;


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