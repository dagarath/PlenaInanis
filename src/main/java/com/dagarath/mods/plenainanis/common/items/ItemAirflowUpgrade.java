package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by dagarath on 2016-02-19.
 */
public class ItemAirflowUpgrade extends Item {

    public IIcon[] icons = new IIcon[3];
    public String[] names = {"minor", "average", "major"};

    public ItemAirflowUpgrade(){
        this.setUnlocalizedName("airflow");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.setHasSubtypes(true);
    }

    @Override
    public void registerIcons(IIconRegister reg){
        for(int i = 0; i < 3; i ++){
            this.icons[i] = reg.registerIcon(PlenaInanisReference.MODID + ":airflow_" + names[i]);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta){
        if(meta > 2) meta = 0;

        return this.icons[meta];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list){
        for( int i = 0; i < 3; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        return this.getUnlocalizedName() + "_" + names[itemStack.getItemDamage()];
    }
}
