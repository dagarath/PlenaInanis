package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaSquasher;
import net.minecraft.item.ItemBlock;

/**
 * Created by dagarath on 2016-01-26.
 */
public class ItemBlockSquasher extends ItemBlock {
    public ItemBlockSquasher(){
        super(new BlockPlenaSquasher());
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
    }
}
