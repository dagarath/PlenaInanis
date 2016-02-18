package com.dagarath.mods.plenainanis.client.renderers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.models.ModelPlenaSieve;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaSieve;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaSieve;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-26.
 */
public class RendererPlenaSieve extends TileEntitySpecialRenderer {

    private ModelPlenaSieve model;

    public RendererPlenaSieve(){
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float scale) {
        renderModel((TilePlenaSieve) tile, x, y, z, scale);
    }

    public void renderModel(TileEntity tile, double x, double y, double z, float scale){

        int angle = 0;
        if(tile != null && tile instanceof TilePlenaSieve){
            ForgeDirection direction = null;
            this.model = new ModelPlenaSieve();
            TilePlenaSieve te = (TilePlenaSieve) tile;
            int dir = te.getDirection();
            //System.out.println("Saved Direction: " + te.getDirection());
            //System.out.println("Direction: " + dir);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
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
            ResourceLocation textures = new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Sieve.png");
            GL11.glRotatef(angle, 0F, 1F, 0F);
            GL11.glRotatef(180, 0F, 0F, 1F);

            Minecraft.getMinecraft().renderEngine.bindTexture(textures);
            this.model.renderModel(0.0625F);
            GL11.glPopMatrix();
            ItemStack inventory = te.getStackInSlot(0);
            if(inventory != null && inventory.getItem() != null) {
                Block processBlock = Block.getBlockFromItem(inventory.getItem());
                renderMaterialBlock(angle, x, y, z, te.getProgressScaled(), processBlock);
                //PlenaInanis.logger.info("Renderer Updated");
            }
        }
    }

    public void renderMaterialBlock(int angle, double x, double y, double z, double progress, Block block){
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
        IIcon blockIcon = block.getIcon(0, 0);

        double u0 = blockIcon.getInterpolatedU(0);
        double u1 = blockIcon.getInterpolatedU(16);
        double v0 = blockIcon.getInterpolatedV(0);
        double v1 = blockIcon.getInterpolatedV(16);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tes = new Tessellator();

        tes.startDrawingQuads();
        tes.setTranslation(-0.45, -1.05 - progress, -0.45);
        tes.addVertexWithUV(0.9, 0, 0.9, u1, v1);
        tes.addVertexWithUV(0.9, 0, 0, u1, v0);
        tes.addVertexWithUV(0, 0, 0, u0, v0);
        tes.addVertexWithUV(0, 0, 0.9, u0, v1);
        tes.draw();

        tes.startDrawingQuads();
        tes.setTranslation(-0.45, -1.35 , -0.45);
        tes.addVertexWithUV(0, 0, 0, u1, v1);
        tes.addVertexWithUV(0.9, 0, 0, u1, v0);
        tes.addVertexWithUV(0.9, 0, 0.9, u0, v0);
        tes.addVertexWithUV(0, 0, 0.9, u0, v1);
        tes.draw();

        GL11.glRotatef(angle, 0F, 1F, 0F);
        GL11.glRotatef(180, 0F, 0F, 1F);
        GL11.glPopMatrix();
    }
}
