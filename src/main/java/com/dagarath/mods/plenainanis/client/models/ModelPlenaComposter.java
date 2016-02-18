package com.dagarath.mods.plenainanis.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by dagarath on 2016-01-22.
 */
public class ModelPlenaComposter extends ModelBase {
    ModelRenderer RearLeftLeg;
    ModelRenderer RearRightLeg;
    ModelRenderer FrontLeftLeg;
    ModelRenderer FrontRightLeg;
    ModelRenderer Interior;
    ModelRenderer LeftWall;
    ModelRenderer RightWall;
    ModelRenderer RearWall;
    ModelRenderer FrontWall;
    ModelRenderer Lid;

    protected static final double CYCLES_PER_BLOCK = 3.0D;
    protected float [] openCycle = new float[] {0F,45F,};
    protected float [] closeCycle = new float[] {-45F,0F};
    protected int cycleIndex = 0;

    public boolean isOpening = false;
    public boolean isOpen = false;
    public boolean isClosing = false;
    public boolean isClose = true;

    private double timeElasped = 0.0D;

    int uvSet = 0;

    public ModelPlenaComposter(int uv)
    {
        this.uvSet = uv;
        textureWidth = 128;
        textureHeight = 128;
        RearLeftLeg = new ModelRenderer(this, 0 + uvSet, 32);
        RearLeftLeg.addBox(0F, 0F, 0F, 1, 15, 1);
        RearLeftLeg.setRotationPoint(7F, 9F, 7F);
        RearLeftLeg.setTextureSize(textureWidth, textureHeight);
        RearLeftLeg.mirror = true;
        setRotation(RearLeftLeg, 0F, 0F, 0F);
        RearRightLeg = new ModelRenderer(this, 0 + uvSet, 32);
        RearRightLeg.addBox(0F, 0F, 0F, 1, 15, 1);
        RearRightLeg.setRotationPoint(7F, 9F, -8F);
        RearRightLeg.setTextureSize(textureWidth, textureHeight);
        RearRightLeg.mirror = true;
        setRotation(RearRightLeg, 0F, 0F, 0F);
        FrontLeftLeg = new ModelRenderer(this, uvSet, 32);
        FrontLeftLeg.addBox(0F, 0F, 0F, 1, 15, 1);
        FrontLeftLeg.setRotationPoint(-8F, 9F, 7F);
        FrontLeftLeg.setTextureSize(textureWidth, textureHeight);
        FrontLeftLeg.mirror = true;
        setRotation(FrontLeftLeg, 0F, 0F, 0F);
        FrontRightLeg = new ModelRenderer(this, uvSet, 32);
        FrontRightLeg.addBox(0F, 0F, 0F, 1, 15, 1);
        FrontRightLeg.setRotationPoint(-8F, 9F, -8F);
        FrontRightLeg.setTextureSize(textureWidth, textureHeight);
        FrontRightLeg.mirror = true;
        setRotation(FrontRightLeg, 0F, 0F, 0F);
        Interior = new ModelRenderer(this, 3, 0);
        Interior.addBox(0F, 0F, 0F, 14, 9, 14);
        Interior.setRotationPoint(-7F, 13F, -7F);
        Interior.setTextureSize(textureWidth, textureHeight);
        Interior.mirror = true;
        setRotation(Interior, 0F, 0F, 0F);
        LeftWall = new ModelRenderer(this, uvSet, 94);
        LeftWall.addBox(0F, 0F, 0F, 14, 13, 1);
        LeftWall.setRotationPoint(-7F, 10F, 7F);
        LeftWall.setTextureSize(textureWidth, textureHeight);
        LeftWall.mirror = true;
        setRotation(LeftWall, 0F, 0F, 0F);
        RightWall = new ModelRenderer(this, 30 + uvSet, 94);
        RightWall.addBox(0F, 0F, 0F, 14, 13, 1);
        RightWall.setRotationPoint(-7F, 10F, -8F);
        RightWall.setTextureSize(textureWidth, textureHeight);
        RightWall.mirror = true;
        setRotation(RightWall, 0F, 0F, 0F);
        RearWall = new ModelRenderer(this, 18 + uvSet, 23);
        RearWall.addBox(0F, 0F, 0F, 1, 14, 14);
        RearWall.setRotationPoint(7F, 9F, -7F);
        RearWall.setTextureSize(textureWidth, textureHeight);
        RearWall.mirror = true;
        setRotation(RearWall, 0F, 0F, 0F);
        FrontWall = new ModelRenderer(this, 16 + uvSet, 68);
        FrontWall.addBox(0F, 0F, 0F, 1, 12, 14);
        FrontWall.setRotationPoint(-8F, 11F, -7F);
        FrontWall.setTextureSize(textureWidth, textureHeight);
        FrontWall.mirror = true;
        setRotation(FrontWall, 0F, 0F, 0F);
        Lid = new ModelRenderer(this, uvSet, 51);
        Lid.addBox(0F, -1F, 0F, 16, 1, 16);
        Lid.setRotationPoint(8F, 8F, -8F);
        Lid.setTextureSize(128, 128);
        Lid.mirror = true;
        setRotation(Lid, 0F, 0F, -180F);
    }



    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {

        super.render(entity, f, f1, f2, f3, f4, f5);
        System.out.println("Float f:" + f);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        RearLeftLeg.render(f5);
        RearRightLeg.render(f5);
        FrontLeftLeg.render(f5);
        FrontRightLeg.render(f5);
        Interior.render(f5);
        LeftWall.render(f5);
        RightWall.render(f5);
        RearWall.render(f5);
        FrontWall.render(f5);
        Lid.render(f5);
    }

    public void renderModel(float f1){
        RearLeftLeg.render(f1);
        RearRightLeg.render(f1);
        FrontLeftLeg.render(f1);
        FrontRightLeg.render(f1);
        Interior.render(f1);
        LeftWall.render(f1);
        RightWall.render(f1);
        RearWall.render(f1);
        FrontWall.render(f1);
        Lid.render(f1);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setOpen(){
        Lid.rotateAngleZ = degToRad(-135f);
    }

    public void setClosed(){
        Lid.rotateAngleZ = degToRad(-180f);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, double time)
    {
        timeElasped = time - timeElasped;
        System.out.println("timeElapsed" + timeElasped);
        cycleIndex = (int) ((timeElasped * CYCLES_PER_BLOCK)%openCycle.length);
        System.out.println("Cycle Index: " + cycleIndex);
        Lid.rotateAngleY = degToRad(openCycle[cycleIndex]);
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }

    protected float degToRad(float degrees)
    {
        return degrees * (float)Math.PI / 180;
    }


    public void setUVWOffset(int offset){
            this.uvSet = offset;
        }

}
