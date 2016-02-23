package com.dagarath.mods.plenainanis;

import com.dagarath.mods.plenainanis.client.handlers.ClientEventHandler;
import com.dagarath.mods.plenainanis.client.handlers.KeyHandler;
import com.dagarath.mods.plenainanis.common.CommonProxy;
import com.dagarath.mods.plenainanis.common.commands.CompostCommander;
import com.dagarath.mods.plenainanis.common.commands.CrucibleCommander;
import com.dagarath.mods.plenainanis.common.commands.SieveCommander;
import com.dagarath.mods.plenainanis.common.commands.SquashCommander;
import com.dagarath.mods.plenainanis.common.handlers.ServerEventHandler;
import com.dagarath.mods.plenainanis.common.helpers.*;
import com.dagarath.mods.plenainanis.common.network.PacketHandler;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.registrars.EntityRegistrar;
import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import com.dagarath.mods.plenainanis.common.registrars.RecipeRegistrar;
import com.dagarath.mods.plenainanis.config.ConfigurationHandler;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by dagarath on 2016-01-22.
 */
@Mod(modid = PlenaInanisReference.MODID, name = PlenaInanisReference.NAME, version = PlenaInanisReference.VERSION, dependencies = PlenaInanisReference.DEPENDS, acceptedMinecraftVersions = PlenaInanisReference.MCVERSIONS)
public class PlenaInanis {

    @SidedProxy(clientSide = "com.dagarath.mods.plenainanis.client.ClientProxy", serverSide = "com.dagarath.mods.plenainanis.common.CommonProxy")
    public static CommonProxy proxy;

    @Instance(PlenaInanisReference.MODID)
    public static PlenaInanis instance;

    public static ClientEventHandler localEventHandler;
    public static ServerEventHandler remoteEventHandler;
    public static Logger logger;
    public static int renderingId;

    public static HashMap<String, String> crucibleAllowedItems = new HashMap<>();
    public static HashMap<String, Integer> crucibleCookTimes = new HashMap<>();
    public static HashMap<String, Integer> crucibleBurnTimes = new HashMap<>();
    public static HashMap<String, Integer> crucibleFuelTemps = new HashMap<>();
    public static HashMap<String, String> bucketNameFluidPair = new HashMap<>();
    public static HashMap<String, String> bucketFluidOutputPair = new HashMap<>();

    public boolean debugMode = false;


    public static final String burnFileName = "config/Dagarath/crucible/burnTimes.cfg";
    public static final String cookFileName = "config/Dagarath/crucible/cookTimes.cfg";
    public static final String temperatureFileName = "config/Dagarath/crucible/temperatureData.cfg";
    public static final String allowedItemsFileName = "config/Dagarath/crucible/crucibleWhitelist.cfg";
    public static final String bucketListFileName = "config/Dagarath/crucible/bucketList.cfg";

    public static CreativeTabs tabPlenaInanis = new CreativeTabs("tabPlenaInanis")
    {
        @Override
        public Item getTabIconItem()
        {

            return ItemRegistrar.itemDummy;
        }
    };

    public static PlenaSaveData saveData = new PlenaSaveData();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        proxy.preInit(event);
        logger = event.getModLog();
        if(debugMode) {FileParser.setVersion();}
        crucibleBurnTimes = FileParser.readStringIntHash(burnFileName,crucibleBurnTimes);
        crucibleFuelTemps = FileParser.readStringIntHash(temperatureFileName, crucibleFuelTemps);
        crucibleAllowedItems = FileParser.readStringStringHash(allowedItemsFileName, crucibleAllowedItems);
        crucibleCookTimes = FileParser.readStringIntHash(cookFileName, crucibleCookTimes);
        PacketHandler.init();
        ObjectSerializer.getSave();
        proxy.registerTileEntities();
        proxy.registerHandlers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        BlockRegistrar.init();
        ItemRegistrar.init();
        EntityRegistrar.init();
        proxy.initRenderers();
        proxy.registerKeyBindings();

    }

    @EventHandler
    public void load(FMLLoadEvent event){
        renderingId = RenderingRegistry.getNextAvailableRenderId();
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

        event.registerServerCommand(new CompostCommander());
        event.registerServerCommand(new SieveCommander());
        event.registerServerCommand(new SquashCommander());
        event.registerServerCommand(new CrucibleCommander());
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){

        RecipeRegistrar.init();
    }
}
