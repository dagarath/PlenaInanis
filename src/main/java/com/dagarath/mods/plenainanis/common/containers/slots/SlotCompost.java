package com.dagarath.mods.plenainanis.common.containers.slots;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.items.ItemAirflowUpgrade;
import com.dagarath.mods.plenainanis.common.items.ItemMoistureUpgrade;
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


    @Override
    public boolean isItemValid(ItemStack itemStack){
        return true;
    }
}