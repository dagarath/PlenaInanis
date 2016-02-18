package com.dagarath.mods.plenainanis.common.containers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotCompost;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotFertilizer;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-01-22.
 */
public class PlenaComposterContainer extends Container {
    public enum SlotType {
        INPUT_1, INPUT_2, INPUT_3,
        INPUT_4, INPUT_5, INPUT_6,
        INPUT_7, INPUT_8, INPUT_9,
        INPUT_10, INPUT_11, INPUT_12,
        OUTPUT_1, OUTPUT_2, OUTPUT_3,
        OUTPUT_4, OUTPUT_5, OUTPUT_6,
        OUTPUT_7, OUTPUT_8, OUTPUT_9

    }
    public TilePlenaComposter tile;
    public int lastProgess;

    public PlenaComposterContainer(IInventory playerInv, TilePlenaComposter tile){
        this.tile = tile;

        // Tile Entity, Slot 0-11, Slot IDs 0-11
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_1.ordinal(), 19, 17));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_2.ordinal(), 37, 17));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_3.ordinal(), 55, 17));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_4.ordinal(), 73, 17));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_5.ordinal(), 19, 35));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_6.ordinal(), 37, 35));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_7.ordinal(), 55, 35));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_8.ordinal(), 73, 35));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_9.ordinal(), 19, 53));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_10.ordinal(), 37, 53));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_11.ordinal(), 55, 53));
        this.addSlotToContainer(new SlotCompost(tile, SlotType.INPUT_12.ordinal(), 73, 53));


        // Output Slot, Slot ID 12
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_1.ordinal(), 105, 17));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_2.ordinal(), 123, 17));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_3.ordinal(), 141, 17));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_4.ordinal(), 105, 35));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_5.ordinal(), 123, 35));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_6.ordinal(), 141, 35));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_7.ordinal(), 105, 53));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_8.ordinal(), 123, 53));
        this.addSlotToContainer(new SlotFertilizer(tile, SlotType.OUTPUT_9.ordinal(), 141, 53));

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
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot){

        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);

        if(slot != null && slot.getHasStack()){
            ItemStack newStack = slot.getStack();
            itemStack = newStack.copy();

            //From Slot
            if(fromSlot > SlotType.OUTPUT_1.ordinal() && fromSlot < SlotType.OUTPUT_9.ordinal())
            {

                if(!this.mergeItemStack(newStack, SlotType.OUTPUT_9.ordinal() + 1, SlotType.OUTPUT_9.ordinal() + 36 + 1, true)){
                    return null;
                }

                slot.onSlotChange(newStack, itemStack);

            }
            else if(fromSlot > SlotType.OUTPUT_9.ordinal())
            {
                if(PlenaInanis.saveData.composterExists(InfoHelper.getFullNameForItemStack(newStack) + ":" + newStack.getItemDamage()))
                {
                    if(!this.mergeItemStack(newStack, SlotType.INPUT_1.ordinal(), SlotType.INPUT_12.ordinal() + 1, false))
                    {
                        return null;
                    }
                }
                else if(fromSlot >= SlotType.OUTPUT_9.ordinal() + 1 && fromSlot <= SlotType.OUTPUT_9.ordinal() + 28)
                {
                    if(!this.mergeItemStack(newStack, SlotType.OUTPUT_9.ordinal() + 28, SlotType.OUTPUT_9.ordinal() + 37, false)){
                        return null;
                    }
                }else if(fromSlot >= SlotType.OUTPUT_9.ordinal() + 28 && fromSlot < SlotType.OUTPUT_9.ordinal() + 37 && !this.mergeItemStack(newStack, SlotType.OUTPUT_9.ordinal() + 1, SlotType.OUTPUT_9.ordinal() +28, false))
                {
                    return null;
                }
            }
            else if(!this.mergeItemStack(newStack, SlotType.OUTPUT_9.ordinal() + 1, SlotType.OUTPUT_9.ordinal() + 37, false))
            {
                return null;
            }else if(fromSlot < SlotType.OUTPUT_9.ordinal()){
                return null;
            }

            if(newStack.stackSize == 0){
                slot.putStack((ItemStack)null);
            }
            else{
                slot.onSlotChanged();
            }

            if(newStack.stackSize == itemStack.stackSize){
                return null;
            }
            slot.onPickupFromSlot(player, newStack);

        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        super.onContainerClosed(player);
        tile.setOpen(false);
    }

    @Override
    public void addCraftingToCrafters(ICrafting p_75132_1_){
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.tile.compostProgress);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }


    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastProgess != this.tile.compostProgress) {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.compostProgress);
            }
        }
        this.lastProgess = this.tile.compostProgress;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
        if (p_75137_1_ == 0)
        {
            this.tile.compostProgress = p_75137_2_;
        }
    }
}
