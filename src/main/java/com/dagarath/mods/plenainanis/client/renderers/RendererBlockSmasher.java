package com.dagarath.mods.plenainanis.client.renderers;

import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-29.
 */
public class RendererBlockSmasher implements IItemRenderer {
    private IModelCustom blockSmasher;
    private ResourceLocation smasherTexture;

    public RendererBlockSmasher(){
        blockSmasher = AdvancedModelLoader.loadModel(new ResourceLocation(PlenaInanisReference.MODID +":models/blockSmasher.obj"));
        smasherTexture = new ResourceLocation(PlenaInanisReference.MODID +":textures/items/blockSmasher.png");
    }


    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch(type){
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        //if(type == ItemRenderType.INVENTORY){
        //    return helper == ItemRendererHelper.BLOCK_3D;
       // }
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch(type) {
            case ENTITY:
                GL11.glPushMatrix();
                GL11.glScalef(0.1f, 0.1f, 0.1f);
                GL11.glTranslatef(10f, -1f, 0f);
                GL11.glRotatef(90, 0f, 1f, 0f);
                GL11.glPushMatrix();
                GL11.glRotatef(-45f,1f,0f,0f);
                Minecraft.getMinecraft().renderEngine.bindTexture(smasherTexture);
                blockSmasher.renderAll();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                return;
            case EQUIPPED_FIRST_PERSON:
                GL11.glPushMatrix();
                GL11.glScalef(0.1f, 0.1f, 0.1f);
                GL11.glTranslatef(5f, -2f, -2f);
                GL11.glRotatef(90, 0f, 1f, 0f);
                GL11.glPushMatrix();
                GL11.glRotatef(25, 0f, 0f, 1f);
                Minecraft.getMinecraft().renderEngine.bindTexture(smasherTexture);
                blockSmasher.renderAll();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                return;
            case INVENTORY:
            case EQUIPPED:
                GL11.glPushMatrix();
                GL11.glScalef(0.07f, 0.07f, 0.07f);
                GL11.glTranslatef(14f, 1f, 0f);
                GL11.glRotatef(90, 0f, 1f, 0f);
                GL11.glPushMatrix();
                GL11.glRotatef(-45f,1f,0f,0f);
                Minecraft.getMinecraft().renderEngine.bindTexture(smasherTexture);
                blockSmasher.renderAll();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
        }


    }
}
