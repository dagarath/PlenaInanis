package com.dagarath.mods.plenainanis.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

/**
 * Created by dagarath on 2016-01-27.
 */
public class SieveCommander extends CommandBase {

    public SieveCommander(){
    }
    @Override
    public String getCommandName() {
        return "sieve";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {

        String helpMsg = "Syntax: sieve <add/remove/removeoutput/update/reload/clear/list> <weight> (put output item in last slot of hotbar)";

        try {
            if(var1 instanceof EntityPlayerMP)
            {/*
                EntityPlayer player = getCommandSenderAsPlayer(var1);
                ItemStack heldItem = player.getCurrentEquippedItem();
                ItemStack outputItem = player.inventory.mainInventory[8];
                if(heldItem != null) {
                    if (var2[0].equals("add")) {
                        if(outputItem.getItem() != null) {
                            if (!PlenaInanis.sieveItems.contains(heldItem.getDisplayName())) {
                                PlenaInanis.sieveItems.add(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                                ObjectSerializer.writeSieve();
                                ObjectSerializer.writeSieve();
                            } else if(!PlenaInanis.sieveItems.outputContains(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem())))
                            {
                                PlenaInanis.sieveItems.addOutput(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                                ObjectSerializer.writeSieve();
                            }
                            else
                            {
                                player.addChatMessage(new ChatComponentText("Output already exists, use the update command"));
                            }
                        }
                    } else if (var2[0].equals("remove")) {
                        if (PlenaInanis.sieveItems.contains(heldItem.getDisplayName())) {
                            PlenaInanis.sieveItems.remove(heldItem.getDisplayName());
                            ObjectSerializer.writeSieve();
                        }
                    } else if (var2[0].equals(("removeoutput"))) {
                        if (PlenaInanis.sieveItems.contains(heldItem.getDisplayName())) {
                            //String[] outputData = PlenaInanis.sieveItems.get(heldItem.getDisplayName()).split(",");
                            if(PlenaInanis.sieveItems.outputContains(heldItem.getDisplayName(),Item.getIdFromItem(outputItem.getItem()))){
                                PlenaInanis.sieveItems.removeOutput(heldItem.getDisplayName(),Item.getIdFromItem(outputItem.getItem()));
                                ObjectSerializer.writeSieve();
                            }
                        } else {
                            player.addChatMessage(new ChatComponentText("Error: You suck!"));
                        }
                    } else if (var2[0].equals("update")) {
                        if (PlenaInanis.sieveItems.contains(heldItem.getDisplayName())) {
                            PlenaInanis.sieveItems.updateOutput(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                            ObjectSerializer.writeSieve();
                       }
                    }else if(var2[0].equals("reload")){
                       ObjectSerializer.getSieve();
                    }
                }else if(var2[0].equals("clear")){
                    PlenaInanis.sieveItems.clear();
                }else if(var2[0].equals("list")){
                    PlenaInanis.sieveItems.getList(player);
                }
                */
            }
            else{
                var1.addChatMessage(new ChatComponentText(helpMsg));
            }
        } catch (Exception ex) {
            System.out.println("Exception handling sieve command");
            ex.printStackTrace();
        }

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/sieve <add/remove/removeoutput/update/reload/clear> <weight> (put output item in last slot of hotbar)";
    }


}

