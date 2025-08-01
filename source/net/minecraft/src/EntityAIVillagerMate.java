package net.minecraft.src;



public class EntityAIVillagerMate extends EntityAIBase {
	private EntityVillager villagerObj;
	private EntityVillager mate;
	private World worldObj;
	private int matingTimeout = 0;
	Village villageObj;

	public EntityAIVillagerMate(EntityVillager var1) {
		this.villagerObj = var1;
		this.worldObj = var1.worldObj;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		if(this.villagerObj.getGrowingAge() != 0) {
			return false;
		} else if(this.villagerObj.getRNG().nextInt(500) != 0) {
			return false;
		} else {
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.villagerObj.posX), MathHelper.floor_double(this.villagerObj.posY), MathHelper.floor_double(this.villagerObj.posZ), 0);
			if(this.villageObj == null) {
				return false;
			} else if(!this.checkSufficientDoorsPresentForNewVillager()) {
				return false;
			} else {
				Entity var1 = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.boundingBox.expand(8.0D, 3.0D, 8.0D), this.villagerObj);
				if(var1 == null) {
					return false;
				} else {
					this.mate = (EntityVillager)var1;
					return this.mate.getGrowingAge() == 0;
				}
			}
		}
	}

	public void startExecuting() {
		this.matingTimeout = 300;
		this.villagerObj.setIsMatingFlag(true);
	}

	public void resetTask() {
		this.villageObj = null;
		this.mate = null;
		this.villagerObj.setIsMatingFlag(false);
	}

	public boolean continueExecuting() {
		return this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0;
	}

	public void updateTask() {
		--this.matingTimeout;
		this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
		if(this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D) {
			this.villagerObj.getNavigator().func_48667_a(this.mate, 0.25F);
		} else if(this.matingTimeout == 0 && this.mate.getIsMatingFlag()) {
			this.giveBirth();
		}

		if(this.villagerObj.getRNG().nextInt(35) == 0) {
			this.spawnHeartParticles(this.villagerObj);
		}

	}

	private boolean checkSufficientDoorsPresentForNewVillager() {
		int var1 = (int)((double)((float)this.villageObj.getNumVillageDoors()) * 0.35D);
		return this.villageObj.getNumVillagers() < var1;
	}

	private void giveBirth() {
		EntityVillager var1 = new EntityVillager(this.worldObj);
		this.mate.setGrowingAge(6000);
		this.villagerObj.setGrowingAge(6000);
		var1.setGrowingAge(-24000);
		var1.setProfession(this.villagerObj.getRNG().nextInt(5));
		var1.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
		this.worldObj.spawnEntityInWorld(var1);
		this.spawnHeartParticles(var1);
	}

	private void spawnHeartParticles(EntityLiving var1) {
		Random var2 = var1.getRNG();

		for(int var3 = 0; var3 < 5; ++var3) {
			double var4 = var2.nextGaussian() * 0.02D;
			double var6 = var2.nextGaussian() * 0.02D;
			double var8 = var2.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle("heart", var1.posX + (double)(var2.nextFloat() * var1.width * 2.0F) - (double)var1.width, var1.posY + 1.0D + (double)(var2.nextFloat() * var1.height), var1.posZ + (double)(var2.nextFloat() * var1.width * 2.0F) - (double)var1.width, var4, var6, var8);
		}

	}
}
