package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer.SlotType;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.OutputData;
import com.dagarath.mods.plenainanis.common.helpers.PlenaSaveData;
import com.dagarath.mods.plenainanis.common.helpers.RandomCollection;
import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import com.dagarath.mods.plenainanis.config.ConfigurationHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

/**
 * Created by dagarath on 2016-01-22.
 */
public class TilePlenaComposter  extends TileEntity implements IInventory {
    private String customName = "Composter";
    private ItemStack[] inventory;

    private int tickCounter = 0;
    public int compostProgress = 0;
    private int storedCompost = 0;
    private int woodType;
    private int compostValue = 0;
    private int compostNumber;

    private String curInputItem = "";
    private String curOutputItem = "";

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
                this.setInventorySlotContents(index, null);
                this.markDirty();
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
        this.markDirty();
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
        return 21;
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
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return true;
    }


    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Direction", this.direction);
        //System.out.println("Saved Direction: " + nbt.getInteger("Direction"));
        nbt.setInteger("Compost", this.storedCompost);
        nbt.setInteger("Tick Rate", this.tickCounter);
        nbt.setInteger("Cost", this.compostProgress);
        nbt.setInteger("Wood Type", this.woodType);
        nbt.setBoolean("Open", this.isOpen);

        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.getCustomName());
        }

        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                //System.out.println("WRITING Slot Item: " + this.getStackInSlot(i));
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);

    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if(nbt.hasKey("Direction")){
            this.direction = nbt.getInteger("Direction");
            //System.out.println("Loaded Direction: " + nbt.getInteger("Direction"));
        }

        if(nbt.hasKey("Compost")){
            this.storedCompost = nbt.getInteger("Compost");
        }

        if(nbt.hasKey("Tick Rate")){
            this.tickCounter = nbt.getInteger("Tick Rate");
        }

        if(nbt.hasKey("Cost")){
            this.compostProgress = nbt.getInteger("Cost");

        }
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }

        if(nbt.hasKey("Wood Type")){
            this.woodType = nbt.getInteger("Wood Type");
        }

        if(nbt.hasKey("Open")){
            this.isOpen = nbt.getBoolean("Open");
        }

        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;

            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
            //System.out.println("Reading ItemStack: " + ItemStack.loadItemStackFromNBT(stackTag));
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
        if (this.worldObj.isRemote) return;

        if(this.worldObj.rand.nextInt(20) == 0){
            if (this.tickCounter < ConfigurationHandler.compostInterval.getInt())
            {
                this.tickCounter++;
            }
            else if(this.tickCounter == ConfigurationHandler.compostInterval.getInt())
            {
                this.tickCounter = 0;
                int compostSpeed;
                curOutputItem = "";
                curInputItem = "";
                for(int i = 0; i < (PlenaComposterContainer.SlotType.INPUT_12.ordinal() + 1); i++){
                    int value = 0;


                    if(this.getStackInSlot(i) != null) {
                        ItemStack items = this.getStackInSlot(i);
                        String itemName = InfoHelper.getFullNameForItemStack(items);
                        if(!itemTypes.contains(itemName)){
                            itemTypes.add(itemName);
                        }
                        //Keep a list of the contents of all 12 slots.
                        //If multiple slots have the same item, no speed modifier is applied
                        //The more different types of items you have the faster the composter works
                        curInputItem = InfoHelper.getFullNameForItemStack(getStackInSlot(i)) + ":" + getStackInSlot(i).getItemDamage();
//                        PlenaInanis.logger.info(curInputItem);
                        HashMap<String, OutputData> outputData = PlenaInanis.saveData.getComposterOutputs(curInputItem);

                        int minOutput = 5;
                        int maxOutput = 10;

                        if(!outputData.isEmpty()) {
                            RandomCollection collection = new RandomCollection();
//                            PlenaInanis.logger.info(outputData.values());
                            for (Iterator<Map.Entry<String, OutputData>> it = outputData.entrySet().iterator(); it.hasNext(); ) {
                                Map.Entry<String, OutputData> entry = it.next();
                                if(entry != null){
                                    OutputData tmpOutput = entry.getValue();
//                                    PlenaInanis.logger.info(tmpOutput.outputName + " " + tmpOutput.weight);
                                    collection.add(tmpOutput.weight, tmpOutput.outputName);
                                }
                            }
                            curOutputItem = collection.next();
                        }else{
                            curOutputItem = "plenainanis:compost:0";
                        }

                        OutputData outputSet = PlenaInanis.saveData.getComposterOutput(curInputItem, curOutputItem);
                        if(outputSet != null){
                            value = (int)(Math.random() * outputSet.maxOutput) + outputSet.minOutput;
                        }else{
                            value = (int)(Math.random() * maxOutput) + minOutput;
                        }


                        compostSpeed = itemTypes.size();
                        //The composter takes 1 item from all 12 slots (or whatever slots are occupied) and calculates a total compost value
                        if(items.stackSize > 1) {
                            items.stackSize--;
                        }else if(items.stackSize == 1){
                            itemTypes.remove(items.getUnlocalizedName());
                            this.setInventorySlotContents(i,(ItemStack)null);
                        }
                        compostValue += value * compostSpeed;
                    }
                }

                if((compostValue + compostProgress) > 0 || storedCompost > 0){
                    createCompost();
                }
            }
        }
    }

    public void createCompost(){
        ItemStack outputItems = this.getStackInSlot(getNextUnfilledSlot());
        int totalCompostValue = compostValue + compostProgress;
        compostValue = 0;
        //Output has compost already
        if(outputItems != null){
            compostNumber = 0;
            //totalCompostValue is greater than the compost cost
            if(totalCompostValue - ConfigurationHandler.compostCost.getInt() > 0) {
                //Calculate total number of compost to output
                compostNumber = (int)Math.floor(totalCompostValue / ConfigurationHandler.compostCost.getInt());
                //compostProgress = compostNumber;
                //reduce compost value by spent amount
                totalCompostValue -= (compostNumber * ConfigurationHandler.compostCost.getInt());
                compostNumber += outputItems.stackSize;
                compostProgress = totalCompostValue;
//                PlenaInanis.logger.info("Case 1 " + compostNumber);
            }else{
                compostProgress = totalCompostValue;
                compostNumber = outputItems.stackSize;
                if(totalCompostValue == ConfigurationHandler.compostCost.getInt()){
                    compostNumber = outputItems.stackSize + 1;
                    compostProgress = 0;
                }
//                PlenaInanis.logger.info("Else 1 " + compostNumber);
            }
            //Ouput stack plus compost output is less than 64
            if(compostNumber < outputItems.getMaxStackSize()) {

                if(storedCompost  > 0 && (outputItems.stackSize + compostNumber + storedCompost) < outputItems.getMaxStackSize()) {
                    //Compost is stored, and adding it to the stack comes to less than 64
                    compostNumber = outputItems.stackSize + storedCompost;
                    storedCompost = 0;
//                    PlenaInanis.logger.info("Case 2 " + compostNumber);
                }else if(storedCompost > 0 && (outputItems.stackSize + compostNumber + storedCompost) > outputItems.getMaxStackSize()){
                    //Compost is stored, adding it to the stack comes to more than 64
                    storedCompost -= (outputItems.getMaxStackSize() - compostNumber - outputItems.stackSize);
                    compostNumber = outputItems.getMaxStackSize();
                    //Never Entered
//                    PlenaInanis.logger.info("Else 2 " + compostNumber + " "+ storedCompost);
                }

            }else{
                //Output stack plus compost ouput is greater than 64
                storedCompost += (compostNumber - outputItems.getMaxStackSize());
                compostNumber = outputItems.getMaxStackSize();
//                PlenaInanis.logger.info("Catch 2 " + compostNumber);
                compostProgress = 0;
            }
            //PlenaInanis.logger.info("Output Stack Size: " + compostNumber);
            spitCurOutput();
        }else
        {
            //PlenaInanis.logger.info("Null Output Compost Value: " + totalCompostValue);
            //Output slot is null
            compostNumber = 0;
            if(totalCompostValue - ConfigurationHandler.compostCost.getInt() > 0 && (int)Math.floor(totalCompostValue / ConfigurationHandler.compostCost.getInt()) < this.getInventoryStackLimit()){
                //Compost value is greater than the compost cost and less than the maximum stack size
                compostNumber = (int)Math.floor(totalCompostValue / ConfigurationHandler.compostCost.getInt());
//                PlenaInanis.logger.info("Case 3 " + compostNumber);
                if(storedCompost > 0 && compostNumber + storedCompost < this.getInventoryStackLimit()) {
                    //Compost is stored and the compost output plus the stored compost is less than the stack limit
                    compostNumber += storedCompost;
//                    PlenaInanis.logger.info("Else 3 " + compostNumber);
                }else if(storedCompost > 0 && compostNumber + storedCompost > this.getInventoryStackLimit()){
                    //Compost is stored and the compost output plus the stored compost is more than the stack limit
                    storedCompost -=  (this.getInventoryStackLimit()  - compostNumber);
                    compostNumber = this.getInventoryStackLimit();
//                    PlenaInanis.logger.info("Catch 3 " + compostNumber);
                }
                if((totalCompostValue - (compostNumber * ConfigurationHandler.compostCost.getInt())) > 0) {
                    //The compost value reduced by the compost output is greater than 0
                    totalCompostValue -= compostNumber * ConfigurationHandler.compostCost.getInt();
                    compostProgress = totalCompostValue;

                }else if(totalCompostValue - (compostNumber * ConfigurationHandler.compostCost.getInt()) == 0){
                    //The compost value reduced by the compost output is less than or eq
//                    PlenaInanis.logger.info("Case 4 " + compostNumber);
                }
            }else if (totalCompostValue - ConfigurationHandler.compostCost.getInt() == 0){
                //Compost value is exactly the compost cost
                compostNumber = 1;
                compostProgress = 0;
            }else if(totalCompostValue > ConfigurationHandler.compostCost.getInt() && (int)Math.floor(totalCompostValue / ConfigurationHandler.compostCost.getInt()) > this.getInventoryStackLimit()){
                //PlenaInanis.logger.info("Entered Unhandled Code Segment");
            }else{
                compostProgress = totalCompostValue;
                if(totalCompostValue == ConfigurationHandler.compostCost.getInt()){
                    compostNumber = 1;
                    compostProgress = 0;
                }
            }
            if(storedCompost > 0 && storedCompost > this.getInventoryStackLimit()){
                storedCompost -= this.getInventoryStackLimit();
                compostNumber = this.getInventoryStackLimit();
            }else if(storedCompost > 0 && storedCompost < this.getInventoryStackLimit()){
                compostNumber = storedCompost;
                storedCompost = 0;
            }
            //PlenaInanis.logger.info("Output Item Number: " + compostNumber);
            spitCurOutput();
        }
    }

    public void spitCurOutput(){
        if(curOutputItem == ""){
            return;
        }
        String[] newItemName = curOutputItem.split(":");
        ItemStack spitStack = InfoHelper.getStackWithNullCheck(newItemName[0] + ":" + newItemName[1]);
        spitStack.stackSize = compostNumber;
        spitStack.setItemDamage(Integer.parseInt(newItemName[2]));
        this.setInventorySlotContents(getNextAvailableSlot(), spitStack);

        if(storedCompost > 0){
            ItemStack dropStack = InfoHelper.getStackWithNullCheck(newItemName[0] + ":" + newItemName[1]);
            dropStack.stackSize = storedCompost;
            storedCompost = 0;
            EntityItem droppedItem = new EntityItem(this.worldObj, xCoord, yCoord + 0.5, zCoord, dropStack);
            droppedItem.setVelocity(0,0,0);
            this.worldObj.spawnEntityInWorld(droppedItem);
        }
    }

    public int getNextUnfilledSlot(){
        for(int i = SlotType.OUTPUT_1.ordinal(); i < SlotType.OUTPUT_9.ordinal(); i++){
            if(getStackInSlot(i) != null) {
//                PlenaInanis.logger.info("Checking slot: " + i);
//                PlenaInanis.logger.info("Slot item is: " + InfoHelper.getFullNameForItemStack(getStackInSlot(i)) + ":" + getStackInSlot(i).getItemDamage());
//                PlenaInanis.logger.info("Current item is: " + curOutputItem);
                if (curOutputItem.equals(InfoHelper.getFullNameForItemStack(getStackInSlot(i)) + ":" + getStackInSlot(i).getItemDamage())){
//                    PlenaInanis.logger.info("Slot items match");
                    if(getStackInSlot(i).stackSize < 64){
                        return i;
                    }else{
                        return i + 1;
                    }

                }
            }else{
                return i;
            }
        }
        return 0;
    }
    public int getNextAvailableSlot(){
        for(int i = SlotType.OUTPUT_1.ordinal(); i < SlotType.OUTPUT_9.ordinal(); i++){
            if(getStackInSlot(i) != null) {
                if (curOutputItem.equals(InfoHelper.getFullNameForItemStack(getStackInSlot(i)) + ":" + getStackInSlot(i).getItemDamage())){
                    if(getStackInSlot(i).stackSize + compostNumber <= 64){
                        return i;
                    }else{
                        return i + 1;
                    }
                }
            }else{
                return i;
            }
        }
        return 0;
    }

    public void setOpen(boolean bool){
        this.isOpen = bool;
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }


    @SideOnly(Side.CLIENT)
    public int getProgressScaled(int scaleValue)
    {
        return this.compostProgress * scaleValue / ConfigurationHandler.compostCost.getInt();
    }

    public void setType(int type){
        this.woodType = type;
    }
    public int getType(){
        return this.woodType;
    }
}
