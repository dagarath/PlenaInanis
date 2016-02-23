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

    private TilePlenaComposter tileComposter;
    private PlenaComposterContainer container;

    public GuiPlenaComposter(PlenaComposterContainer container, TilePlenaComposter tile){
        super(container);
        this.container = container;
        this.tileComposter = tile;
        this.xSize = 241;
        this.ySize = 166;
    }

    @Override
    public void initGui(){
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(new ResourceLocation(PlenaInanisReference.MODID + ":textures/gui/container/composter.png"));

        this.drawTexturedModalRect(this.guiLeft + 31, this.guiTop, 0, 0, 176, this.ySize);

        this.drawTexturedModalRect(this.guiLeft + 209, this.guiTop + 10, 1, 168, 100, 47);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        String s = this.tileComposter.getCustomName();
        this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2  + 31, 6, 4210752);        //#404040
        this.fontRendererObj.drawString("Inventory", 8 + 31, 72, 4210752);                     //#404040
        this.fontRendererObj.drawStringWithShadow("Humidity", 237, 19, 0xEEEEEE);
        this.fontRendererObj.drawStringWithShadow("Airflow", 237, 40, 0xEEEEEE);
        String humidity = "" + (this.tileComposter.getMoisture() * 10);
        String airflow = "" + (this.tileComposter.getAirflow() * 10);
        this.fontRendererObj.drawStringWithShadow(humidity + "%", 284, 19, 0xFFFF00);
        this.fontRendererObj.drawStringWithShadow(airflow + "%", 284, 40, 0xFFFF00);
    }
}
