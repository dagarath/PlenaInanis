package com.dagarath.mods.plenainanis.client.gui;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-23.
 */
public class GuiPlenaComposter extends GuiContainer {

    private IInventory playerInv;
    private TilePlenaComposter tile;

    public GuiPlenaComposter(IInventory playerInv, TilePlenaComposter tile){
        super(new PlenaComposterContainer(playerInv, tile));
        this.playerInv = playerInv;
        this.tile = tile;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(new ResourceLocation(PlenaInanisReference.MODID + ":textures/gui/container/composter.png"));

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = this.tile.getCustomName();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);        //#404040
        this.fontRendererObj.drawString("Inventory", 8, 72, 4210752);                     //#404040

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPushMatrix();

        Tessellator tes = Tessellator.instance;
        GL11.glTranslatef(115, 73, 0);
        tes.startDrawingQuads();
        tes.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
        tes.addVertex(0, 0, 0);
        tes.addVertex(0, 6, 0);
        tes.addVertex(34, 6, 0);
        tes.addVertex(34, 0, 0);
        tes.draw();
        GL11.glPushMatrix();
        // System.out.println("Calculated Ratio: " +  ConfigurationHandler.compostCost.getInt());
        float progress = this.tile.getProgressScaled(100);
        progress = progress / 100;
        //PlenaInanis.logger.debug("Progess: "+ progress + "/1");
        tes.startDrawingQuads();
        tes.setColorRGBA_F(0.0F, 1.0F, 0.0F, 1.0F);
        tes.addVertex(0, 0, 0);
        tes.addVertex(0, 6, 0);
        tes.addVertex(34 * progress, 6, 0);
        tes.addVertex(34 * progress, 0, 0);
        tes.draw();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
