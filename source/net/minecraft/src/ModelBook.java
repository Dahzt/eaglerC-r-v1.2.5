package net.minecraft.src;

public class ModelBook extends ModelBase {
	public ModelRenderer coverRight = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6, 10, 0);
	public ModelRenderer coverLeft = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5.0F, 0.0F, 6, 10, 0);
	public ModelRenderer pagesRight = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4.0F, -0.99F, 5, 8, 1);
	public ModelRenderer pagesLeft = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4.0F, -0.01F, 5, 8, 1);
	public ModelRenderer flippingPageRight = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
	public ModelRenderer flippingPageLeft = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4.0F, 0.0F, 5, 8, 0);
	public ModelRenderer bookSpine = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1.0F, -5.0F, 0.0F, 2, 10, 0);

	public ModelBook() {
		this.coverRight.setRotationPoint(0.0F, 0.0F, -1.0F);
		this.coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
		this.bookSpine.rotateAngleY = (float)Math.PI * 0.5F;
	}

	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.setRotationAngles(var2, var3, var4, var5, var6, var7);
		this.coverRight.render(var7);
		this.coverLeft.render(var7);
		this.bookSpine.render(var7);
		this.pagesRight.render(var7);
		this.pagesLeft.render(var7);
		this.flippingPageRight.render(var7);
		this.flippingPageLeft.render(var7);
	}

	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		float var7 = (MathHelper.sin(var1 * 0.02F) * 0.1F + 1.25F) * var4;
		this.coverRight.rotateAngleY = (float)Math.PI + var7;
		this.coverLeft.rotateAngleY = -var7;
		this.pagesRight.rotateAngleY = var7;
		this.pagesLeft.rotateAngleY = -var7;
		this.flippingPageRight.rotateAngleY = var7 - var7 * 2.0F * var2;
		this.flippingPageLeft.rotateAngleY = var7 - var7 * 2.0F * var3;
		this.pagesRight.rotationPointX = MathHelper.sin(var7);
		this.pagesLeft.rotationPointX = MathHelper.sin(var7);
		this.flippingPageRight.rotationPointX = MathHelper.sin(var7);
		this.flippingPageLeft.rotationPointX = MathHelper.sin(var7);
	}
}
