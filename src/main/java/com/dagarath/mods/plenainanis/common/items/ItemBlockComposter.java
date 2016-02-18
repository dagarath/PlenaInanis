package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaComposter;
import net.minecraft.item.ItemBlock;

/**
 * Created by dagarath on 2016-01-23.
 */
public class ItemBlockComposter extends ItemBlock{
    public ItemBlockComposter(int type){
        super(new BlockPlenaComposter(type));
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
    }
}
