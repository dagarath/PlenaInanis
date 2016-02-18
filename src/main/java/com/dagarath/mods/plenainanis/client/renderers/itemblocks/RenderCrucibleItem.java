package com.dagarath.mods.plenainanis.client.renderers.itemblocks;

import com.dagarath.mods.plenainanis.client.models.ModelPlenaCrucible;
import com.dagarath.mods.plenainanis.client.models.ModelPlenaSieve;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-02-12.
 */
public class RenderCrucibleItem implements IItemRenderer{

    private ModelPlenaCrucible model;

    public RenderCrucibleItem()
    {
        this.model = new ModelPlenaCrucible();
    }

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
    {
        switch (type)
        {
            case ENTITY:
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                break;
            case FIRST_PERSON_MAP:
                return false;
            case INVENTORY:
                break;
        }
        return true;
    }


    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
    {
        switch (type)
        {
            case ENTITY:
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                break;
            case FIRST_PERSON_MAP:
                return false;
            case INVENTORY:
                break;
        }

        switch (helper)
        {
            case BLOCK_3D:
                break;
            case ENTITY_BOBBING:
                break;
            case ENTITY_ROTATION:
                break;
            case EQUIPPED_BLOCK:
                break;
            case INVENTORY_BLOCK:
                break;
            default:
                break;

        }
        return true;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
    {
        renderSieve(type, item);
    }

    private void renderSieve(IItemRenderer.ItemRenderType type, ItemStack item)
    {
        GL11.glPushMatrix();
        GL11.glScalef(0.35F, 0.35F, 0.35F);
        GL11.glRotatef(180, 1F, 0F, 0F);

        switch (type)
        {
            case EQUIPPED:
                GL11.glTranslatef(-0.5F, -1.5F, 0.5F);
                break;

            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0F, -1.6F, 0.6F);
                break;

            case ENTITY:
                GL11.glTranslatef(0F, -1.0F, 0F);
                break;

            case INVENTORY:
                GL11.glTranslatef(0F, 0F, 0F);
                break;

            default:
                GL11.glTranslatef(0F, 0F, 0F);
                break;
        }
        ResourceLocation textures = new ResourceLocation(PlenaInanisReference.MODID + ":textures/blocks/Crucible.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(textures);
        model.renderModel(0.0625F);

        GL11.glPopMatrix();

    }
}
