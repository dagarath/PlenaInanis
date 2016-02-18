package com.dagarath.mods.plenainanis.common.containers.slots;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.PlenaSaveData;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagarath on 2016-01-22.
 */
public class SlotCompost extends Slot {
//    private PlenaSaveData.MachineData machineData;
    private List<String> itemNames = new ArrayList<String>();
    public SlotCompost(IInventory inventory, int x, int y, int z){
        super(inventory, x, y, z);
    }


//    @Override
//    public boolean isItemValid(ItemStack itemStack){
//        if(!PlenaInanis.saveData.machineDataExists("Composter")) {
//            PlenaInanis.saveData.createMachineData("Composter");
//        }
//        this.machineData = PlenaInanis.saveData.readMachineData("Composter");
//        String itemName = InfoHelper.getFullNameForItemStack(itemStack);
//        return machineData.inputExists(itemName, itemStack.getItemDamage());
//    }

}