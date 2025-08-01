package net.minecraft.src;

public class EntityCloudFX extends EntityFX {
	float field_35135_a;

	public EntityCloudFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D);
		float var14 = 2.5F;
		this.motionX *= (double)0.1F;
		this.motionY *= (double)0.1F;
		this.motionZ *= (double)0.1F;
		this.motionX += var8;
		this.motionY += var10;
		this.motionZ += var12;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * (double)0.3F);
		this.particleScale *= 12.0F / 16.0F;
		this.particleScale *= var14;
		this.field_35135_a = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.3D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * var14);
		this.noClip = false;
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = ((float)this.particleAge + var2) / (float)this.particleMaxAge * 32.0F;
		if(var8 < 0.0F) {
			var8 = 0.0F;
		}

		if(var8 > 1.0F) {
			var8 = 1.0F;
		}

		this.particleScale = this.field_35135_a * var8;
		super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setDead();
		}

		this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 2.0D);
		if(var1 != null && this.posY > var1.boundingBox.minY) {
			this.posY += (var1.boundingBox.minY - this.posY) * 0.2D;
			this.motionY += (var1.motionY - this.motionY) * 0.2D;
			this.setPosition(this.posX, this.posY, this.posZ);
		}

		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}
