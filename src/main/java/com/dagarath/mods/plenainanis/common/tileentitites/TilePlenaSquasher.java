package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.OutputData;
import com.dagarath.mods.plenainanis.common.helpers.PlenaSaveData;
import com.dagarath.mods.plenainanis.common.helpers.RandomCollection;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

/**
 * Created by dagarath on 2016-01-26.
 */
public class TilePlenaSquasher extends TileEntity implements IInventory {

    private int direction;
    private int smashProgress = 0;
    private String customName = "Squasher";
    private ItemStack[] inventory;



    public TilePlenaSquasher(){

        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);

                String curInputItem = InfoHelper.getFullNameForItemStack(getStackInSlot(0)) + ":" + getStackInSlot(0).getItemDamage();
//                PlenaInanis.logger.info(curInputItem);
                HashMap<String, OutputData> outputData = PlenaInanis.saveData.getSquasherOutputs(curInputItem);
                int minOutput = 0;
                int maxOutput = 0;
                Item itemToDrop;
                String curOutputItem = "";
                if(outputData != null) {
                    RandomCollection collection = new RandomCollection();
                    for (Iterator<Map.Entry<String, OutputData>> it = outputData.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<String, OutputData> entry = it.next();
                        if(entry != null){
                            OutputData tmpOutput = entry.getValue();
                            PlenaInanis.logger.info(tmpOutput.outputName);
                            collection.add(tmpOutput.weight, tmpOutput.outputName);
                        }
                    }
                    curOutputItem = collection.next();
                }
                PlenaInanis.logger.info(curOutputItem.substring(0, curOutputItem.lastIndexOf(":")));
                OutputData outputSet = PlenaInanis.saveData.getSquasherOutput(curInputItem, curOutputItem);

                ItemStack dropStack = InfoHelper.getStackWithNullCheck(curOutputItem.substring(0, curOutputItem.lastIndexOf(":")));

                PlenaInanis.logger.info("Damage " + curOutputItem.substring(curOutputItem.lastIndexOf(":") + 1));

                if(dropStack != null) {
                    dropStack.setItemDamage(Integer.parseInt(curOutputItem.substring(curOutputItem.lastIndexOf(":") + 1)));
                    dropStack.stackSize = (int)(Math.random() * outputSet.maxOutput) + outputSet.minOutput;
                    EntityItem droppedItem = new EntityItem(this.worldObj, xCoord, yCoord + 0.5, zCoord, dropStack);
                    droppedItem.setVelocity(0,0,0);
                    this.worldObj.spawnEntityInWorld(droppedItem);
                    this.setInventorySlotContents(index, null);
                    this.markDirty();
                    return null;
                }
                return null;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        if(this.worldObj != null) {
            this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
        this.markDirty();
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.squasher";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public int getSizeInventory(){
        return 1;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;//this.worldObj.getTileEntity(xCoord,yCoord,zCoord) == this && player.getDistanceSq(xCoord,yCoord,zCoord) <= 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }


    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Direction", this.direction);
        //System.out.println("Saved Direction: " + nbt.getInteger("Direction"));
        nbt.setInteger("Progress", this.smashProgress);

        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.getCustomName());
        }

        NBTTagCompound stackTag = new NBTTagCompound();
        if (this.getStackInSlot(0) != null) {

            stackTag.setByte("Slot", (byte) 0);
            this.getStackInSlot(0).writeToNBT(stackTag);
        }
        nbt.setTag("Items",stackTag);

    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if(nbt.hasKey("Direction")){
            this.direction = nbt.getInteger("Direction");
            //System.out.println("Loaded Direction: " + nbt.getInteger("Direction"));
        }

        if(nbt.hasKey("Progress")){
            this.smashProgress = nbt.getInteger("Progress");
        }

        if(nbt.hasKey("CustomName")){
            this.customName = nbt.getString("CustomName");
        }


        NBTTagCompound stackTag = nbt.getCompoundTag("Items");
        int slot = stackTag.getByte("Slot") & 255;
        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));

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
    public int getProgress(){
        return this.smashProgress;
    }

    public double getProgressScaled(){
        return (this.smashProgress * 0.0375);
    }

    public void setProgress(int progress, EntityPlayer player){
        if(this.getStackInSlot(0) != null) {
            this.smashProgress = progress;
            if (this.smashProgress == 15) {
                this.decrStackSize(0, 1);
                this.smashProgress = 0;
            }
            worldObj.playSoundAtEntity(player, PlenaInanisReference.MODID + ":thwap", 0.5f, 0.8f);
            if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
        }
    }

    public int getDirection()
    {
        return this.direction;
    }

    public void setDirection(int par1)
    { this.direction = par1; }

    @Override
    public void updateEntity(){

    }

    public void removeBlock(EntityPlayer player){
        ItemStack itemStack =this.getStackInSlot(0);
        this.setInventorySlotContents(0, null);
        EntityItem droppedItem = new EntityItem(this.worldObj, xCoord, yCoord + 0.5, zCoord, itemStack);
        droppedItem.setVelocity(0, 0.25, 0);
        this.worldObj.spawnEntityInWorld(droppedItem);
        markDirty();
    }

}
