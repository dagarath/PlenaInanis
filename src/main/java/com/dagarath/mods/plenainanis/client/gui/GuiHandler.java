package com.dagarath.mods.plenainanis.client.gui;

import com.dagarath.mods.plenainanis.client.inventory.InventoryRecipes;
import com.dagarath.mods.plenainanis.common.containers.PlenaComposterContainer;
import com.dagarath.mods.plenainanis.common.containers.PlenaCrucibleContainer;
import com.dagarath.mods.plenainanis.common.containers.PlenaRecipeManagerContainer;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaComposter;
import com.dagarath.mods.plenainanis.common.tileentitites.TilePlenaCrucible;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

/**
 * Created by dagarath on 2016-01-23.
 */
public class GuiHandler implements IGuiHandler {

    public static final int COMPOST_GUI = 0;
    public static final int RECIPE_MAKER_GUI = 1;
    public static final int CRUCIBLE_GUI = 2;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == COMPOST_GUI)
        {
            return new PlenaComposterContainer(player.inventory, (TilePlenaComposter) world.getTileEntity(x, y, z));
        }
        if(ID == CRUCIBLE_GUI){
            return new PlenaCrucibleContainer(player.inventory, (TilePlenaCrucible) world.getTileEntity(x,y,z));
        }
        if(ID == RECIPE_MAKER_GUI)
        {
            return new PlenaRecipeManagerContainer(player.inventory);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == COMPOST_GUI)
        {
            return new GuiPlenaComposter(new PlenaComposterContainer(player.inventory, (TilePlenaComposter) world.getTileEntity(x, y, z)),(TilePlenaComposter) world.getTileEntity(x, y, z));
        }
        if(ID == CRUCIBLE_GUI){
            return new GuiPlenaCrucible(new PlenaCrucibleContainer(player.inventory, (TilePlenaCrucible)world.getTileEntity(x, y, z)), (TilePlenaCrucible) world.getTileEntity(x, y, z));
        }
        if(ID == RECIPE_MAKER_GUI)
        {
            return new GuiRecipeManager(new PlenaRecipeManagerContainer(player.inventory));
        }

        return null;
    }

}
