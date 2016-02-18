package com.dagarath.mods.plenainanis.config;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

/**
 * Created by dagarath on 2016-01-22.
 */
public class ConfigurationHandler {
    public static Configuration configuration;

    public static Property compostInterval;
    public static Property compostCost;
    public static Property compostSound;

    public static Property sieveInterval;

    public static Property squasherInterval;

    public static void init(File configFile)
    {
        // Create the configuration object from the given configuration file
        if (configuration == null)
        {
            configuration = new Configuration(new File("config/Dagarath/PlenaInanis.cfg"));
            loadConfiguration();
        }
    }

    private static void loadConfiguration()
    {
        configuration.setCategoryComment("Composter", "Composter Settings");
        compostInterval = configuration.get("Composter","Composter Tick Rate", 20, "This is how often the composter consumes organic items.(In seconds)");
        compostCost = configuration.get("Composter","Composter Cost", 100, "This is how many items need to be consumed to produce bone meal.");
        compostSound = configuration.get("Composter", "Composter Sound", true, "Whether you are cool or not, false means you are not cool");

        configuration.setCategoryComment("Sieve", "Sieve Settings");
        sieveInterval = configuration.get("Sieve","Sieve Click Rate", 10, "This is how many times you have to use a sieve to process an item.");

        configuration.setCategoryComment("Squasher", "Squasher Settings");
        squasherInterval = configuration.get("Squasher","Squasher Smash Rate", 15, "This is how many times you need to smash a block before exposing the gooey centre.");


        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(PlenaInanisReference.MODID))
        {
            loadConfiguration();
        }
    }
}