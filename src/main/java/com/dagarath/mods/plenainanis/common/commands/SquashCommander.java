package com.dagarath.mods.plenainanis.common.commands;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.ObjectSerializer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

/**
 * Created by dagarath on 2016-01-27.
 */
public class SquashCommander extends CommandBase {

    public SquashCommander(){
    }
    @Override
    public String getCommandName() {
        return "squash";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {

        String helpMsg = "Syntax: squash <add/remove/removeoutput/update/reload/clear/list> <weight> (put output item in last slot of hotbar)";

        try {
            /*
            if(var1 instanceof EntityPlayerMP)
            {
                EntityPlayer player = getCommandSenderAsPlayer(var1);
                ItemStack heldItem = player.getCurrentEquippedItem();
                ItemStack outputItem = player.inventory.mainInventory[8];
                if(heldItem != null) {
                    if (var2[0].equals("add")) {
                        if(outputItem.getItem() != null) {
                            if (!PlenaInanis.squasherItems.contains(heldItem.getDisplayName())) {
                                PlenaInanis.squasherItems.add(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                                ObjectSerializer.writeSquasher();
                                ObjectSerializer.writeSquasher();
                            } else if(!PlenaInanis.squasherItems.outputContains(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem())))
                            {
                                PlenaInanis.squasherItems.addOutput(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                                ObjectSerializer.writeSquasher();
                            }
                            else
                            {
                                player.addChatMessage(new ChatComponentText("Output already exists, use the update command"));
                            }
                        }
                    } else if (var2[0].equals("remove")) {
                        if (PlenaInanis.squasherItems.contains(heldItem.getDisplayName())) {
                            PlenaInanis.squasherItems.remove(heldItem.getDisplayName());
                            ObjectSerializer.writeSquasher();
                        }
                    } else if (var2[0].equals(("removeoutput"))) {
                        if (PlenaInanis.squasherItems.contains(heldItem.getDisplayName())) {
                            //String[] outputData = PlenaInanis.sieveItems.get(heldItem.getDisplayName()).split(",");
                            if(PlenaInanis.squasherItems.outputContains(heldItem.getDisplayName(),Item.getIdFromItem(outputItem.getItem()))){
                                PlenaInanis.squasherItems.removeOutput(heldItem.getDisplayName(),Item.getIdFromItem(outputItem.getItem()));
                                ObjectSerializer.writeSquasher();
                            }
                        } else {
                            player.addChatMessage(new ChatComponentText("Error: You suck!"));
                        }
                    } else if (var2[0].equals("update")) {
                        if (PlenaInanis.squasherItems.contains(heldItem.getDisplayName())) {
                            PlenaInanis.squasherItems.updateOutput(heldItem.getDisplayName(), Item.getIdFromItem(outputItem.getItem()), Integer.parseInt(var2[1]));
                            ObjectSerializer.writeSquasher();
                        }
                    }else if(var2[0].equals("reload")){
                        ObjectSerializer.getSieve();
                    }
                }else if(var2[0].equals("clear")){
                    PlenaInanis.squasherItems.clear();
                    ObjectSerializer.getSieve();
                }else if(var2[0].equals("list")){
                    PlenaInanis.squasherItems.getList(player);
                }
            }
            else{
                var1.addChatMessage(new ChatComponentText(helpMsg));
            }*/
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
        return "/squash <add/remove/removeoutput/update/reload/clear/list> <weight> (put output item in last slot of hotbar)";
    }
}


