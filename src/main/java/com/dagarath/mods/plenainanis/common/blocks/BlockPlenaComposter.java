package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.config.ConfigurationHandler;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by dagarath on 2016-01-22.
 */
public class BlockPlenaComposter extends BlockContainer {

    public int type;



    public BlockPlenaComposter(int type) {
        super(Material.wood);
        this.setBlockName("composter");
        this.setBlockTextureName(PlenaInanisReference.MODID + ":sandwich");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.setHardness(1.0F);
        this.type = type;
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setTickRandomly(false);
        useNeighborBrightness = false;
    }


    @Override
    public void updateTick(World world, int x, int y, int z, Random interval) {

    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        if(type == 0){
            return Item.getItemFromBlock(BlockRegistrar.composterOak);
        }else if(type == 1){
            return Item.getItemFromBlock(BlockRegistrar.composterSpruce);
        }else if(type == 2){
            return Item.getItemFromBlock(BlockRegistrar.composterBirch);
        }else if(type == 3){
            return Item.getItemFromBlock(BlockRegistrar.composterJungle);
        }else if(type == 4){
            return Item.getItemFromBlock(BlockRegistrar.composterAcacia);
        }else if(type == 5){
            return Item.getItemFromBlock(BlockRegistrar.composterDarkOak);
        }

        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        TilePlenaComposter composter = new TilePlenaComposter();
        composter.setType(this.type);
        return composter;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int  y, int z, Block block, int p_149749_6_) {
        TilePlenaComposter te = (TilePlenaComposter) world.getTileEntity(x, y, z);
        this.type = te.getType();
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemStack){
        int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        int direction = 0;
        if(facing == 0){
            direction = ForgeDirection.NORTH.ordinal();
        }else if (facing == 1){
            direction = ForgeDirection.EAST.ordinal();
        }else if(facing == 2){
            direction = ForgeDirection.SOUTH.ordinal();
        }else if (facing == 3){
            direction = ForgeDirection.WEST.ordinal();
        }
        TileEntity te = world.getTileEntity(x, y, z);
        TilePlenaComposter tile = (TilePlenaComposter) te;
        tile.setDirection(direction);
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TilePlenaComposter te = (TilePlenaComposter) world.getTileEntity(x, y, z);
        if(!world.isRemote){
            player.openGui(PlenaInanis.instance, GuiHandler.COMPOST_GUI, world, x, y, z);
            if(ConfigurationHandler.compostSound.getBoolean()) {
                world.playSoundAtEntity(player, PlenaInanisReference.MODID + ":bloop", 1.0f, 1.0f);
            }
            te.setOpen(true);
        }

        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block){
        if(world.getBlock(x, y + 1, z) != Blocks.air){
            Block blockToMove = world.getBlock(x, y + 1, z);
            world.setBlockToAir(x, y + 1, z);
            world.setBlock(x, y + 2, z, blockToMove);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 1.75, (double)z + 1);
    }
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 1.75, (double)z + 1);
    }
}
