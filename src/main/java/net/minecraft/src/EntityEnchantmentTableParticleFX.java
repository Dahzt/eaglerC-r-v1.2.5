package net.minecraft.src;

public class EntityEnchantmentTableParticleFX extends EntityFX {
	private float field_40107_a;
	private double field_40109_aw;
	private double field_40108_ax;
	private double field_40106_ay;

	public EntityEnchantmentTableParticleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, var8, var10, var12);
		this.motionX = var8;
		this.motionY = var10;
		this.motionZ = var12;
		this.field_40109_aw = this.posX = var2;
		this.field_40108_ax = this.posY = var4;
		this.field_40106_ay = this.posZ = var6;
		float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
		this.field_40107_a = this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F * var14;
		this.particleGreen *= 0.9F;
		this.particleRed *= 0.9F;
		this.particleMaxAge = (int)(Math.random() * 10.0D) + 30;
		this.noClip = true;
		this.setParticleTextureIndex((int)(Math.random() * 26.0D + 1.0D + 224.0D));
	}

	public int getBrightnessForRender(float var1) {
		int var2 = super.getBrightnessForRender(var1);
		float var3 = (float)this.particleAge / (float)this.particleMaxAge;
		var3 *= var3;
		var3 *= var3;
		int var4 = var2 & 255;
		int var5 = var2 >> 16 & 255;
		var5 += (int)(var3 * 15.0F * 16.0F);
		if(var5 > 240) {
			var5 = 240;
		}

		return var4 | var5 << 16;
	}

	public float getBrightness(float var1) {
		float var2 = super.getBrightness(var1);
		float var3 = (float)this.particleAge / (float)this.particleMaxAge;
		var3 *= var3;
		var3 *= var3;
		return var2 * (1.0F - var3) + var3;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float var1 = (float)this.particleAge / (float)this.particleMaxAge;
		var1 = 1.0F - var1;
		float var2 = 1.0F - var1;
		var2 *= var2;
		var2 *= var2;
		this.posX = this.field_40109_aw + this.motionX * (double)var1;
		this.posY = this.field_40108_ax + this.motionY * (double)var1 - (double)(var2 * 1.2F);
		this.posZ = this.field_40106_ay + this.motionZ * (double)var1;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

	}
}
