package com.dagarath.mods.plenainanis.common.network.Packets;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.network.Packets.Helpers.PacketHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by dagarath on 2016-02-02.
 */
public class PacketDropItemFromGui implements IMessage, IMessageHandler<PacketDropItemFromGui, IMessage> {
    EntityPlayer player;
    String itemName;
    int amount;
    public PacketDropItemFromGui(){}

    public PacketDropItemFromGui(EntityPlayer player, String itemName, int amount){
        this.player = player;
        this.itemName = itemName;
        this.amount = amount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketHelper.writeString(this.itemName, buf);
        buf.writeInt(this.amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketHelper.readString(buf);
        buf.readInt();
    }

    @Override
    public IMessage onMessage(PacketDropItemFromGui message, MessageContext ctx)
    {
        //Item item = GameRegistry.findItem(message.itemName)
        //ItemStack itemStack = new ItemStack(item);
        //ctx.getServerHandler().playerEntity.dropPlayerItemWithRandomChoice(, false);
        return null;
    }
}
