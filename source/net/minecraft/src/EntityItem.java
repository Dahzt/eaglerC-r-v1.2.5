package net.minecraft.src;

public class EntityItem extends Entity {
	public ItemStack item;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float field_804_d = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World var1, double var2, double var4, double var6, ItemStack var8) {
		super(var1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var4, var6);
		this.item = var8;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityItem(World var1) {
		super(var1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	protected void entityInit() {
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		if(this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) == Material.lava) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float var1 = 0.98F;
		if(this.onGround) {
			var1 = 0.1F * 0.1F * 58.8F;
			int var2 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
			if(var2 > 0) {
				var1 = Block.blocksList[var2].slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)var1;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)var1;
		if(this.onGround) {
			this.motionY *= -0.5D;
		}

		++this.age;
		if(this.age >= 6000) {
			this.setDead();
		}

	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
	}

	protected void dealFireDamage(int var1) {
		this.attackEntityFrom(DamageSource.inFire, var1);
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		this.setBeenAttacked();
		this.health -= var2;
		if(this.health <= 0) {
			this.setDead();
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("Health", (short)((byte)this.health));
		var1.setShort("Age", (short)this.age);
		var1.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.health = var1.getShort("Health") & 255;
		this.age = var1.getShort("Age");
		NBTTagCompound var2 = var1.getCompoundTag("Item");
		this.item = ItemStack.loadItemStackFromNBT(var2);
		if(this.item == null) {
			this.setDead();
		}

	}

	public void onCollideWithPlayer(EntityPlayer var1) {
		if(!this.worldObj.isRemote) {
			int var2 = this.item.stackSize;
			if(this.delayBeforeCanPickup == 0 && var1.inventory.addItemStackToInventory(this.item)) {
				if(this.item.itemID == Block.wood.blockID) {
					var1.triggerAchievement(AchievementList.mineWood);
				}

				if(this.item.itemID == Item.leather.shiftedIndex) {
					var1.triggerAchievement(AchievementList.killCow);
				}

				if(this.item.itemID == Item.diamond.shiftedIndex) {
					var1.triggerAchievement(AchievementList.diamonds);
				}

				if(this.item.itemID == Item.blazeRod.shiftedIndex) {
					var1.triggerAchievement(AchievementList.blazeRod);
				}

				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				var1.onItemPickup(this, var2);
				if(this.item.stackSize <= 0) {
					this.setDead();
				}
			}

		}
	}

	public boolean canAttackWithItem() {
		return false;
	}
}
