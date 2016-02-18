package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.RandomCollection;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by dagarath on 2016-01-30.
 */
public class TilePlenaRichSoil extends TileEntity {
    private int direction;
    private int lifeTime = 0;
    private int totalLife = 72000;

    public TilePlenaRichSoil(){

    }




    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Direction", this.direction);
        //System.out.println("Saved Direction: " + nbt.getInteger("Direction"));
        nbt.setInteger("Lifetime", this.lifeTime);
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if(nbt.hasKey("Direction")){
            this.direction = nbt.getInteger("Direction");
            //System.out.println("Loaded Direction: " + nbt.getInteger("Direction"));
        }

        if(nbt.hasKey("Lifetime")){
            this.lifeTime = nbt.getInteger("Lifetime");
        }

    }

    @Override
    public Packet getDescriptionPacket(){
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet){
        this.readFromNBT(packet.func_148857_g());
    }


    public int getDirection()
    {
        return this.direction;
    }

    public void setDirection(int par1)
    { this.direction = par1; }

    public void refillLife(EntityPlayer player){
        this.incrementLife(-9000);
        ItemStack playerStack = player.getCurrentEquippedItem();
        if(playerStack.stackSize > 1){
            playerStack.stackSize--;
        }else{
            player.destroyCurrentEquippedItem();
        }
    }


    public void incrementLife(int increase){
        this.lifeTime += increase;
        if(this.lifeTime >= this.totalLife){
            this.worldObj.setBlock(xCoord,yCoord,zCoord, Blocks.dirt);
        }else if(this.lifeTime < 0){
            this.lifeTime = 0;
        }
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }
    @Override
    public void updateEntity(){
        super.updateEntity();
    }
}
