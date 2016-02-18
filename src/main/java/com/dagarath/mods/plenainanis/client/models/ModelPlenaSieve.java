package com.dagarath.mods.plenainanis.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by dagarath on 2016-01-26.
 */
public class ModelPlenaSieve extends ModelBase {

    ModelRenderer Front;
    ModelRenderer Rear;
    ModelRenderer Right;
    ModelRenderer Left;
    ModelRenderer Sieve;


    public ModelPlenaSieve()
    {
        textureWidth = 64;
        textureHeight = 32;

        Front = new ModelRenderer(this, 0, 0);
        Front.addBox(-8F, -8F, 0F, 16, 8, 1);
        Front.setRotationPoint(0F, 24F, -8F);
        Front.setTextureSize(64, 32);
        Front.mirror = true;
        setRotation(Front, 0F, 0F, 0F);
        Rear = new ModelRenderer(this, 0, 0);
        Rear.addBox(-8F, 0F, 0F, 16, 8, 1);
        Rear.setRotationPoint(0F, 16F, 7F);
        Rear.setTextureSize(64, 32);
        Rear.mirror = true;
        setRotation(Rear, 0F, 0F, 0F);
        Right = new ModelRenderer(this, 34, 0);
        Right.addBox(-7F, -8F, 0F, 14, 8, 1);
        Right.setRotationPoint(-7F, 24F, 0F);
        Right.setTextureSize(64, 32);
        Right.mirror = true;
        setRotation(Right, 0F, -1.570796F, 0F);
        Left = new ModelRenderer(this, 34, 0);
        Left.addBox(-7F, -8F, 0F, 14, 8, 1);
        Left.setRotationPoint(7F, 24F, 0F);
        Left.setTextureSize(64, 32);
        Left.mirror = true;
        setRotation(Left, 0F, 1.570796F, 0F);
        Sieve = new ModelRenderer(this, 2, 17);
        Sieve.addBox(-7.5F, 0.5F, -7.5F, 15, 0, 15);
        Sieve.setRotationPoint(0F, 22F, 0F);
        Sieve.setTextureSize(64, 32);
        Sieve.mirror = true;
        setRotation(Sieve, 0F, 0F, 0F);

    }

    public void renderModel(float f1){
        Front.render(f1);
        Rear.render(f1);
        Right.render(f1);
        Left.render(f1);
        Sieve.render(f1);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Front.render(f5);
        Rear.render(f5);
        Right.render(f5);
        Left.render(f5);
        Sieve.render(f5);
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
