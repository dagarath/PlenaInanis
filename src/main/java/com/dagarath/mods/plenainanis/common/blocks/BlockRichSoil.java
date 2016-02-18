package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaRichSoil;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSieve;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by dagarath on 2016-01-29.
 */
public class BlockRichSoil extends BlockContainer {
    private int timeToLive;
    private int lifeTime;

    @SideOnly(Side.CLIENT)
    private IIcon field_149824_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_149823_b;

    public BlockRichSoil(){
        super(Material.ground);
        lifeTime = 0;
        this.setBlockTextureName(PlenaInanisReference.MODID + ":richSoil");
        this.setBlockName("richSoil");
        this.setHardness(2.0f);
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
    }
    @Override
    public void onFallenUpon(World p_149746_1_, int p_149746_2_, int p_149746_3_, int p_149746_4_, Entity p_149746_5_, float p_149746_6_){

    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TilePlenaRichSoil();
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);
        TilePlenaRichSoil te = (TilePlenaRichSoil)world.getTileEntity(x, y, z);
        te.incrementLife(1);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote) {
            ItemStack testStack = new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockCompost), 1);
            TilePlenaRichSoil te = (TilePlenaRichSoil) world.getTileEntity(x, y, z);
            if (player.getCurrentEquippedItem() == testStack) {
                te.refillLife(player);
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? (p_149691_2_ > 0 ? this.field_149824_a : this.field_149823_b) : Blocks.dirt.getBlockTextureFromSide(p_149691_1_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149824_a = p_149651_1_.registerIcon(this.getTextureName());
        this.field_149823_b = p_149651_1_.registerIcon(this.getTextureName());
    }
}
