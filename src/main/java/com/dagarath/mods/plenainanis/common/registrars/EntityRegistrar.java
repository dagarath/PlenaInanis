package com.dagarath.mods.plenainanis.common.registrars;

/**
 * Created by dagarath on 2016-01-29.
 *
 */
import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.entities.EntityPebble;
import cpw.mods.fml.common.registry.EntityRegistry;
public class EntityRegistrar {

    public static void init(){
        registerEntities();
    }

    public static void registerEntities(){
        EntityRegistry.registerModEntity(EntityPebble.class, "Pebble", 4, PlenaInanis.instance, 80, 3, true);    }
}
