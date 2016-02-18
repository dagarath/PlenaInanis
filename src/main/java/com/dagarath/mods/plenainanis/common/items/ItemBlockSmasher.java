package com.dagarath.mods.plenainanis.common.items;

import com.dagarath.mods.plenainanis.PlenaInanis;
import com.dagarath.mods.plenainanis.config.PlenaInanisReference;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;

/**
 * Created by dagarath on 2016-01-29.
 */
public class ItemBlockSmasher extends ItemSword {
    private float damageModifier;
    public ItemBlockSmasher() {
        super(ToolMaterial.WOOD);
        this.setTextureName(PlenaInanisReference.MODID +":smasher");
        this.setFull3D();
        this.setUnlocalizedName("smasher");
        this.setCreativeTab(PlenaInanis.tabPlenaInanis);
        this.damageModifier = 1f;
    }

    public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.damageModifier, 0));
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLiving, EntityLivingBase entityLiving1)
    {
        double d = entityLiving1.posX - entityLiving.posX;
        double d2 = entityLiving1.posZ - entityLiving.posZ;

        int facing = MathHelper.floor_double((double) ((entityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        //entityLiving.rotationYaw = facing;
        entityLiving.addVelocity(-d * 0.5, 0.25, -d2 * 0.5);
        entityLiving1.worldObj.playSoundAtEntity(entityLiving, PlenaInanisReference.MODID + ":thwap", 1f, 0.5f);
        return true;
    }


}
