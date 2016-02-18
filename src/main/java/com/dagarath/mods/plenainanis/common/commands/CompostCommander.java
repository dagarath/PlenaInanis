package com.dagarath.mods.plenainanis.common.commands;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.ObjectSerializer;
import com.dagarath.mods.plenainanis.common.helpers.PlenaSaveData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagarath on 2016-01-23.
 */
public class CompostCommander extends CommandBase {

    public CompostCommander(){
    }
    @Override
    public String getCommandName() {
        return "compost";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {

        String helpMsg = "Syntax: compost <add/remove/update> <cost>";

        try {
            if(var1 instanceof EntityPlayerMP)
            {
                EntityPlayer player = getCommandSenderAsPlayer(var1);
                ItemStack heldItem = player.getCurrentEquippedItem();

                if (var2[0].equals("add")) {
//                    PlenaSaveData.MachineData compostData = PlenaInanis.saveData.readMachineData("Composter");
                    //if(!PlenaInanis.compostItems.containsKey(heldItem.getUnlocalizedName())){
                    //    PlenaInanis.compostItems.put(heldItem.getUnlocalizedName(), Integer.parseInt(var2[1]));
                    //    ObjectSerializer.writeCompost();
                    //}
                }else if(var2[0].equals("remove")){
                    //if(PlenaInanis.compostItems.containsKey(heldItem.getUnlocalizedName())){
                    //    PlenaInanis.compostItems.remove(heldItem.getUnlocalizedName());
                    //    ObjectSerializer.writeCompost();
                    //}
                }else if(var2[0].equals("update")){
                    //if(PlenaInanis.compostItems.containsKey(heldItem.getUnlocalizedName())){
                    //    PlenaInanis.compostItems.replace(heldItem.getUnlocalizedName(), Integer.parseInt(var2[1]));
                    //    ObjectSerializer.writeCompost();
                    //}
                }
            }
            else{
                var1.addChatMessage(new ChatComponentText(helpMsg));
            }
        } catch (Exception ex) {
            System.out.println("Exception handling Growing Up! command");
            ex.printStackTrace();
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/compost <add/remove/update> <cost>";
    }
}

