package com.dagarath.mods.plenainanis.common.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-01-22.
 */
public class SlotFertilizer extends Slot {
    public SlotFertilizer(IInventory inventory, int x, int y, int z){
        super(inventory, x, y, z);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack){
        return false;
        /*if(itemStack.getDisplayName() == "Bone Meal"){
            System.out.println("itemIsValid");
            return true;
        }else{
            return false;
        }*/
    }
}
