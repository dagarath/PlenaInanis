package com.dagarath.mods.plenainanis.client.renderers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.models.ModelPlenaCrucible;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-02-12.
 */
public class RendererPlenaCrucible extends TileEntitySpecialRenderer {
    private ModelPlenaCrucible model;

    private int uvAnimFrame = 0;

    public RendererPlenaCrucible(){

    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {
        renderModel((TilePlenaCrucible) tile, x, y, z, scale);
    }

    public void renderModel(TileEntity tile, double x, double y, double z, float scale){
        int angle = 0;
        if(tile != null && tile instanceof TilePlenaCrucible){
            ForgeDirection direction = null;
            this.model = new ModelPlenaCrucible();
            TilePlenaCrucible te = (TilePlenaCrucible) tile;
            int dir = te.getDirection();
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
            direction = ForgeDirection.getOrientation(dir);
            if(direction != null) {
                if (direction == ForgeDirection.NORTH) {
                    angle = 0;
                } else if (direction == ForgeDirection.EAST) {
                    angle = -90;

                } else if (direction == ForgeDirection.SOUTH) {

                    angle = 180;
                } else if (direction == ForgeDirection.WEST) {
                    angle = 90;
                }
            }

            ResourceLocation textures = new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Crucible.png");
            GL11.glRotatef(angle, 0F, 1F, 0F);
            GL11.glRotatef(180, 0F, 0F, 1F);
            if(!te.isOpen()){
                this.model.setDoorRotations(0f);

            }else if (te.isOpen()){
                this.model.setDoorRotations(1.56f);
            }
            Minecraft.getMinecraft().renderEngine.bindTexture(textures);
            this.model.renderModel(0.0625F);
            GL11.glPushMatrix();
            Tessellator tes = new Tessellator();
            double uMin = 0d;
            double uMax = 0d;
            double vMin = 0d;
            double vMax = 0d;

            if(!te.isOpen()) {
                GL11.glScalef(1.01f, 1.01f, 1.01f);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/blocks/glass_silver.png"));
                GL11.glTranslatef(-0.475f, -0.95f, -1.1f);
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 1, 0, 0, 0);
                tes.addVertexWithUV(1, 1, 0, 0, 1);
                tes.addVertexWithUV(1, 0, 0, 1, 1);
                tes.addVertexWithUV(0, 0, 0, 1, 0);
                tes.draw();
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.25f, 1.275f,-0.19f);
                Minecraft.getMinecraft().renderEngine.bindTexture(textures);
                if(te.temperature > 0){
                    if(te.getWorldObj().getWorldTime() % 20 == 0){
                        if(uvAnimFrame < 3){
                            uvAnimFrame++;
                        }else{
                            uvAnimFrame = 0;
                        }
                    }
                    vMin = 142d/256d;
                    vMax = 155d/256d;
                    if(uvAnimFrame == 0){
                        uMin = 1d/256d;
                        uMax = 26d/256d;
                    }else if(uvAnimFrame == 1){
                        uMin = 26d/256d;
                        uMax = 51d/256d;
                    }else if(uvAnimFrame == 2){
                        uMin = 51d/256d;
                        uMax = 76d/256d;
                    }else if(uvAnimFrame == 3){
                        uMin = 76d/256d;
                        uMax = 101d/256d;
                    }
                }else if (te.temperature == 0){
                    uMin = 12d/256d;
                    uMax = 37d/256d;
                    vMin = 70d/256d;
                    vMax = 83d/256d;
                }
                GL11.glTranslatef(-0.02f, -0.01f, 0f);
                GL11.glScalef(1.5625f, 0.8125f, 1f);
                tes.startDrawingQuads();
                tes.addVertexWithUV(1, 1, 0, uMax, vMax);
                tes.addVertexWithUV(1, 0, 0, uMax, vMin);
                tes.addVertexWithUV(0, 0, 0, uMin, vMin);
                tes.addVertexWithUV(0, 1, 0, uMin, vMax);
                tes.draw();
                GL11.glScalef(1f, 1f, 0.15f);
                GL11.glPushMatrix();
                double uMin1 = 1d/256d;
                double uMax1 = 2d/256d;
                double vMin1 = 142d/256d;
                double vMax1 = 155d/256d;
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 0, 0, uMin1, vMin1);
                tes.addVertexWithUV(0, 0, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(1f, 0, 0);
                tes.addVertexWithUV(0, 1, 1, uMin1, vMin1);
                tes.addVertexWithUV(0, 0, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 0, 0, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(0f, -1f, 0);
                tes.addVertexWithUV(1, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMin1);
                tes.addVertexWithUV(1, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(0f, 0f, 0);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMin1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMin1);
                tes.addVertexWithUV(1, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(1, 1, 0, uMin1, vMax1);
                tes.draw();
                GL11.glPopMatrix();
            }else{
                GL11.glScalef(1.01f, 1.01f, 1.01f);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/blocks/glass_silver.png"));
                GL11.glTranslatef(-1.45f, -0.925f, -2.575f);
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 0, 1, 0, 0);
                tes.addVertexWithUV(0, 1, 1, 0, 1);
                tes.addVertexWithUV(0, 1, 0, 1, 1);
                tes.addVertexWithUV(0, 0, 0, 1, 0);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(0.25f, 0f, 0f);
                tes.addVertexWithUV(0, 1, 0, 0, 0);
                tes.addVertexWithUV(0, 1, 1, 0, 1);
                tes.addVertexWithUV(0, 0, 1, 1, 1);
                tes.addVertexWithUV(0, 0, 0, 1, 0);
                tes.draw();
                GL11.glPushMatrix();
                GL11.glRotatef(90, 0f, 1f, 0f);
                Minecraft.getMinecraft().renderEngine.bindTexture(textures);
                if(te.temperature > 0){
                    if(te.getWorldObj().getWorldTime() % 20 == 0){
                        if(uvAnimFrame < 3){
                            uvAnimFrame++;
                        }else{
                            uvAnimFrame = 0;
                        }
                    }
                    vMin = 142d/256d;
                    vMax = 155d/256d;
                    if(uvAnimFrame == 0){
                        uMin = 1d/256d;
                        uMax = 26d/256d;
                    }else if(uvAnimFrame == 1){
                        uMin = 26d/256d;
                        uMax = 51d/256d;
                    }else if(uvAnimFrame == 2){
                        uMin = 51d/256d;
                        uMax = 76d/256d;
                    }else if(uvAnimFrame == 3){
                        uMin = 76d/256d;
                        uMax = 101d/256d;
                    }
                }else if (te.temperature == 0){
                    uMin = 12d/256d;
                    uMax = 37d/256d;
                    vMin = 70d/256d;
                    vMax = 83d/256d;
                }
                GL11.glTranslatef(-1.6575f, 1.235f, -0.17f);
                GL11.glScalef(1.5625f, 0.8125f, 1f);
                tes.startDrawingQuads();
                tes.addVertexWithUV(1, 1, 0, uMax, vMax);
                tes.addVertexWithUV(1, 0, 0, uMax, vMin);
                tes.addVertexWithUV(0, 0, 0, uMin, vMin);
                tes.addVertexWithUV(0, 1, 0, uMin, vMax);
                tes.draw();
                GL11.glScalef(1f, 1f, 0.15f);
                GL11.glPushMatrix();
                double uMin1 = 1d/256d;
                double uMax1 = 2d/256d;
                double vMin1 = 142d/256d;
                double vMax1 = 155d/256d;
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 0, 0, uMin1, vMin1);
                tes.addVertexWithUV(0, 0, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(1.25f, 0, 0);
                tes.addVertexWithUV(0, 1, 1, uMin1, vMin1);
                tes.addVertexWithUV(0, 0, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 0, 0, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(0.25f, -1f, 0);
                tes.addVertexWithUV(1, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMin1);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMin1);
                tes.addVertexWithUV(1, 1, 0, uMin1, vMax1);
                tes.draw();
                tes.startDrawingQuads();
                tes.setTranslation(0.25f, 0f, 0);
                tes.addVertexWithUV(0, 1, 0, uMin1, vMin1);
                tes.addVertexWithUV(0, 1, 1, uMax1, vMin1);
                tes.addVertexWithUV(1, 1, 1, uMax1, vMax1);
                tes.addVertexWithUV(1, 1, 0, uMin1, vMax1);
                tes.draw();
                GL11.glPopMatrix();
            }
            //25 x 13

//            PlenaInanis.logger.info(uvAnimFrame);

            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

}
