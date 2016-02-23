package com.dagarath.mods.plenainanis.common.helpers;


import com.dagarath.mods.plenainanis.PlenaInanis;
import com.ibm.icu.util.Output;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.*;

/**
 * Created by dagarath on 2016-01-31.
 */
public class PlenaSaveData implements Serializable {
    private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("parentMap", HashMap.class), new ObjectStreamField("currentMachineIndex", Integer.class)};
    //List of all local configurations
    private HashMap<String, HashMap<String, HashMap<String, OutputData>>> parentMap = new HashMap<>();
    private Integer currentMachineIndex = 0;

    //Goal is to store varying data compound within a parent hashmap
    //Individual data maked by String, and is of type Hashmap
    //Type conversion is handled by addition/extraction methods
    //Internalmap contains Unique MachineData for each machine. with String(name)/MachineData pair.
    //MachineData contains an input/output Hashmap

    //Machine<Input<Output

    public PlenaSaveData(){
        parentMap.put("Composter", new HashMap<>());
        parentMap.put("Sieve", new HashMap<>());
        parentMap.put("Squasher", new HashMap<>());
    }

    // <editor-fold desc="Private Methods">
    private void putInput(String machineName, String inputName){
        parentMap.get(machineName).put(inputName, new HashMap<>());
        ObjectSerializer.writeSave();
    }

    private void putOutput(String machineName, String inputName, String outputName){
        parentMap.get(machineName).get(inputName).put(outputName, new OutputData());
        ObjectSerializer.writeSave();
    }

    private boolean exists(String machineName, String inputName){
        return parentMap.get(machineName).containsKey(inputName);
    }

    public void isSetup(){
        if(!parentMap.containsKey("Composter")){
            parentMap.put("Composter", new HashMap<>());
            ObjectSerializer.writeSave();
        }
        if(!parentMap.containsKey("Sieve")){
            parentMap.put("Sieve", new HashMap<>());
            ObjectSerializer.writeSave();
        }
        if(!parentMap.containsKey("Squasher")){
            parentMap.put("Squasher", new HashMap<>());
            ObjectSerializer.writeSave();
        }
    }

    private void listInputs(String machineName, EntityPlayer player){
        for(Map.Entry<String, HashMap<String, HashMap<String, OutputData>>> parentEntry : parentMap.entrySet()){
            if(parentEntry.getKey().equals(machineName)) {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.BOLD + machineName + " Inputs"));
                int num = 0;
                for (Iterator<Map.Entry<String, HashMap<String, OutputData>>> it = parentEntry.getValue().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, HashMap<String, OutputData>> entry = it.next();
                    ItemStack printStack = InfoHelper.getStackWithNullCheck(entry.getKey().substring(0, entry.getKey().lastIndexOf(":")));
                    if(printStack != null) {
                        printStack.setItemDamage(Integer.parseInt(entry.getKey().substring(entry.getKey().lastIndexOf(":") + 1)));
                        player.addChatMessage(new ChatComponentText(printStack.getDisplayName()));
                    }
                    num++;
                }
                if(num == 0){
                    player.addChatMessage(new ChatComponentText("None"));
                }
            }
        }
    }
    private void removeInputs(String machineName){

        for(Map.Entry<String, HashMap<String, HashMap<String, OutputData>>> parentEntry : parentMap.entrySet()){
            if(parentEntry.getKey().equals(machineName)) {
                for (Iterator<Map.Entry<String, HashMap<String, OutputData>>> it = parentEntry.getValue().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, HashMap<String, OutputData>> entry = it.next();
                    removeOutputsFromInput(machineName, entry.getKey());
                    it.remove();
                }
            }
        }
        ObjectSerializer.writeSave();
    }

    private void removeOutputsFromInput(String machineName, String inputName){
        for(Map.Entry<String, HashMap<String, HashMap<String, OutputData>>> parentEntry : parentMap.entrySet()){
            if(parentEntry.getKey().equals(machineName)) {
                for (Map.Entry<String, HashMap<String, OutputData>> inputEntry : parentEntry.getValue().entrySet()) {
                    if (inputEntry.getKey().equals(inputName)) {
                        for (Iterator<Map.Entry<String, OutputData>> it = inputEntry.getValue().entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<String, OutputData> entry = it.next();
                            if (entry != null) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }
        ObjectSerializer.writeSave();
    }

    private void removeinput(String machineName, String inputName){
        for(Map.Entry<String, HashMap<String, HashMap<String, OutputData>>> parentEntry : parentMap.entrySet()){
            if(parentEntry.getKey().equals(machineName)) {
                for (Iterator<Map.Entry<String, HashMap<String, OutputData>>> it = parentEntry.getValue().entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, HashMap<String, OutputData>> entry = it.next();
                    removeOutputsFromInput(machineName, entry.getKey());
                    if (entry.getKey().equals(inputName)) {
                        it.remove();
                    }
                }
            }
        }
        ObjectSerializer.writeSave();
    }

    private void removeOutput(String machineName, String inputName, String outputName){
        for(Map.Entry<String, HashMap<String, HashMap<String, OutputData>>> parentEntry : parentMap.entrySet()){
            if(parentEntry.getKey().equals(machineName)) {
                for (Map.Entry<String, HashMap<String, OutputData>> inputEntry : parentEntry.getValue().entrySet()) {
                    if (inputEntry.getKey().equals(inputName)) {
                        for (Iterator<Map.Entry<String, OutputData>> it = inputEntry.getValue().entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry<String, OutputData> entry = it.next();
                            if (entry != null) {
                                if(entry.getKey().equals(outputName)) {
                                    it.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
        ObjectSerializer.writeSave();
    }

    private boolean outputExists(String machineName, String inputName, String outputName){
        return parentMap.get(machineName).get(inputName).containsKey(outputName);
    }

    private boolean anyOutputs(String machineName, String inputName){
        return !parentMap.get(machineName).get(inputName).isEmpty();
    }

    private OutputData getOutput(String machineName, String inputName, String outputName){return parentMap.get(machineName).get(inputName).get(outputName);}

    private HashMap<String,OutputData> getOutputs (String machineName, String inputName){return parentMap.get(machineName).get(inputName);}

    private void addOutput(String machineName, String inputName, OutputData outputData){
        parentMap.get(machineName).get(inputName).put(outputData.outputName, outputData);
        ObjectSerializer.writeSave();
    }
    // </editor-fold>


    // <editor-fold desc="Public Methods">
    //Composter
    public void composterInput (String inputName){ putInput("Composter", inputName); }
    public void composterRemove(String inputName){removeinput("Composter", inputName);}
    public boolean composterExists(String inputName){
        return exists("Composter", inputName);
    }
    public void listComposterInputs(EntityPlayer player){listInputs("Composter", player);}
    public void composterRemoveAll(){removeInputs("Composter");}
    public boolean composterOutputExists(String inputName, String outputName){return outputExists("Composter", inputName, outputName);}
    public void addComposterOutput(String inputName, OutputData outputData){addOutput("Composter", inputName, outputData);}
    public OutputData getComposterOutput(String inputName, String outputName){return getOutput("Composter", inputName, outputName);}
    public HashMap<String, OutputData> getComposterOutputs(String inputName){return getOutputs("Composter", inputName);}
    public boolean anyComposterOutputs(String inputName){return anyOutputs("Composter",inputName);}
    public void removeComposterOutput(String inputName, String outputName){removeOutput("Composter", inputName, outputName);}

    //Sieve
    public void sieveInput (String inputName){ putInput("Sieve", inputName); }
    public void sieveRemove(String inputName){removeinput("Sieve", inputName);}
    public boolean sieveExists(String inputName){
        return exists("Sieve", inputName);
    }
    public void listSieveInputs(EntityPlayer player){listInputs("Sieve", player);}
    public void sieveRemoveAll(){removeInputs("Sieve");}
    public boolean sieveOutputExists(String inputName, String outputName){return outputExists("Sieve", inputName, outputName);}
    public void addSieveOutput(String inputName, OutputData outputData){addOutput("Sieve", inputName, outputData);}
    public OutputData getSieveOutput(String inputName, String outputName){return getOutput("Sieve", inputName, outputName);}
    public HashMap<String, OutputData> getSieveOutputs(String inputName){return getOutputs("Sieve", inputName);}
    public boolean anySieveOutputs(String inputName){return anyOutputs("Sieve",inputName);}
    public void removeSieveOutput(String inputName, String outputName){removeOutput("Sieve", inputName, outputName);}

    //Squasher
    public void squasherInput (String inputName){ putInput("Squasher", inputName); }
    public void squasherRemove(String inputName){removeinput("Squasher", inputName);}
    public boolean squasherExists(String inputName){
        return exists("Squasher", inputName);
    }
    public void listSquasherInputs(EntityPlayer player){listInputs("Squasher", player);}
    public void squasherRemoveAll(){removeInputs("Squasher");}
    public boolean squasherOutputExists(String inputName, String outputName){return outputExists("Squasher", inputName, outputName);}
    public void addSquasherOutput(String inputName, OutputData outputData){addOutput("Squasher", inputName, outputData);}
    public OutputData getSquasherOutput(String inputName, String outputName){return getOutput("Squasher", inputName, outputName);}
    public HashMap<String, OutputData> getSquasherOutputs(String inputName){return getOutputs("Squasher", inputName);}
    public boolean anySquasherOutputs(String inputName){return anyOutputs("Squasher",inputName);}
    public void removeSquasherOutput(String inputName, String outputName){removeOutput("Squasher", inputName, outputName);}

    // </editor-fold>

}



