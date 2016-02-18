package com.dagarath.mods.plenainanis.common;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.GuiHandler;
import com.dagarath.mods.plenainanis.common.handlers.ServerEventHandler;
import com.dagarath.mods.plenainanis.common.tileentitites.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;


/**
 * Created by dagarath on 2016-01-22.
 */
public class CommonProxy {


    public void preInit(FMLPreInitializationEvent event) {
    }

    public void initSounds()
    {

    }

    public void initRenderers()
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(PlenaInanis.instance, new GuiHandler());
    }

    public void initGui()
    {

    }

    public void initCapes()
    {

    }

    public void registerTileEntities(){

        GameRegistry.registerTileEntity(TilePlenaComposter.class, "Composter");
        GameRegistry.registerTileEntity(TilePlenaSquasher.class, "Squasher");
        GameRegistry.registerTileEntity(TilePlenaSieve.class, "Sieve");
        GameRegistry.registerTileEntity(TilePlenaRichSoil.class, "RichSoil");
        GameRegistry.registerTileEntity(TilePlenaCrucible.class, "Crucible");
        GameRegistry.registerTileEntity(TileCruciNull.class, "CruciNull");
    }


    public EntityPlayer getPlayerFromMessageContext(MessageContext ctx)
    {
        switch(ctx.side)
        {
            case CLIENT:
            {
                assert false : "Message for CLIENT received on dedicated server";
            }
            case SERVER:
            {
                EntityPlayer entityPlayerMP = ctx.getServerHandler().playerEntity;
                return entityPlayerMP;
            }
            default:
                assert false : "Invalid side in TestMsgHandler: " + ctx.side;
        }
        return null;
    }

    public void registerKeyBindings() {}

    public EntityPlayer getPlayer() {
        return null;
    }

    public World getWorld() {
        return DimensionManager.getWorld(0);
    }

    public void registerHandlers(){
        PlenaInanis.remoteEventHandler = new ServerEventHandler();
        MinecraftForge.EVENT_BUS.register(PlenaInanis.remoteEventHandler);
        FMLCommonHandler.instance().bus().register(PlenaInanis.remoteEventHandler);
    }
}
