package net.minecraft.src;

public class EntityZombie extends EntityMob {
	public EntityZombie(World var1) {
		super(var1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.23F;
		this.attackStrength = 4;
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIBreakDoor(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, this.moveSpeed, true));
		this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
		this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, this.moveSpeed, false));
		this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
	}

	public int getMaxHealth() {
		return 20;
	}

	public int getTotalArmorValue() {
		return 2;
	}

	protected boolean isAIEnabled() {
		return true;
	}

	public void onLivingUpdate() {
		if(this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float var1 = this.getBrightness(1.0F);
			if(var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F) {
				this.setFire(8);
			}
		}

		super.onLivingUpdate();
	}

	protected String getLivingSound() {
		return "mob.zombie";
	}

	protected String getHurtSound() {
		return "mob.zombiehurt";
	}

	protected String getDeathSound() {
		return "mob.zombiedeath";
	}

	protected int getDropItemId() {
		return Item.rottenFlesh.shiftedIndex;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	protected void dropRareDrop(int var1) {
		switch(this.rand.nextInt(4)) {
		case 0:
			this.dropItem(Item.swordSteel.shiftedIndex, 1);
			break;
		case 1:
			this.dropItem(Item.helmetSteel.shiftedIndex, 1);
			break;
		case 2:
			this.dropItem(Item.ingotIron.shiftedIndex, 1);
			break;
		case 3:
			this.dropItem(Item.shovelSteel.shiftedIndex, 1);
		}

	}
}
