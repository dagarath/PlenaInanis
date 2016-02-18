package com.dagarath.mods.plenainanis.client.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-02-05.
 */
public class SmallButton extends GuiButton{
    int posX = 0;
    int posY = 0;
    ResourceLocation texture;

    public SmallButton(int id, int x, int y, ResourceLocation texture, int posX, int posY) {
        super(id, x, y, "");
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public void drawButton(Minecraft mc, int parX, int parY){
        if(visible){
            boolean isbuttonPressed = (parX >= xPosition
                    && parY >= yPosition
                    && parX < xPosition + width
                    && parY < yPosition + height);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(this.texture);
            int textureX = this.posX;
            int textureY = this.posY;

            if(isbuttonPressed){
                textureX += 8;
            }

            drawTexturedModalRect(xPosition, yPosition, textureX, textureY, 8, 8);
        }
    }
}
