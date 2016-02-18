package com.dagarath.mods.plenainanis.common.registrars;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by dagarath on 2016-01-23.
 */
public class RecipeRegistrar {
    static ItemStack oakPlanks;
    static ItemStack oakSlabs;
    static ItemStack sprucePlanks;
    static ItemStack spruceSlabs;
    static ItemStack birchPlanks;
    static ItemStack birchSlabs;
    static ItemStack junglePlanks;
    static ItemStack jungleSlabs;
    static ItemStack acaciaPlanks;
    static ItemStack acaciaSlabs;
    static ItemStack darkOakPlanks;
    static ItemStack darkOakSlabs;
    static ItemStack string;
    static ItemStack cobbleStone;
    static ItemStack dirt;
    static ItemStack compost;
    static ItemStack stone;
    static ItemStack compostItem;
    static ItemStack furnace;
    static ItemStack glass;

    public static void init(){
        oakPlanks = new ItemStack(Blocks.planks, 0,0);
        oakSlabs = new ItemStack(Blocks.wooden_slab, 0, 0);
        sprucePlanks = new ItemStack(Blocks.planks, 0,1);
        spruceSlabs = new ItemStack(Blocks.wooden_slab, 0, 1);
        birchPlanks = new ItemStack(Blocks.planks, 0, 2);
        birchSlabs = new ItemStack(Blocks.wooden_slab, 0, 2);
        junglePlanks = new ItemStack(Blocks.planks, 0, 3);
        jungleSlabs = new ItemStack(Blocks.wooden_slab, 0, 3);
        acaciaPlanks = new ItemStack(Blocks.planks, 0, 4);
        acaciaSlabs = new ItemStack(Blocks.wooden_slab, 0, 4);
        darkOakPlanks = new ItemStack(Blocks.planks, 0, 5);
        darkOakSlabs = new ItemStack(Blocks.wooden_slab, 0, 5);
        string = new ItemStack(Items.string);
        cobbleStone = new ItemStack(Item.getItemFromBlock(Blocks.cobblestone));
        dirt = new ItemStack(Item.getItemFromBlock(Blocks.dirt));
        compost = new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockCompost));
        stone = new ItemStack(Item.getItemFromBlock(Blocks.stone));
        compostItem = new ItemStack(ItemRegistrar.itemCompost);
        furnace = new ItemStack(Item.getItemFromBlock(Blocks.furnace));
        glass = new ItemStack(Item.getItemFromBlock(Blocks.glass));

        registerComposters();
    }

    public static void registerComposters(){

        //Composters
        GameRegistry.addRecipe(new ItemStack(BlockRegistrar.composterOak), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', oakPlanks, 'B', oakSlabs});

        GameRegistry.addRecipe(new ItemStack(BlockRegistrar.composterSpruce), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', sprucePlanks, 'B', spruceSlabs});

        GameRegistry.addRecipe(new ItemStack(BlockRegistrar.composterBirch), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', birchPlanks, 'B', birchSlabs});

        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.composterJungle), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', junglePlanks, 'B', jungleSlabs});

        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.composterAcacia), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', acaciaPlanks, 'B',acaciaSlabs});

        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.composterDarkOak), new Object[]{
                "ABA",
                "ABA",
                "A A",
                'A', darkOakPlanks, 'B',darkOakSlabs});
        //Dirt
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.dirt), new Object[]{
                "AAA",
                "AAA",
                "AAA",
                'A', ItemRegistrar.itemCompost});
        //Sieve
        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.blockSieve), new Object[]{
                "ABA",
                "BBB",
                "ABA",
                'A', oakPlanks,  'B', string});
        //Compost Block
        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.blockCompost), new Object[]{
                "AA",
                "AA",
                'A', ItemRegistrar.itemCompost});
        //Rich Soil
        GameRegistry.addShapedRecipe(new ItemStack(BlockRegistrar.blockRichSoil), new Object[]{
                "ABA",
                "BAB",
                "ABA",
                'A', compost, 'B', dirt});
        //Block Smasher
        GameRegistry.addShapedRecipe(new ItemStack(ItemRegistrar.itemBlockSmasher), new Object[]{
                "AAA",
                " B ",
                " B ",
                'A', cobbleStone, 'B', oakPlanks});
        //Cobblestone
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.cobblestone), new Object[]{
                "AAA",
                "AAA",
                "AAA",
                'A', ItemRegistrar.itemPebble});
        //Block Squasher
        GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockSquasher)), new Object[]{
                "AAA",
                " A ",
                "AAA",
                'A', stone});
        //Fertilizer
        GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockSquasher)), new Object[]{
                "AAA",
                "ABA",
                "AAA",
                'A', compostItem, 'B', compost});
        //Crucible
        GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlockRegistrar.blockCrucible)), new Object[]{
                "AAA",
                "ABA",
                "AAA",
                'A', furnace, 'B', glass});
    }
}
