package com.dagarath.mods.plenainanis.client.renderers.entities;

import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-29.
 */
public class RenderPebble extends Render {

    @Override
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float p_76986_9_) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        Tessellator tes = new Tessellator();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(-20, 0, 1, 0);
        tes.startDrawingQuads();
        tes.addVertexWithUV(0, 0, 0, 1, 1);
        tes.addVertexWithUV(0, 1, 0, 1, 0);
        tes.addVertexWithUV(1, 1, 0, 0, 0);
        tes.addVertexWithUV(1, 0, 0, 0, 1);
        tes.draw();
        tes.startDrawingQuads();
        tes.addVertexWithUV(1, 1, 0, 1, 1);
        tes.addVertexWithUV(0, 1, 0, 1, 0);
        tes.addVertexWithUV(0, 0, 0, 0, 0);
        tes.addVertexWithUV(1, 0, 0, 0, 1);
        tes.draw();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return new ResourceLocation(PlenaInanisReference.MODID + ":textures/items/pebble.png");
    }

}
