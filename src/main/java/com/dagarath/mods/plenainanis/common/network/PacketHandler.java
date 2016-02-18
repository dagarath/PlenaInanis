package com.dagarath.mods.plenainanis.common.network;

import com.dagarath.mods.plenainanis.common.network.Packets.*;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by dagarath on 2016-01-27.
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(PlenaInanisReference.MODID.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(PacketOpenRecipeGui.class, PacketOpenRecipeGui.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketDropItemFromGui.class, PacketDropItemFromGui.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncRecipeSlot.class, PacketSyncRecipeSlot.class, 2, Side.SERVER);
        INSTANCE.registerMessage(PacketClearRecipeSlots.class, PacketClearRecipeSlots.class, 3, Side.SERVER);
        INSTANCE.registerMessage(PacketPurgeCrucible.class, PacketPurgeCrucible.class, 4, Side.SERVER);
        INSTANCE.registerMessage(PacketDumpCrucible.class, PacketDumpCrucible.class, 5, Side.SERVER);
    }
}
