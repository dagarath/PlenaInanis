package com.dagarath.mods.plenainanis.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by dagarath on 2016-01-26.
 */
public class ModelPlenaSquasher extends ModelBase
{
    //fields
    ModelRenderer BaseBottom;
    ModelRenderer Base;
    ModelRenderer Pillar;
    ModelRenderer Pillar1;
    ModelRenderer Pedestal1;
    ModelRenderer Pedestal2;
    ModelRenderer PedestalTop;
    ModelRenderer BaseSideRight;
    ModelRenderer BaseSideLeft;
    ModelRenderer BaseSideRear;
    ModelRenderer BaseSideFront;

    public ModelPlenaSquasher()
    {
        textureWidth = 64;
        textureHeight = 64;

        BaseBottom = new ModelRenderer(this, 16, 50);
        BaseBottom.addBox(-6F, -1F, -6F, 12, 2, 12);
        BaseBottom.setRotationPoint(0F, 23F, 0F);
        BaseBottom.setTextureSize(64, 64);
        BaseBottom.mirror = true;
        setRotation(BaseBottom, 0F, 0F, 0F);
        Base = new ModelRenderer(this, 24, 38);
        Base.addBox(-5F, -1F, -5F, 10, 2, 10);
        Base.setRotationPoint(0F, 21F, 0F);
        Base.setTextureSize(64, 64);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Pillar = new ModelRenderer(this, 8, 34);
        Pillar.addBox(-1F, -6F, -1F, 2, 6, 2);
        Pillar.setRotationPoint(0F, 20F, 0F);
        Pillar.setTextureSize(64, 64);
        Pillar.mirror = true;
        setRotation(Pillar, 0F, 0F, 0F);
        Pillar1 = new ModelRenderer(this, 8, 34);
        Pillar1.addBox(-1F, -6F, -1F, 2, 6, 2);
        Pillar1.setRotationPoint(0F, 20F, 0F);
        Pillar1.setTextureSize(64, 64);
        Pillar1.mirror = true;
        setRotation(Pillar1, 0F, 0.7853982F, 0F);
        Pedestal1 = new ModelRenderer(this, 6, 30);
        Pedestal1.addBox(-1.5F, -2F, -1.5F, 3, 1, 3);
        Pedestal1.setRotationPoint(0F, 15F, 0F);
        Pedestal1.setTextureSize(64, 64);
        Pedestal1.mirror = true;
        setRotation(Pedestal1, 0F, 0F, 0F);
        Pedestal2 = new ModelRenderer(this, 0, 22);
        Pedestal2.addBox(-3F, -1F, -3F, 6, 2, 6);
        Pedestal2.setRotationPoint(0F, 12F, 0F);
        Pedestal2.setTextureSize(64, 64);
        Pedestal2.mirror = true;
        setRotation(Pedestal2, 0F, 0F, 0F);
        PedestalTop = new ModelRenderer(this, 0, 0);
        PedestalTop.addBox(-8F, -4F, -8F, 16, 4, 16);
        PedestalTop.setRotationPoint(0F, 11F, 0F);
        PedestalTop.setTextureSize(64, 64);
        PedestalTop.mirror = true;
        setRotation(PedestalTop, 0F, 0F, 0F);
        BaseSideRight = new ModelRenderer(this, 52, 32);
        BaseSideRight.addBox(0F, -3F, -1.5F, 3, 3, 3);
        BaseSideRight.setRotationPoint(-8F, 24F, 0F);
        BaseSideRight.setTextureSize(64, 64);
        BaseSideRight.mirror = true;
        setRotation(BaseSideRight, 0F, 0F, 0F);
        BaseSideLeft = new ModelRenderer(this, 52, 32);
        BaseSideLeft.addBox(-3F, -3F, -1.5F, 3, 3, 3);
        BaseSideLeft.setRotationPoint(8F, 24F, 0F);
        BaseSideLeft.setTextureSize(64, 64);
        BaseSideLeft.mirror = true;
        setRotation(BaseSideLeft, 0F, 0F, 0F);
        BaseSideRear = new ModelRenderer(this, 52, 32);
        BaseSideRear.addBox(-1.5F, -3F, -3F, 3, 3, 3);
        BaseSideRear.setRotationPoint(0F, 24F, 8F);
        BaseSideRear.setTextureSize(64, 64);
        BaseSideRear.mirror = true;
        setRotation(BaseSideRear, 0F, 0F, 0F);
        BaseSideFront = new ModelRenderer(this, 52, 32);
        BaseSideFront.addBox(-1.5F, -3F, 0F, 3, 3, 3);
        BaseSideFront.setRotationPoint(0F, 24F, -8F);
        BaseSideFront.setTextureSize(64, 64);
        BaseSideFront.mirror = true;
        setRotation(BaseSideFront, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        BaseBottom.render(f5);
        Base.render(f5);
        Pillar.render(f5);
        Pillar1.render(f5);
        Pedestal1.render(f5);
        Pedestal2.render(f5);
        PedestalTop.render(f5);
        BaseSideRight.render(f5);
        BaseSideLeft.render(f5);
        BaseSideRear.render(f5);
        BaseSideFront.render(f5);
    }

    public void renderModel(float f1){
        BaseBottom.render(f1);
        Base.render(f1);
        Pillar.render(f1);
        Pillar1.render(f1);
        Pedestal1.render(f1);
        Pedestal2.render(f1);
        PedestalTop.render(f1);
        BaseSideRight.render(f1);
        BaseSideLeft.render(f1);
        BaseSideRear.render(f1);
        BaseSideFront.render(f1);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

}
