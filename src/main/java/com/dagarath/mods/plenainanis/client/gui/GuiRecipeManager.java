package com.dagarath.mods.plenainanis.client.gui;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.buttons.LargeButton;
import com.dagarath.mods.plenainanis.common.containers.PlenaRecipeManagerContainer;
import com.dagarath.mods.plenainanis.common.helpers.InfoHelper;
import com.dagarath.mods.plenainanis.common.helpers.ObjectSerializer;
import com.dagarath.mods.plenainanis.common.helpers.OutputData;
import com.dagarath.mods.plenainanis.common.network.PacketHandler;
import com.dagarath.mods.plenainanis.common.network.Packets.PacketClearRecipeSlots;
import com.dagarath.mods.plenainanis.common.network.Packets.PacketSyncRecipeSlot;
import com.dagarath.mods.plenainanis.common.registrars.BlockRegistrar;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dagarath on 2016-02-01.
 */
@SideOnly(Side.CLIENT)
public class GuiRecipeManager extends GuiContainer {

    // <editor-fold desc="Fields">

    enum State{
        IDLE, COMPOSTER_ACTIVE, SIEVE_ACTIVE, SQUASHER_ACTIVE
    }

    enum Event {
        IDLE, INPUT_FILLED, INPUT_EXISTS, VIEWING, EDITING, OUTPUT_EMPTY, OUTPUT_FILLED, OUTPUT_SAVED, OUTPUT_CHANGED
    }

    public State state;
    public Event event;

    int xPos;
    int yPos;
    int curTabId= 0;
    int xTabInitial;
    int yTabInitial;
    int setupForIndex = 0;
    boolean valuesSet = false;
    PlenaRecipeManagerContainer container;
    GuiButton addInput;
    GuiButton removeInput;
    GuiButton updateOutput;
    GuiButton editInput;
    GuiButton listInputs;
    GuiButton removeInputs;
    GuiButton exitEdit;

    GuiButton nextSlot;
    GuiButton prevSlot;
    GuiTextField weightValue;
    GuiTextField minOutput;
    GuiTextField maxOutput;
    String lastOutput;

    ResourceLocation recipeTexture = new ResourceLocation(PlenaInanisReference.MODID + ":textures/gui/container/recipeEditor.png");

    // </editor-fold>


    // <editor-fold desc="Initialization/Construction">

    public GuiRecipeManager(PlenaRecipeManagerContainer container) {
        super(container);
        this.xSize = 226;
        this.ySize = 144;
//        this.curTabId = PlenaInanis.saveData.getCurrentMachineIndex();
        if(!ObjectSerializer.exists()){
            PlenaInanis.logger.debug(ObjectSerializer.exists());
            ObjectSerializer.writeSave();
        }
        this.container = container;
        state = State.IDLE;
        event = Event.IDLE;
    }
    @Override
    public void initGui(){
        super.initGui();
        this.buttonList.add(this.listInputs = new LargeButton(0, this.guiLeft + 26, this.guiTop + 8, recipeTexture, 0, 168));
        this.listInputs.width = this.listInputs.height =  12;
        this.listInputs.enabled = false;
        this.listInputs.visible = false;
        this.buttonList.add(this.removeInputs = new LargeButton(0, this.guiLeft + 42, this.guiTop + 8, recipeTexture, 24, 168));
        this.removeInputs.width = this.removeInputs.height = 12;
        this.removeInputs.enabled = false;
        this.removeInputs.visible = false;
        this.buttonList.add(this.addInput = new LargeButton(0, this.guiLeft + 32, this.guiTop + 10, recipeTexture, 24, 144));
        this.addInput.width = this.addInput.height = 12;
        this.addInput.enabled = false;
        this.addInput.visible = false;
        this.buttonList.add(this.removeInput = new LargeButton(0, this.guiLeft + 32, this.guiTop + 10, recipeTexture, 24, 156));
        this.removeInput.width = this.removeInput.height = 12;
        this.removeInput.enabled = false;
        this.removeInput.visible = false;
        this.buttonList.add(this.editInput = new LargeButton(0, this.guiLeft + 10, this.guiTop + 25, recipeTexture, 0, 192));
        this.editInput.width = this.editInput.height = 12;
        this.editInput.enabled = false;
        this.editInput.visible = false;
        this.buttonList.add(this.updateOutput = new LargeButton(0, this.guiLeft + 10, this.guiTop + 25, recipeTexture, 0, 180));
        this.updateOutput.width = this.updateOutput.height = 12;
        this.updateOutput.enabled = false;
        this.updateOutput.visible = false;
        this.buttonList.add(this.prevSlot = new LargeButton(0, this.guiLeft + 26, this.guiTop + 25, recipeTexture, 0, 144));
        this.prevSlot.width = this.prevSlot.height = 12;
        this.prevSlot.enabled = false;
        this.prevSlot.visible = false;
        this.buttonList.add(this.nextSlot = new LargeButton(0, this.guiLeft + 40, this.guiTop + 25, recipeTexture, 0, 156));
        this.nextSlot.width = this.nextSlot.height = 12;
        this.nextSlot.enabled = false;
        this.nextSlot.visible = false;
        this.buttonList.add(this.exitEdit = new LargeButton(0, this.guiLeft + 64, this.guiTop + 25, recipeTexture, 24, 180));
        this.exitEdit.width = this.exitEdit.height = 12;
        this.exitEdit.enabled = false;
        this.exitEdit.visible = false;
        this.weightValue = new GuiTextField(this.fontRendererObj, 171, 76, 45, 10);
        this.minOutput = new GuiTextField(this.fontRendererObj, 171,  101, 45, 10);
        this.maxOutput = new GuiTextField(this.fontRendererObj, 171,  126, 45, 10);
        PlenaInanis.saveData.isSetup();
    }


    // </editor-fold>

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        if(this.container != null) {
            updateState();
        }
    }
    public void updateState(){
        switch(state){
            case IDLE:
                switch(curTabId){
                    case 0:
                        state = State.COMPOSTER_ACTIVE;
                        break;
                    case 1:
                        state = State.SIEVE_ACTIVE;
                        break;
                    case 2:
                        state = State.SQUASHER_ACTIVE;
                        break;
                }
                break;
            case COMPOSTER_ACTIVE:
                drawControls();
                break;
            case SIEVE_ACTIVE:
                drawControls();
                break;
            case SQUASHER_ACTIVE:
                drawControls();
                break;
        }
    }

    public void updateEvent(){

    }

    // <editor-fold desc="Controls">
    public void drawControls(){
        switch(event){
            case IDLE:
                this.listInputs.enabled = this.listInputs.visible = true;
                if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
                    this.removeInputs.enabled = this.removeInputs.visible = true;
                }
                this.addInput.enabled = this.addInput.visible = this.removeInput.enabled = this.removeInput.visible =
                        this.editInput.enabled = this.editInput.visible = this.exitEdit.visible = this.exitEdit.enabled =
                                this.nextSlot.visible = this.nextSlot.enabled = this.prevSlot.visible = this.prevSlot.enabled =
                                        this.updateOutput.enabled = this.updateOutput.visible = false;
                if(isThereInput()){
                    event = Event.INPUT_FILLED;
                }
//                PlenaInanis.logger.info("Idle");
                clearAll();
                break;
            case INPUT_FILLED:
                if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode){
                    event = Event.VIEWING;
                    return;
                }
                this.removeInputs.enabled = this.removeInputs.visible = this.listInputs.enabled = this.listInputs.visible =
                        this.exitEdit.visible = this.exitEdit.enabled = this.nextSlot.visible = this.nextSlot.enabled =
                                this.prevSlot.visible = this.prevSlot.enabled = this.updateOutput.enabled = this.updateOutput.visible = false;
                this.addInput.enabled = this.addInput.visible = true;
                if(!isThereInput()){
                    event = Event.IDLE;
                    return;
                }
                switch(state){
                    case COMPOSTER_ACTIVE:
                        if(PlenaInanis.saveData.composterExists(getSlotName(0))){
                            event = Event.INPUT_EXISTS;
                        }
                        break;
                    case SIEVE_ACTIVE:
                        if(PlenaInanis.saveData.sieveExists(getSlotName(0))){
                            event = Event.INPUT_EXISTS;
                        }
                        break;
                    case SQUASHER_ACTIVE:
                        if(PlenaInanis.saveData.squasherExists(getSlotName(0))){
                            event = Event.INPUT_EXISTS;
                        }
                        break;
                }
//                PlenaInanis.logger.info("Input Filled");
                break;
            case INPUT_EXISTS:
                this.removeInputs.enabled = this.removeInputs.visible = this.listInputs.enabled = this.listInputs.visible
                        = this.addInput.enabled = this.addInput.visible = this.exitEdit.visible = this.exitEdit.enabled =
                        this.nextSlot.visible = this.nextSlot.enabled = this.prevSlot.visible = this.prevSlot.enabled =
                                this.updateOutput.enabled = this.updateOutput.visible = false;
                this.removeInput.enabled = this.removeInput.visible = this.editInput.enabled = this.editInput.visible = true;
                if(!isThereInput()){
                    event = Event.IDLE;
                    return;
                }
                switch (state){
                    case COMPOSTER_ACTIVE:
                        if(PlenaInanis.saveData.anyComposterOutputs(getSlotName(0))){
                            HashMap<String, OutputData> outputData = PlenaInanis.saveData.getComposterOutputs(getSlotName(0));
                            getAndSetOutputs(outputData);
                            event = Event.OUTPUT_FILLED;
                        }
                        break;
                    case SIEVE_ACTIVE:
                        if(PlenaInanis.saveData.anySieveOutputs(getSlotName(0))){
                            HashMap<String, OutputData> outputData = PlenaInanis.saveData.getSieveOutputs(getSlotName(0));
                            getAndSetOutputs(outputData);
                            event = Event.OUTPUT_FILLED;
                        }
                        break;
                    case SQUASHER_ACTIVE:
                        if(PlenaInanis.saveData.anySquasherOutputs(getSlotName(0))){
                            HashMap<String, OutputData> outputData = PlenaInanis.saveData.getSquasherOutputs(getSlotName(0));
                            getAndSetOutputs(outputData);
                            event = Event.OUTPUT_FILLED;
                        }
                        break;
                }
//                PlenaInanis.logger.info("Input Exists");
                break;
            case OUTPUT_FILLED:
                if(getSlotContents(0) == null){
                    event = Event.IDLE;
                    return;
                }
                if(getSlotContents(this.container.selectedOutput) != null) {
                    lastOutput = getSlotName(this.container.selectedOutput);
                }
//                PlenaInanis.logger.info("Output Filled");
                break;
            case VIEWING:
//                PlenaInanis.logger.info("Event VIEWING");
                if(getSlotContents(0) == null){
                    event = Event.IDLE;
                    return;
                }
                switch (state){
                    case COMPOSTER_ACTIVE:
                        if(PlenaInanis.saveData.composterExists(getSlotName(0))) {
                            if (PlenaInanis.saveData.anyComposterOutputs(getSlotName(0))) {
                                HashMap<String, OutputData> outputData = PlenaInanis.saveData.getComposterOutputs(getSlotName(0));
                                getAndSetOutputs(outputData);
                            }
                        }else{
                            return;
                        }
                        break;
                    case SIEVE_ACTIVE:
                        if(PlenaInanis.saveData.sieveExists(getSlotName(0))) {
                            if (PlenaInanis.saveData.anySieveOutputs(getSlotName(0))) {
                                HashMap<String, OutputData> outputData = PlenaInanis.saveData.getSieveOutputs(getSlotName(0));
                                getAndSetOutputs(outputData);
                            }
                        }else{
                            return;
                        }
                        break;
                    case SQUASHER_ACTIVE:
                        if(PlenaInanis.saveData.squasherExists(getSlotName(0))) {
                            if (PlenaInanis.saveData.anySquasherOutputs(getSlotName(0))) {
                                HashMap<String, OutputData> outputData = PlenaInanis.saveData.getSquasherOutputs(getSlotName(0));
                                getAndSetOutputs(outputData);
                            }
                        }else{
                            return;
                        }
                        break;
                }
                this.nextSlot.enabled = this.nextSlot.visible = this.prevSlot.enabled = this.prevSlot.visible = true;
                if(this.container.selectedOutput > 0 && this.container.selectedOutput < 13){
                    drawSlotHighlight();
                }
                break;
            case EDITING:
                this.removeInputs.enabled = this.removeInputs.visible = this.listInputs.enabled = this.listInputs.visible
                        = this.addInput.enabled = this.addInput.visible = this.removeInput.enabled = this.removeInput.visible =
                        this.editInput.enabled = this.editInput.visible = this.updateOutput.enabled = this.updateOutput.visible = false;
                this.container.setEditMode(true);
                this.exitEdit.visible = this.exitEdit.enabled = this.nextSlot.visible = this.nextSlot.enabled =
                        this.prevSlot.visible = this.prevSlot.enabled = true;
                if(this.container.selectedOutput > 0 && this.container.selectedOutput < 13){
                    if(getSlotContents(this.container.selectedOutput) != null){
                        event = Event.OUTPUT_CHANGED;
                    }else{
                        event = Event.OUTPUT_EMPTY;
                    }
                }
//                PlenaInanis.logger.info("Editing Mode");
                break;
            case OUTPUT_EMPTY:
//                PlenaInanis.logger.info("Output Empty");
                if(getSlotContents(this.container.selectedOutput) != null){
                    switch(state){
                        case COMPOSTER_ACTIVE:
                            if(PlenaInanis.saveData.composterOutputExists(getSlotName(0), getSlotName(this.container.selectedOutput))){
                                event = Event.OUTPUT_SAVED;
                                return;
                            }
                            break;
                        case SIEVE_ACTIVE:
                            if(PlenaInanis.saveData.sieveOutputExists(getSlotName(0), getSlotName(this.container.selectedOutput))){
                                event = Event.OUTPUT_SAVED;
                                return;
                            }
                            break;
                        case SQUASHER_ACTIVE:
                            if(PlenaInanis.saveData.squasherOutputExists(getSlotName(0), getSlotName(this.container.selectedOutput))){
                                event = Event.OUTPUT_SAVED;
                                return;
                            }
                            break;
                    }
                    this.updateOutput.enabled = this.updateOutput.visible = true;
                    this.weightValue.setText("100");
                    this.minOutput.setText("1");
                    this.maxOutput.setText("1");
                    event = Event.OUTPUT_CHANGED;
                }
                drawSlotHighlight();
                break;
            case OUTPUT_SAVED:
//                PlenaInanis.logger.info("Output Saved");
                this.updateOutput.enabled = this.updateOutput.visible = false;
                drawSlotHighlight();
                OutputData outputData = new OutputData();
                if(getSlotContents(this.container.selectedOutput) == null){
                    switch (state){
                        case COMPOSTER_ACTIVE:
                            PlenaInanis.saveData.removeComposterOutput(getSlotName(0), lastOutput);
                            event = Event.OUTPUT_EMPTY;
                            return;
                        case SIEVE_ACTIVE:
                            PlenaInanis.saveData.removeSieveOutput(getSlotName(0), lastOutput);
                            event = Event.OUTPUT_EMPTY;
                            return;
                        case SQUASHER_ACTIVE:
                            PlenaInanis.saveData.removeSquasherOutput(getSlotName(0), lastOutput);
                            event = Event.OUTPUT_EMPTY;
                            return;
                    }
                }
                switch(state){
                    case COMPOSTER_ACTIVE:
                        outputData = PlenaInanis.saveData.getComposterOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SIEVE_ACTIVE:
                        outputData = PlenaInanis.saveData.getSieveOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SQUASHER_ACTIVE:
                        outputData = PlenaInanis.saveData.getSquasherOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                }
                if(outputData.weight != Integer.parseInt(this.weightValue.getText())){
                    event = Event.OUTPUT_CHANGED;
                    return;
                }
                if(outputData.minOutput != Integer.parseInt(this.minOutput.getText())){
                    event = Event.OUTPUT_CHANGED;
                    return;
                }
                if(outputData.maxOutput != Integer.parseInt(this.maxOutput.getText())){
                    event = Event.OUTPUT_CHANGED;
                    return;
                }
                this.weightValue.setText("" + outputData.weight);
                this.minOutput.setText("" + outputData.minOutput);
                this.maxOutput.setText("" + outputData.maxOutput);
                break;
            case OUTPUT_CHANGED:
//                PlenaInanis.logger.info("Output Changed");
                if(getSlotContents(this.container.selectedOutput) == null){
                    switch (state){
                        case COMPOSTER_ACTIVE:
                            PlenaInanis.saveData.removeComposterOutput(getSlotName(0), lastOutput);
                            return;
                        case SIEVE_ACTIVE:
                            PlenaInanis.saveData.removeSieveOutput(getSlotName(0), lastOutput);
                            return;
                        case SQUASHER_ACTIVE:
                            PlenaInanis.saveData.removeSquasherOutput(getSlotName(0), lastOutput);
                            return;
                    }
                }
                this.updateOutput.enabled = this.updateOutput.visible = true;
                drawSlotHighlight();

                break;

        }
    }

    public void getAndSetOutputs(HashMap<String, OutputData> outputData){
        ItemStack putStack;
        int slotNum = 1;
        for(Iterator<Map.Entry<String, OutputData>> it = outputData.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<String, OutputData> entry = it.next();
            if(entry != null) {
                putStack = InfoHelper.getStackWithNullCheck(entry.getKey().substring(0, entry.getKey().lastIndexOf(":")));
                if (putStack != null) {
                    putStack.setItemDamage(Integer.parseInt(entry.getKey().substring(entry.getKey().lastIndexOf(":") + 1)));
                    PacketHandler.INSTANCE.sendToServer(new PacketSyncRecipeSlot(Minecraft.getMinecraft().thePlayer, slotNum, putStack));
                    if(slotNum == this.container.selectedOutput){
                        OutputData fillOutput = entry.getValue();
                        this.weightValue.setText("" + fillOutput.weight);
                        this.minOutput.setText("" + fillOutput.minOutput);
                        this.maxOutput.setText("" + fillOutput.maxOutput);
                    }
                    slotNum++;
                }
            }
        }
    }

    // </editor-fold>

    // <editor-fold desc="Input">
    @Override
    protected void mouseClicked(int x, int y, int button) {
        if(x < xTabInitial + 32){
            if(x > xTabInitial) {
                if(y > yTabInitial && y < (yTabInitial + 32)) {
                    curTabId = 0;
                    state = State.IDLE;
                    event = Event.IDLE;
                    updateScreen();
                }else if(y > yTabInitial + 32 && y < (yTabInitial + 64)){
                    curTabId = 1;
                    state = State.IDLE;
                    event = Event.IDLE;
                    updateScreen();
                } else if(y > yTabInitial + 64 && y < (yTabInitial + 80)) {
                    curTabId = 2;
                    state = State.IDLE;
                    event = Event.IDLE;
                    updateScreen();
                }
            }
        }else{
            if(PlenaInanis.proxy.getPlayer().capabilities.isCreativeMode){
                if(x > (this.guiLeft + 171)) {
                    if (y > (this.guiTop + 76) && y < (this.guiTop + 86)) {
                        this.weightValue.setFocused(true);
                        this.minOutput.setFocused(false);
                        this.maxOutput.setFocused(false);
                    }
                    if (y > (this.guiTop + 101) && y < (this.guiTop + 111)) {
                        this.minOutput.setFocused(true);
                        this.maxOutput.setFocused(false);
                        this.weightValue.setFocused(false);
                    }
                    if (y > (this.guiTop + 126) && y < (this.guiTop + 136)) {
                        this.maxOutput.setFocused(true);
                        this.minOutput.setFocused(false);
                        this.weightValue.setFocused(false);
                    }
                }
            }else{
                this.weightValue.setFocused(false);
                this.minOutput.setFocused(false);
                this.maxOutput.setFocused(false);
            }
           super.mouseClicked(x,y,button);
        }
    }

    protected void keyTyped(char par1, int par2)
    {
        if(!isThereInput() || PlenaInanis.proxy.getPlayer().capabilities.isCreativeMode || this.container.editMode()) {
            super.keyTyped(par1, par2);
        }else{
            PlenaInanis.proxy.getPlayer().addChatMessage(new ChatComponentText("Remove item from input slot"));
        }
        if(par1 == '\t'){
            if(this.weightValue.isFocused()){
                this.weightValue.setFocused(false);
                this.maxOutput.setFocused(false);
                this.minOutput.setFocused(true);
            }else if(this.minOutput.isFocused()){
                this.weightValue.setFocused(false);
                this.minOutput.setFocused(false);
                this.maxOutput.setFocused(true);
            }else if(this.maxOutput.isFocused()){
                this.minOutput.setFocused(false);
                this.maxOutput.setFocused(false);
                this.weightValue.setFocused(true);
            }
        }else if(par1 == '0' || par1 == '1' || par1 == '2' || par1 == '3' || par1 == '4' || par1 == '5' || par1 == '6'
                || par1 == '7' || par1 == '8' || par1 == '9'  || par1 == '\b')
        {
            if (this.weightValue.isFocused()) {
                this.weightValue.textboxKeyTyped(par1, par2);
                event = Event.OUTPUT_CHANGED;
            }
            if (this.minOutput.isFocused()) {
                this.minOutput.textboxKeyTyped(par1, par2);
                event = Event.OUTPUT_CHANGED;
            }
            if (this.maxOutput.isFocused()) {
                this.maxOutput.textboxKeyTyped(par1, par2);
                event = Event.OUTPUT_CHANGED;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button == this.listInputs){
            switch (state){
                case COMPOSTER_ACTIVE:
                    PlenaInanis.saveData.listComposterInputs(PlenaInanis.proxy.getPlayer());
                    break;
                case SIEVE_ACTIVE:
                    PlenaInanis.saveData.listSieveInputs(PlenaInanis.proxy.getPlayer());
                    break;
                case SQUASHER_ACTIVE:
                    PlenaInanis.saveData.listSquasherInputs(PlenaInanis.proxy.getPlayer());
                    break;
            }
        }
        if(button == this.removeInputs){
            switch (state){
                case COMPOSTER_ACTIVE:
                    PlenaInanis.saveData.composterRemoveAll();
                    clearAll();
                    break;
                case SIEVE_ACTIVE:
                    PlenaInanis.saveData.sieveRemoveAll();
                    clearAll();
                    break;
                case SQUASHER_ACTIVE:
                    PlenaInanis.saveData.squasherRemoveAll();
                    clearAll();
                    break;
            }
        }
        if(button == this.addInput){
            switch(state){
                case COMPOSTER_ACTIVE:
                    PlenaInanis.saveData.composterInput(getSlotName(0));
                    event = Event.INPUT_EXISTS;
                    break;
                case SIEVE_ACTIVE:
                    PlenaInanis.saveData.sieveInput(getSlotName(0));
                    event = Event.INPUT_EXISTS;
                    break;
                case SQUASHER_ACTIVE:
                    PlenaInanis.saveData.squasherInput(getSlotName(0));
                    event = Event.INPUT_EXISTS;
                    break;
            }

        }
        if(button == this.removeInput){
            switch(curTabId){
                case 0:
                    PlenaInanis.saveData.composterRemove(getSlotName(0));
                    this.removeInput.visible = this.removeInput.enabled = false;
                    event = Event.IDLE;
                    break;
                case 1:
                    PlenaInanis.saveData.sieveRemove(getSlotName(0));
                    this.removeInput.visible = this.removeInput.enabled = false;
                    event = Event.IDLE;
                    break;
                case 2:
                    PlenaInanis.saveData.squasherRemove(getSlotName(0));
                    this.removeInput.visible = this.removeInput.enabled = false;
                    event = Event.IDLE;
                    break;
            }
            clearAll();
        }
        if(button == this.editInput){
            this.container.selectedOutput = 1;
            if(getSlotContents(1) != null){
                OutputData outputData = new OutputData();
                switch(state){
                    case COMPOSTER_ACTIVE:
                        outputData = PlenaInanis.saveData.getComposterOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SIEVE_ACTIVE:
                        outputData = PlenaInanis.saveData.getSieveOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SQUASHER_ACTIVE:
                        outputData = PlenaInanis.saveData.getSquasherOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                }
                this.weightValue.setText("" + outputData.weight);
                this.minOutput.setText("" + outputData.minOutput);
                this.maxOutput.setText("" + outputData.maxOutput);
            }
            event = Event.EDITING;
        }
        if(button == this.updateOutput){
            if(this.container.selectedOutput > 0 && this.container.selectedOutput < 13) {
                if (getSlotContents(this.container.selectedOutput) != null) {
                    OutputData outputData = new OutputData();
                    outputData.outputName = getSlotName(this.container.selectedOutput);
                    outputData.weight = Integer.parseInt(this.weightValue.getText());
                    outputData.minOutput = Integer.parseInt(this.minOutput.getText());
                    outputData.maxOutput = Integer.parseInt(this.maxOutput.getText());
                    switch (curTabId) {
                        case 0:
                            PlenaInanis.saveData.addComposterOutput(getSlotName(0), outputData);
                            break;
                        case 1:
                            PlenaInanis.saveData.addSieveOutput(getSlotName(0), outputData);
                            break;
                        case 2:
                            PlenaInanis.saveData.addSquasherOutput(getSlotName(0), outputData);
                            break;
                    }
                    event = Event.OUTPUT_SAVED;
                    return;
                }
            }
            event = Event.EDITING;
        }
        if(button == this.nextSlot){
            if(this.container.selectedOutput > 0 && this.container.selectedOutput < 12){
                this.container.selectedOutput++;
            }else{
                this.container.selectedOutput = 1;
            }
            if(getSlotContents(this.container.selectedOutput) != null){
                OutputData outputData = new OutputData();
                switch(state){
                    case COMPOSTER_ACTIVE:
                        outputData = PlenaInanis.saveData.getComposterOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SIEVE_ACTIVE:
                        outputData = PlenaInanis.saveData.getSieveOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SQUASHER_ACTIVE:
                        outputData = PlenaInanis.saveData.getSquasherOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                }
                this.weightValue.setText("" + outputData.weight);
                this.minOutput.setText("" + outputData.minOutput);
                this.maxOutput.setText("" + outputData.maxOutput);
            }
            if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
                event = Event.EDITING;
            }
        }
        if(button== this.prevSlot){
            if(this.container.selectedOutput > 1 && this.container.selectedOutput < 13){
                this.container.selectedOutput--;
            }else{
                this.container.selectedOutput = 12;
            }
            if(getSlotContents(this.container.selectedOutput) != null){
                OutputData outputData = new OutputData();
                switch(state){
                    case COMPOSTER_ACTIVE:
                        outputData = PlenaInanis.saveData.getComposterOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SIEVE_ACTIVE:
                        outputData = PlenaInanis.saveData.getSieveOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                    case SQUASHER_ACTIVE:
                        outputData = PlenaInanis.saveData.getSquasherOutput(getSlotName(0), getSlotName(this.container.selectedOutput));
                        break;
                }
                this.weightValue.setText("" + outputData.weight);
                this.minOutput.setText("" + outputData.minOutput);
                this.maxOutput.setText("" + outputData.maxOutput);
            }
            if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
                event = Event.EDITING;
            }
        }
        if(button == this.exitEdit){
            this.container.setEditMode(false);
            event = Event.IDLE;
        }
    }
    // </editor-fold>

    // <editor-fold desc="Generic Methods">

    public String getSlotName(int index){
        return InfoHelper.getFullNameForItemStack(this.container.getSlot(index).getStack()) + ":" + this.container.getSlot(index).getStack().getItemDamage();
    }

    public ItemStack getSlotContents(int index){
        return this.container.getSlot(index).getStack();
    }

    public boolean isThereInput(){
        ItemStack inputSlot = this.container.getSlot(0).getStack();
        return inputSlot != null;
    }

    public void clearAll(){
        //this.container.clearOutputs();
        PacketHandler.INSTANCE.sendToServer(new PacketClearRecipeSlots(Minecraft.getMinecraft().thePlayer));
        updateScreen();
        super.updateScreen();
    }

    public void editOutputs(){

    }

    public void drawSlotHighlight(){
        if(this.container.selectedOutput > 0 && this.container.selectedOutput < 13) {
            Slot slot = this.container.getSlot(this.container.selectedOutput);
            int j1 = slot.xDisplayPosition;
            int k1 = slot.yDisplayPosition;
            this.mc.getTextureManager().bindTexture(recipeTexture);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(j1, k1, 1, 239, 16, 16);

            if(getSlotContents(this.container.selectedOutput) != null) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                mc.renderEngine.bindTexture(recipeTexture);
                this.drawTexturedModalRect(163, 65, 206, 177, 50, 9);
                this.weightValue.drawTextBox();
                mc.renderEngine.bindTexture(recipeTexture);
                this.drawTexturedModalRect(169, 90, 206, 185, 50, 9);
                this.minOutput.drawTextBox();
                mc.renderEngine.bindTexture(recipeTexture);
                this.drawTexturedModalRect(169, 114, 206, 194, 50, 9);
                this.maxOutput.drawTextBox();
                GL11.glPopMatrix();
            }
        }
    }
    // </editor-fold>

    // <editor-fold desc="Seldom Used">
    @Override
    public void drawGuiContainerBackgroundLayer(float zIndex, int x, int y) {
        this.xPos = (width - xSize) / 2;
        this.yPos = (height - ySize) / 2;
        this.xTabInitial = this.guiLeft - 30;
        this.yTabInitial = this.guiTop + 16;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(recipeTexture);
        GL11.glScalef(1f,1f,1f);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
        drawTabs();
    }

    private void drawTabs(){
        if(this.curTabId == 0) {
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 64, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 32, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial, 0, 204, 32, 32);
            //drawComposterTab();
        }
        if(this.curTabId == 1){
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 64, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 32, 0, 204, 32, 32);
            //drawSieveTab();
        }
        if(this.curTabId == 2){
            this.drawTexturedModalRect(xTabInitial, yTabInitial, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 32, 32, 204, 32, 32);
            this.drawTexturedModalRect(xTabInitial, yTabInitial + 64, 0, 204, 32, 32);
            //drawSquasherTab();
        }
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj,mc.renderEngine, new ItemStack(Item.getItemFromBlock(BlockRegistrar.composterOak)), xTabInitial + 8, yTabInitial + 8);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj,mc.renderEngine, new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockSieve)), xTabInitial + 8, yTabInitial + 40);
        itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj,mc.renderEngine, new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockSquasher)), xTabInitial + 8, yTabInitial + 72);
    }
    // </editor-fold>
}
