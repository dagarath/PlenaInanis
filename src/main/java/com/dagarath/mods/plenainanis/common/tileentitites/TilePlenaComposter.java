package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer.SlotType;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.OutputData;
import com.dagarath.mods.plenainanis.common.helpers.PlenaSaveData;
import com.dagarath.mods.plenainanis.common.helpers.RandomCollection;
import com.dagarath.mods.plenainanis.common.items.ItemAirflowUpgrade;
import com.dagarath.mods.plenainanis.common.items.ItemMoistureUpgrade;
import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import com.dagarath.mods.plenainanis.config.ConfigurationHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * Created by dagarath on 2016-01-22.
 */
public class TilePlenaComposter  extends TileEntity implements ISidedInventory {
    private String customName = "Composter";
    private ItemStack[] inventory;

    private int tickCounter = 0;
    public int compostProgress = 0;
    private int storedCompost = 0;
    private int woodType;
    private int compostSpeed = 1;
    private int moistureModifer = 1;
    private int airFlowModifier = 1;

    public boolean isOpen = false;


    public List<String> itemTypes = new ArrayList<>();

    public TilePlenaComposter(){
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    private HashMap<String, Integer> composterCache = new HashMap<>();

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
            PlenaInanis.logger.info("Tile 1 Count: " + count);
            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                PlenaInanis.logger.info("Tile 1 Stack Size: " + itemstack.stackSize);
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);
                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                PlenaInanis.logger.info("Tile 2 Stack Size: " + itemstack.stackSize);
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
    public void setInventorySlotContents(int index, ItemStack itemStack)
    {

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit() && index < 13 && isItemValidForSlot(index, itemStack)) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
        this.inventory[index] = itemStack;
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.composter";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public int getSizeInventory(){
        return 23;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(xCoord,yCoord,zCoord) == this && player.getDistanceSq(xCoord,yCoord,zCoord) <= 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {

        if(itemStack != null && index >= 0 && index < 13){
            if(PlenaInanis.saveData.composterExists(InfoHelper.getFullNameForItemStack(itemStack) + ":" + itemStack.getItemDamage())){
                return true;
            }
        }
        if(itemStack != null && index == 13){
            if(itemStack.getItem() instanceof ItemMoistureUpgrade){
                return true;
            }
        }
        if(itemStack != null && index == 14){
            if(itemStack.getItem() instanceof ItemAirflowUpgrade){
                return true;
            }
        }
        return false;
    }


    @Override
    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("Direction", this.direction);
        data.setInteger("Compost", this.storedCompost);
        data.setInteger("Tick Rate", this.tickCounter);
        data.setInteger("Cost", this.compostProgress);
        data.setInteger("Wood Type", this.woodType);
        data.setBoolean("Open", this.isOpen);
        data.setInteger("Airflow", this.airFlowModifier);
        data.setInteger("Moisture", this.moistureModifer);

        if (this.hasCustomInventoryName()) {
            data.setString("CustomName", this.getCustomName());
        }

        NBTTagList compostList = new NBTTagList();
        composterCache.forEach((k, v)-> {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString("Name", k);
            entry.setInteger("Value", v);
            compostList.appendTag(entry);
        });
        data.setTag("Compost", compostList);

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }

        data.setTag("Items", list);

    }


    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);

        if(data.hasKey("Direction")){
            this.direction = data.getInteger("Direction");
            //System.out.println("Loaded Direction: " + nbt.getInteger("Direction"));
        }
        if(data.hasKey("Compost")){
            this.storedCompost = data.getInteger("Compost");
        }
        if(data.hasKey("Tick Rate")){
            this.tickCounter = data.getInteger("Tick Rate");
        }
        if(data.hasKey("Cost")){
            this.compostProgress = data.getInteger("Cost");

        }
        if (data.hasKey("CustomName", 8)) {
            this.setCustomName(data.getString("CustomName"));
        }
        if(data.hasKey("Wood Type")){
            this.woodType = data.getInteger("Wood Type");
        }
        if(data.hasKey("Open")){
            this.isOpen = data.getBoolean("Open");
        }
        if(data.hasKey("Airflow")){
            this.airFlowModifier = data.getInteger("Airflow");
        }
        if(data.hasKey("Moisture")){
            this.moistureModifer = data.getInteger("Moisture");
        }

        NBTTagList compostList = data.getTagList("Compost", 10);
        for(int i = 0; i < compostList.tagCount(); i++){
            NBTTagCompound entry = compostList.getCompoundTagAt(i);
            composterCache.put(entry.getString("Name"), entry.getInteger("Value"));
        }

        NBTTagList list = data.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            byte b0 = stackTag.getByte("Slot");
            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(stackTag);
            }
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

    private int direction;

    public int getDirection()
    {
        return this.direction;
    }

    public void setDirection(int par1)
    { this.direction = par1; }

    @Override
    public void updateEntity(){
        super.updateEntity();
        if (!this.worldObj.isRemote) {

            if (getStackInSlot(SlotType.UPGRADE_MOISTURE.ordinal()) != null) {
                this.moistureModifer = (getStackInSlot(SlotType.UPGRADE_MOISTURE.ordinal()).getItemDamage() + 1) * 2;
                this.markDirty();
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else if (getStackInSlot(SlotType.UPGRADE_MOISTURE.ordinal()) == null) {
                this.moistureModifer = 1;
                this.markDirty();
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }

            if (getStackInSlot(SlotType.UPGRADE_AIRFLOW.ordinal()) != null) {
                this.airFlowModifier = (getStackInSlot(SlotType.UPGRADE_AIRFLOW.ordinal()).getItemDamage() + 1) * 2;
                this.markDirty();
                if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else if (getStackInSlot(SlotType.UPGRADE_AIRFLOW.ordinal()) == null) {
                this.airFlowModifier = 1;
                this.markDirty();
            }


            if (this.worldObj.getTotalWorldTime() % 20 == 0) {
                if (this.tickCounter < ConfigurationHandler.compostInterval.getInt()) {
                    this.tickCounter++;
                } else if (this.tickCounter == ConfigurationHandler.compostInterval.getInt()) {
                    this.tickCounter = 0;
                    calculateModifier();
                    updateCompostCache();
                    calculateAndSetOutputs();
                    this.markDirty();
                    if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        }
    }

    public void setOpen(boolean bool){
        this.isOpen = bool;
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void calculateModifier(){
        itemTypes.clear();
        for(int i = 0; i < (PlenaComposterContainer.SlotType.INPUT_12.ordinal() + 1); i++) {
            if (this.getStackInSlot(i) != null) {
                ItemStack items = this.getStackInSlot(i);
                String itemName = InfoHelper.getFullNameForItemStack(items);
                String constructedName = itemName + ":" + items.getItemDamage();
                decrStackSize(i, 1);
                if(PlenaInanis.saveData.composterExists(constructedName)){
                    if (!itemTypes.contains(constructedName)) {
                        itemTypes.add(constructedName);
                    }
                }

            }
        }
        this.compostSpeed = itemTypes.size() * (moistureModifer * airFlowModifier);
    }

    public void updateCompostCache(){
        itemTypes.forEach(s -> { HashMap<String, OutputData> cacheMap = PlenaInanis.saveData.getComposterOutputs(s);
            for(int i = 0; i < SlotType.INPUT_12.ordinal() + 1; i ++) {
                if(getStackInSlot(i) != null) {
            cacheMap.forEach((k,v) -> {
                        if (composterCache.containsKey(k)) {
                            int value = composterCache.get(k);
                            value += (int) (Math.random() * (v.maxOutput - v.minOutput) + v.minOutput) * this.compostSpeed;
                            composterCache.put(k, value);
                        } else {
                            int value = (int) (Math.random() * (v.maxOutput - v.minOutput) + v.minOutput) * this.compostSpeed;
                            composterCache.put(k, value);
                        }
                    });
                }
            }
        });
    }

    public void calculateAndSetOutputs(){
        int compostCost = ConfigurationHandler.compostCost.getInt();
        composterCache.forEach((k,v) ->{
            if(v >= compostCost){
                int outputValue = v / compostCost;
                String name = k.substring(0, k.lastIndexOf(":"));
                int meta = Integer.parseInt(k.substring(k.lastIndexOf(":") + 1));
                composterCache.put(k, v - outputValue * compostCost);
                ItemStack putStack = InfoHelper.getStackWithNullCheck(name);
                if(putStack != null) {
                    putStack.setItemDamage(meta);
                    while(outputValue > 0) {
                        int slot = this.doesItemAlreadyExist(putStack);
                        if(slot > -1){
                            int calculatedOutput = this.calculateOutputForSlot(slot, putStack);
                            if(calculatedOutput > 0) {
                                ItemStack oldStack = this.getStackInSlot(slot).copy();
//                                PlenaInanis.logger.info("Calculated output for " + k + ": " + calculatedOutput);
                                if(outputValue <= calculatedOutput){
//                                    PlenaInanis.logger.info("Output Value <= for "+ k +": " + outputValue);
                                    oldStack.stackSize += outputValue;
                                    outputValue = 0;
                                    this.setInventorySlotContents(slot, oldStack);
                                }else{
//                                    PlenaInanis.logger.info("Output Value > for "+ k +": " + outputValue);
                                    oldStack.stackSize += calculatedOutput;
                                    outputValue -= calculatedOutput;
                                    this.setInventorySlotContents(slot, oldStack);
                                }
                            }
                        }else {
                            if(getNextEmptySlot() != 0){
                                ItemStack newStack = putStack.copy();
                                if(outputValue <= 64){
                                    newStack.stackSize = outputValue;
                                    outputValue = 0;
                                }else{
                                    newStack.stackSize = 64;
                                    outputValue -= 64;
                                }
                                this.setInventorySlotContents(getNextEmptySlot(), newStack);
                            }else{
                                ItemStack dropStack = putStack.copy();
                                if(outputValue <= 64){
                                    dropStack.stackSize = outputValue;
                                    outputValue = 0;
                                }else{
                                    dropStack.stackSize = 64;
                                    outputValue -= 64;
                                }
                                EntityItem droppedItem = new EntityItem(this.worldObj, xCoord, yCoord + 0.5, zCoord, dropStack);
                                droppedItem.setVelocity(0,0,0);
                                this.worldObj.spawnEntityInWorld(droppedItem);
                            }
                        }
                    }
                }
            }
        });
    }

    public int calculateOutputForSlot(int index, ItemStack itemStack){
        if(getStackInSlot(index) == null){
            return 64;
        }else if(itemStack.getUnlocalizedName().equals(getStackInSlot(index).getUnlocalizedName())  && itemStack.getItemDamage() == getStackInSlot(index).getItemDamage()){
            return 64 - getStackInSlot(index).stackSize;
        }else{
            return 0;
        }
    }

    public int doesItemAlreadyExist(ItemStack itemStack){
        int i = SlotType.OUTPUT_1.ordinal();
        while (i < SlotType.OUTPUT_9.ordinal() + 1) {
            if(getStackInSlot(i) != null) {
                if (itemStack.getUnlocalizedName().equals(getStackInSlot(i).getUnlocalizedName())
                        && itemStack.getItemDamage() == getStackInSlot(i).getItemDamage()
                        && getStackInSlot(i).stackSize < 64) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    public int getNextEmptySlot(){
        int i = SlotType.OUTPUT_1.ordinal();
        while (i < SlotType.OUTPUT_9.ordinal() + 1) {
            if(getStackInSlot(i) == null){
                return i;
            }
            i++;
        }
        return 0;
    }


    @SideOnly(Side.CLIENT)
    public int getMoisture()
    {
        return this.moistureModifer;
    }

    @SideOnly(Side.CLIENT)
    public int getAirflow()
    {
        return this.airFlowModifier;
    }

    public void setType(int type){
        this.woodType = type;
    }
    public int getType(){
        return this.woodType;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {

        if(ForgeDirection.getOrientation(side) == ForgeDirection.DOWN){
            return new int[]{SlotType.OUTPUT_1.ordinal(),SlotType.OUTPUT_2.ordinal(),SlotType.OUTPUT_3.ordinal(),
                    SlotType.OUTPUT_4.ordinal(),SlotType.OUTPUT_5.ordinal(),SlotType.OUTPUT_6.ordinal(),
                    SlotType.OUTPUT_7.ordinal(),SlotType.OUTPUT_8.ordinal(),SlotType.OUTPUT_9.ordinal()};
        }else if(ForgeDirection.getOrientation(side) != ForgeDirection.UP && side != this.getDirection()){
            return new int[]{SlotType.INPUT_1.ordinal(), SlotType.INPUT_2.ordinal(), SlotType.INPUT_3.ordinal(),
                    SlotType.INPUT_4.ordinal(), SlotType.INPUT_5.ordinal(), SlotType.INPUT_6.ordinal(),
                    SlotType.INPUT_7.ordinal(), SlotType.INPUT_8.ordinal(), SlotType.INPUT_9.ordinal(),
                    SlotType.INPUT_10.ordinal(), SlotType.INPUT_11.ordinal(), SlotType.INPUT_12.ordinal()};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side) {
        return this.isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        if(ForgeDirection.getOrientation(side) == ForgeDirection.DOWN && slotIndex > SlotType.INPUT_12.ordinal() && slotIndex < SlotType.UPGRADE_MOISTURE.ordinal()){
            return true;
        }
        return false;
    }
}
