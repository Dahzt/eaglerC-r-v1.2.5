package net.minecraft.src;

public class EntityAIMoveTwardsRestriction extends EntityAIBase {
	private EntityCreature theEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private float field_48352_e;

	public EntityAIMoveTwardsRestriction(EntityCreature var1, float var2) {
		this.theEntity = var1;
		this.field_48352_e = var2;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.theEntity.isWithinHomeDistanceCurrentPosition()) {
			return false;
		} else {
			ChunkCoordinates var1 = this.theEntity.getHomePosition();
			Vec3D var2 = RandomPositionGenerator.func_48620_a(this.theEntity, 16, 7, Vec3D.createVector((double)var1.posX, (double)var1.posY, (double)var1.posZ));
			if(var2 == null) {
				return false;
			} else {
				this.movePosX = var2.xCoord;
				this.movePosY = var2.yCoord;
				this.movePosZ = var2.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting() {
		return !this.theEntity.getNavigator().noPath();
	}

	public void startExecuting() {
		this.theEntity.getNavigator().func_48666_a(this.movePosX, this.movePosY, this.movePosZ, this.field_48352_e);
	}
}
