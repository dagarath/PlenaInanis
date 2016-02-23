package com.dagarath.mods.plenainanis.common.tileentitites;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaCrucible;
import com.dagarath.mods.plenainanis.common.containers.PlenaCrucibleContainer.SlotType;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

import java.util.Arrays;

/**
 * Created by dagarath on 2016-02-11.
 */
public class TilePlenaCrucible extends TileEntity implements IFluidHandler, ISidedInventory {
    public ItemStack[] inventory;
    private int direction;
    private boolean isOpen;
    private boolean opening;
    private String customName = "Crucible";

    public int crucibleBurnTime = 0;
    public int crucibleCookTime = 0;


    public int currentItemBurnTime;
    public int currentItemCookTime;
    private double currentItemTemperature;
    private int currentItemProcessing = 0;
    private double currentFluidOutput = 0;

    public double temperature = 0D;

    public boolean dumping;
    public boolean purging;

    private State state = State.IDLE;

    enum State{
        IDLE,
        PURGING,
        DUMPING,
        ACTIVE
    }



    public FluidTank tank = new FluidTank(135000);

    public TilePlenaCrucible(){
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    @Override
    public void writeToNBT(NBTTagCompound data){

        super.writeToNBT(data);
        data.setInteger("Direction", this.direction);

        data.setDouble("Temperature", this.temperature);

        data.setBoolean("isOpen", this.isOpen);
        data.setBoolean("Opening", this.opening);
        data.setInteger("CookTime", this.crucibleCookTime);
        data.setInteger("BurnTime", this.crucibleBurnTime);
        data.setBoolean("Dumping", this.dumping);
        data.setBoolean("Purging", this.purging);

        if (this.hasCustomInventoryName()) {
            data.setString("CustomName", this.getCustomName());
        }

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

        this.tank.writeToNBT(data);

    }

    @Override
    public void readFromNBT(NBTTagCompound data){

        super.readFromNBT(data);
        if(data.hasKey("Direction")) {
            this.direction = data.getInteger("Direction");
        }
        if(data.hasKey("Temperature")) {
            this.temperature = data.getDouble("Temperature");
        }
        if(data.hasKey("isOpen")) {
            this.isOpen = data.getBoolean("isOpen");
        }
        if(data.hasKey("Opening")) {
            this.opening = data.getBoolean("opening");
        }
        if(data.hasKey("CookTime")){
            this.crucibleCookTime = data.getInteger("CookTime");
        }
        if(data.hasKey("BurnTime")){
            this.crucibleBurnTime = data.getInteger("BurnTime");
        }
        if(data.hasKey("Dumping")){
            this.dumping = data.getBoolean("Dumping");
        }
        if(data.hasKey("Purging")){
            this.purging = data.getBoolean("Purging");
        }

        if (data.hasKey("CustomName", 8)) {
            this.setCustomName(data.getString("CustomName"));
        }

        NBTTagList list = data.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            byte b0 = stackTag.getByte("Slot");
            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(stackTag);
            }
        }

        this.tank.readFromNBT(data);

        this.currentItemTemperature = getItemTemperature(this.inventory[0]);
        this.currentItemBurnTime = getItemBurnTime(this.inventory[0]);
        if(containsAnyInputs() && PlenaInanis.crucibleCookTimes.containsKey(InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())))){
            this.currentItemCookTime = PlenaInanis.crucibleCookTimes.get(InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())));
        }
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
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

    public boolean isOpen(){
        return this.isOpen;
    }

    public void isOpen(boolean bool){
        if(this.tank.getFluidAmount() == 0) {
            this.isOpen = bool;
            markDirty();
            if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public boolean isOpening(){
        return this.opening;
    }

    public void setOpening(boolean bool){
        this.opening = bool;
    }

    @Override
    public void updateEntity(){
        super.updateEntity();

        boolean flag = this.crucibleBurnTime > 0;
        boolean flag1 = false;

        if (!this.worldObj.isRemote)
        {
            switch (state){
                case IDLE:
                    if(this.crucibleBurnTime > 0 || this.temperature > 0 || containsAnyInputs()){
                        state = State.ACTIVE;
                    }
                    if(this.purging()){
                        state = State.PURGING;
                    }
                    if(this.dumping()){
                        state = State.DUMPING;
                    }
                    //Slot Fill from Input
                    updateInputSlots();
                    burnFurnaceInput();
                    transferLiquidsInternally();
                    break;
                case PURGING:
                    if(!this.purging()){
                        if(this.dumping()){
                            state = State.DUMPING;
                        }else{
                            state = State.IDLE;
                        }
                    }

                    this.temperature -= 20;

                    if(this.temperature <= 0){
                        this.temperature = 0;
                        this.crucibleBurnTime = 0;
                        this.purging(false);
                    }
                    markDirty();
                    if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    break;
                case DUMPING:
                    if(!this.dumping()){
                        if(this.purging()){
                            state = State.PURGING;
                        }else{
                            state = State.IDLE;
                        }
                    }

                    this.tank.drain(100, true);

                    if(this.tank.getFluidAmount() <= 0){
                        this.tank.setFluid(null);
                        this.dumping(false);
                        this.crucibleCookTime = 0;
                    }

                    markDirty();
                    if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    break;
                case ACTIVE:

                    if(this.purging()){
                        state = State.PURGING;
                    }
                    if(this.dumping()){
                        state = State.DUMPING;
                    }
                    //Reduce Temperature Passively
                    if(worldObj.getWorldTime() % 100 == 0){
                        if(this.temperature >= 1) {
                            if(this.temperature > 8000){
                                this.temperature = 8000;
                                markDirty();
                            }
                            this.temperature--;

                        }
                    }

                    //Slot Fill from Input
                    updateInputSlots();

                    //BurnTime / Temperature Update
                    if (this.crucibleBurnTime > 0)
                    {
                        if(this.currentItemTemperature > 0 && this.currentItemBurnTime < 4000){
                            if(this.temperature < 8000) {
                                --this.crucibleBurnTime;
                            }
                            this.temperature += getTemperatureScaled();
                        }else if(this.currentItemBurnTime >= 4000 && this.currentItemBurnTime < 8000) {
                            if(this.temperature < 8000) {
                                this.crucibleBurnTime -= 2;
                            }
                            this.temperature += getTemperatureScaled() * 2.0D;
                        }else if(this.currentItemBurnTime >= 8000 && this.currentItemBurnTime < 16000) {
                            if(this.temperature < 8000) {
                                this.crucibleBurnTime -= 3;
                            }
                            this.temperature += getTemperatureScaled()  * 3.0D;
                        }else if(this.currentItemBurnTime >= 16000) {
                            if(this.temperature < 8000) {
                                this.crucibleBurnTime -= 4;
                            }
                            this.temperature += getTemperatureScaled()  * 4.0D;
                        }
                    }

                    if(containsAnyInputs() && this.temperature >= 100 && InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())).equals("minecraft:snow")) {
                        if (this.crucibleCookTime == 0) {
                            this.currentItemProcessing = getFirstItemToSmelt();
                            this.crucibleCookTime = this.currentItemCookTime = PlenaInanis.crucibleCookTimes.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing)));
                            if (this.tank.getInfo().fluid == null) {
                                FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 0);
                                fill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack, true);
                            }
                        }
                        if(this.crucibleCookTime > 0 && canSmelt()){
                            if(this.temperature >= 100  && this.temperature < 200) {
                                this.crucibleCookTime--;
                                this.temperature--;
                                if (this.crucibleCookTime == 0) {
                                    if (this.tank.getInfo().fluid == null) {
                                        FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 1000);
                                        if(canFill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack.getFluid())) {
                                            fill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack, true);
                                        }

                                    }else {
                                        FluidStack fluidStack = this.tank.getInfo().fluid;
                                        fluidStack.amount += 1000;
                                        this.tank.setFluid(fluidStack);
                                    }
                                }
                            }else if(this.temperature >= 200 && this.temperature < 400){
                                this.crucibleCookTime-=2;
                                this.temperature--;
                                if (this.crucibleCookTime == 0) {
                                    if (this.tank.getInfo().fluid == null) {
                                        FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 1000);
                                        if(canFill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack.getFluid())) {
                                            fill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack, true);
                                        }

                                    }else {
                                        FluidStack fluidStack = this.tank.getInfo().fluid;
                                        fluidStack.amount += 1000;
                                        this.tank.setFluid(fluidStack);
                                    }
                                }
                            }else if (this.temperature >= 400){
                                this.crucibleCookTime-=4;
                                this.temperature-=2;
                                if (this.crucibleCookTime == 0) {
                                    if (this.tank.getInfo().fluid == null) {
                                        FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 1000);
                                        if(canFill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack.getFluid())) {
                                            fill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack, true);
                                        }

                                    }else {
                                        FluidStack fluidStack = this.tank.getInfo().fluid;
                                        fluidStack.amount += 1000;
                                        this.tank.setFluid(fluidStack);
                                    }
                                }
                            }
                            markDirty();
                            if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                        }
                    }else if(containsAnyInputs() && this.temperature >= 400 && (!InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())).equals("minecraft:snow"))){
                        if (this.crucibleCookTime == 0 && canSmelt()) {
                            this.currentItemProcessing = getFirstItemToSmelt();
                            this.crucibleCookTime = this.currentItemCookTime = PlenaInanis.crucibleCookTimes.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing)));
                            if (this.tank.getFluid() == null) {
                                FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 0);
                                if(canFill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack.getFluid())) {
                                    fill(ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[this.getDirection()]), fluidStack, true);
                                }
                            }
                        }

                        if(this.crucibleCookTime > 0 && canSmelt()){
                            if(this.temperature >= 400) {
                                this.crucibleCookTime--;
                                this.temperature--;
                                if (this.crucibleCookTime == 0) {
                                    if(this.tank.getInfo().fluid != null) {
                                        FluidStack fluidStack = this.tank.getFluid();
                                        fluidStack.amount += 1000;
                                        this.tank.setFluid(fluidStack);
                                        this.decrStackSize(currentItemProcessing, 1);
                                        this.currentItemProcessing = 0;
                                    }else{
                                        FluidStack fluidStack = FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))), 1000);
                                        fluidStack.amount = 1000;
                                        this.tank.setFluid(fluidStack);
                                        this.decrStackSize(currentItemProcessing, 1);
                                        this.currentItemProcessing = 0;
                                    }
                                }
                            }
                        }

                    }

                    if(this.temperature > 100){
                        if (this.crucibleCookTime == 0 && currentItemProcessing != 0) {
                            this.setInventorySlotContents(currentItemProcessing, null);
                            this.currentItemProcessing = 0;
                        }
                    }
                    burnFurnaceInput();
                    transferLiquidsInternally();
                    break;
            }

            //Cleanup
            if (flag != this.crucibleBurnTime > 0)
            {
                flag1 = true;
                BlockPlenaCrucible.updateFurnaceBlockState(this.crucibleBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }

        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    public  boolean canSmelt()
    {

        if (!containsAnyInputs())
        {
            return false;
        }
        else
        {
            String outputFluid = PlenaInanis.crucibleAllowedItems.get(getSlotName(getFirstItemToSmelt()));
            return this.tank.getFluidAmount() < 135000 && this.tank.getFluid() == null || (outputFluid.equals(this.tank.getFluid().getUnlocalizedName().substring(this.tank.getFluid().getUnlocalizedName().lastIndexOf(".") + 1)));
        }
    }

    public void updateFurnaceItems(){
        this.currentItemBurnTime = this.crucibleBurnTime = getItemBurnTime(getStackInSlot(0));
        this.currentItemTemperature = getItemTemperature(getStackInSlot(0));
        if (this.inventory[0] != null & this.currentItemBurnTime > 0) {
            if(getStackInSlot(0).getItem() == Items.lava_bucket){
                setInventorySlotContents(3, new ItemStack(Items.bucket));
            }
            this.decrStackSize(0, 1);
        }
    }

    @Override
    public int getSizeInventory() {
        return 31;
    }

    public void burnFurnaceInput(){
        //PlenaInanis.logger.info("Here");
        if(this.crucibleBurnTime == 0){
            if(getStackInSlot(0) != null) {
                updateFurnaceItems();
            }
        }
        markDirty();
        if (worldObj != null) worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void transferLiquidsInternally(){
        if (getStackInSlot(SlotType.CONTAINER_INPUT.ordinal()) != null) {

            FluidStack testFluid = this.tank.getInfo().fluid;
            ItemStack containerStack = getStackInSlot(SlotType.CONTAINER_INPUT.ordinal());
            if(testFluid != null){
                FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
                for(int i = 0; i < data.length; i++){
                    FluidContainerData testData = data[i];
                    if (containerStack.getUnlocalizedName().equals(testData.emptyContainer.getUnlocalizedName()) && testFluid.getFluid().getUnlocalizedName().equals(testData.fluid.getUnlocalizedName())) {
                        ItemStack returnStack = testData.filledContainer.copy();
                        if (getStackInSlot(SlotType.CONTAINER_OUTPUT.ordinal()) == null) {
                            this.setInventorySlotContents(SlotType.CONTAINER_OUTPUT.ordinal(), returnStack);
                            this.tank.drain(FluidContainerRegistry.getContainerCapacity(testData.filledContainer), true);
                            decrStackSize(SlotType.CONTAINER_INPUT.ordinal(), 1);
                        }
                    }else if(getStackInSlot(SlotType.CONTAINER_OUTPUT.ordinal()) == null || getStackInSlot(SlotType.CONTAINER_OUTPUT.ordinal()).getUnlocalizedName().equals(testData.emptyContainer.getUnlocalizedName())) {
                        if (containerStack.getUnlocalizedName().equals(testData.filledContainer.getUnlocalizedName()) && testFluid.getFluid().getUnlocalizedName().equals(testData.fluid.getUnlocalizedName())) {
                            ItemStack returnStack = testData.emptyContainer.copy();
                            if(this.tank.getInfo().fluid.amount < this.tank.getCapacity() - FluidContainerRegistry.getContainerCapacity(containerStack)) {
                                if (getStackInSlot(SlotType.CONTAINER_OUTPUT.ordinal()) == null) {
                                    this.setInventorySlotContents(SlotType.CONTAINER_OUTPUT.ordinal(), returnStack);
                                } else {
                                    ItemStack slotStack = getStackInSlot(SlotType.CONTAINER_OUTPUT.ordinal());
                                    slotStack.stackSize++;
                                    this.setInventorySlotContents(SlotType.CONTAINER_OUTPUT.ordinal(), slotStack);
                                }
                                this.tank.getInfo().fluid.amount += FluidContainerRegistry.getContainerCapacity(containerStack);
                                decrStackSize(SlotType.CONTAINER_INPUT.ordinal(), 1);
                            }
                        }
                    }
                }
            }else{
                FluidContainerData[] data = FluidContainerRegistry.getRegisteredFluidContainerData();
                for(int i = 0; i < data.length; i++) {
                    FluidContainerData testData = data[i];
                    if(containerStack.getUnlocalizedName().equals(testData.filledContainer.getUnlocalizedName())){
                        this.tank.setFluid(testData.fluid);
                        this.tank.getFluid().amount = 1000;
                        this.setInventorySlotContents(SlotType.CONTAINER_OUTPUT.ordinal(), testData.emptyContainer.copy());
                        decrStackSize(SlotType.CONTAINER_INPUT.ordinal(), 1);
                    }
                }
            }
        }

        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

    }


    public void smeltItem()
    {
        if (this.canSmelt())
        {
            if(this.tank.getFluid() == null){
                this.tank.setFluid(FluidRegistry.getFluidStack(PlenaInanis.crucibleAllowedItems.get(getSlotName(getFirstItemToSmelt())), 1000));
            }

            this.tank.getFluid().amount += 1000;
            setInventorySlotContents(getFirstItemToSmelt(), (ItemStack)null);
        }
    }
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale)
    {
        if(this.currentItemCookTime == 0) {
            this.currentItemCookTime = PlenaInanis.crucibleCookTimes.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing)));
        }
        return this.crucibleCookTime * scale / this.currentItemCookTime;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = 20000;
        }
        return this.crucibleBurnTime * scale / this.currentItemBurnTime;
    }

    public double getLiquidScaled(){
        return (double)( 1000D / (double)PlenaInanis.crucibleCookTimes.get(InfoHelper.getFullNameForItemStack(getStackInSlot(currentItemProcessing))));
    }

    public double getTemperatureScaled(){
        if(this.currentItemBurnTime > 0){
            //Total of 25,
            return (double)(this.currentItemTemperature / this.currentItemBurnTime);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public double getTotalTemperatureScaled(double scale){
        if(this.temperature > 8000D){
            this.temperature = 8000D;
        }

        return this.temperature * scale / 8000D;
    }

    public static int getItemTemperature(ItemStack itemStack){
        if (itemStack == null) {
            return 0;
        }
        if(PlenaInanis.crucibleFuelTemps.containsKey(InfoHelper.getFullNameForItemStack(itemStack))){
            return PlenaInanis.crucibleFuelTemps.get(InfoHelper.getFullNameForItemStack(itemStack));
        }else{
            if(GameRegistry.getFuelValue(itemStack) >= 4000){
                return (int)Math.ceil(GameRegistry.getFuelValue(itemStack) / 2.5);
            }else if(GameRegistry.getFuelValue(itemStack) > 0 && GameRegistry.getFuelValue(itemStack) < 4000){
                return (int)Math.ceil(GameRegistry.getFuelValue(itemStack) / 3);
            }
        }
        return 0;
    }

    public static int getItemBurnTime(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        if(PlenaInanis.crucibleBurnTimes.containsKey(InfoHelper.getFullNameForItemStack(itemStack))){
            return PlenaInanis.crucibleBurnTimes.get(InfoHelper.getFullNameForItemStack(itemStack));
        }else{
            return GameRegistry.getFuelValue(itemStack);
        }
    }

    public void updateInputSlots(){
        if(isSlotAvailable() && getStackInSlot(SlotType.PROCESS_INPUT.ordinal()) != null){
            ItemStack itemCopy = getStackInSlot(SlotType.PROCESS_INPUT.ordinal()).copy();
            itemCopy.stackSize = 1;
            ItemStack checkItem = getStackInSlot(SlotType.PROCESS_INPUT.ordinal());
            String containedFluid = "";
            String newFluid = "";
            if(PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())))){
                containedFluid = PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(getStackInSlot(getFirstItemToSmelt())));
            }
            if(PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(getStackInSlot(SlotType.PROCESS_INPUT.ordinal())))){
                newFluid = PlenaInanis.crucibleAllowedItems.get(InfoHelper.getFullNameForItemStack(checkItem));
            }

            if(!containsAnyInputs() || (!containedFluid.equals("") && !newFluid.equals("")) && containedFluid.equals(newFluid) || this.tank.getInfo().fluid == null){
                if(getStackInSlot(SlotType.PROCESS_INPUT.ordinal()).stackSize > 1){
                    decrStackSize(SlotType.PROCESS_INPUT.ordinal(), 1);
                }else{
                    setInventorySlotContents(SlotType.PROCESS_INPUT.ordinal(), null);
                }
                setInventorySlotContents(getNextSlot(),itemCopy);
            }
        }
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
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        this.inventory[index] = itemStack;
        if(itemStack != null && itemStack.stackSize > this.getInventoryStackLimit() && index < 3 && isItemValidForSlot(index, itemStack)){

            itemStack.stackSize = this.getInventoryStackLimit();
        }
        if(itemStack != null && index > 2 && isItemValidForSlot(index, itemStack)){
            itemStack.stackSize = 1;
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.crucible";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.customName != null && !this.customName.equals("");
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

        switch (index){
            case 0:
                if(PlenaInanis.crucibleBurnTimes.containsKey(InfoHelper.getFullNameForItemStack(itemStack))){
                    return true;
                }
                break;
            case 1:
                if(PlenaInanis.crucibleAllowedItems.containsKey(InfoHelper.getFullNameForItemStack(itemStack)) && isSlotAvailable()){
                    return true;
                }
                break;
            case 2:
                return true;
        }
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if(this.canFill(from, resource.getFluid())) {
            return this.tank.fill(resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.tank.drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if(from != ForgeDirection.getOrientation(this.getDirection())) {
            FluidStack testFluid = new FluidStack(fluid, 0);
            if(tank.getInfo().fluid == null){
                return true;
            }
            if(!this.dumping() && tank.getInfo().fluid.getUnlocalizedName().equals(testFluid.getUnlocalizedName()) &&  this.tank.getFluidAmount() < this.tank.getCapacity()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    public boolean canAddItem(String itemName){
        return (PlenaInanis.crucibleAllowedItems.containsKey(itemName) && (tank.getFluid().getFluid() == null || tank.getInfo().fluid.getUnlocalizedName().equals(PlenaInanis.crucibleAllowedItems.get(itemName))));
    }

    public int getFirstItemToSmelt(){
        for(int i = 3; i < 30; i++){
            if(PlenaInanis.crucibleAllowedItems.containsKey(getSlotName(i))){
                return i;
            }
        }
        return 0;
    }


    public String getSlotName(int index){
        return InfoHelper.getFullNameForItemStack(getStackInSlot(index));
    }

    public int getNextSlot(){
        if(isSlotAvailable()) {
            for (int i = 4; i < 31; i++) {
                if (this.getStackInSlot(i) == null) {
                    return i;
                }
            }
        }
        return 0;
    }

    public boolean isSlotAvailable(){
        for(int i = 4; i < 31; i++){
            if(this.getStackInSlot(i) == null){
                return true;
            }
        }
        return false;
    }

    public boolean containsAnyInputs(){
        for(int i = 4; i < 31; i++) {
            if(this.getStackInSlot(i) != null){
                return true;
            }
        }
        return false;
    }

    public boolean isBurning()
    {
        return this.crucibleBurnTime > 0;
    }

    public boolean containsFluid(){
        return tank.getFluidAmount() > 0;
    }

    public boolean isLocked(){
        return this.tank.getFluidAmount() > 0 || this.temperature > 0;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        switch(direction){
            case DOWN:
                return new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
            case UP:
                return new int[]{0, 1};
            case NORTH:
                return new int[]{2, 3};
            case SOUTH:
                return new int[]{2, 3};
            case EAST:
                return new int[]{2, 3};
            case WEST:
                return new int[]{2, 3};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack itemStack, int side)
    {
        return this.isItemValidForSlot(slotIndex, itemStack);
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack itemStack, int side) {
        if(ForgeDirection.getOrientation(side) == ForgeDirection.DOWN && slotIndex >= 3){
            return true;
        }
        return false;
    }

    public void purging (boolean bool){
        this.purging = bool;
        markDirty();
    }

    public boolean purging (){
        return this.purging;
    }

    public void dumping (boolean bool){
        this.dumping = bool;
        markDirty();
    }

    public boolean dumping (){
        return this.dumping;
    }


    public ItemStack[] getInventory(){
        return this.inventory;
    }

}
