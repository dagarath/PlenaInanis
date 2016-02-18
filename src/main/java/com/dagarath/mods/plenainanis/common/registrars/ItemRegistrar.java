package com.dagarath.mods.plenainanis.common.registrars;

import com.dagarath.mods.plenainanis.common.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * Created by dagarath on 2016-01-22.
 */
public class ItemRegistrar {

    public static Item itemDummy;
    public static Item itemCompost;
    public static Item itemFertilizer;
    public static Item itemPebble;
    public static Item itemBlockSmasher;
    public static Item itemBlockCrucible;



    public static void init(){
        try {
            GameRegistry.registerItem(itemDummy = new ItemDummy(), "dummy");
            GameRegistry.registerItem(itemCompost = new ItemCompost(), "compost");
            GameRegistry.registerItem(itemFertilizer = new ItemFertilizer("fertilizer"), "fertilizer");
            GameRegistry.registerItem(itemPebble = new ItemPebble(), "pebble");
            GameRegistry.registerItem(itemBlockSmasher = new ItemBlockSmasher(), "smasher");

        }catch(Exception e){
            System.err.println("plenainanis Item Registry Error: " + e);
        }
    }
}
