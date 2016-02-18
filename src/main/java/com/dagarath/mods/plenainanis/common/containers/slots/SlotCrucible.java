package com.dagarath.mods.plenainanis.common.containers.slots;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryNamespaced;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dagarath on 2016-02-15.
 */
public class SlotCrucible extends Slot {

    public SlotCrucible(IInventory iInventory, int index, int xPos, int yPos) {
        super(iInventory, index, xPos, yPos);
    }

    public boolean isItemValid(ItemStack itemStack)
    {
        if(getSlotIndex() == 0){
            if(GameRegistry.getFuelValue(itemStack) > 0){
                return true;
            }
            if(PlenaInanis.crucibleBurnTimes.containsKey(InfoHelper.getFullNameForItemStack(itemStack))) {
                return true;
            }

        }else if(getSlotIndex() == 1){
            if(PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(itemStack))) {
                return true;
            }
        }else if(getSlotIndex() == 2 && itemStack.getItem() == Items.bucket || itemStack.getItem() == Items.lava_bucket || itemStack.getItem() == Items.water_bucket) {
            return true;
        }

        return false;
    }
}
