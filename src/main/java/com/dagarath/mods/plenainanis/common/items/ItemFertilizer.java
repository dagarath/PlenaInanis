package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import com.google.common.base.CaseFormat;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.util.List;

/**
 * Created by dagarath on 2016-01-15.
 */
public class ItemFertilizer extends Item {

    private IIcon[] icons = new IIcon[3];

    private enum NAMES
    {
        FERTILIZER,
        HAPPY_FERTILIZER,
        ANGRY_FERTILIZER
    }

    public ItemFertilizer(String unlocalizedName)
    {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setHasSubtypes(true);
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_){
        if (itemstack.getItemDamage() == 0)
        {
            if (applyBonemeal(itemstack, world, x, y, z, player))
            {
                if (!world.isRemote)
                {
                    world.playSoundAtEntity(player, PlenaInanisReference.MODID + ":bloop", 1.0f, 1.0f);
                }

                return true;
            }
        }
        return false;
    }

    public static boolean applyBonemeal(ItemStack itemstack, World world, int x, int y, int z, EntityPlayer player) {
        Block block = world.getBlock(x, y, z);

        BonemealEvent event = new BonemealEvent(player, world, block, x, y, z);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return false;
        }

        if (event.getResult() == Event.Result.ALLOW) {
            if (!world.isRemote) {
                itemstack.stackSize--;
            }
            return true;
        }

        if (block instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) block;

            if (igrowable.func_149851_a(world, x, y, z, world.isRemote)) {//canBonemeal
                if (!world.isRemote) {
                    if (igrowable.func_149852_a(world, world.rand, x, y, z)) {//doBonemealCheck
                        igrowable.func_149853_b(world, world.rand, x, y, z);//doGrowthTick
                    }

                    --itemstack.stackSize;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        for(int i = 0; i < 3; i++)
        {
            this.icons[i] = register.registerIcon(PlenaInanisReference.MODID + ":" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, NAMES.values()[i].name()));
        }
    }
    @Override
    public IIcon getIconFromDamage(int meta) {
        if (meta > 2)
            meta = 0;

        return this.icons[meta];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i ++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return NAMES.values()[stack.getItemDamage()].name().toLowerCase().replaceAll("_","");
    }
}
