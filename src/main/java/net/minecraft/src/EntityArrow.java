package net.minecraft.src;

import java.util.List;

public class EntityArrow extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private int inData = 0;
	private boolean inGround = false;
	public boolean doesArrowBelongToPlayer = false;
	public int arrowShake = 0;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir = 0;
	private double damage = 2.0D;
	private int field_46027_au;
	public boolean arrowCritical = false;

	public EntityArrow(World var1) {
		super(var1);
		this.setSize(0.5F, 0.5F);
	}

	public EntityArrow(World var1, double var2, double var4, double var6) {
		super(var1);
		this.setSize(0.5F, 0.5F);
		this.setPosition(var2, var4, var6);
		this.yOffset = 0.0F;
	}

	public EntityArrow(World var1, EntityLiving var2, EntityLiving var3, float var4, float var5) {
		super(var1);
		this.shootingEntity = var2;
		this.doesArrowBelongToPlayer = var2 instanceof EntityPlayer;
		this.posY = var2.posY + (double)var2.getEyeHeight() - (double)0.1F;
		double var6 = var3.posX - var2.posX;
		double var8 = var3.posY + (double)var3.getEyeHeight() - (double)0.7F - this.posY;
		double var10 = var3.posZ - var2.posZ;
		double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);
		if(var12 >= 1.0E-7D) {
			float var14 = (float)(Math.atan2(var10, var6) * 180.0D / (double)((float)Math.PI)) - 90.0F;
			float var15 = (float)(-(Math.atan2(var8, var12) * 180.0D / (double)((float)Math.PI)));
			double var16 = var6 / var12;
			double var18 = var10 / var12;
			this.setLocationAndAngles(var2.posX + var16, this.posY, var2.posZ + var18, var14, var15);
			this.yOffset = 0.0F;
			float var20 = (float)var12 * 0.2F;
			this.setArrowHeading(var6, var8 + (double)var20, var10, var4, var5);
		}
	}

	public EntityArrow(World var1, EntityLiving var2, float var3) {
		super(var1);
		this.shootingEntity = var2;
		this.doesArrowBelongToPlayer = var2 instanceof EntityPlayer;
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.posY -= (double)0.1F;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
		this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
		this.setArrowHeading(this.motionX, this.motionY, this.motionZ, var3 * 1.5F, 1.0F);
	}

	protected void entityInit() {
	}

	public void setArrowHeading(double var1, double var3, double var5, float var7, float var8) {
		float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		var1 /= (double)var9;
		var3 /= (double)var9;
		var5 /= (double)var9;
		var1 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var3 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var5 += this.rand.nextGaussian() * (double)0.0075F * (double)var8;
		var1 *= (double)var7;
		var3 *= (double)var7;
		var5 *= (double)var7;
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / (double)((float)Math.PI));
		this.ticksInGround = 0;
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var7) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}

	}

	public void onUpdate() {
		super.onUpdate();
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / (double)((float)Math.PI));
		}

		int var15 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
		if(var15 > 0) {
			Block.blocksList[var15].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
			AxisAlignedBB var2 = Block.blocksList[var15].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
			if(var2 != null && var2.isVecInside(Vec3D.createVector(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if(this.arrowShake > 0) {
			--this.arrowShake;
		}

		if(this.inGround) {
			var15 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			int var18 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
			if(var15 == this.inTile && var18 == this.inData) {
				++this.ticksInGround;
				if(this.ticksInGround == 1200) {
					this.setDead();
				}

			} else {
				this.inGround = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		} else {
			++this.ticksInAir;
			Vec3D var16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			Vec3D var17 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var3 = this.worldObj.rayTraceBlocks_do_do(var16, var17, false, true);
			var16 = Vec3D.createVector(this.posX, this.posY, this.posZ);
			var17 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if(var3 != null) {
				var17 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			int var8;
			float var10;
			for(var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if(var9.canBeCollidedWith() && (var9 != this.shootingEntity || this.ticksInAir >= 5)) {
					var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.calculateIntercept(var16, var17);
					if(var12 != null) {
						double var13 = var16.distanceTo(var12.hitVec);
						if(var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if(var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			float var19;
			if(var3 != null) {
				if(var3.entityHit != null) {
					var19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int var20 = (int)Math.ceil((double)var19 * this.damage);
					if(this.arrowCritical) {
						var20 += this.rand.nextInt(var20 / 2 + 2);
					}

					DamageSource var21 = null;
					if(this.shootingEntity == null) {
						var21 = DamageSource.causeArrowDamage(this, this);
					} else {
						var21 = DamageSource.causeArrowDamage(this, this.shootingEntity);
					}

					if(this.isBurning()) {
						var3.entityHit.setFire(5);
					}

					if(var3.entityHit.attackEntityFrom(var21, var20)) {
						if(var3.entityHit instanceof EntityLiving) {
							++((EntityLiving)var3.entityHit).arrowHitTempCounter;
							if(this.field_46027_au > 0) {
								float var23 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
								if(var23 > 0.0F) {
									var3.entityHit.addVelocity(this.motionX * (double)this.field_46027_au * (double)0.6F / (double)var23, 0.1D, this.motionZ * (double)this.field_46027_au * (double)0.6F / (double)var23);
								}
							}
						}

						this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
						this.setDead();
					} else {
						this.motionX *= (double)-0.1F;
						this.motionY *= (double)-0.1F;
						this.motionZ *= (double)-0.1F;
						this.rotationYaw += 180.0F;
						this.prevRotationYaw += 180.0F;
						this.ticksInAir = 0;
					}
				} else {
					this.xTile = var3.blockX;
					this.yTile = var3.blockY;
					this.zTile = var3.blockZ;
					this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
					this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					this.motionX = (double)((float)(var3.hitVec.xCoord - this.posX));
					this.motionY = (double)((float)(var3.hitVec.yCoord - this.posY));
					this.motionZ = (double)((float)(var3.hitVec.zCoord - this.posZ));
					var19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / (double)var19 * (double)0.05F;
					this.posY -= this.motionY / (double)var19 * (double)0.05F;
					this.posZ -= this.motionZ / (double)var19 * (double)0.05F;
					this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
					this.arrowCritical = false;
				}
			}

			if(this.arrowCritical) {
				for(var8 = 0; var8 < 4; ++var8) {
					this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double)var8 / 4.0D, this.posY + this.motionY * (double)var8 / 4.0D, this.posZ + this.motionZ * (double)var8 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			var19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / (double)((float)Math.PI));

			for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var19) * 180.0D / (double)((float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
			float var22 = 0.99F;
			var10 = 0.05F;
			if(this.isInWater()) {
				for(int var24 = 0; var24 < 4; ++var24) {
					float var25 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var25, this.posY - this.motionY * (double)var25, this.posZ - this.motionZ * (double)var25, this.motionX, this.motionY, this.motionZ);
				}

				var22 = 0.8F;
			}

			this.motionX *= (double)var22;
			this.motionY *= (double)var22;
			this.motionZ *= (double)var22;
			this.motionY -= (double)var10;
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.xTile);
		var1.setShort("yTile", (short)this.yTile);
		var1.setShort("zTile", (short)this.zTile);
		var1.setByte("inTile", (byte)this.inTile);
		var1.setByte("inData", (byte)this.inData);
		var1.setByte("shake", (byte)this.arrowShake);
		var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		var1.setBoolean("player", this.doesArrowBelongToPlayer);
		var1.setDouble("damage", this.damage);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.xTile = var1.getShort("xTile");
		this.yTile = var1.getShort("yTile");
		this.zTile = var1.getShort("zTile");
		this.inTile = var1.getByte("inTile") & 255;
		this.inData = var1.getByte("inData") & 255;
		this.arrowShake = var1.getByte("shake") & 255;
		this.inGround = var1.getByte("inGround") == 1;
		this.doesArrowBelongToPlayer = var1.getBoolean("player");
		if(var1.hasKey("damage")) {
			this.damage = var1.getDouble("damage");
		}

	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		if(!this.worldObj.isRemote) {
			if(this.inGround && this.doesArrowBelongToPlayer && this.arrowShake <= 0 && var1.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1))) {
				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				var1.onItemPickup(this, 1);
				this.setDead();
			}

		}
	}

	public float getShadowSize() {
		return 0.0F;
	}

	public void setDamage(double var1) {
		this.damage = var1;
	}

	public double getDamage() {
		return this.damage;
	}

	public void func_46023_b(int var1) {
		this.field_46027_au = var1;
	}

	public boolean canAttackWithItem() {
		return false;
	}
}
