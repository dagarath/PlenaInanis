package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSieve;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by dagarath on 2016-01-26.
 */
public class BlockPlenaSieve extends BlockContainer {

    private static long delayUse = 0;
    public BlockPlenaSieve() {
        super(Material.wood);
        this.setBlockName("sieve");
        this.setBlockTextureName(PlenaInanisReference.MODID + ":sandwich");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.setHardness(1.0F);
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        setTickRandomly(false);
        useNeighborBrightness = false;
    }


    @Override
    public void updateTick(World world, int x, int y, int z, Random interval) {

    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(BlockRegistrar.blockSieve);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TilePlenaSieve();
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
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemStack){

            int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
            int direction = 0;
            if (facing == 0) {
                direction = ForgeDirection.NORTH.ordinal();
            } else if (facing == 1) {
                direction = ForgeDirection.EAST.ordinal();
            } else if (facing == 2) {
                direction = ForgeDirection.SOUTH.ordinal();
            } else if (facing == 3) {
                direction = ForgeDirection.WEST.ordinal();
            }
            TileEntity te = world.getTileEntity(x, y, z);
            TilePlenaSieve tile = (TilePlenaSieve) te;
            tile.setDirection(direction);
            world.markBlockForUpdate(x, y, z);
        if(world.isRemote) {
            Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TilePlenaSieve te = (TilePlenaSieve) world.getTileEntity(x, y, z);
        ItemStack itemStack = player.getCurrentEquippedItem();
        ItemStack testStack = te.getStackInSlot(0);

        if (!world.isRemote) {
            if (testStack == null) {
                if(itemStack != null && itemStack.getItem() != null) {
                    if (Block.getBlockFromItem(itemStack.getItem()) != null) {
                        Block checkBlock = Block.getBlockFromItem(itemStack.getItem());
                        if (PlenaInanis.saveData.sieveExists(InfoHelper.getFullNameForBlock(checkBlock) + ":" + itemStack.getItemDamage())) {
                            te.setInventorySlotContents(0, new ItemStack(itemStack.getItem(), 1));
                            if (itemStack.stackSize == 1) {
                                player.destroyCurrentEquippedItem();
                            } else {
                                itemStack.stackSize--;
                            }
                        }
                    }
                }
            } else{
                if (te.getProgress() == 0) {
                    te.setProgress(1);
                    //progress = 1;
                    //PlenaInanis.logger.info("Setting world time to:" + world.getTotalWorldTime());
                    delayUse = world.getTotalWorldTime();
                }  else {
                  if (world.getTotalWorldTime() - delayUse >= 10 && te.getProgress() < 10) {
                      te.setProgress(te.getProgress() + 1);
                      //progress ++;
                      //PlenaInanis.logger.info("Progress is: " + te.getProgress());
                      delayUse = world.getTotalWorldTime();
                  }
                        //PlenaInanis.logger.info(world.getTotalWorldTime() - delayUse);
                }
            }

        }
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 0.5, (double)z + 1);
    }
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 0.5, (double)z + 1);
    }

}
