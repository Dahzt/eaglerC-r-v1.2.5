package net.minecraft.src;

import java.util.List;

public class EntityPigZombie extends EntityZombie {
	private int angerLevel = 0;
	private int randomSoundDelay = 0;
	private static final ItemStack defaultHeldItem = new ItemStack(Item.swordGold, 1);

	public EntityPigZombie(World var1) {
		super(var1);
		this.texture = "/mob/pigzombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 5;
		this.isImmuneToFire = true;
	}

	protected boolean isAIEnabled() {
		return false;
	}

	public void onUpdate() {
		this.moveSpeed = this.entityToAttack != null ? 0.95F : 0.5F;
		if(this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
			this.worldObj.playSoundAtEntity(this, "mob.zombiepig.zpigangry", this.getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		super.onUpdate();
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.difficultySetting > 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setShort("Anger", (short)this.angerLevel);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.angerLevel = var1.getShort("Anger");
	}

	protected Entity findPlayerToAttack() {
		return this.angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		Entity var3 = var1.getEntity();
		if(var3 instanceof EntityPlayer) {
			List var4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(32.0D, 32.0D, 32.0D));

			for(int var5 = 0; var5 < var4.size(); ++var5) {
				Entity var6 = (Entity)var4.get(var5);
				if(var6 instanceof EntityPigZombie) {
					EntityPigZombie var7 = (EntityPigZombie)var6;
					var7.becomeAngryAt(var3);
				}
			}

			this.becomeAngryAt(var3);
		}

		return super.attackEntityFrom(var1, var2);
	}

	private void becomeAngryAt(Entity var1) {
		this.entityToAttack = var1;
		this.angerLevel = 400 + this.rand.nextInt(400);
		this.randomSoundDelay = this.rand.nextInt(40);
	}

	protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}

	protected String getHurtSound() {
		return "mob.zombiepig.zpighurt";
	}

	protected String getDeathSound() {
		return "mob.zombiepig.zpigdeath";
	}

	protected void dropFewItems(boolean var1, int var2) {
		int var3 = this.rand.nextInt(2 + var2);

		int var4;
		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.rottenFlesh.shiftedIndex, 1);
		}

		var3 = this.rand.nextInt(2 + var2);

		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.goldNugget.shiftedIndex, 1);
		}

	}

	protected void dropRareDrop(int var1) {
		if(var1 > 0) {
			ItemStack var2 = new ItemStack(Item.swordGold);
			EnchantmentHelper.func_48441_a(this.rand, var2, 5);
			this.entityDropItem(var2, 0.0F);
		} else {
			int var3 = this.rand.nextInt(3);
			if(var3 == 0) {
				this.dropItem(Item.ingotGold.shiftedIndex, 1);
			} else if(var3 == 1) {
				this.dropItem(Item.swordGold.shiftedIndex, 1);
			} else if(var3 == 2) {
				this.dropItem(Item.helmetGold.shiftedIndex, 1);
			}
		}

	}

	protected int getDropItemId() {
		return Item.rottenFlesh.shiftedIndex;
	}

	public ItemStack getHeldItem() {
		return defaultHeldItem;
	}
}
