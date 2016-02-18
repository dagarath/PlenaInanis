package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.item.Item;

/**
 * Created by dagarath on 2016-01-26.
 */
public class ItemCompost extends Item{

    public ItemCompost(){
        this.setUnlocalizedName("compost");
        this.setTextureName(PlenaInanisReference.MODID.toLowerCase() + ":" + "compost");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);

    }
}
