package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TileCruciNull;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by dagarath on 2016-02-12.
 */
public class BlockPlenaCrucible extends BlockContainer {
    private static boolean keepInventory;
    private static boolean active;

    public BlockPlenaCrucible(boolean activeState){
        super(Material.rock);
        active = activeState;
        this.setBlockName("crucible");
        this.setBlockTextureName(PlenaInanisReference.MODID + ":sandwich");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.setHardness(1.0f);
        blockParticleGravity = 1.0F;
        slipperiness = 0.6F;
        setBlockBounds(-1.0F, 0.0F, -1.0F, 1.0F, 3.0F, 1.0F);
        useNeighborBrightness = false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6){

        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(BlockRegistrar.blockCrucible);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if(!world.isRemote && !player.isSneaking()) {
            TilePlenaCrucible crucibleTile = (TilePlenaCrucible) world.getTileEntity(x, y, z);
            ItemStack itemStack = null;
            if(player.getCurrentEquippedItem() != null) {
                itemStack = player.getCurrentEquippedItem().copy();
            }
            if(crucibleTile.getNextSlot() != 0 && itemStack != null){
                if(Block.getBlockFromItem(itemStack.getItem()) != null) {
                    itemStack.stackSize = 1;
                    if (PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(player.getCurrentEquippedItem()))) {
                        crucibleTile.setInventorySlotContents(crucibleTile.getNextSlot(), itemStack);
                        if (player.getCurrentEquippedItem().stackSize == 1) {
                            player.destroyCurrentEquippedItem();
                        } else {
                            player.getCurrentEquippedItem().stackSize--;
                        }
                    }
                }

            }
            if(crucibleTile.isLocked()){
                player.openGui(PlenaInanis.instance, GuiHandler.CRUCIBLE_GUI, world, x, y, z);
            }
        }else if (player.isSneaking()){
            player.openGui(PlenaInanis.instance, GuiHandler.CRUCIBLE_GUI, world, x, y, z);
        }
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        int nullY = 0;
        int nullX = -1;
        int nullZ = -1;

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if(!world.isAirBlock(x + nullX, y + nullY, z + nullZ)
                            && !(world.getBlock(x + nullX, y + nullY, z + nullZ) instanceof BlockTallGrass)){
                        return false;
                    }
                    nullZ += 1;
                }
                nullX += 1;
                nullZ = -1;
            }
            nullY += 1;
            nullX = -1;
        }
        return world.getBlock(x, y, z).isReplaceable(world, x, y, z);
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
        EntityPlayer player = (EntityPlayer)entityliving;

        if(!player.capabilities.isCreativeMode){
            if(itemStack.stackSize > 1){
                --itemStack.stackSize;
            }else{
                player.destroyCurrentEquippedItem();
            }
        }


        if(!world.isRemote){
            int dir = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;


            int nullY = 0;
            int nullX = -1;
            int nullZ = -1;

            for(int i = 0; i < 3; i++){
                for(int j= 0; j < 3; j++){
                    for(int k = 0; k < 3; k++){
                        if(!(nullY == 0 && nullX == 0 && nullZ == 0)){
                            world.setBlock(x + nullX, y + nullY, z + nullZ, BlockRegistrar.cruciNull, dir, 0x02);
                            TileCruciNull tileNull = (TileCruciNull)world.getTileEntity(x + nullX, y + nullY, z + nullZ);
                            if(tileNull != null){
                                tileNull.primaryX = x;
                                tileNull.primaryY = y;
                                tileNull.primaryZ = z;
                                tileNull.setDirection();
                            }
                        }
                        nullZ += 1;
                    }
                    nullX += 1;
                    nullZ = -1;
                }
                nullY += 1;
                nullX = -1;
            }
        }

        TileEntity te = world.getTileEntity(x, y, z);
        TilePlenaCrucible tile = (TilePlenaCrucible) te;
        tile.setDirection(direction);
        world.markBlockForUpdate(x, y, z);
    }

    public static void updateFurnaceBlockState(boolean activeState, World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        keepInventory = true;

        active = activeState;

        keepInventory = false;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);

        if (tileentity != null)
        {
            tileentity.validate();
            world.setTileEntity(x, y, z, tileentity);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int par1){
        return false;
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
    public TileEntity createNewTileEntity(World world, int meta){
        return new TilePlenaCrucible();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 1, (double)z + 1);
    }
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1, (double)y + 1, (double)z + 1);
    }


}
