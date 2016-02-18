package com.dagarath.mods.plenainanis.client.renderers.itemblocks;

import com.dagarath.mods.plenainanis.client.models.ModelPlenaComposter;
import com.dagarath.mods.plenainanis.common.blocks.BlockPlenaComposter;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by dagarath on 2016-01-22.
 */
public class RenderComposterItem implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        int type = 0;

        if(block instanceof BlockPlenaComposter){
            BlockPlenaComposter composter = (BlockPlenaComposter)block;
            type = composter.type;
        }

        TilePlenaComposter composter = new TilePlenaComposter();
        composter.setType(type);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(composter, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }


    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return -1;
    }
}
