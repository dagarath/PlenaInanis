package com.dagarath.mods.plenainanis.common.containers.slots;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaRecipeManagerContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-02-01.
 */
public class SlotRecipeInput extends Slot {

    public SlotRecipeInput(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public void onSlotChange(ItemStack itemStack, ItemStack itemStack1) {
        if (itemStack != null && itemStack1 != null) {
            if (itemStack.getItem() == itemStack1.getItem()) {
                int i = itemStack1.stackSize - itemStack.stackSize;

                if (i > 0) {
                    this.onCrafting(itemStack, i);
                }
            }

        }
    }

    public ItemStack slotContains(){
        return this.getStack();
    }

    @Override
    public boolean canTakeStack(EntityPlayer p_82869_1_){
        PlenaRecipeManagerContainer container = (PlenaRecipeManagerContainer) PlenaInanis.proxy.getPlayer().openContainer;
        return !container.editMode();
    }
}
