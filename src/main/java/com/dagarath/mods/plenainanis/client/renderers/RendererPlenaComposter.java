package com.dagarath.mods.plenainanis.client.renderers;

import com.dagarath.mods.plenainanis.client.models.ModelPlenaComposter;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-22.
 */
public class RendererPlenaComposter extends TileEntitySpecialRenderer {
    private ModelPlenaComposter model;

    private enum WoodType{
        OAK,
        SPRUCE,
        BIRCH,
        JUNGLE,
        ACACIA,
        DARK_OAK
    }

    public RendererPlenaComposter(){
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {
        renderModel((TilePlenaComposter) tile, x, y, z, scale);
    }

    public void renderModel(TileEntity tile, double d, double d1, double d2, float f){

        int angle = 0;
        if(tile != null && tile instanceof TilePlenaComposter){
            ForgeDirection direction = null;

            TilePlenaComposter te = (TilePlenaComposter) tile;
            int dir = te.getDirection();
            //System.out.println("Saved Direction: " + te.getDirection());
            //System.out.println("Direction: " + dir);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
            direction = ForgeDirection.getOrientation(dir);
            if(direction != null) {
                if (direction == ForgeDirection.NORTH) {
                    angle = 90;
                } else if (direction == ForgeDirection.EAST) {
                    angle = 0;

                } else if (direction == ForgeDirection.SOUTH) {
                    angle = -90;
                } else if (direction == ForgeDirection.WEST) {
                    angle = 180;
                }
            }
            ResourceLocation textures = new ResourceLocation("");
            GL11.glRotatef(angle, 0F, 1F, 0F);
            GL11.glRotatef(180, 0F, 0F, 1F);
            if(te.getType() == WoodType.OAK.ordinal()){
                this.model = new ModelPlenaComposter(0);
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter1.png"));
            }else if(te.getType() == WoodType.SPRUCE.ordinal()){
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter1.png"));
                this.model = new ModelPlenaComposter(64);
            }else if(te.getType() == WoodType.BIRCH.ordinal()) {
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter2.png"));
                this.model = new ModelPlenaComposter(0);
            }else if(te.getType() == WoodType.JUNGLE.ordinal()) {
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter3.png"));
                this.model = new ModelPlenaComposter(64);
            }else if(te.getType() == WoodType.ACACIA.ordinal()){
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter2.png"));
                this.model = new ModelPlenaComposter(64);
            }else if(te.getType() == WoodType.DARK_OAK.ordinal()){
                textures = (new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Composter3.png"));
                this.model = new ModelPlenaComposter(0);
            }
            Minecraft.getMinecraft().renderEngine.bindTexture(textures);

            if(te.isOpen){
                this.model.setOpen();
            }else{
                this.model.setClosed();
            }

            this.model.renderModel(0.0625F);
            GL11.glPopMatrix();
        }
    }

}
