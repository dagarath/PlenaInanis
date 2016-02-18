package com.dagarath.mods.plenainanis.common.network.Packets;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by dagarath on 2016-02-16.
 */
public class PacketDumpCrucible implements IMessage, IMessageHandler<PacketDumpCrucible, IMessage> {
    EntityPlayer player;
    int x;
    int y;
    int z;

    public PacketDumpCrucible(){}

    public PacketDumpCrucible(EntityPlayer player, int x, int y, int z){

        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public IMessage onMessage(PacketDumpCrucible message, MessageContext ctx)
    {
        if(ctx.side == Side.SERVER) {
            TilePlenaCrucible crucible = (TilePlenaCrucible) ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.x, message.y, message.z);

            if (crucible != null) {
                crucible.dumping(true);
            }
        }
        return null;
    }
}
