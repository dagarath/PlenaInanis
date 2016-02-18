package com.dagarath.mods.plenainanis.common.entities;

import com.dagarath.mods.plenainanis.common.registrars.ItemRegistrar;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Created by dagarath on 2016-01-29.
 */
public class EntityPebble extends EntityThrowable {


    public EntityPebble(World world, EntityPlayer player) {

        super(world, player);

    }

    public EntityPebble(World world, double x, double y, double z)
    {
        super(world, x, y, z);

    }

    public EntityPebble(World world)
    {
        super(world);
        this.setSize(0.25F, 0.25F);
    }


    @Override
    protected void onImpact(MovingObjectPosition position) {
        if(!this.worldObj.isRemote) {
            if (position.entityHit != null) {
                byte b0 = 2;

                position.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) b0);
            }

            int chanceToBreak = (int) (Math.random() * 100) + 1;



            if ((chanceToBreak > 20 && chanceToBreak < 85)) {
                this.setDead();
            } else {
                ItemStack itemToDrop = new ItemStack(ItemRegistrar.itemPebble);
                itemToDrop.stackSize = 1;
                EntityItem droppedItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, itemToDrop);
                droppedItem.setVelocity(0, 0, 0);
                this.setDead();
                this.worldObj.spawnEntityInWorld(droppedItem);
            }
        }else{
            for (int i = 0; i < 8; ++i) {
                float positionMod = 0.75f;
                if (((Math.random() * 20) + 1) < 10) {
                    positionMod *= -1;
                }
                this.worldObj.spawnParticle("crit", this.posX + positionMod, this.posY + positionMod, this.posZ + positionMod, 0.0D, 0.0D, 0.0D);
            }
        }
    }

}
