package net.minecraft.src;


import org.lwjgl.opengl.GL11;

public class ModelGhast extends ModelBase {
	ModelRenderer body;
	ModelRenderer[] tentacles = new ModelRenderer[9];

	public ModelGhast() {
		byte var1 = -16;
		this.body = new ModelRenderer(this, 0, 0);
		this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16);
		this.body.rotationPointY += (float)(24 + var1);
		Random var2 = new Random(1660L);

		for(int var3 = 0; var3 < this.tentacles.length; ++var3) {
			this.tentacles[var3] = new ModelRenderer(this, 0, 0);
			float var4 = (((float)(var3 % 3) - (float)(var3 / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
			float var5 = ((float)(var3 / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
			int var6 = var2.nextInt(7) + 8;
			this.tentacles[var3].addBox(-1.0F, 0.0F, -1.0F, 2, var6, 2);
			this.tentacles[var3].rotationPointX = var4;
			this.tentacles[var3].rotationPointZ = var5;
			this.tentacles[var3].rotationPointY = (float)(31 + var1);
		}

	}

	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		for(int var7 = 0; var7 < this.tentacles.length; ++var7) {
			this.tentacles[var7].rotateAngleX = 0.2F * MathHelper.sin(var3 * 0.3F + (float)var7) + 0.4F;
		}

	}

	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.setRotationAngles(var2, var3, var4, var5, var6, var7);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.6F, 0.0F);
		this.body.render(var7);
		ModelRenderer[] var8 = this.tentacles;
		int var9 = var8.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			ModelRenderer var11 = var8[var10];
			var11.render(var7);
		}

		GL11.glPopMatrix();
	}
}
