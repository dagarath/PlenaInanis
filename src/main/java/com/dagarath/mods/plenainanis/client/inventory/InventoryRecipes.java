package com.dagarath.mods.plenainanis.client.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-02-01.
 */
public class InventoryRecipes implements IInventory {
    private ItemStack[] stackList;
    private Container eventHandler;

    public InventoryRecipes(Container container){
        this.eventHandler = container;
        this.stackList = new ItemStack[13];
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? null : this.stackList[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        if (this.stackList[index] != null)
        {
            ItemStack itemstack;

            if (this.stackList[index].stackSize <= amount)
            {
                itemstack = this.stackList[index];
                this.stackList[index] = null;
                //this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
            else
            {
                itemstack = this.stackList[index].splitStack(amount);

                if (this.stackList[index].stackSize == 0)
                {
                    this.stackList[index] = null;
                }

                //this.eventHandler.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }


    public void setInventorySlotContents(int index, ItemStack itemstack) {
        this.stackList[index] = itemstack;
    }

    @Override
    public String getInventoryName() {
        return "container.recipemaker";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }
}
