package com.dagarath.mods.plenainanis.common.containers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotCrucible;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-02-12.
 */
public class PlenaCrucibleContainer extends Container {

    public TilePlenaCrucible tile;


    public enum SlotType{
        FUEL,
        PROCESS_INPUT,
        CONTAINER_INPUT,
        CONTAINER_OUTPUT
    }

    public PlenaCrucibleContainer(IInventory playerInv, TilePlenaCrucible tile) {
        this.tile = tile;
        this.addSlotToContainer(new SlotCrucible(this.tile, SlotType.FUEL.ordinal(), 8, 35));
        this.addSlotToContainer(new SlotCrucible(this.tile, SlotType.PROCESS_INPUT.ordinal(), 77, 17));
        this.addSlotToContainer(new SlotCrucible(this.tile, SlotType.CONTAINER_INPUT.ordinal(), 110, 17));
        this.addSlotToContainer(new SlotCrucible(this.tile, SlotType.CONTAINER_OUTPUT.ordinal(), 110, 53));

        // Player Inventory, Slot 13-39, Slot IDs 13-39
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 40-48
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        super.onContainerClosed(player);
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting){
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, this.tile.crucibleBurnTime);
        iCrafting.sendProgressBarUpdate(this, 1, this.tile.crucibleCookTime);
        iCrafting.sendProgressBarUpdate(this, 2, this.tile.currentItemBurnTime);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot){

        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack newStack = slot.getStack();
            itemStack = newStack.copy();

            if(fromSlot < tile.getSizeInventory()){
                if(!this.mergeItemStack(newStack, SlotType.CONTAINER_OUTPUT.ordinal() + 1, SlotType.CONTAINER_OUTPUT.ordinal() + 36 + 1, true)){
                    return null;
                }
            }else if(newStack.getItem() instanceof ItemBucket) {
                if (!this.mergeItemStack(newStack, SlotType.CONTAINER_INPUT.ordinal(), SlotType.CONTAINER_INPUT.ordinal() + 1, false)) {
                    return null;
                }
            }else if(PlenaInanis.crucibleFuelTemps.containsKey(InfoHelper.getFullNameForItemStack(itemStack))){
                if(!this.mergeItemStack(newStack, SlotType.FUEL.ordinal(), SlotType.FUEL.ordinal() + 1, false)){
                    return null;
                }
            }else if( PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(itemStack))){
                if(!this.mergeItemStack(newStack, SlotType.PROCESS_INPUT.ordinal(), SlotType.PROCESS_INPUT.ordinal() + 1, false)){
                    return null;
                }
            }else {
                return null;
            }

            if(newStack.stackSize == 0){
                slot.putStack((ItemStack)null);
            }
            else{
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, newStack);
        }
        return itemStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }
}
