package com.dagarath.mods.plenainanis.common.registrars;

import com.dagarath.mods.plenainanis.common.blocks.*;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

/**
 * Created by dagarath on 2016-01-22.
 */
public class BlockRegistrar {
    public static Block composterOak;
    public static Block composterSpruce;
    public static Block composterBirch;
    public static Block composterJungle;
    public static Block composterAcacia;
    public static Block composterDarkOak;
    public static Block blockSquasher;
    public static Block blockSieve;
    public static Block blockCompost;
    public static Block blockRichSoil;
    public static Block blockCrucible;
    public static Block cruciNull;


    public static void init(){
        registerBlocks();
    }

    public static void registerBlocks(){
        try {
            composterOak = new BlockPlenaComposter(0);
            composterSpruce = new BlockPlenaComposter(1);
            composterBirch = new BlockPlenaComposter(2);
            composterJungle = new BlockPlenaComposter(3);
            composterAcacia = new BlockPlenaComposter(4);
            composterDarkOak = new BlockPlenaComposter(5);
            blockSquasher = new BlockPlenaSquasher();
            blockSieve = new BlockPlenaSieve();
            blockCompost = new BlockCompost();
            blockRichSoil = new BlockRichSoil();
            blockCrucible = new BlockPlenaCrucible(false);
            cruciNull = new BlockCruciNull();


            //Composters
            GameRegistry.registerBlock(composterOak, "plenaComposterOak");
            GameRegistry.registerBlock(composterSpruce, "plenaComposterSpruce");
            GameRegistry.registerBlock(composterBirch, "plenaComposterBirch");
            GameRegistry.registerBlock(composterJungle, "plenaComposterJungle");
            GameRegistry.registerBlock(composterAcacia, "plenaComposterAcacia");
            GameRegistry.registerBlock(composterDarkOak, "plenaComposterDarkOak");

            //Squasher
            GameRegistry.registerBlock(blockSquasher, "plenaBlockSquasher");

            //Sieve
            GameRegistry.registerBlock(blockSieve, "plenaSieve");

            //Compost
            GameRegistry.registerBlock(blockCompost, "plenaCompost");


            //Rich Soil
            GameRegistry.registerBlock(blockRichSoil, "plenaRichSoil");

            //Crucible
            GameRegistry.registerBlock(blockCrucible, "plenaCrucible");
            GameRegistry.registerBlock(cruciNull, "cruciNull");


        }catch(Exception e){
            System.err.println(PlenaInanisReference.MODID + " Block Registry Error: " + e);
        }
    }
}
