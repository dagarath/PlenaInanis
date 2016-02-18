package com.dagarath.mods.plenainanis.client.gui;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.client.gui.buttons.LargeButton;
import com.dagarath.mods.plenainanis.client.gui.buttons.SmallButton;
import com.dagarath.mods.plenainanis.common.containers.PlenaCrucibleContainer;
import com.dagarath.mods.plenainanis.common.network.PacketHandler;
import com.dagarath.mods.plenainanis.common.network.Packets.PacketDumpCrucible;
import com.dagarath.mods.plenainanis.common.network.Packets.PacketPurgeCrucible;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dagarath on 2016-02-12.
 */
public class GuiPlenaCrucible extends GuiContainer {

    private TilePlenaCrucible tileCrucible;
    private double fluidAnimPosition = 0.0d;
    private PlenaCrucibleContainer container;
    private GuiButton purgeTemp;
    private GuiButton dumpLiquid;
    private ResourceLocation crucibleTexture = new ResourceLocation(PlenaInanisReference.MODID + ":textures/gui/container/crucible.png");

    public GuiPlenaCrucible(PlenaCrucibleContainer container, TilePlenaCrucible tile) {
        super(container);
        this.tileCrucible = tile;
        this.container = container;
    }

    @Override
    public void initGui(){
        super.initGui();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float zIndex, int x, int y) {
        this.mc.getTextureManager().bindTexture(crucibleTexture);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        GL11.glPushMatrix();
        this.mc.getTextureManager().bindTexture(crucibleTexture);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //Temperature Gauge
        GL11.glDisable(GL11.GL_BLEND);
        Tessellator tes = new Tessellator();
        if(this.tileCrucible.temperature > 0.000000000f) {
            GL11.glPushMatrix();
            double scale = 8000D;
            double i1 = this.tileCrucible.getTotalTemperatureScaled(scale);
            double uMin1 = 177f / 256f ;
            double vMin1 = 72f / 256f;
            double uMax1 = 195f / 256f;
            double vMax1 = 136f / 256f;

            double vSize1 = (i1 + 1) / scale;
            double vOffset1 = ((vMax1 - vMin1)* vSize1);
            double test = vMin1 +(vMax1 - vMin1)  * vSize1;
            double xOffset1 = ((double)this.guiLeft + 35D) / 18D;
            double yOffset1 = ((double)this.guiTop + 14D) / 64D;


            GL11.glScalef(18f, 64f, 0);
            tes.startDrawingQuads();
            tes.setTranslation(xOffset1, yOffset1, 0);
            tes.addVertexWithUV(1, 1, 0, uMax1, vMax1);
            tes.addVertexWithUV(1, 1 - vSize1, 0, uMax1, vMax1 - vOffset1);
            tes.addVertexWithUV(0, 1 - vSize1, 0, uMin1, vMax1 - vOffset1);
            tes.addVertexWithUV(0, 1, 0, uMin1, vMax1);
            tes.draw();
            GL11.glPopMatrix();
        }

//        this.drawTexturedModalRect(35, 14, 177 + 64 - i1, 72 - i1, 18, i1);



        if (this.tileCrucible.isBurning())
        {
            //Furnace Active Fire
            GL11.glPushMatrix();
            float i2 = this.tileCrucible.getBurnTimeRemainingScaled(13);
            double uMin2 = 177f / 256f ;
            double vMin2 = 58f / 256f;
            double uMax2 = 191f / 256f;
            double vMax2 = 71f / 256f;

            double vSize2 = (i2 + 1) / 14;
            double vOffset2 = vMin2 + ((vMax2 - vMin2) * vSize2);
            double xOffset2 = ((double)this.guiLeft + 9D) / 14D;
            double yOffset2 = ((double)this.guiTop + 18D) / 14D;


            GL11.glScalef(14f, 14f, 0);
            tes.startDrawingQuads();
            tes.setTranslation(xOffset2, yOffset2, 0);
            tes.addVertexWithUV(1, 1, 0, uMax2, vMax2);
            tes.addVertexWithUV(1, vSize2, 0, uMax2,  vOffset2);
            tes.addVertexWithUV(0, vSize2, 0, uMin2, vOffset2);
            tes.addVertexWithUV(0, 1, 0, uMin2, vMax2);
            tes.draw();
            GL11.glPopMatrix();
        }

        if(this.tileCrucible.crucibleCookTime > 0){

            //Crucible Active
            GL11.glPushMatrix();
            float i3 = this.tileCrucible.getCookProgressScaled(22);
            double uMin3 = 195f / 256f ;
            double vMin3 = 2f / 256f;
            double uMax3 = 213f / 256f;
            double vMax3 = 24f / 256f;

            double vSize3 = (i3 + 1) / 22D;
            double vOffset3 = vMax3 - ((vMax3 - vMin3) * vSize3);
            double xOffset3 = ((double)this.guiLeft + 76D) / 18D;
            double yOffset3 = ((double)this.guiTop + 46D) / 22D;


            GL11.glScalef(18f, 22f, 0);
            tes.startDrawingQuads();
            tes.setTranslation(xOffset3, yOffset3, 0);
            tes.addVertexWithUV(1, 1 - vSize3, 0, uMax3, vOffset3);
            tes.addVertexWithUV(1, 0, 0, uMax3,  vMin3);
            tes.addVertexWithUV(0, 0, 0, uMin3, vMin3);
            tes.addVertexWithUV(0, 1 - vSize3, 0, uMin3, vOffset3);
            tes.draw();
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        super.drawGuiContainerForegroundLayer(x, y);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        List hoverList1 = new ArrayList<>();
        hoverList1.add(EnumChatFormatting.ITALIC + "Temperature");
        if (this.tileCrucible.temperature < 100) {
            hoverList1.add(EnumChatFormatting.DARK_GREEN + "" + Math.round(this.tileCrucible.temperature) + " °C");
        } else if (this.tileCrucible.temperature >= 100 && this.tileCrucible.temperature < 2000) {
            hoverList1.add(EnumChatFormatting.GREEN + "" + Math.round(this.tileCrucible.temperature) + " °C");
        } else if (this.tileCrucible.temperature >= 2000 && this.tileCrucible.temperature < 3500) {
            hoverList1.add(EnumChatFormatting.YELLOW + "" + Math.round(this.tileCrucible.temperature) + " °C");
        } else if (this.tileCrucible.temperature >= 3500 && this.tileCrucible.temperature < 6500) {
            hoverList1.add(EnumChatFormatting.GOLD + "" + Math.round(this.tileCrucible.temperature) + " °C");
        } else if (this.tileCrucible.temperature >= 6500 && this.tileCrucible.temperature < 7400) {
            hoverList1.add(EnumChatFormatting.RED + "" + Math.round(this.tileCrucible.temperature) + " °C");
        } else if (this.tileCrucible.temperature >= 7400) {
            hoverList1.add(EnumChatFormatting.DARK_RED + "" + Math.round(this.tileCrucible.temperature) + " °C");
        }


        List hoverList2 = new ArrayList<>();
        FluidStack fluidCheck = this.tileCrucible.tank.getFluid();
        Tessellator tes = new Tessellator();
        if (mc.theWorld.getWorldTime() % 40 == 0) {
            if (fluidAnimPosition < 0.8D) {
                fluidAnimPosition += 0.2D;
            } else {
                fluidAnimPosition = 0.0D;
            }
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (fluidCheck != null) {
            if (fluidCheck.getFluid() == FluidRegistry.LAVA) {
                double d1 = this.tileCrucible.tank.getFluidAmount() / 135000D;
                //16, 55
                GL11.glPushMatrix();
                GL11.glTranslatef(142, 15, 0);
                GL11.glScalef(16f, 55f, 1f);
                hoverList2.add(EnumChatFormatting.RED + "Lava");
                String numFormatted = String.format("%,d",fluidCheck.amount);
                hoverList2.add("" + numFormatted + "mb / " + EnumChatFormatting.DARK_PURPLE + "135,000mb");
                this.mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/blocks/lava_still.png"));
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 1 - d1, 0, 0, fluidAnimPosition);
                tes.addVertexWithUV(0, 1, 0, 0, (fluidAnimPosition + (0.2 * d1)));
                tes.addVertexWithUV(1, 1, 0, 1, (fluidAnimPosition + (0.2 * d1)));
                tes.addVertexWithUV(1, 1 - d1, 0, 1, fluidAnimPosition);
                tes.draw();
                GL11.glTranslatef(0, 0, 0);
                GL11.glPopMatrix();

                //this.drawTexturedModalRect(142, 15, 0, 0, 16, 55);
            }
            if (fluidCheck.getFluid() == FluidRegistry.WATER) {
                double d1 = this.tileCrucible.tank.getFluidAmount() / 135000D;
                GL11.glPushMatrix();
                GL11.glTranslatef(142, 15, 0);
                GL11.glScalef(16f, 55f, 1f);
                hoverList2.add(EnumChatFormatting.BLUE + "Water");
                String numFormatted = String.format("%,d",fluidCheck.amount);
                hoverList2.add("" + numFormatted + "mb / " + EnumChatFormatting.DARK_PURPLE + "135,000mb");
                this.mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/blocks/water_still.png"));
                tes.startDrawingQuads();
                tes.addVertexWithUV(0, 1 - d1, 0, 0, fluidAnimPosition);
                tes.addVertexWithUV(0, 1, 0, 0, (fluidAnimPosition + (0.2 * d1)));
                tes.addVertexWithUV(1, 1, 0, 1, (fluidAnimPosition + (0.2 * d1)));
                tes.addVertexWithUV(1, 1 - d1, 0, 1, fluidAnimPosition);
                tes.draw();
                GL11.glTranslatef(0, 0, 0);
                GL11.glPopMatrix();

            }
        } else {
            hoverList2.add(EnumChatFormatting.ITALIC + "Empty");
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(PlenaInanisReference.MODID + ":textures/gui/container/crucible.png"));
        this.drawTexturedModalRect(142, 15, 177, 1, 16, 55);
        List hoverList3 = new ArrayList<>();
        hoverList3.add("Purge!");
        if(tileCrucible.purging()){
            if(mc.theWorld.getWorldTime() % 10 > 2  && mc.theWorld.getWorldTime() % 10 < 8) {
                this.drawTexturedModalRect(49, 13, 1, 167, 12, 12);


            }
            hoverList3.clear();
            hoverList3.add("Purging!");
        }
        List hoverList4 = new ArrayList<>();
        hoverList4.add("Dump!");
        if(tileCrucible.dumping()){
            if(mc.theWorld.getWorldTime() % 10 > 2  && mc.theWorld.getWorldTime() % 10 < 8) {
                this.drawTexturedModalRect(160, 15, 1, 168, 12, 12);
            }
            hoverList4.clear();
            hoverList4.add("Dumping!");
        }
        GL11.glDisable(GL11.GL_BLEND);
        //160, 14
        if(x > (k + 48) && x < (k + 61) && y > (l + 12) && y < (l + 25)){
            if(tileCrucible.temperature > 0) {
                this.drawHoveringText(hoverList3, x - k, y - l, this.fontRendererObj);
            }
        }else if(x > (k + 160) && x < (k + 172) && y > (l + 13) && y < (l + 26)){
            if(tileCrucible.containsFluid()) {
                this.drawHoveringText(hoverList4, x - k, y - l, this.fontRendererObj);
            }
        }else if (x > (k + 34) && x < (k + 54) && y > (l + 13) && y < (l + 79)){
            this.drawHoveringText(hoverList1, x - k, y - l, this.fontRendererObj);
        }else if(x > (k + 142) && x < (k + 158) && y > (l + 15) && y < (l + 70)){
            this.drawHoveringText(hoverList2, x - k, y - l, this.fontRendererObj);
        }

    }
    @Override
    public void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        if(x > this.guiLeft + 49 && x < this.guiLeft +  62 && y > this.guiTop + 13 && y < this.guiTop + 26){
            PacketHandler.INSTANCE.sendToServer(new PacketPurgeCrucible(PlenaInanis.proxy.getPlayer(), tileCrucible.xCoord, tileCrucible.yCoord, tileCrucible.zCoord));
        }
        if(x > this.guiLeft + 160 && x < this.guiLeft +  172 && y > this.guiTop + 14 && y < this.guiTop + 27){
            PacketHandler.INSTANCE.sendToServer(new PacketDumpCrucible(PlenaInanis.proxy.getPlayer(), tileCrucible.xCoord, tileCrucible.yCoord, tileCrucible.zCoord));
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

}
