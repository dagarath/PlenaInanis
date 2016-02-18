package com.dagarath.mods.plenainanis.common.commands;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.FileParser;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.HashMap;

/**
 * Created by dagarath on 2016-02-13.
 */
public class CrucibleCommander extends CommandBase {

    public CrucibleCommander(){}

    @Override
    public String getCommandName() {
        return "crucible";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {

        String helpMsg = "Syntax: crucible <add/remove/update/save/reload> <fuel/input> (<burnTime> <temperature> for fuel or <fluid> <cooktime>  for inputs.";

        try {
            if(var1 instanceof EntityPlayerMP)
            {
                EntityPlayer player = getCommandSenderAsPlayer(var1);
                ItemStack heldItem = player.getCurrentEquippedItem();

                if (var2[0].equals("add")) {
                    if(var2[1].equals("fuel") && !var2[2].isEmpty() && !var2[3].isEmpty()){
                        String checkname = InfoHelper.getFullNameForItemStack(heldItem);
                       if(!PlenaInanis.crucibleBurnTimes.containsKey(checkname)){
                           PlenaInanis.crucibleBurnTimes.put(checkname, Integer.parseInt(var2[2]));
                           FileParser.writeStringIntHashFile(PlenaInanis.burnFileName, PlenaInanis.crucibleBurnTimes);
                           PlenaInanis.crucibleFuelTemps.put(checkname, Integer.parseInt(var2[3]));
                           FileParser.writeStringIntHashFile(PlenaInanis.temperatureFileName, PlenaInanis.crucibleFuelTemps);
                       }else if(PlenaInanis.crucibleBurnTimes.containsKey(checkname)){
                           player.addChatMessage(new ChatComponentText(checkname +" already exists, use update command."));
                       }else{
                           player.addChatMessage(new ChatComponentText("You messed up, try again!"));
                       }
                    }else if(var2[1].equals("input")){
                        String checkname = InfoHelper.getFullNameForItemStack(heldItem);
                        if(!PlenaInanis.crucibleAllowedItems.containsKey(checkname)){
                            PlenaInanis.crucibleAllowedItems.put(checkname, var2[2]);
                            FileParser.writeStringStringHashFile(PlenaInanis.allowedItemsFileName, PlenaInanis.crucibleAllowedItems);
                            PlenaInanis.crucibleCookTimes.put(checkname, Integer.parseInt(var2[3]));
                            FileParser.writeStringIntHashFile(PlenaInanis.cookFileName, PlenaInanis.crucibleCookTimes);
                        }else if(PlenaInanis.crucibleAllowedItems.containsKey(checkname)){
                            player.addChatMessage(new ChatComponentText(checkname +" already exists, use update command."));
                        }else{
                            player.addChatMessage(new ChatComponentText("You messed up, try again!"));
                        }
                    }
                }
                else if(var2[0].equals("remove"))
                {
                    if(var2[1].equals("fuel")){

                    }else if(var2[1].equals("input")){

                    }
                }
                else if(var2[0].equals("update"))
                {
                    if(var2[1].equals("fuel")){

                    }else if(var2[1].equals("input")){

                    }
                }else if(var2[0].equals("reload")){
                    FileParser.readStringIntHash(PlenaInanis.burnFileName, PlenaInanis.crucibleBurnTimes);
                    FileParser.readStringIntHash(PlenaInanis.temperatureFileName, PlenaInanis.crucibleFuelTemps);
                    FileParser.readStringStringHash(PlenaInanis.allowedItemsFileName, PlenaInanis.crucibleAllowedItems);
                }else if(var2[0].equals("save")){
                    FileParser.writeStringIntHashFile(PlenaInanis.burnFileName, PlenaInanis.crucibleBurnTimes);
                    FileParser.writeStringIntHashFile(PlenaInanis.temperatureFileName, PlenaInanis.crucibleFuelTemps);
                    FileParser.writeStringStringHashFile(PlenaInanis.allowedItemsFileName, PlenaInanis.crucibleAllowedItems);
                    FileParser.writeStringIntHashFile(PlenaInanis.cookFileName, PlenaInanis.crucibleCookTimes);
                }
            }
            else{
                var1.addChatMessage(new ChatComponentText(helpMsg));
            }
        } catch (Exception ex) {
            System.out.println("Exception handling Plena Inanis Crucible command");
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
        return "/crucible <add/remove/update/save/reload> <fuel/input> (<burnTime> <temperature> for fuel or <fluid> <cooktime>  for inputs with item in hand)";
    }

}
