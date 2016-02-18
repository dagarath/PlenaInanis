package com.dagarath.mods.plenainanis.common.network.Packets;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.network.Packets.Helpers.PacketHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
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
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by dagarath on 2016-02-02.
 */
public class PacketSyncRecipeSlot implements IMessage, IMessageHandler<PacketSyncRecipeSlot, IMessage> {
    EntityPlayer player;
    String playerName;
    ItemStack itemStack;
    int slot;
    public PacketSyncRecipeSlot(){}

    public PacketSyncRecipeSlot(EntityPlayer player, int slot, ItemStack itemStack){
        this.player = player;
        this.playerName = this.player.getDisplayName();
        this.slot = slot;
        this.itemStack = itemStack;
//        PlenaInanis.logger.info("Slot Sync Requested for:" + slot);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketHelper.writeString(this.playerName, buf);
        ByteBufUtils.writeItemStack(buf, this.itemStack);
        buf.writeInt(slot);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketHelper.readString(buf);
        this.itemStack = ByteBufUtils.readItemStack(buf);
        this.slot = buf.readInt();
    }

    @Override
    public IMessage onMessage(PacketSyncRecipeSlot message, MessageContext ctx)
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
        if(message.itemStack != null) {
            Slot tmpSlot = (Slot) playerMP.openContainer.inventorySlots.get(message.slot);
                tmpSlot.putStack(message.itemStack);
        }
        return null;
    }
}
