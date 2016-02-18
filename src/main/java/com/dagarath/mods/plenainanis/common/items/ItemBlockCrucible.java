package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaCrucible;
import net.minecraft.item.ItemBlock;

/**
 * Created by dagarath on 2016-02-12.
 */

public class ItemBlockCrucible extends ItemBlock{

    public ItemBlockCrucible(){
        super(new BlockPlenaCrucible(false));
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
    }

}
