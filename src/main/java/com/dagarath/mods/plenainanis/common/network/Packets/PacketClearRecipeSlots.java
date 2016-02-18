package com.dagarath.mods.plenainanis.common.network.Packets;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.network.Packets.Helpers.PacketHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dagarath on 2016-02-09.
 */
public class PacketClearRecipeSlots implements IMessage, IMessageHandler<PacketClearRecipeSlots, IMessage> {
    EntityPlayer player;
    String playerName;
    public PacketClearRecipeSlots(){}

    public PacketClearRecipeSlots(EntityPlayer player){
        this.player = player;
        this.playerName = this.player.getDisplayName();
//        PlenaInanis.logger.info("Slot clear request");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketHelper.writeString(this.playerName, buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketHelper.readString(buf);
    }

    @Override
    public IMessage onMessage(PacketClearRecipeSlots message, MessageContext ctx)
    {
        EntityPlayerMP playerMP = null;
        ArrayList list = (ArrayList) MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        for(Iterator iterator = list.iterator(); iterator.hasNext();) {
            playerMP = (EntityPlayerMP) iterator.next();
            if (playerMP.getDisplayName().equals(message.playerName)) {
//                PlenaInanis.logger.debug("Found Match for: " + playerMP.getDisplayName());
                break;
            }
        }
        Slot tmpSlot;
        for(int i = 1; i < 13; i++) {
            tmpSlot = (Slot) playerMP.openContainer.inventorySlots.get(i);
            if(tmpSlot.getStack() != null) {
//                PlenaInanis.logger.info("Clearing slot: " + i);
                tmpSlot.putStack((ItemStack) null);
            }
        }
        return null;
    }
}
