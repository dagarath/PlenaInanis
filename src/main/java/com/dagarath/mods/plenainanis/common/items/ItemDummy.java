package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.item.Item;

/**
 * Created by dagarath on 2016-01-06.
 */
public class ItemDummy extends Item{
    public ItemDummy () {
        this.setUnlocalizedName("dummy");
        this.setTextureName(PlenaInanisReference.MODID.toLowerCase() + ":" + "PlenaInanis");
    }
}
