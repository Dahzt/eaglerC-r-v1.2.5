package net.minecraft.src;

public class EntityEnderEye extends Entity {
	public int field_40096_a = 0;
	private double field_40094_b;
	private double field_40095_c;
	private double field_40091_d;
	private int despawnTimer;
	private boolean shatterOrDrop;

	public EntityEnderEye(World var1) {
		super(var1);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {
	}

	public boolean isInRangeToRenderDist(double var1) {
		double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return var1 < var3 * var3;
	}

	public EntityEnderEye(World var1, double var2, double var4, double var6) {
		super(var1);
		this.despawnTimer = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(var2, var4, var6);
		this.yOffset = 0.0F;
	}

	public void func_40090_a(double var1, int var3, double var4) {
		double var6 = var1 - this.posX;
		double var8 = var4 - this.posZ;
		float var10 = MathHelper.sqrt_double(var6 * var6 + var8 * var8);
		if(var10 > 12.0F) {
			this.field_40094_b = this.posX + var6 / (double)var10 * 12.0D;
			this.field_40091_d = this.posZ + var8 / (double)var10 * 12.0D;
			this.field_40095_c = this.posY + 8.0D;
		} else {
			this.field_40094_b = var1;
			this.field_40095_c = (double)var3;
			this.field_40091_d = var4;
		}

		this.despawnTimer = 0;
		this.shatterOrDrop = this.rand.nextInt(5) > 0;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var7) * 180.0D / (double)((float)Math.PI));
		}

	}

	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		super.onUpdate();
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		if(!this.worldObj.isRemote) {
			double var2 = this.field_40094_b - this.posX;
			double var4 = this.field_40091_d - this.posZ;
			float var6 = (float)Math.sqrt(var2 * var2 + var4 * var4);
			float var7 = (float)Math.atan2(var4, var2);
			double var8 = (double)var1 + (double)(var6 - var1) * 0.0025D;
			if(var6 < 1.0F) {
				var8 *= 0.8D;
				this.motionY *= 0.8D;
			}

			this.motionX = Math.cos((double)var7) * var8;
			this.motionZ = Math.sin((double)var7) * var8;
			if(this.posY < this.field_40095_c) {
				this.motionY += (1.0D - this.motionY) * (double)0.015F;
			} else {
				this.motionY += (-1.0D - this.motionY) * (double)0.015F;
			}
		}

		float var10 = 0.25F;
		if(this.isInWater()) {
			for(int var3 = 0; var3 < 4; ++var3) {
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var10, this.posY - this.motionY * (double)var10, this.posZ - this.motionZ * (double)var10, this.motionX, this.motionY, this.motionZ);
			}
		} else {
			this.worldObj.spawnParticle("portal", this.posX - this.motionX * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double)var10 - 0.5D, this.posZ - this.motionZ * (double)var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ);
		}

		if(!this.worldObj.isRemote) {
			this.setPosition(this.posX, this.posY, this.posZ);
			++this.despawnTimer;
			if(this.despawnTimer > 80 && !this.worldObj.isRemote) {
				this.setDead();
				if(this.shatterOrDrop) {
					this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.eyeOfEnder)));
				} else {
					this.worldObj.playAuxSFX(2003, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0);
				}
			}
		}

	}

	public void writeEntityToNBT(NBTTagCompound var1) {
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public float getBrightness(float var1) {
		return 1.0F;
	}

	public int getBrightnessForRender(float var1) {
		return 15728880;
	}

	public boolean canAttackWithItem() {
		return false;
	}
}
