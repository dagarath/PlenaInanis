package com.dagarath.mods.plenainanis.common.blocks;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TileCruciNull;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by dagarath on 2016-02-12.
 */
public class BlockCruciNull extends BlockContainer {

    public BlockCruciNull(){
        super(Material.rock);
        this.setBlockName("cruciNull");
        this.setBlockTextureName(PlenaInanisReference.MODID + ":sandwich");
        this.setHardness(1.0f);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        TileCruciNull tile = (TileCruciNull) world.getTileEntity(x, y, z);
        TilePlenaCrucible crucibleTile = null;
        if(tile != null){
            if(!world.isRemote) {
                crucibleTile = (TilePlenaCrucible) world.getTileEntity(tile.primaryX, tile.primaryY, tile.primaryZ);
                if (!(tile.primaryX == 0 && tile.primaryY == 0 && tile.primaryZ == 0)) {
                    if (crucibleTile != null) {
                        if (!crucibleTile.isOpening()) {
                            world.setBlockToAir(tile.primaryX, tile.primaryY, tile.primaryZ);
                            world.removeTileEntity(tile.primaryX, tile.primaryY, tile.primaryZ);
                            world.removeTileEntity(x, y, z);
                        }
                    }
                }
            }
            super.breakBlock(world, x, y, z, world.getBlock(tile.primaryX, tile.primaryY, tile.primaryZ), 0);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block){
        TileCruciNull tile = (TileCruciNull)world.getTileEntity(x, y, z);

        if (world.isAirBlock(tile.primaryX, tile.primaryY, tile.primaryZ)) {
            world.setBlockToAir(x, y, z);
            world.removeTileEntity(x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        TileCruciNull tile = (TileCruciNull)world.getTileEntity(x, y, z);
        TilePlenaCrucible crucibleTile = null;
        if(!world.isRemote) {
            if (!(tile.primaryX == 0 && tile.primaryY == 0 && tile.primaryZ == 0)) {
                crucibleTile = (TilePlenaCrucible) world.getTileEntity(tile.primaryX, tile.primaryY, tile.primaryZ);
            }
            if (!player.isSneaking() && crucibleTile != null) {
                ForgeDirection direction = ForgeDirection.getOrientation(crucibleTile.getDirection());
                int dir = direction.ordinal();
                if(crucibleTile.isLocked()){
                    player.openGui(PlenaInanis.instance, GuiHandler.CRUCIBLE_GUI, world, tile.primaryX, tile.primaryY, tile.primaryZ);
                }else{
                    switch (direction) {
                        case NORTH:
                            PlenaInanis.logger.info("NORTH");
                            //Negative Z
                            if (tile.zCoord < crucibleTile.zCoord) {
                                boolean canOpen = true;
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 2; j++) {
                                        canOpen = world.getBlock(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, (crucibleTile.zCoord - 2) - j) == Blocks.air;
                                        if(!canOpen){
                                            break;
                                        }
                                    }
                                    if(!canOpen){
                                        break;
                                    }
                                }
                                if(canOpen || crucibleTile.isOpen()) {
                                    crucibleTile.isOpen(!crucibleTile.isOpen());
                                }
                                if (crucibleTile.isOpen()) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlockToAir(crucibleTile.xCoord - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1);
                                            world.setBlock(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, (crucibleTile.zCoord - 2) - j, BlockRegistrar.cruciNull, dir, 0x02);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, (crucibleTile.zCoord - 2) - j);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                } else if (!crucibleTile.isOpen() && tile.xCoord > crucibleTile.xCoord) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlock(crucibleTile.xCoord - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1, BlockRegistrar.cruciNull, dir, 0x02);
                                            world.setBlockToAir(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, (crucibleTile.zCoord - 2) - j);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                }
                            }
                            break;
                        case EAST:
                            PlenaInanis.logger.info("EAST");
                            //Positive X
                            if (tile.xCoord > crucibleTile.xCoord) {
                                boolean canOpen = true;
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 2; j++) {
                                        canOpen = world.getBlock((crucibleTile.xCoord + 2) + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1) == Blocks.air;
                                        if(!canOpen){
                                            break;
                                        }
                                    }
                                    if(!canOpen){
                                        break;
                                    }
                                }
                                if(canOpen || crucibleTile.isOpen()) {
                                    crucibleTile.isOpen(!crucibleTile.isOpen());
                                }
                                if (crucibleTile.isOpen()) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlockToAir(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, crucibleTile.zCoord - j);
                                            world.setBlock((crucibleTile.xCoord + 2) + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1, BlockRegistrar.cruciNull, dir, 0x02);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity((crucibleTile.xCoord + 2) + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                } else if (!crucibleTile.isOpen() && tile.zCoord > crucibleTile.zCoord) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlock(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, crucibleTile.zCoord - j, BlockRegistrar.cruciNull, dir, 0x02);
                                            world.setBlockToAir((crucibleTile.xCoord + 2) + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord + 1, crucibleTile.yCoord + i, crucibleTile.zCoord - j);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                }
                            }
                            break;
                        case WEST:
                            PlenaInanis.logger.info("WEST");
                            //Negative X
                            if (tile.xCoord < crucibleTile.xCoord) {
                                boolean canOpen = true;
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 2; j++) {
                                        canOpen = world.getBlock((crucibleTile.xCoord - 2) - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1) == Blocks.air;
                                        if(!canOpen){
                                            break;
                                        }
                                    }
                                    if(!canOpen){
                                        break;
                                    }
                                }
                                if(canOpen || crucibleTile.isOpen()) {
                                    crucibleTile.isOpen(!crucibleTile.isOpen());
                                }

                                if (crucibleTile.isOpen()) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlockToAir(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, crucibleTile.zCoord + j);
                                            world.setBlock((crucibleTile.xCoord - 2) - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1, BlockRegistrar.cruciNull, dir, 0x02);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity((crucibleTile.xCoord - 2) - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                } else if (!crucibleTile.isOpen() && tile.zCoord < crucibleTile.zCoord) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlock(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, crucibleTile.zCoord + j, BlockRegistrar.cruciNull, dir, 0x02);
                                            world.setBlockToAir((crucibleTile.xCoord - 2) - j, crucibleTile.yCoord + i, crucibleTile.zCoord - 1);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, crucibleTile.zCoord + j);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                }
                            }
                            break;
                        case SOUTH:
                            PlenaInanis.logger.info("SOUTH");
                            //Positive Z
                            if (tile.zCoord > crucibleTile.zCoord) {
                                boolean canOpen = true;
                                for (int i = 0; i < 3; i++) {
                                    for (int j = 0; j < 2; j++) {
                                        canOpen = world.getBlock(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, (crucibleTile.zCoord + 2) + j) == Blocks.air;
                                        if(!canOpen){
                                            break;
                                        }
                                    }
                                    if(!canOpen){
                                        break;
                                    }
                                }
                                if(canOpen || crucibleTile.isOpen()) {
                                    crucibleTile.isOpen(!crucibleTile.isOpen());
                                }
                                if (crucibleTile.isOpen()) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlockToAir(crucibleTile.xCoord + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1);
                                            world.setBlock(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, (crucibleTile.zCoord + 2) + j, BlockRegistrar.cruciNull, dir, 0x02);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, (crucibleTile.zCoord + 2) + j);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                } else if (!crucibleTile.isOpen() && tile.xCoord < crucibleTile.xCoord) {
                                    crucibleTile.setOpening(true);
                                    for (int i = 0; i < 3; i++) {
                                        for (int j = 0; j < 2; j++) {
                                            world.setBlock(crucibleTile.xCoord + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1, BlockRegistrar.cruciNull, dir, 0x02);
                                            world.setBlockToAir(crucibleTile.xCoord - 1, crucibleTile.yCoord + i, (crucibleTile.zCoord + 2) + j);
                                            TileCruciNull tileNull = (TileCruciNull) world.getTileEntity(crucibleTile.xCoord + j, crucibleTile.yCoord + i, crucibleTile.zCoord + 1);
                                            if (tileNull != null) {
                                                tileNull.primaryX = crucibleTile.xCoord;
                                                tileNull.primaryY = crucibleTile.yCoord;
                                                tileNull.primaryZ = crucibleTile.zCoord;
                                                tileNull.setDirection();
                                            }
                                        }
                                    }
                                    crucibleTile.setOpening(false);
                                }
                            }
                            break;
                    }
                }

            }else{
                player.openGui(PlenaInanis.instance, GuiHandler.CRUCIBLE_GUI, world, tile.primaryX, tile.primaryY, tile.primaryZ);
            }
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockCrucible));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l){
        return false;
    }
    //This tells minecraft to render surrounding blocks.
    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileCruciNull();
    }
}
