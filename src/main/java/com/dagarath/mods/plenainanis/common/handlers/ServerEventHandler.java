package com.dagarath.mods.plenainanis.common.handlers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.blocks.*;
import com.dagarath.mods.plenainanis.common.items.ItemBlockSmasher;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TileCruciNull;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSquasher;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.BlockFluidBase;

/**
 * Created by dagarath on 2016-01-23.
 */
public class ServerEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event){
        //Block
        ItemStack itemStack =event.getPlayer().getCurrentEquippedItem();
        if(itemStack != null){
            Item heldItem = itemStack.getItem();
            if(heldItem instanceof ItemBlockSmasher && event.block instanceof BlockPlenaSquasher){
                event.setResult(Event.Result.DENY);
                event.setCanceled(true);
            }

        }
    }

//    @SubscribeEvent
//    public void creativeClick(ClickEvent event){
//    }

    @SubscribeEvent
    public void harvestDrop(BlockEvent.HarvestDropsEvent event){
        if(event.block instanceof BlockCruciNull){
            event.drops.clear();
            event.drops.add(new ItemStack(BlockRegistrar.blockCrucible));
        }
    }


    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event){
        EntityPlayer player = event.entityPlayer;
        if(event.action == Action.RIGHT_CLICK_BLOCK)
        {
            if(event.world.getBlock(event.x, event.y, event.z) instanceof BlockPlenaComposter){
                BlockPlenaComposter composter = (BlockPlenaComposter)event.world.getBlock(event.x, event.y, event.z);
                player = event.entityPlayer;
                ItemStack itemStack = player.getCurrentEquippedItem();
                if(itemStack != null && itemStack.getItem() != null && Block.getBlockFromItem(itemStack.getItem()) != null){
//                    event.useBlock = Event.Result.DEFAULT;
                    composter.onBlockActivated(event.world, event.x, event.y, event.z, player, 0, 0, 0 ,0);
                    event.useItem = Event.Result.DENY;
                }
            }else if(event.world.getBlock(event.x, event.y, event.z) instanceof BlockPlenaCrucible){
                if(event.entityPlayer.getCurrentEquippedItem() != null) {
                    if (Block.getBlockFromItem(event.entityPlayer.getCurrentEquippedItem().getItem())!= null) {
                        BlockPlenaCrucible crucible = (BlockPlenaCrucible) event.world.getBlock(event.x, event.y, event.z);
                        PlenaInanis.logger.info("Trying");
                        crucible.onBlockActivated(event.world, event.x, event.y, event.z, player, 0, 0, 0, 0);
                        //event.useItem = Event.Result.DENY;
                        //event.useBlock = Event.Result.DENY;
                    }
                }
//                PlenaInanis.logger.info("Right Clicked");
            }else if(event.world.getBlock(event.x, event.y, event.z) instanceof BlockCruciNull){
                TileCruciNull cruciNull = (TileCruciNull)event.world.getTileEntity(event.x, event.y, event.z);
                if(cruciNull.xCoord == cruciNull.primaryX && cruciNull.zCoord == cruciNull.primaryZ && cruciNull.yCoord == cruciNull.primaryY + 1) {
                    BlockPlenaCrucible crucible = (BlockPlenaCrucible) event.world.getBlock(cruciNull.primaryX, cruciNull.primaryY, cruciNull.primaryZ);
                    crucible.onBlockActivated(event.world, cruciNull.primaryX, cruciNull.primaryY, cruciNull.primaryZ, player, 0, 0, 0, 0);
                    event.useItem = Event.Result.DENY;
//                    PlenaInanis.logger.info("Right Clicked CruciNull");
                }
            }
            else if(event.world.getBlock(event.x, event.y, event.z) instanceof BlockPlenaSquasher){
                    event.useItem = Event.Result.DENY;
            }else if(event.world.getBlock(event.x, event.y, event.z) instanceof BlockPlenaSieve){
                    //event.useBlock = Event.Result.DEFAULT;
                    event.useItem = Event.Result.ALLOW;
                event.useBlock = Event.Result.ALLOW;
            }
        }else if(event.action == Action.LEFT_CLICK_BLOCK){
            ItemStack itemStack = player.getCurrentEquippedItem();
            if(itemStack != null) {
                Item heldItem = itemStack.getItem();
                if(heldItem != null) {
                    Block testBlock = event.world.getBlock(event.x, event.y, event.z);
                    if (event.world.getBlock(event.x, event.y, event.z) instanceof BlockPlenaSquasher) {

                        if (heldItem instanceof ItemBlockSmasher) {
                            TilePlenaSquasher squasher = (TilePlenaSquasher) event.world.getTileEntity(event.x, event.y, event.z);

                            squasher.setProgress(squasher.getProgress() + 1, player);

                        }
                    } else if (heldItem instanceof ItemBlockSmasher
                            && !(testBlock instanceof BlockObsidian)
                            && !(testBlock instanceof BlockFluidBase)
                            && !(testBlock instanceof BlockPlenaCrucible)
                            && !testBlock.getUnlocalizedName().equals("tile.bedrock")) {
                        event.world.setBlockToAir(event.x, event.y, event.z);
                        event.world.playSoundAtEntity(event.entityPlayer, PlenaInanisReference.MODID +":thwap", 0.5f, 0.75f);
                        //PlenaInanis.logger.info(testBlock.getUnlocalizedName());
                    }
                }
            }
        }
    }
}
