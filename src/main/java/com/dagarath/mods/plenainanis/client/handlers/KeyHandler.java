package com.dagarath.mods.plenainanis.client.handlers;


import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.network.PacketHandler;
import com.dagarath.mods.plenainanis.common.network.Packets.PacketOpenRecipeGui;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Created by dagarath on 2016-02-01.
 */
public class KeyHandler {

    public KeyBinding key = new KeyBinding("Plena Recipe Maker",
            Keyboard.KEY_R, "key.plenainanis.recipemaker");

    public KeyHandler() {
        ClientRegistry.registerKeyBinding(key);
    }

    @SideOnly(value= Side.CLIENT)
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER) return;
        if (event.phase == TickEvent.Phase.START ) {
            if (key.getIsKeyPressed() && FMLClientHandler.instance().getClient().inGameHasFocus) {
                //event.player.openGui(PlenaInanis.instance, 1, PlenaInanis.proxy.getWorld(), (int)event.player.posX, (int)event.player.posY, (int)event.player.posZ);
                //event.player.openGui(PlenaInanis.instance, 1,  PlenaInanis.proxy.getWorld(), (int) event.player.posX, (int) event.player.posY, (int) event.player.posZ);
                PacketHandler.INSTANCE.sendToServer(new PacketOpenRecipeGui(event.player));
            }
        }
    }

}
