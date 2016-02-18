package com.dagarath.mods.plenainanis.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by dagarath on 2016-02-12.
 */
public class ModelPlenaCrucible extends ModelBase {
    //fields
    ModelRenderer Base;
    ModelRenderer BackWall;
    ModelRenderer LeftWall;
    ModelRenderer RightWall;
    ModelRenderer FrontWallTop;
    ModelRenderer FrontWallRight;
    ModelRenderer FrontWallLeft;
    ModelRenderer FrontWallGlass;
    ModelRenderer Roof;
    ModelRenderer FrontWallBase;
    ModelRenderer DoorLatch;
    ModelRenderer LockBase;
    ModelRenderer LockTop;
    ModelRenderer LockBottom;
    ModelRenderer TopHinge;
    ModelRenderer BottomHinge;

    public ModelPlenaCrucible()
    {
        textureWidth = 256;
        textureHeight = 256;

        Base = new ModelRenderer(this, 0, 200);
        Base.addBox(-19F, -1F, -18F, 39, 1, 34);
        Base.setRotationPoint(0F, 24F, 4F);
        Base.setTextureSize(256, 256);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        BackWall = new ModelRenderer(this, 0, 90);
        BackWall.addBox(-19F, -46F, -3F, 39, 46, 5);
        BackWall.setRotationPoint(0F, 23F, 18F);
        BackWall.setTextureSize(256, 256);
        BackWall.mirror = true;
        setRotation(BackWall, 0F, 0F, 0F);
        LeftWall = new ModelRenderer(this, 110, 78);
        LeftWall.addBox(-3F, -46F, -14F, 5, 46, 29);
        LeftWall.setRotationPoint(18F, 23F, 0F);
        LeftWall.setTextureSize(256, 256);
        LeftWall.mirror = true;
        setRotation(LeftWall, 0F, 0F, 0F);
        RightWall = new ModelRenderer(this, 110, 0);
        RightWall.addBox(-1F, -46F, -14F, 4, 46, 29);
        RightWall.setRotationPoint(-18F, 23F, 0F);
        RightWall.setTextureSize(256, 256);
        RightWall.mirror = true;
        setRotation(RightWall, 0F, 0F, 0F);
        FrontWallTop = new ModelRenderer(this, 0, 0);
        FrontWallTop.addBox(0F, -48F, -5F, 39, 10, 5);
        FrontWallTop.setRotationPoint(-19F, 24F, -14F);
        FrontWallTop.setTextureSize(256, 256);
        FrontWallTop.mirror = true;
        setRotation(FrontWallTop, 0F, 0F, 0F);
        FrontWallRight = new ModelRenderer(this, 0, 16);
        FrontWallRight.addBox(0F, -38F, -5F, 12, 14, 5);
        FrontWallRight.setRotationPoint(-19F, 24F, -14F);
        FrontWallRight.setTextureSize(256, 256);
        FrontWallRight.mirror = true;
        setRotation(FrontWallRight, 0F, 0F, 0F);
        FrontWallLeft = new ModelRenderer(this, 54, 16);
        FrontWallLeft.addBox(27F, -38F, -5F, 12, 14, 5);
        FrontWallLeft.setRotationPoint(-19F, 24F, -14F);
        FrontWallLeft.setTextureSize(256, 256);
        FrontWallLeft.mirror = true;
        setRotation(FrontWallLeft, 0F, 0F, 0F);
        FrontWallGlass = new ModelRenderer(this, 25, 38);
        FrontWallGlass.addBox(12F, -38F, -4F, 15, 14, 4);
        FrontWallGlass.setRotationPoint(-19F, 24F, -14F);
        FrontWallGlass.setTextureSize(256, 256);
        FrontWallGlass.mirror = true;
        setRotation(FrontWallGlass, 0F, 0F, 0F);
        Roof = new ModelRenderer(this, 0, 164);
        Roof.addBox(-19F, -1F, -19F, 39, 1, 34);
        Roof.setRotationPoint(0F, -23F, 5F);
        Roof.setTextureSize(256, 256);
        Roof.mirror = true;
        setRotation(Roof, 0F, 0F, 0F);
        FrontWallBase = new ModelRenderer(this, 0, 60);
        FrontWallBase.addBox(0F, -24F, -5F, 39, 24, 5);
        FrontWallBase.setRotationPoint(-19F, 24F, -14F);
        FrontWallBase.setTextureSize(256, 256);
        FrontWallBase.mirror = true;
        setRotation(FrontWallBase, 0F, 0F, 0F);
        DoorLatch = new ModelRenderer(this, 89, 0);
        DoorLatch.addBox(36F, -40.66667F, -6F, 4, 2, 1);
        DoorLatch.setRotationPoint(-19F, 24F, -14F);
        DoorLatch.setTextureSize(256, 256);
        DoorLatch.mirror = true;
        setRotation(DoorLatch, 0F, 0F, 0F);
        LockBase = new ModelRenderer(this, 94, 13);
        LockBase.addBox(0F, -3F, -6F, 1, 5, 6);
        LockBase.setRotationPoint(20F, -15F, -11F);
        LockBase.setTextureSize(256, 256);
        LockBase.mirror = true;
        setRotation(LockBase, 0F, 0F, 0F);
        LockTop = new ModelRenderer(this, 100, 0);
        LockTop.addBox(0F, -1F, -3F, 1, 1, 3);
        LockTop.setRotationPoint(20F, -17F, -17F);
        LockTop.setTextureSize(256, 256);
        LockTop.mirror = true;
        setRotation(LockTop, 0F, 0F, 0F);
        LockBottom = new ModelRenderer(this, 100, 5);
        LockBottom.addBox(0F, -1F, -3F, 1, 1, 3);
        LockBottom.setRotationPoint(20F, -13F, -17F);
        LockBottom.setTextureSize(256, 256);
        LockBottom.mirror = true;
        setRotation(LockBottom, 0F, 0F, 0F);
        TopHinge = new ModelRenderer(this, 0, 36);
        TopHinge.addBox(0F, -1F, -1F, 1, 7, 1);
        TopHinge.setRotationPoint(-20F, -19F, -13F);
        TopHinge.setTextureSize(256, 256);
        TopHinge.mirror = true;
        setRotation(TopHinge, 0F, 0F, 0F);
        BottomHinge = new ModelRenderer(this, 0, 36);
        BottomHinge.addBox(0F, 0F, 0F, 1, 7, 1);
        BottomHinge.setRotationPoint(-20F, 12F, -14F);
        BottomHinge.setTextureSize(256, 256);
        BottomHinge.mirror = true;
        setRotation(BottomHinge, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base.render(f5);
        BackWall.render(f5);
        LeftWall.render(f5);
        RightWall.render(f5);
        FrontWallTop.render(f5);
        FrontWallRight.render(f5);
        FrontWallLeft.render(f5);
        FrontWallGlass.render(f5);
        Roof.render(f5);
        FrontWallBase.render(f5);
        DoorLatch.render(f5);
        LockBase.render(f5);
        LockTop.render(f5);
        LockBottom.render(f5);
        TopHinge.render(f5);
        BottomHinge.render(f5);
    }

    public void renderModel(float f1){
        Base.render(f1);
        BackWall.render(f1);
        LeftWall.render(f1);
        RightWall.render(f1);
        FrontWallTop.render(f1);
        FrontWallRight.render(f1);
        FrontWallLeft.render(f1);
//        FrontWallGlass.render(f1);
        Roof.render(f1);
        FrontWallBase.render(f1);
        DoorLatch.render(f1);
        LockBase.render(f1);
        LockTop.render(f1);
        LockBottom.render(f1);
        TopHinge.render(f1);
        BottomHinge.render(f1);
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

    public void setDoorRotations(float f){
        setRotation(FrontWallTop, 0, 0, 0);
        setRotation(FrontWallRight, 0, 0, 0);
        setRotation(FrontWallLeft, 0, 0, 0);
        setRotation(FrontWallBase, 0, 0, 0);
        setRotation(DoorLatch, 0, 0, 0);
        setRotation(FrontWallTop, 0, f, 0);
        setRotation(FrontWallRight, 0, f, 0);
        setRotation(FrontWallLeft, 0, f, 0);
        setRotation(FrontWallBase, 0, f, 0);
        setRotation(DoorLatch, 0, f, 0);
    }
}
