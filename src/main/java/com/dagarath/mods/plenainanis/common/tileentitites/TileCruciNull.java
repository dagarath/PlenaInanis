package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by dagarath on 2016-02-12.
 */
public class TileCruciNull extends TileEntity implements ISidedInventory, IFluidHandler{

    public int primaryX;
    public int primaryY;
    public int primaryZ;
    public int direction;
    public ItemStack[] inventory = new ItemStack[1];

    public TileCruciNull(){
    }

    @Override
    public void writeToNBT(NBTTagCompound data){

        super.writeToNBT(data);
        data.setInteger("Direction", this.direction);
        data.setInteger("primaryX", this.primaryX);
        data.setInteger("primaryY", this.primaryY);
        data.setInteger("primaryZ", this.primaryZ);

    }


    @Override
    public void readFromNBT(NBTTagCompound data){

        super.readFromNBT(data);
        if(data.hasKey("Direction")) {
            this.direction = data.getInteger("Direction");
        }
        this.primaryX = data.getInteger("primaryX");
        this.primaryY = data.getInteger("primaryY");
        this.primaryZ = data.getInteger("primaryZ");
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

    @Override
    public void updateEntity(){
        super.updateEntity();
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if(worldObj.getTileEntity(primaryX, primaryY, primaryZ) != null) {
            return ((ISidedInventory) worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getAccessibleSlotsFromSide(side);
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        return worldObj.getTileEntity(primaryX, primaryY, primaryZ) != null && ((ISidedInventory) worldObj.getTileEntity(primaryX, primaryY, primaryZ)).canInsertItem(slotIndex, itemStack, side);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        return worldObj.getTileEntity(primaryX, primaryY, primaryZ) != null && ((ISidedInventory) worldObj.getTileEntity(primaryX, primaryY, primaryZ)).canExtractItem(slotIndex, itemStack, side);
    }

    @Override
    public int getSizeInventory() {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getStackInSlot(slotIndex);
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int count) {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).decrStackSize(slotIndex, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getStackInSlotOnClosing(slotIndex);
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
        ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).setInventorySlotContents(slotIndex, itemStack);
    }

    @Override
    public String getInventoryName() {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit() {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).isUseableByPlayer(player);
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) {
        return ((ISidedInventory)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).isItemValidForSlot(slotIndex, itemStack);
    }

    public void setDirection(){
        this.direction = ((TilePlenaCrucible)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getDirection();
    }

    public int getDirection()
    {
        return ((TilePlenaCrucible)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getDirection();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).fill(from, resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).drain(from, resource, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).drain(from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).canFill(from, fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).canDrain(from, fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return ((IFluidHandler)worldObj.getTileEntity(primaryX, primaryY, primaryZ)).getTankInfo(from);
    }
}
