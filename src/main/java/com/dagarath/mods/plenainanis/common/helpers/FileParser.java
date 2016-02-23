package com.dagarath.mods.plenainanis.common.helpers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dagarath on 2016-01-22.
 */
public class FileParser {

    static File file;
    public static List<String> fileInfo = new ArrayList<>();

    private final static String versionFile = "../Plena Inanis/build.properties";
    static File verFile = new File(versionFile);

    public static boolean exists(String fileName){
            file = new File(fileName);
        return file.exists();
    }

    public static List<String> readFileList(String fileName){
        try(FileInputStream fstream = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream))){
            fileInfo.clear();
            if (file.exists()) {
                String strLine;

                while((strLine = br.readLine()) != null){
                        fileInfo.add(strLine);
                }
                return fileInfo;
            }
        }catch(IOException  e){
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFileList(String fileName, List<String> fileInfo){
        try {
            file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                File folder = new File(fileName.substring(0, fileName.lastIndexOf("/")));
                folder.mkdirs();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(int i = 0; i < fileInfo.size(); i++){
                bw.write(fileInfo.get(i));
                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Integer> readStringIntHash(String fileName, HashMap<String, Integer> hashMap){
        try(FileInputStream fstream = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream))){
            file = new File(fileName);
            if (file.exists()) {
                String strLine;
                HashMap<String, Integer> returnHash = new HashMap<>();
                while((strLine = br.readLine()) != null){
                    String[] readString = strLine.split(",");
                    returnHash.put(readString[0], Integer.parseInt(readString[1]));
                }
                br.close();
                return returnHash;
            }
        }catch(IOException  e){
            //Cook Times
            if(fileName.equals(PlenaInanis.cookFileName)){
                PlenaInanis.crucibleCookTimes.put("minecraft:stone", 800);
                PlenaInanis.crucibleCookTimes.put("minecraft:netherrack", 600);
                PlenaInanis.crucibleCookTimes.put("minecraft:snow", 400);
                PlenaInanis.crucibleCookTimes.put("minecraft:cobblestone", 800);
                FileParser.writeStringIntHashFile(PlenaInanis.cookFileName, PlenaInanis.crucibleCookTimes);
            }
            //Burn Times
            if(fileName.equals(PlenaInanis.burnFileName)){
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.wooden_slab))),150);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.coal_block))),16000);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.stick)), 100);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.coal)), 1600);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.lava_bucket)), 20000);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.sapling))),100);
                PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.blaze_rod)), 2400);
                for(Iterator<Block> it = GameData.getBlockRegistry().iterator(); it.hasNext();){
                    Block block = it.next();
                    if(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(block))) != null && block.getMaterial() == Material.wood){
                        PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(block))), 300);
                    }
                }
                for(Iterator<Item> it = GameData.getItemRegistry().iterator(); it.hasNext();){
                    Item item = it.next();
                    if(InfoHelper.getFullNameForItemStack(new ItemStack(item)) != null) {
                        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 200);
                        }
                        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 200);
                        }
                        if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleBurnTimes.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 200);
                        }
                    }
                }
                FileParser.writeStringIntHashFile(PlenaInanis.burnFileName, PlenaInanis.crucibleBurnTimes);
            }

            //Temperature
            if(fileName.equals(PlenaInanis.temperatureFileName)){
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.wooden_slab))), 50);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.coal_block))), 6400);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.stick)), 34);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.coal)), 534);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.lava_bucket)), 8000);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(Blocks.sapling))), 34);
                PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Items.blaze_rod)), 800);

                for(Iterator<Block> it = GameData.getBlockRegistry().iterator(); it.hasNext();){
                    Block block = it.next();
                    if(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(block))) != null && block.getMaterial() == Material.wood){
                        PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(Item.getItemFromBlock(block))), 100);
                    }
                }
                for(Iterator<Item> it = GameData.getItemRegistry().iterator(); it.hasNext();){
                    Item item = it.next();
                    if(InfoHelper.getFullNameForItemStack(new ItemStack(item)) != null) {
                        if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 67);
                        }
                        if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 67);
                        }
                        if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) {
                            PlenaInanis.crucibleFuelTemps.put(InfoHelper.getFullNameForItemStack(new ItemStack(item)), 67);
                        }
                    }
                }
                FileParser.writeStringIntHashFile(PlenaInanis.temperatureFileName, PlenaInanis.crucibleFuelTemps);
            }

            PlenaInanis.logger.info("Configuration file " + fileName + " not found, writing new files");
        }
        return hashMap;
    }

    public static HashMap<String, String> readStringStringHash(String fileName, HashMap<String, String> hashMap){
        try(FileInputStream fstream = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream))){
            file = new File(fileName);
            if (file.exists()) {
                String strLine;
                HashMap<String, String> returnHash = new HashMap<>();
                while((strLine = br.readLine()) != null){
                    String[] readString = strLine.split(",");
                    returnHash.put(readString[0], readString[1]);
                }
                br.close();
                return returnHash;
            }
        }catch(IOException  e){
            //Cook Times
            if(fileName.equals(PlenaInanis.allowedItemsFileName)){
                PlenaInanis.crucibleAllowedItems.put("minecraft:stone", "lava");
                PlenaInanis.crucibleAllowedItems.put("minecraft:netherrack", "lava");
                PlenaInanis.crucibleAllowedItems.put("minecraft:snow", "water");
                PlenaInanis.crucibleAllowedItems.put("minecraft:cobblestone", "lava");
                FileParser.writeStringStringHashFile(PlenaInanis.allowedItemsFileName, PlenaInanis.crucibleAllowedItems);
            }
            PlenaInanis.logger.info("Configuration file " + fileName + " not found, writing new files");
        }
        return hashMap;
    }

    public static void writeStringIntHashFile(String fileName, HashMap<String, Integer>hashMap){
        try {
            file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                File folder = new File(fileName.substring(0, fileName.lastIndexOf("/")));
                folder.mkdirs();

                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Iterator<Map.Entry<String, Integer>> it = hashMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Integer> entry = it.next();
                if(entry != null){
                    bw.write(entry.getKey() + "," + entry.getValue());
                    bw.newLine();
                }
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeStringStringHashFile(String fileName, HashMap<String, String>hashMap){
        try {
            file = new File(fileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                File folder = new File(fileName.substring(0, fileName.lastIndexOf("/")));
                folder.mkdirs();
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Iterator<Map.Entry<String, String>> it = hashMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                if(entry != null){
                    bw.write(entry.getKey() + "," + entry.getValue());
                    bw.newLine();
                }
            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setVersion(){
        try {
            if (!verFile.exists()) {
                verFile.createNewFile();
            }

            FileWriter fw = new FileWriter(verFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            DateFormat dateFormat = new SimpleDateFormat("E d, M, y h:m:s:S");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();

            bw.write("#" + dateFormat.format(cal.getTime()));
            bw.newLine();
            String mcVer = PlenaInanisReference.MCVERSIONS.substring(1, 7);
            bw.write("minecraft_version=" + mcVer);
            bw.newLine();
            String forgeVer = PlenaInanisReference.DEPENDS.substring(22,PlenaInanisReference.DEPENDS.length() -2);
            bw.write("forge_version=" + forgeVer);
            bw.newLine();
            bw.write("mod_version=" + PlenaInanisReference.VERSION);
            bw.newLine();
            bw.write("dev_plena_src=/src/main/java/");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
