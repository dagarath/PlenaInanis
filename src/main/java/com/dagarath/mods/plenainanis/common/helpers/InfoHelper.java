package com.dagarath.mods.plenainanis.common.helpers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by dagarath on 2016-02-02.
 */
public class InfoHelper {

    public static String getModidForBlock(Block block) {
        String nameForObject = GameData.getBlockRegistry().getNameForObject(block);
        if (nameForObject == null) {
            return "?";
        }
        String[] lst = StringUtils.split(nameForObject, ":");
        if (lst.length >= 2) {
            return lst[0];
        } else {
            return "?";
        }
    }

    public static String getFullNameForBlock(Block block){
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    public static String getFullNameForItem(Item item){
        return GameData.getItemRegistry().getNameForObject(item);
    }

    public static String getModidForItem(Item item) {
        String nameForObject = GameData.getItemRegistry().getNameForObject(item);
        if (nameForObject == null) {
            return "?";
        }
        String[] lst = StringUtils.split(nameForObject, ":");
        if (lst.length >= 2) {
            return lst[0];
        } else {
            return "?";
        }
    }

    public static String getFullNameForItemStack(ItemStack itemStack){
//        PlenaInanis.logger.info("Itemstack: " + itemStack);
        if(itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                return getFullNameForBlock(Block.getBlockFromItem(itemStack.getItem()));
            } else {
                return getFullNameForItem(itemStack.getItem());
            }
        }
        return "";
    }

    public static ItemStack getStackWithNullCheck(String stackName){

        Class gameDataClass = GameData.class;
        try{
            ItemStack inputStack = null;
            Method method = gameDataClass.getDeclaredMethod("getMain");
            method.setAccessible(true);
            GameData gamedata = (GameData)method.invoke(null);
            FMLControlledNamespacedRegistry<Block> b = gamedata.getBlockRegistry();
            FMLControlledNamespacedRegistry<Item> i = gamedata.getItemRegistry();
            Block found = b.getObject(stackName);
            Item foundItem = i.getObject(stackName);
            if(found != Blocks.air){
                inputStack = new ItemStack(Item.getItemFromBlock(GameData.getBlockRegistry().getObject(stackName)));
            }
            if(foundItem != null){
                inputStack = new ItemStack(GameData.getItemRegistry().getObject(stackName));
            }
            return inputStack;
        }catch(Exception e){
            PlenaInanis.logger.error(e);
        }
        return null;
    }

}
