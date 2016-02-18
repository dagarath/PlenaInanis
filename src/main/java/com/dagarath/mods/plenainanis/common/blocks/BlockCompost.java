package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;


/**
 * Created by dagarath on 2016-01-27.
 */
public class BlockCompost extends Block {
    public BlockCompost() {
        super(Material.ground);
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.setBlockName("blockCompost");
        this.setBlockTextureName(PlenaInanisReference.MODID + ":compost");
        this.setHardness(1.0f);
    }


}
