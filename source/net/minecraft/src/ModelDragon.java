package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelDragon extends ModelBase {
	private ModelRenderer head;
	private ModelRenderer neck;
	private ModelRenderer jaw;
	private ModelRenderer body;
	private ModelRenderer rearLeg;
	private ModelRenderer frontLeg;
	private ModelRenderer rearLegTip;
	private ModelRenderer frontLegTip;
	private ModelRenderer rearFoot;
	private ModelRenderer frontFoot;
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	private float field_40317_s;

	public ModelDragon(float var1) {
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.setTextureOffset("body.body", 0, 0);
		this.setTextureOffset("wing.skin", -56, 88);
		this.setTextureOffset("wingtip.skin", -56, 144);
		this.setTextureOffset("rearleg.main", 0, 0);
		this.setTextureOffset("rearfoot.main", 112, 0);
		this.setTextureOffset("rearlegtip.main", 196, 0);
		this.setTextureOffset("head.upperhead", 112, 30);
		this.setTextureOffset("wing.bone", 112, 88);
		this.setTextureOffset("head.upperlip", 176, 44);
		this.setTextureOffset("jaw.jaw", 176, 65);
		this.setTextureOffset("frontleg.main", 112, 104);
		this.setTextureOffset("wingtip.bone", 112, 136);
		this.setTextureOffset("frontfoot.main", 144, 104);
		this.setTextureOffset("neck.box", 192, 104);
		this.setTextureOffset("frontlegtip.main", 226, 138);
		this.setTextureOffset("body.scale", 220, 53);
		this.setTextureOffset("head.scale", 0, 0);
		this.setTextureOffset("neck.scale", 48, 0);
		this.setTextureOffset("head.nostril", 112, 0);
		float var2 = -16.0F;
		this.head = new ModelRenderer(this, "head");
		this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + var2, 12, 5, 16);
		this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + var2, 16, 16, 16);
		this.head.mirror = true;
		this.head.addBox("scale", -5.0F, -12.0F, 12.0F + var2, 2, 4, 6);
		this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + var2, 2, 2, 4);
		this.head.mirror = false;
		this.head.addBox("scale", 3.0F, -12.0F, 12.0F + var2, 2, 4, 6);
		this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + var2, 2, 2, 4);
		this.jaw = new ModelRenderer(this, "jaw");
		this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + var2);
		this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
		this.head.addChild(this.jaw);
		this.neck = new ModelRenderer(this, "neck");
		this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
		this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
		this.body = new ModelRenderer(this, "body");
		this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
		this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
		this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
		this.wing = new ModelRenderer(this, "wing");
		this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
		this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wingTip = new ModelRenderer(this, "wingtip");
		this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
		this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
		this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wing.addChild(this.wingTip);
		this.frontLeg = new ModelRenderer(this, "frontleg");
		this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
		this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
		this.frontLegTip = new ModelRenderer(this, "frontlegtip");
		this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
		this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
		this.frontLeg.addChild(this.frontLegTip);
		this.frontFoot = new ModelRenderer(this, "frontfoot");
		this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
		this.frontLegTip.addChild(this.frontFoot);
		this.rearLeg = new ModelRenderer(this, "rearleg");
		this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
		this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
		this.rearLegTip = new ModelRenderer(this, "rearlegtip");
		this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
		this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
		this.rearLeg.addChild(this.rearLegTip);
		this.rearFoot = new ModelRenderer(this, "rearfoot");
		this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
		this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
		this.rearLegTip.addChild(this.rearFoot);
	}

	public void setLivingAnimations(EntityLiving var1, float var2, float var3, float var4) {
		this.field_40317_s = var4;
	}

	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		GL11.glPushMatrix();
		EntityDragon var8 = (EntityDragon)var1;
		float var9 = var8.field_40173_aw + (var8.field_40172_ax - var8.field_40173_aw) * this.field_40317_s;
		this.jaw.rotateAngleX = (float)(Math.sin((double)(var9 * (float)Math.PI * 2.0F)) + 1.0D) * 0.2F;
		float var10 = (float)(Math.sin((double)(var9 * (float)Math.PI * 2.0F - 1.0F)) + 1.0D);
		var10 = (var10 * var10 * 1.0F + var10 * 2.0F) * 0.05F;
		GL11.glTranslatef(0.0F, var10 - 2.0F, -3.0F);
		GL11.glRotatef(var10 * 2.0F, 1.0F, 0.0F, 0.0F);
		float var11 = -30.0F;
		float var13 = 0.0F;
		float var14 = 1.5F;
		double[] var15 = var8.func_40160_a(6, this.field_40317_s);
		float var16 = this.updateRotations(var8.func_40160_a(5, this.field_40317_s)[0] - var8.func_40160_a(10, this.field_40317_s)[0]);
		float var17 = this.updateRotations(var8.func_40160_a(5, this.field_40317_s)[0] + (double)(var16 / 2.0F));
		var11 += 2.0F;
		float var18 = var9 * (float)Math.PI * 2.0F;
		var11 = 20.0F;
		float var12 = -12.0F;

		float var21;
		for(int var19 = 0; var19 < 5; ++var19) {
			double[] var20 = var8.func_40160_a(5 - var19, this.field_40317_s);
			var21 = (float)Math.cos((double)((float)var19 * 0.45F + var18)) * 0.15F;
			this.neck.rotateAngleY = this.updateRotations(var20[0] - var15[0]) * (float)Math.PI / 180.0F * var14;
			this.neck.rotateAngleX = var21 + (float)(var20[1] - var15[1]) * (float)Math.PI / 180.0F * var14 * 5.0F;
			this.neck.rotateAngleZ = -this.updateRotations(var20[0] - (double)var17) * (float)Math.PI / 180.0F * var14;
			this.neck.rotationPointY = var11;
			this.neck.rotationPointZ = var12;
			this.neck.rotationPointX = var13;
			var11 = (float)((double)var11 + Math.sin((double)this.neck.rotateAngleX) * 10.0D);
			var12 = (float)((double)var12 - Math.cos((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			var13 = (float)((double)var13 - Math.sin((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			this.neck.render(var7);
		}

		this.head.rotationPointY = var11;
		this.head.rotationPointZ = var12;
		this.head.rotationPointX = var13;
		double[] var22 = var8.func_40160_a(0, this.field_40317_s);
		this.head.rotateAngleY = this.updateRotations(var22[0] - var15[0]) * (float)Math.PI / 180.0F * 1.0F;
		this.head.rotateAngleZ = -this.updateRotations(var22[0] - (double)var17) * (float)Math.PI / 180.0F * 1.0F;
		this.head.render(var7);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-var16 * var14 * 1.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(0.0F, -1.0F, 0.0F);
		this.body.rotateAngleZ = 0.0F;
		this.body.render(var7);

		for(int var23 = 0; var23 < 2; ++var23) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			var21 = var9 * (float)Math.PI * 2.0F;
			this.wing.rotateAngleX = 2.0F / 16.0F - (float)Math.cos((double)var21) * 0.2F;
			this.wing.rotateAngleY = 0.25F;
			this.wing.rotateAngleZ = (float)(Math.sin((double)var21) + 0.125D) * 0.8F;
			this.wingTip.rotateAngleZ = -((float)(Math.sin((double)(var21 + 2.0F)) + 0.5D)) * (12.0F / 16.0F);
			this.rearLeg.rotateAngleX = 1.0F + var10 * 0.1F;
			this.rearLegTip.rotateAngleX = 0.5F + var10 * 0.1F;
			this.rearFoot.rotateAngleX = 12.0F / 16.0F + var10 * 0.1F;
			this.frontLeg.rotateAngleX = 1.3F + var10 * 0.1F;
			this.frontLegTip.rotateAngleX = -0.5F - var10 * 0.1F;
			this.frontFoot.rotateAngleX = 12.0F / 16.0F + var10 * 0.1F;
			this.wing.render(var7);
			this.frontLeg.render(var7);
			this.rearLeg.render(var7);
			GL11.glScalef(-1.0F, 1.0F, 1.0F);
			if(var23 == 0) {
				GL11.glCullFace(GL11.GL_FRONT);
			}
		}

		GL11.glPopMatrix();
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float var24 = -((float)Math.sin((double)(var9 * (float)Math.PI * 2.0F))) * 0.0F;
		var18 = var9 * (float)Math.PI * 2.0F;
		var11 = 10.0F;
		var12 = 60.0F;
		var13 = 0.0F;
		var15 = var8.func_40160_a(11, this.field_40317_s);

		for(int var25 = 0; var25 < 12; ++var25) {
			var22 = var8.func_40160_a(12 + var25, this.field_40317_s);
			var24 = (float)((double)var24 + Math.sin((double)((float)var25 * 0.45F + var18)) * (double)0.05F);
			this.neck.rotateAngleY = (this.updateRotations(var22[0] - var15[0]) * var14 + 180.0F) * (float)Math.PI / 180.0F;
			this.neck.rotateAngleX = var24 + (float)(var22[1] - var15[1]) * (float)Math.PI / 180.0F * var14 * 5.0F;
			this.neck.rotateAngleZ = this.updateRotations(var22[0] - (double)var17) * (float)Math.PI / 180.0F * var14;
			this.neck.rotationPointY = var11;
			this.neck.rotationPointZ = var12;
			this.neck.rotationPointX = var13;
			var11 = (float)((double)var11 + Math.sin((double)this.neck.rotateAngleX) * 10.0D);
			var12 = (float)((double)var12 - Math.cos((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			var13 = (float)((double)var13 - Math.sin((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			this.neck.render(var7);
		}

		GL11.glPopMatrix();
	}

	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		super.setRotationAngles(var1, var2, var3, var4, var5, var6);
	}

	private float updateRotations(double var1) {
		while(var1 >= 180.0D) {
			var1 -= 360.0D;
		}

		while(var1 < -180.0D) {
			var1 += 360.0D;
		}

		return (float)var1;
	}
}
