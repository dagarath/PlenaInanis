package com.dagarath.mods.plenainanis.client;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.handlers.ClientEventHandler;
import com.dagarath.mods.plenainanis.client.handlers.KeyHandler;
import com.dagarath.mods.plenainanis.client.renderers.*;
import com.dagarath.mods.plenainanis.client.renderers.entities.RenderPebble;
import com.dagarath.mods.plenainanis.client.renderers.itemblocks.RenderComposterItem;
import com.dagarath.mods.plenainanis.client.renderers.itemblocks.RenderCrucibleItem;
import com.dagarath.mods.plenainanis.client.renderers.itemblocks.RenderSieveItem;
import com.dagarath.mods.plenainanis.client.renderers.itemblocks.RenderSquasherItem;
import com.dagarath.mods.plenainanis.common.CommonProxy;
import com.dagarath.mods.plenainanis.common.entities.EntityPebble;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSieve;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSquasher;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by dagarath on 2016-01-22.
 */
public class ClientProxy extends CommonProxy {
    public KeyHandler keyHandler = new KeyHandler();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }


    @Override
    public EntityPlayer getPlayerFromMessageContext(MessageContext ctx) {
        switch (ctx.side) {
            case CLIENT: {
                return Minecraft.getMinecraft().thePlayer;
            }
            case SERVER: {
                return ctx.getServerHandler().playerEntity;
            }
            default:
                assert false : "Invalid side in TestMsgHandler: " + ctx.side;
        }
        return null;
    }

    @Override
    public World getWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public EntityPlayer getPlayer() {
        return FMLClientHandler.instance().getClient().thePlayer;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initRenderers() {
        super.initRenderers();
        //Composters
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlenaComposter.class, new RendererPlenaComposter());
        RenderingRegistry.registerBlockHandler(new RenderComposterItem());

        //Squasher
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlenaSquasher.class, new RendererPlenaSquasher());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockRegistrar.blockSquasher), new RenderSquasherItem());

        //Sieve
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlenaSieve.class, new RendererPlenaSieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockRegistrar.blockSieve), new RenderSieveItem());

        //Smasher
        MinecraftForgeClient.registerItemRenderer(ItemRegistrar.itemBlockSmasher, new RendererBlockSmasher());

        //Pebble
        RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, new RenderPebble());

        //Crucible
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlenaCrucible.class, new RendererPlenaCrucible());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BlockRegistrar.blockCrucible), new RenderCrucibleItem());
    }

    @Override
    public void registerTileEntities() {
        super.registerTileEntities();



    }

    @Override
    public void registerKeyBindings() {
        FMLCommonHandler.instance().bus().register(keyHandler);
    }


    @Override
    public void registerHandlers() {
        super.registerHandlers();

        PlenaInanis.localEventHandler = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(PlenaInanis.localEventHandler);
        MinecraftForge.EVENT_BUS.register(PlenaInanis.localEventHandler);
    }
}
