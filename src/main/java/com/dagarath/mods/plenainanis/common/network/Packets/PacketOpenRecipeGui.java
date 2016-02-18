package com.dagarath.mods.plenainanis.common.network.Packets;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.network.PacketHandler;
import com.dagarath.mods.plenainanis.common.network.Packets.Helpers.PacketHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-02-01.
 */
public class PacketOpenRecipeGui implements IMessage, IMessageHandler<PacketOpenRecipeGui, IMessage> {

    EntityPlayer player;
    public PacketOpenRecipeGui(){}

    public PacketOpenRecipeGui(EntityPlayer player){
        this.player = player;
    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public IMessage onMessage(PacketOpenRecipeGui message, MessageContext ctx)
    {
        ctx.getServerHandler().playerEntity.openGui(PlenaInanis.instance, GuiHandler.RECIPE_MAKER_GUI, ctx.getServerHandler().playerEntity.worldObj, (int)ctx.getServerHandler().playerEntity.posX, (int)ctx.getServerHandler().playerEntity.posY, (int)ctx.getServerHandler().playerEntity.posZ);
        return null;
    }
}
