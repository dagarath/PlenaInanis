package com.dagarath.mods.plenainanis.common.containers.slots;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaRecipeManagerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-02-01.
 */
public class SlotRecipeManager extends Slot {


    public SlotRecipeManager(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }


    @Override
    public void onSlotChange(ItemStack itemStack, ItemStack itemStack1) {
//        if (itemStack != null && itemStack1 != null) {
//            if (itemStack.getItem() == itemStack1.getItem()) {
//                int i = itemStack1.stackSize - itemStack.stackSize;
//
//                if (i > 0) {
//                    this.onCrafting(itemStack, i);
//                }
//            }
//        }
    }

    @Override
    public boolean isItemValid(ItemStack itemStack){
        PlenaRecipeManagerContainer container = (PlenaRecipeManagerContainer)PlenaInanis.proxy.getPlayer().openContainer;
        return container.editMode();
    }


    @Override
    public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
    }


    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_){
        PlenaRecipeManagerContainer container = (PlenaRecipeManagerContainer)PlenaInanis.proxy.getPlayer().openContainer;
        return container.editMode();
    }


}
