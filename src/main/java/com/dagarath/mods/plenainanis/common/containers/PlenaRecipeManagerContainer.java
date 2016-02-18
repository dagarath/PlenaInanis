package com.dagarath.mods.plenainanis.common.containers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.inventory.InventoryRecipes;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotRecipeInput;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotRecipeManager;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.ObjectSerializer;
import com.dagarath.mods.plenainanis.common.helpers.OutputData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by dagarath on 2016-02-01.
 */
public class PlenaRecipeManagerContainer extends Container {

    InventoryRecipes recipeMatrix = new InventoryRecipes(this);
    public int selectedOutput = 0;


    private boolean isInEditMode = false;
    public ArrayList<String> outputIDs = new ArrayList();
    public ArrayList<Integer> weightValues = new ArrayList();
    public ArrayList<Integer> minOutputs = new ArrayList();
    public ArrayList<Integer> maxOutputs = new ArrayList();
    public ArrayList<Integer> metaData = new ArrayList();

    private int setForSlot = -1;

    public enum SlotType {
        INPUT, OUTPUT_1, OUTPUT_2,
        OUTPUT_3, OUTPUT_4, OUTPUT_5,
        OUTPUT_6, OUTPUT_7, OUTPUT_8,
        OUTPUT_9, OUTPUT_10, OUTPUT_11,
        OUTPUT_12
    }

    public PlenaRecipeManagerContainer(InventoryPlayer playerInv){
        //When an item is placed in the input slots the output slots are populated.
        //When the input slot is clicked with an item present you are given the option to remove the input from the save data
        //When an item is placed in an output slot or an output slot is clicked with an item present, you are presented with text fields that allow you to setup the item, if the item does not exist it will setup with default values
        //Slots are highlighted when clicked, and all other slots highlights are removed.

        //Slot 1 (ID 0)
        this.addSlotToContainer(new SlotRecipeInput(this.recipeMatrix, SlotType.INPUT.ordinal(),7, 7));

        //Slot 2-11 (ID 1-10)
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_1.ordinal(), 7, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_2.ordinal(), 7 + 18, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_3.ordinal(), 7 + 36, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_4.ordinal(), 7 + 54, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_5.ordinal(), 7 + 72, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_6.ordinal(), 7 + 90, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_7.ordinal(), 7 + 108, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_8.ordinal(), 7 + 126, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_9.ordinal(), 7 +  144, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_10.ordinal(), 7 +  162, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_11.ordinal(), 7 +  180, 40));
        this.addSlotToContainer(new SlotRecipeManager(this.recipeMatrix, SlotType.OUTPUT_12.ordinal(), 7 +  198, 40));


        //Slot 12-30 (ID 11-29)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 7 + j * 18, 62 + i * 18));
            }
        }

        // Slot 31-39 (ID 30-38)
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInv, i, 7 + i * 18, 120));
        }
        this.onCraftMatrixChanged(this.recipeMatrix);
    }
    @Override
    public void onCraftMatrixChanged(IInventory inventory){
        super.onCraftMatrixChanged(inventory);
    }


    public void populateInventory(){
        if(getSlot(0).getStack() == null) {
            PlenaInanis.logger.warn("No input exists");
        }else if(getSlot(0).getStack() != null){

//            switch (PlenaInanis.saveData.getCurrentMachineIndex()) {
//                case 0:
//                    this.machineData = PlenaInanis.saveData.readMachineData("Composter");
//                    if (this.machineData.inputExists(inputName(), getSlot(0).getStack().getItemDamage())) {
//                        getAndSetOutputs();
//                    }
//                    break;
//                case 1:
//                    this.machineData = PlenaInanis.saveData.readMachineData("Sieve");
//                    if (this.machineData.inputExists(inputName(), getSlot(0).getStack().getItemDamage())) {
//                        getAndSetOutputs();
//                    }
//                    break;
//                case 2:
//                    this.machineData = PlenaInanis.saveData.readMachineData("Squasher");
//                    if (this.machineData.inputExists(inputName(), getSlot(0).getStack().getItemDamage())) {
//                        getAndSetOutputs();
//                    }
//                    break;
//            }
        }
    }

    public String inputName(){
        ItemStack testStack = getSlot(0).getStack();
        return InfoHelper.getFullNameForItemStack(testStack);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if(!player.capabilities.isCreativeMode){

            if (!PlenaInanis.proxy.getWorld().isRemote)
            {
                ItemStack itemstack = this.recipeMatrix.getStackInSlotOnClosing(0);

                if (itemstack != null)
                {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
        this.setEditMode(false);
        super.onContainerClosed(player);
        //ObjectSerializer.writeSave();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public boolean editMode(){
        return this.isInEditMode;
    }

    public void setEditMode(boolean bool){
        this.isInEditMode = bool;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot)
    {
        ItemStack itemsStack = null;
        Slot slot = (Slot)this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {

            ItemStack newStack = slot.getStack();
            itemsStack = newStack.copy();
//            PlenaInanis.logger.info("Slot: " + fromSlot + " itemStack: " + newStack.getUnlocalizedName());
            if (fromSlot == SlotType.INPUT.ordinal()) {
                if (!this.mergeItemStack(newStack, SlotType.OUTPUT_12.ordinal() + 1, SlotType.OUTPUT_12.ordinal() + 36 + 1, true)) {
                    return null;
                }
                slot.onSlotChange(newStack, itemsStack);
            }else if(fromSlot > SlotType.INPUT.ordinal() && fromSlot < SlotType.OUTPUT_12.ordinal() + 1) {
                return null;
            }else if(fromSlot >= SlotType.OUTPUT_9.ordinal() + 1 && fromSlot <= SlotType.OUTPUT_12.ordinal() + 28) {
                if (!this.mergeItemStack(newStack, SlotType.OUTPUT_1.ordinal(), SlotType.OUTPUT_12.ordinal() + 1, false)) {
                    return null;
                }
            }else if(fromSlot >= SlotType.OUTPUT_12.ordinal() + 28 && fromSlot < SlotType.OUTPUT_12.ordinal() + 37 ) {
                if(!this.mergeItemStack(newStack, SlotType.OUTPUT_12.ordinal() + 1, SlotType.OUTPUT_12.ordinal() + 28, false)) {
                    return null;
                }
            }

            if(newStack.stackSize == 0){
                slot.putStack((ItemStack)null);
            }
            else{
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, newStack);
        }
        return itemsStack;
    }
    @Override
    public void addCraftingToCrafters(ICrafting p_75132_1_) {
        super.addCraftingToCrafters(p_75132_1_);
    }

    public void clearOutputs(){
        for(int i = 1; i < 13; i++){
            Slot clearSlot = getSlot(i);
            clearSlot.putStack((ItemStack)null);
        }
    }

    @Override
    public ItemStack slotClick(int index, int p_75144_2_, int p_75144_3_, EntityPlayer player){

//        if(index < 13 && !editMode()){
//            PlenaInanis.logger.info("Slot info: " + index + " " + p_75144_2_ + " " + p_75144_3_);
//            this.selectedOutput = index;
//
//        }else if (!editMode() && index > 13){
//            this.selectedOutput = -1;
//        }
        return super.slotClick(index, p_75144_2_, p_75144_3_, player);
    }


}
