package net.minecraft.src;

import java.util.List;


public abstract class Entity {
	private static int nextEntityID = 0;
	public int entityId = nextEntityID++;
	public double renderDistanceWeight = 1.0D;
	public boolean preventEntitySpawning = false;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	public World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	public boolean onGround = false;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided = false;
	public boolean velocityChanged = false;
	protected boolean isInWeb;
	public boolean field_9293_aM = true;
	public boolean isDead = false;
	public float yOffset = 0.0F;
	public float width = 0.6F;
	public float height = 1.8F;
	public float prevDistanceWalkedModified = 0.0F;
	public float distanceWalkedModified = 0.0F;
	public float fallDistance = 0.0F;
	private int nextStepDistance = 1;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float ySize = 0.0F;
	public float stepHeight = 0.0F;
	public boolean noClip = false;
	public float entityCollisionReduction = 0.0F;
	protected Random rand = new Random();
	public int ticksExisted = 0;
	public int fireResistance = 1;
	private int fire = 0;
	protected boolean inWater = false;
	public int heartsLife = 0;
	private boolean firstUpdate = true;
	public String skinUrl;
	public String cloakUrl;
	protected boolean isImmuneToFire = false;
	protected DataWatcher dataWatcher = new DataWatcher();
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk = false;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	public int serverPosX;
	public int serverPosY;
	public int serverPosZ;
	public boolean ignoreFrustumCheck;
	public boolean isAirBorne;

	public Entity(World var1) {
		this.worldObj = var1;
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(1, Short.valueOf((short)300));
		this.entityInit();
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher() {
		return this.dataWatcher;
	}

	public boolean equals(Object var1) {
		return var1 instanceof Entity ? ((Entity)var1).entityId == this.entityId : false;
	}

	public int hashCode() {
		return this.entityId;
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

	public void setDead() {
		this.isDead = true;
	}

	protected void setSize(float var1, float var2) {
		this.width = var1;
		this.height = var2;
	}

	protected void setRotation(float var1, float var2) {
		this.rotationYaw = var1 % 360.0F;
		this.rotationPitch = var2 % 360.0F;
	}

	public void setPosition(double var1, double var3, double var5) {
		this.posX = var1;
		this.posY = var3;
		this.posZ = var5;
		float var7 = this.width / 2.0F;
		float var8 = this.height;
		this.boundingBox.setBounds(var1 - (double)var7, var3 - (double)this.yOffset + (double)this.ySize, var5 - (double)var7, var1 + (double)var7, var3 - (double)this.yOffset + (double)this.ySize + (double)var8, var5 + (double)var7);
	}

	public void setAngles(float var1, float var2) {
		float var3 = this.rotationPitch;
		float var4 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)var1 * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)var2 * 0.15D);
		if(this.rotationPitch < -90.0F) {
			this.rotationPitch = -90.0F;
		}

		if(this.rotationPitch > 90.0F) {
			this.rotationPitch = 90.0F;
		}

		this.prevRotationPitch += this.rotationPitch - var3;
		this.prevRotationYaw += this.rotationYaw - var4;
	}

	public void onUpdate() {
		this.onEntityUpdate();
	}

	public void onEntityUpdate() {
		Profiler.startSection("entityBaseTick");
		if(this.ridingEntity != null && this.ridingEntity.isDead) {
			this.ridingEntity = null;
		}

		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		int var3;
		if(this.isSprinting() && !this.isInWater()) {
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
			var3 = MathHelper.floor_double(this.posZ);
			int var4 = this.worldObj.getBlockId(var1, var2, var3);
			if(var4 > 0) {
				this.worldObj.spawnParticle("tilecrack_" + var4, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
			}
		}

		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.firstUpdate) {
				float var6 = MathHelper.sqrt_double(this.motionX * this.motionX * (double)0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * (double)0.2F) * 0.2F;
				if(var6 > 1.0F) {
					var6 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", var6, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				float var7 = (float)MathHelper.floor_double(this.boundingBox.minY);

				float var5;
				float var8;
				for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
					var8 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)var8, (double)(var7 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
					var8 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)var8, (double)(var7 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.worldObj.isRemote) {
			this.fire = 0;
		} else if(this.fire > 0) {
			if(this.isImmuneToFire) {
				this.fire -= 4;
				if(this.fire < 0) {
					this.fire = 0;
				}
			} else {
				if(this.fire % 20 == 0) {
					this.attackEntityFrom(DamageSource.onFire, 1);
				}

				--this.fire;
			}
		}

		if(this.handleLavaMovement()) {
			this.setOnFireFromLava();
			this.fallDistance *= 0.5F;
		}

		if(this.posY < -64.0D) {
			this.kill();
		}

		if(!this.worldObj.isRemote) {
			this.setFlag(0, this.fire > 0);
			this.setFlag(2, this.ridingEntity != null);
		}

		this.firstUpdate = false;
		Profiler.endSection();
	}

	protected void setOnFireFromLava() {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom(DamageSource.lava, 4);
			this.setFire(15);
		}

	}

	public void setFire(int var1) {
		int var2 = var1 * 20;
		if(this.fire < var2) {
			this.fire = var2;
		}

	}

	public void extinguish() {
		this.fire = 0;
	}

	protected void kill() {
		this.setDead();
	}

	public boolean isOffsetPositionInLiquid(double var1, double var3, double var5) {
		AxisAlignedBB var7 = this.boundingBox.getOffsetBoundingBox(var1, var3, var5);
		List var8 = this.worldObj.getCollidingBoundingBoxes(this, var7);
		return var8.size() > 0 ? false : !this.worldObj.isAnyLiquid(var7);
	}

	public void moveEntity(double var1, double var3, double var5) {
		if(this.noClip) {
			this.boundingBox.offset(var1, var3, var5);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			Profiler.startSection("move");
			this.ySize *= 0.4F;
			double var7 = this.posX;
			double var9 = this.posZ;
			if(this.isInWeb) {
				this.isInWeb = false;
				var1 *= 0.25D;
				var3 *= (double)0.05F;
				var5 *= 0.25D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			double var11 = var1;
			double var13 = var3;
			double var15 = var5;
			AxisAlignedBB var17 = this.boundingBox.copy();
			boolean var18 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
			if(var18) {
				double var19;
				for(var19 = 0.05D; var1 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(var1, -1.0D, 0.0D)).size() == 0; var11 = var1) {
					if(var1 < var19 && var1 >= -var19) {
						var1 = 0.0D;
					} else if(var1 > 0.0D) {
						var1 -= var19;
					} else {
						var1 += var19;
					}
				}

				for(; var5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, var5)).size() == 0; var15 = var5) {
					if(var5 < var19 && var5 >= -var19) {
						var5 = 0.0D;
					} else if(var5 > 0.0D) {
						var5 -= var19;
					} else {
						var5 += var19;
					}
				}

				while(var1 != 0.0D && var5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(var1, -1.0D, var5)).size() == 0) {
					if(var1 < var19 && var1 >= -var19) {
						var1 = 0.0D;
					} else if(var1 > 0.0D) {
						var1 -= var19;
					} else {
						var1 += var19;
					}

					if(var5 < var19 && var5 >= -var19) {
						var5 = 0.0D;
					} else if(var5 > 0.0D) {
						var5 -= var19;
					} else {
						var5 += var19;
					}

					var11 = var1;
					var15 = var5;
				}
			}

			List var35 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(var1, var3, var5));

			for(int var20 = 0; var20 < var35.size(); ++var20) {
				var3 = ((AxisAlignedBB)var35.get(var20)).calculateYOffset(this.boundingBox, var3);
			}

			this.boundingBox.offset(0.0D, var3, 0.0D);
			if(!this.field_9293_aM && var13 != var3) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			boolean var36 = this.onGround || var13 != var3 && var13 < 0.0D;

			int var21;
			for(var21 = 0; var21 < var35.size(); ++var21) {
				var1 = ((AxisAlignedBB)var35.get(var21)).calculateXOffset(this.boundingBox, var1);
			}

			this.boundingBox.offset(var1, 0.0D, 0.0D);
			if(!this.field_9293_aM && var11 != var1) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			for(var21 = 0; var21 < var35.size(); ++var21) {
				var5 = ((AxisAlignedBB)var35.get(var21)).calculateZOffset(this.boundingBox, var5);
			}

			this.boundingBox.offset(0.0D, 0.0D, var5);
			if(!this.field_9293_aM && var15 != var5) {
				var5 = 0.0D;
				var3 = var5;
				var1 = var5;
			}

			double var23;
			int var28;
			double var37;
			if(this.stepHeight > 0.0F && var36 && (var18 || this.ySize < 0.05F) && (var11 != var1 || var15 != var5)) {
				var37 = var1;
				var23 = var3;
				double var25 = var5;
				var1 = var11;
				var3 = (double)this.stepHeight;
				var5 = var15;
				AxisAlignedBB var27 = this.boundingBox.copy();
				this.boundingBox.setBB(var17);
				var35 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(var11, var3, var15));

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var3 = ((AxisAlignedBB)var35.get(var28)).calculateYOffset(this.boundingBox, var3);
				}

				this.boundingBox.offset(0.0D, var3, 0.0D);
				if(!this.field_9293_aM && var13 != var3) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var1 = ((AxisAlignedBB)var35.get(var28)).calculateXOffset(this.boundingBox, var1);
				}

				this.boundingBox.offset(var1, 0.0D, 0.0D);
				if(!this.field_9293_aM && var11 != var1) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				for(var28 = 0; var28 < var35.size(); ++var28) {
					var5 = ((AxisAlignedBB)var35.get(var28)).calculateZOffset(this.boundingBox, var5);
				}

				this.boundingBox.offset(0.0D, 0.0D, var5);
				if(!this.field_9293_aM && var15 != var5) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				}

				if(!this.field_9293_aM && var13 != var3) {
					var5 = 0.0D;
					var3 = var5;
					var1 = var5;
				} else {
					var3 = (double)(-this.stepHeight);

					for(var28 = 0; var28 < var35.size(); ++var28) {
						var3 = ((AxisAlignedBB)var35.get(var28)).calculateYOffset(this.boundingBox, var3);
					}

					this.boundingBox.offset(0.0D, var3, 0.0D);
				}

				if(var37 * var37 + var25 * var25 >= var1 * var1 + var5 * var5) {
					var1 = var37;
					var3 = var23;
					var5 = var25;
					this.boundingBox.setBB(var27);
				} else {
					double var40 = this.boundingBox.minY - (double)((int)this.boundingBox.minY);
					if(var40 > 0.0D) {
						this.ySize = (float)((double)this.ySize + var40 + 0.01D);
					}
				}
			}

			Profiler.endSection();
			Profiler.startSection("rest");
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = var11 != var1 || var15 != var5;
			this.isCollidedVertically = var13 != var3;
			this.onGround = var13 != var3 && var13 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			this.updateFallState(var3, this.onGround);
			if(var11 != var1) {
				this.motionX = 0.0D;
			}

			if(var13 != var3) {
				this.motionY = 0.0D;
			}

			if(var15 != var5) {
				this.motionZ = 0.0D;
			}

			var37 = this.posX - var7;
			var23 = this.posZ - var9;
			int var26;
			int var38;
			int var39;
			if(this.canTriggerWalking() && !var18 && this.ridingEntity == null) {
				this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(var37 * var37 + var23 * var23) * 0.6D);
				var38 = MathHelper.floor_double(this.posX);
				var26 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
				var39 = MathHelper.floor_double(this.posZ);
				var28 = this.worldObj.getBlockId(var38, var26, var39);
				if(var28 == 0 && this.worldObj.getBlockId(var38, var26 - 1, var39) == Block.fence.blockID) {
					var28 = this.worldObj.getBlockId(var38, var26 - 1, var39);
				}

				if(this.distanceWalkedModified > (float)this.nextStepDistance && var28 > 0) {
					this.nextStepDistance = (int)this.distanceWalkedModified + 1;
					this.playStepSound(var38, var26, var39, var28);
					Block.blocksList[var28].onEntityWalking(this.worldObj, var38, var26, var39, this);
				}
			}

			var38 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
			var26 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
			var39 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
			var28 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
			int var29 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
			int var30 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);
			if(this.worldObj.checkChunksExist(var38, var26, var39, var28, var29, var30)) {
				for(int var31 = var38; var31 <= var28; ++var31) {
					for(int var32 = var26; var32 <= var29; ++var32) {
						for(int var33 = var39; var33 <= var30; ++var33) {
							int var34 = this.worldObj.getBlockId(var31, var32, var33);
							if(var34 > 0) {
								Block.blocksList[var34].onEntityCollidedWithBlock(this.worldObj, var31, var32, var33, this);
							}
						}
					}
				}
			}

			boolean var41 = this.isWet();
			if(this.worldObj.isBoundingBoxBurning(this.boundingBox.contract(0.001D, 0.001D, 0.001D))) {
				this.dealFireDamage(1);
				if(!var41) {
					++this.fire;
					if(this.fire == 0) {
						this.setFire(8);
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(var41 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

			Profiler.endSection();
		}
	}

	protected void playStepSound(int var1, int var2, int var3, int var4) {
		StepSound var5 = Block.blocksList[var4].stepSound;
		if(this.worldObj.getBlockId(var1, var2 + 1, var3) == Block.snow.blockID) {
			var5 = Block.snow.stepSound;
			this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.15F, var5.getPitch());
		} else if(!Block.blocksList[var4].blockMaterial.isLiquid()) {
			this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.15F, var5.getPitch());
		}

	}

	protected boolean canTriggerWalking() {
		return true;
	}

	protected void updateFallState(double var1, boolean var3) {
		if(var3) {
			if(this.fallDistance > 0.0F) {
				if(this instanceof EntityLiving) {
					int var4 = MathHelper.floor_double(this.posX);
					int var5 = MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset);
					int var6 = MathHelper.floor_double(this.posZ);
					int var7 = this.worldObj.getBlockId(var4, var5, var6);
					if(var7 == 0 && this.worldObj.getBlockId(var4, var5 - 1, var6) == Block.fence.blockID) {
						var7 = this.worldObj.getBlockId(var4, var5 - 1, var6);
					}

					if(var7 > 0) {
						Block.blocksList[var7].onFallenUpon(this.worldObj, var4, var5, var6, this, this.fallDistance);
					}
				}

				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else if(var1 < 0.0D) {
			this.fallDistance = (float)((double)this.fallDistance - var1);
		}

	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int var1) {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom(DamageSource.inFire, var1);
		}

	}

	public final boolean isImmuneToFire() {
		return this.isImmuneToFire;
	}

	protected void fall(float var1) {
		if(this.riddenByEntity != null) {
			this.riddenByEntity.fall(var1);
		}

	}

	public boolean isWet() {
		return this.inWater || this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isInWater() {
		return this.inWater;
	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, (double)-0.4F, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this);
	}

	public boolean isInsideOfMaterial(Material var1) {
		double var2 = this.posY + (double)this.getEyeHeight();
		int var4 = MathHelper.floor_double(this.posX);
		int var5 = MathHelper.floor_float((float)MathHelper.floor_double(var2));
		int var6 = MathHelper.floor_double(this.posZ);
		int var7 = this.worldObj.getBlockId(var4, var5, var6);
		if(var7 != 0 && Block.blocksList[var7].blockMaterial == var1) {
			float var8 = BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(var4, var5, var6)) - 1.0F / 9.0F;
			float var9 = (float)(var5 + 1) - var8;
			return var2 < (double)var9;
		} else {
			return false;
		}
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand((double)-0.1F, (double)-0.4F, (double)-0.1F), Material.lava);
	}

	public void moveFlying(float var1, float var2, float var3) {
		float var4 = MathHelper.sqrt_float(var1 * var1 + var2 * var2);
		if(var4 >= 0.01F) {
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = var3 / var4;
			var1 *= var4;
			var2 *= var4;
			float var5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float var6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(var1 * var6 - var2 * var5);
			this.motionZ += (double)(var2 * var6 + var1 * var5);
		}
	}

	public int getBrightnessForRender(float var1) {
		int var2 = MathHelper.floor_double(this.posX);
		int var3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.blockExists(var2, 0, var3)) {
			double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
			int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
			return this.worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 0);
		} else {
			return 0;
		}
	}

	public float getBrightness(float var1) {
		int var2 = MathHelper.floor_double(this.posX);
		int var3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.blockExists(var2, 0, var3)) {
			double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
			int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
			return this.worldObj.getLightBrightness(var2, var6, var3);
		} else {
			return 0.0F;
		}
	}

	public void setWorld(World var1) {
		this.worldObj = var1;
	}

	public void setPositionAndRotation(double var1, double var3, double var5, float var7, float var8) {
		this.prevPosX = this.posX = var1;
		this.prevPosY = this.posY = var3;
		this.prevPosZ = this.posZ = var5;
		this.prevRotationYaw = this.rotationYaw = var7;
		this.prevRotationPitch = this.rotationPitch = var8;
		this.ySize = 0.0F;
		double var9 = (double)(this.prevRotationYaw - var7);
		if(var9 < -180.0D) {
			this.prevRotationYaw += 360.0F;
		}

		if(var9 >= 180.0D) {
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(var7, var8);
	}

	public void setLocationAndAngles(double var1, double var3, double var5, float var7, float var8) {
		this.lastTickPosX = this.prevPosX = this.posX = var1;
		this.lastTickPosY = this.prevPosY = this.posY = var3 + (double)this.yOffset;
		this.lastTickPosZ = this.prevPosZ = this.posZ = var5;
		this.rotationYaw = var7;
		this.rotationPitch = var8;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public float getDistanceToEntity(Entity var1) {
		float var2 = (float)(this.posX - var1.posX);
		float var3 = (float)(this.posY - var1.posY);
		float var4 = (float)(this.posZ - var1.posZ);
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public double getDistanceSq(double var1, double var3, double var5) {
		double var7 = this.posX - var1;
		double var9 = this.posY - var3;
		double var11 = this.posZ - var5;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public double getDistance(double var1, double var3, double var5) {
		double var7 = this.posX - var1;
		double var9 = this.posY - var3;
		double var11 = this.posZ - var5;
		return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	public double getDistanceSqToEntity(Entity var1) {
		double var2 = this.posX - var1.posX;
		double var4 = this.posY - var1.posY;
		double var6 = this.posZ - var1.posZ;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	public void onCollideWithPlayer(EntityPlayer var1) {
	}

	public void applyEntityCollision(Entity var1) {
		if(var1.riddenByEntity != this && var1.ridingEntity != this) {
			double var2 = var1.posX - this.posX;
			double var4 = var1.posZ - this.posZ;
			double var6 = MathHelper.abs_max(var2, var4);
			if(var6 >= (double)0.01F) {
				var6 = (double)MathHelper.sqrt_double(var6);
				var2 /= var6;
				var4 /= var6;
				double var8 = 1.0D / var6;
				if(var8 > 1.0D) {
					var8 = 1.0D;
				}

				var2 *= var8;
				var4 *= var8;
				var2 *= (double)0.05F;
				var4 *= (double)0.05F;
				var2 *= (double)(1.0F - this.entityCollisionReduction);
				var4 *= (double)(1.0F - this.entityCollisionReduction);
				this.addVelocity(-var2, 0.0D, -var4);
				var1.addVelocity(var2, 0.0D, var4);
			}

		}
	}

	public void addVelocity(double var1, double var3, double var5) {
		this.motionX += var1;
		this.motionY += var3;
		this.motionZ += var5;
		this.isAirBorne = true;
	}

	protected void setBeenAttacked() {
		this.velocityChanged = true;
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		this.setBeenAttacked();
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity var1, int var2) {
	}

	public boolean isInRangeToRenderVec3D(Vec3D var1) {
		double var2 = this.posX - var1.xCoord;
		double var4 = this.posY - var1.yCoord;
		double var6 = this.posZ - var1.zCoord;
		double var8 = var2 * var2 + var4 * var4 + var6 * var6;
		return this.isInRangeToRenderDist(var8);
	}

	public boolean isInRangeToRenderDist(double var1) {
		double var3 = this.boundingBox.getAverageEdgeLength();
		var3 *= 64.0D * this.renderDistanceWeight;
		return var1 < var3 * var3;
	}

	public String getTexture() {
		return null;
	}

	public boolean addEntityID(NBTTagCompound var1) {
		String var2 = this.getEntityString();
		if(!this.isDead && var2 != null) {
			var1.setString("id", var2);
			this.writeToNBT(var1);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound var1) {
		var1.setTag("Pos", this.newDoubleNBTList(new double[]{this.posX, this.posY + (double)this.ySize, this.posZ}));
		var1.setTag("Motion", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		var1.setTag("Rotation", this.newFloatNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
		var1.setFloat("FallDistance", this.fallDistance);
		var1.setShort("Fire", (short)this.fire);
		var1.setShort("Air", (short)this.getAir());
		var1.setBoolean("OnGround", this.onGround);
		this.writeEntityToNBT(var1);
	}

	public void readFromNBT(NBTTagCompound var1) {
		NBTTagList var2 = var1.getTagList("Pos");
		NBTTagList var3 = var1.getTagList("Motion");
		NBTTagList var4 = var1.getTagList("Rotation");
		this.motionX = ((NBTTagDouble)var3.tagAt(0)).data;
		this.motionY = ((NBTTagDouble)var3.tagAt(1)).data;
		this.motionZ = ((NBTTagDouble)var3.tagAt(2)).data;
		if(Math.abs(this.motionX) > 10.0D) {
			this.motionX = 0.0D;
		}

		if(Math.abs(this.motionY) > 10.0D) {
			this.motionY = 0.0D;
		}

		if(Math.abs(this.motionZ) > 10.0D) {
			this.motionZ = 0.0D;
		}

		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)var2.tagAt(0)).data;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)var2.tagAt(1)).data;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)var2.tagAt(2)).data;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)var4.tagAt(0)).data;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)var4.tagAt(1)).data;
		this.fallDistance = var1.getFloat("FallDistance");
		this.fire = var1.getShort("Fire");
		this.setAir(var1.getShort("Air"));
		this.onGround = var1.getBoolean("OnGround");
		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(this.rotationYaw, this.rotationPitch);
		this.readEntityFromNBT(var1);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound var1);

	protected abstract void writeEntityToNBT(NBTTagCompound var1);

	protected NBTTagList newDoubleNBTList(double... var1) {
		NBTTagList var2 = new NBTTagList();
		double[] var3 = var1;
		int var4 = var1.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			double var6 = var3[var5];
			var2.appendTag(new NBTTagDouble((String)null, var6));
		}

		return var2;
	}

	protected NBTTagList newFloatNBTList(float... var1) {
		NBTTagList var2 = new NBTTagList();
		float[] var3 = var1;
		int var4 = var1.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			float var6 = var3[var5];
			var2.appendTag(new NBTTagFloat((String)null, var6));
		}

		return var2;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public EntityItem dropItem(int var1, int var2) {
		return this.dropItemWithOffset(var1, var2, 0.0F);
	}

	public EntityItem dropItemWithOffset(int var1, int var2, float var3) {
		return this.entityDropItem(new ItemStack(var1, var2, 0), var3);
	}

	public EntityItem entityDropItem(ItemStack var1, float var2) {
		EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + (double)var2, this.posZ, var1);
		var3.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(var3);
		return var3;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}

	public boolean isEntityInsideOpaqueBlock() {
		for(int var1 = 0; var1 < 8; ++var1) {
			float var2 = ((float)((var1 >> 0) % 2) - 0.5F) * this.width * 0.8F;
			float var3 = ((float)((var1 >> 1) % 2) - 0.5F) * 0.1F;
			float var4 = ((float)((var1 >> 2) % 2) - 0.5F) * this.width * 0.8F;
			int var5 = MathHelper.floor_double(this.posX + (double)var2);
			int var6 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight() + (double)var3);
			int var7 = MathHelper.floor_double(this.posZ + (double)var4);
			if(this.worldObj.isBlockNormalCube(var5, var6, var7)) {
				return true;
			}
		}

		return false;
	}

	public boolean interact(EntityPlayer var1) {
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity var1) {
		return null;
	}

	public void updateRidden() {
		if(this.ridingEntity.isDead) {
			this.ridingEntity = null;
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.onUpdate();
			if(this.ridingEntity != null) {
				this.ridingEntity.updateRiderPosition();
				this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

				for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
				}

				while(this.entityRiderYawDelta < -180.0D) {
					this.entityRiderYawDelta += 360.0D;
				}

				while(this.entityRiderPitchDelta >= 180.0D) {
					this.entityRiderPitchDelta -= 360.0D;
				}

				while(this.entityRiderPitchDelta < -180.0D) {
					this.entityRiderPitchDelta += 360.0D;
				}

				double var1 = this.entityRiderYawDelta * 0.5D;
				double var3 = this.entityRiderPitchDelta * 0.5D;
				float var5 = 10.0F;
				if(var1 > (double)var5) {
					var1 = (double)var5;
				}

				if(var1 < (double)(-var5)) {
					var1 = (double)(-var5);
				}

				if(var3 > (double)var5) {
					var3 = (double)var5;
				}

				if(var3 < (double)(-var5)) {
					var3 = (double)(-var5);
				}

				this.entityRiderYawDelta -= var1;
				this.entityRiderPitchDelta -= var3;
				this.rotationYaw = (float)((double)this.rotationYaw + var1);
				this.rotationPitch = (float)((double)this.rotationPitch + var3);
			}
		}
	}

	public void updateRiderPosition() {
		this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	}

	public double getYOffset() {
		return (double)this.yOffset;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D;
	}

	public void mountEntity(Entity var1) {
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;
		if(var1 == null) {
			if(this.ridingEntity != null) {
				this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.boundingBox.minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
				this.ridingEntity.riddenByEntity = null;
			}

			this.ridingEntity = null;
		} else if(this.ridingEntity == var1) {
			this.ridingEntity.riddenByEntity = null;
			this.ridingEntity = null;
			this.setLocationAndAngles(var1.posX, var1.boundingBox.minY + (double)var1.height, var1.posZ, this.rotationYaw, this.rotationPitch);
		} else {
			if(this.ridingEntity != null) {
				this.ridingEntity.riddenByEntity = null;
			}

			if(var1.riddenByEntity != null) {
				var1.riddenByEntity.ridingEntity = null;
			}

			this.ridingEntity = var1;
			var1.riddenByEntity = this;
		}
	}

	public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9) {
		this.setPosition(var1, var3, var5);
		this.setRotation(var7, var8);
		List var10 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(1.0D / 32.0D, 0.0D, 1.0D / 32.0D));
		if(var10.size() > 0) {
			double var11 = 0.0D;

			for(int var13 = 0; var13 < var10.size(); ++var13) {
				AxisAlignedBB var14 = (AxisAlignedBB)var10.get(var13);
				if(var14.maxY > var11) {
					var11 = var14.maxY;
				}
			}

			var3 += var11 - this.boundingBox.minY;
			this.setPosition(var1, var3, var5);
		}

	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	public Vec3D getLookVec() {
		return null;
	}

	public void setInPortal() {
	}

	public void setVelocity(double var1, double var3, double var5) {
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
	}

	public void handleHealthUpdate(byte var1) {
	}

	public void performHurtAnimation() {
	}

	public void updateCloak() {
	}

	public void outfitWithItem(int var1, int var2, int var3) {
	}

	public boolean isBurning() {
		return this.fire > 0 || this.getFlag(0);
	}

	public boolean isRiding() {
		return this.ridingEntity != null || this.getFlag(2);
	}

	public boolean isSneaking() {
		return this.getFlag(1);
	}

	public void setSneaking(boolean var1) {
		this.setFlag(1, var1);
	}

	public boolean isSprinting() {
		return this.getFlag(3);
	}

	public void setSprinting(boolean var1) {
		this.setFlag(3, var1);
	}

	public boolean isEating() {
		return this.getFlag(4);
	}

	public void setEating(boolean var1) {
		this.setFlag(4, var1);
	}

	protected boolean getFlag(int var1) {
		return (this.dataWatcher.getWatchableObjectByte(0) & 1 << var1) != 0;
	}

	protected void setFlag(int var1, boolean var2) {
		byte var3 = this.dataWatcher.getWatchableObjectByte(0);
		if(var2) {
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 | 1 << var1)));
		} else {
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 & ~(1 << var1))));
		}

	}

	public int getAir() {
		return this.dataWatcher.getWatchableObjectShort(1);
	}

	public void setAir(int var1) {
		this.dataWatcher.updateObject(1, Short.valueOf((short)var1));
	}

	public void onStruckByLightning(EntityLightningBolt var1) {
		this.dealFireDamage(5);
		++this.fire;
		if(this.fire == 0) {
			this.setFire(8);
		}

	}

	public void onKillEntity(EntityLiving var1) {
	}

	protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		double var10 = var1 - (double)var7;
		double var12 = var3 - (double)var8;
		double var14 = var5 - (double)var9;
		if(this.worldObj.isBlockNormalCube(var7, var8, var9)) {
			boolean var16 = !this.worldObj.isBlockNormalCube(var7 - 1, var8, var9);
			boolean var17 = !this.worldObj.isBlockNormalCube(var7 + 1, var8, var9);
			boolean var18 = !this.worldObj.isBlockNormalCube(var7, var8 - 1, var9);
			boolean var19 = !this.worldObj.isBlockNormalCube(var7, var8 + 1, var9);
			boolean var20 = !this.worldObj.isBlockNormalCube(var7, var8, var9 - 1);
			boolean var21 = !this.worldObj.isBlockNormalCube(var7, var8, var9 + 1);
			byte var22 = -1;
			double var23 = 9999.0D;
			if(var16 && var10 < var23) {
				var23 = var10;
				var22 = 0;
			}

			if(var17 && 1.0D - var10 < var23) {
				var23 = 1.0D - var10;
				var22 = 1;
			}

			if(var18 && var12 < var23) {
				var23 = var12;
				var22 = 2;
			}

			if(var19 && 1.0D - var12 < var23) {
				var23 = 1.0D - var12;
				var22 = 3;
			}

			if(var20 && var14 < var23) {
				var23 = var14;
				var22 = 4;
			}

			if(var21 && 1.0D - var14 < var23) {
				var23 = 1.0D - var14;
				var22 = 5;
			}

			float var25 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(var22 == 0) {
				this.motionX = (double)(-var25);
			}

			if(var22 == 1) {
				this.motionX = (double)var25;
			}

			if(var22 == 2) {
				this.motionY = (double)(-var25);
			}

			if(var22 == 3) {
				this.motionY = (double)var25;
			}

			if(var22 == 4) {
				this.motionZ = (double)(-var25);
			}

			if(var22 == 5) {
				this.motionZ = (double)var25;
			}

			return true;
		} else {
			return false;
		}
	}

	public void setInWeb() {
		this.isInWeb = true;
		this.fallDistance = 0.0F;
	}

	public Entity[] getParts() {
		return null;
	}

	public boolean isEntityEqual(Entity var1) {
		return this == var1;
	}

	public void func_48079_f(float var1) {
	}

	public boolean canAttackWithItem() {
		return true;
	}
}
