package net.minecraft.src;

public class ModelMinecart extends ModelBase {
	public ModelRenderer[] sideModels = new ModelRenderer[7];

	public ModelMinecart() {
		this.sideModels[0] = new ModelRenderer(this, 0, 10);
		this.sideModels[1] = new ModelRenderer(this, 0, 0);
		this.sideModels[2] = new ModelRenderer(this, 0, 0);
		this.sideModels[3] = new ModelRenderer(this, 0, 0);
		this.sideModels[4] = new ModelRenderer(this, 0, 0);
		this.sideModels[5] = new ModelRenderer(this, 44, 10);
		byte var1 = 20;
		byte var2 = 8;
		byte var3 = 16;
		byte var4 = 4;
		this.sideModels[0].addBox((float)(-var1 / 2), (float)(-var3 / 2), -1.0F, var1, var3, 2, 0.0F);
		this.sideModels[0].setRotationPoint(0.0F, (float)var4, 0.0F);
		this.sideModels[5].addBox((float)(-var1 / 2 + 1), (float)(-var3 / 2 + 1), -1.0F, var1 - 2, var3 - 2, 1, 0.0F);
		this.sideModels[5].setRotationPoint(0.0F, (float)var4, 0.0F);
		this.sideModels[1].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
		this.sideModels[1].setRotationPoint((float)(-var1 / 2 + 1), (float)var4, 0.0F);
		this.sideModels[2].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
		this.sideModels[2].setRotationPoint((float)(var1 / 2 - 1), (float)var4, 0.0F);
		this.sideModels[3].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
		this.sideModels[3].setRotationPoint(0.0F, (float)var4, (float)(-var3 / 2 + 1));
		this.sideModels[4].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
		this.sideModels[4].setRotationPoint(0.0F, (float)var4, (float)(var3 / 2 - 1));
		this.sideModels[0].rotateAngleX = (float)Math.PI * 0.5F;
		this.sideModels[1].rotateAngleY = (float)Math.PI * 3.0F / 2.0F;
		this.sideModels[2].rotateAngleY = (float)Math.PI * 0.5F;
		this.sideModels[3].rotateAngleY = (float)Math.PI;
		this.sideModels[5].rotateAngleX = (float)Math.PI * -0.5F;
	}

	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.sideModels[5].rotationPointY = 4.0F - var4;

		for(int var8 = 0; var8 < 6; ++var8) {
			this.sideModels[var8].render(var7);
		}

	}

	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
	}
}
