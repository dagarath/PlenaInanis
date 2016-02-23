package com.dagarath.mods.plenainanis.common.containers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotCompost;
import com.dagarath.mods.plenainanis.common.containers.slots.SlotFertilizer;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.items.ItemAirflowUpgrade;
import com.dagarath.mods.plenainanis.common.items.ItemMoistureUpgrade;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.config.ConfigurationHandler;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
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
        OUTPUT_7, OUTPUT_8, OUTPUT_9,
        UPGRADE_MOISTURE, UPGRADE_AIRFLOW

    }
    public TilePlenaComposter tile;

    public PlenaComposterContainer(IInventory playerInv, TilePlenaComposter tile){
        int offset = 31;
        this.tile = tile;

        // Tile Entity, Slot 0-11, Slot IDs 0-11
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_1.ordinal(), offset + 19, 17));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_2.ordinal(), offset + 37, 17));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_3.ordinal(), offset + 55, 17));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_4.ordinal(), offset + 73, 17));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_5.ordinal(), offset + 19, 35));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_6.ordinal(), offset + 37, 35));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_7.ordinal(), offset + 55, 35));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_8.ordinal(), offset + 73, 35));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_9.ordinal(), offset + 19, 53));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_10.ordinal(), offset + 37, 53));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_11.ordinal(), offset + 55, 53));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.INPUT_12.ordinal(), offset + 73, 53));


        // Output Slot, Slot ID 12
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_1.ordinal(), offset + 105, 17));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_2.ordinal(), offset + 123, 17));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_3.ordinal(), offset + 141, 17));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_4.ordinal(), offset + 105, 35));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_5.ordinal(), offset + 123, 35));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_6.ordinal(), offset + 141, 35));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_7.ordinal(), offset + 105, 53));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_8.ordinal(), offset + 123, 53));
        this.addSlotToContainer(new SlotFertilizer(this.tile, SlotType.OUTPUT_9.ordinal(), offset + 141, 53));

        //Upgrade Slots, Slot ID 13 & 14
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.UPGRADE_MOISTURE.ordinal(), offset + 183, 15));
        this.addSlotToContainer(new SlotCompost(this.tile, SlotType.UPGRADE_AIRFLOW.ordinal(), offset + 183, 36));

        // Player Inventory, Slot 15-41, Slot IDs 15-41
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18 + offset, 84 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 42-50
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x , 8 + x * 18 + offset, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot){

        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);

        if(slot != null && slot.getHasStack()){
            ItemStack newStack = slot.getStack();
            itemStack = newStack.copy();

            if(fromSlot < tile.getSizeInventory()){
                PlenaInanis.logger.info("Container Stack Size: " + newStack.stackSize);
                if(!this.mergeItemStack(newStack, SlotType.UPGRADE_AIRFLOW.ordinal() + 1, SlotType.UPGRADE_AIRFLOW.ordinal() + 36 + 1, true)){
                    return null;
                }

            }else if(newStack.getItem() instanceof ItemMoistureUpgrade ){
                    if(!this.mergeItemStack(newStack, SlotType.UPGRADE_MOISTURE.ordinal(), SlotType.UPGRADE_MOISTURE.ordinal() + 1, false)){
                        return null;
                    }
            }else if(newStack.getItem() instanceof ItemAirflowUpgrade ){
                    if(!this.mergeItemStack(newStack, SlotType.UPGRADE_AIRFLOW.ordinal(), SlotType.UPGRADE_AIRFLOW.ordinal() + 1, false)){
                        return null;
                    }
            }else if(PlenaInanis.saveData.composterExists(InfoHelper.getFullNameForItemStack(itemStack) + ":" + itemStack.getItemDamage())){
                    if(!this.mergeItemStack(newStack, SlotType.INPUT_1.ordinal(), SlotType.INPUT_9.ordinal() + 1, false)){
                        return null;
                    }
            }else {
                return null;
            }

            if(newStack.stackSize == 0){
                slot.putStack(null);
            }
            else{
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, newStack);

        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer player){
        super.onContainerClosed(player);
        if(ConfigurationHandler.compostSound.getBoolean()){
            player.playSound(PlenaInanisReference.MODID + ":bloop", 1.0f, 1.0f);
        }else {
            player.playSound("random.chestclosed", 1.0f, 1.0f);
        }
        tile.setOpen(false);
    }

    @Override
    public void addCraftingToCrafters(ICrafting p_75132_1_){
        super.addCraftingToCrafters(p_75132_1_);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tile.isUseableByPlayer(player);
    }


    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int p_75137_1_, int p_75137_2_) {

    }
}
