package com.dagarath.mods.plenainanis.common.network.Packets.Helpers;

import io.netty.buffer.ByteBuf;

/**
 * Created by dagarath on 2016-02-02.
 */
public class PacketHelper {
    public static void writeString(String string, ByteBuf data) throws IndexOutOfBoundsException
    {
        byte[] stringBytes = string.getBytes();
        data.writeInt(stringBytes.length);
        data.writeBytes(stringBytes);
    }

    public static String readString(ByteBuf data) throws IndexOutOfBoundsException
    {
        int length = data.readInt();
        byte[] stringBytes = new byte[length];
        data.readBytes(stringBytes);

        return new String(stringBytes);
    }
}
