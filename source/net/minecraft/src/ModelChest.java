package net.minecraft.src;

public class ModelChest extends ModelBase {
	public ModelRenderer chestLid = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
	public ModelRenderer chestBelow;
	public ModelRenderer chestKnob;

	public ModelChest() {
		this.chestLid.addBox(0.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F);
		this.chestLid.rotationPointX = 1.0F;
		this.chestLid.rotationPointY = 7.0F;
		this.chestLid.rotationPointZ = 15.0F;
		this.chestKnob = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
		this.chestKnob.addBox(-1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F);
		this.chestKnob.rotationPointX = 8.0F;
		this.chestKnob.rotationPointY = 7.0F;
		this.chestKnob.rotationPointZ = 15.0F;
		this.chestBelow = (new ModelRenderer(this, 0, 19)).setTextureSize(64, 64);
		this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 10, 14, 0.0F);
		this.chestBelow.rotationPointX = 1.0F;
		this.chestBelow.rotationPointY = 6.0F;
		this.chestBelow.rotationPointZ = 1.0F;
	}

	public void renderAll() {
		this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
		this.chestLid.render(1.0F / 16.0F);
		this.chestKnob.render(1.0F / 16.0F);
		this.chestBelow.render(1.0F / 16.0F);
	}
}
