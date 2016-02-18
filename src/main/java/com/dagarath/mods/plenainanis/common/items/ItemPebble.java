package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.entities.EntityPebble;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by dagarath on 2016-01-27.
 */
public class ItemPebble extends Item {

    public ItemPebble(){
        this.setUnlocalizedName("pebble");
        this.setTextureName(PlenaInanisReference.MODID.toLowerCase() + ":" + "pebble");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);

    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer player){

        if (!player.capabilities.isCreativeMode)
        {
            --p_77659_1_.stackSize;
        }

        p_77659_2_.playSoundAtEntity(player, "step.stone", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!p_77659_2_.isRemote)
        {
            p_77659_2_.spawnEntityInWorld(new EntityPebble(p_77659_2_, player));
        }

        return p_77659_1_;

    }
}
