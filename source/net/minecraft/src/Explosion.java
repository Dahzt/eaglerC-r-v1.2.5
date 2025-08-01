package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

public class Explosion {
	public boolean isFlaming = false;
	private Random explosionRNG = new Random();
	private World worldObj;
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public Entity exploder;
	public float explosionSize;
	public Set destroyedBlockPositions = new HashSet();

	public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9) {
		this.worldObj = var1;
		this.exploder = var2;
		this.explosionSize = var9;
		this.explosionX = var3;
		this.explosionY = var5;
		this.explosionZ = var7;
	}

	public void doExplosionA() {
		float var1 = this.explosionSize;
		byte var2 = 16;

		int var3;
		int var4;
		int var5;
		double var15;
		double var17;
		double var19;
		for(var3 = 0; var3 < var2; ++var3) {
			for(var4 = 0; var4 < var2; ++var4) {
				for(var5 = 0; var5 < var2; ++var5) {
					if(var3 == 0 || var3 == var2 - 1 || var4 == 0 || var4 == var2 - 1 || var5 == 0 || var5 == var2 - 1) {
						double var6 = (double)((float)var3 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var8 = (double)((float)var4 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var10 = (double)((float)var5 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
						var6 /= var12;
						var8 /= var12;
						var10 /= var12;
						float var14 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						var15 = this.explosionX;
						var17 = this.explosionY;
						var19 = this.explosionZ;

						for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * (12.0F / 16.0F)) {
							int var22 = MathHelper.floor_double(var15);
							int var23 = MathHelper.floor_double(var17);
							int var24 = MathHelper.floor_double(var19);
							int var25 = this.worldObj.getBlockId(var22, var23, var24);
							if(var25 > 0) {
								var14 -= (Block.blocksList[var25].getExplosionResistance(this.exploder) + 0.3F) * var21;
							}

							if(var14 > 0.0F) {
								this.destroyedBlockPositions.add(new ChunkPosition(var22, var23, var24));
							}

							var15 += var6 * (double)var21;
							var17 += var8 * (double)var21;
							var19 += var10 * (double)var21;
						}
					}
				}
			}
		}

		this.explosionSize *= 2.0F;
		var3 = MathHelper.floor_double(this.explosionX - (double)this.explosionSize - 1.0D);
		var4 = MathHelper.floor_double(this.explosionX + (double)this.explosionSize + 1.0D);
		var5 = MathHelper.floor_double(this.explosionY - (double)this.explosionSize - 1.0D);
		int var29 = MathHelper.floor_double(this.explosionY + (double)this.explosionSize + 1.0D);
		int var7 = MathHelper.floor_double(this.explosionZ - (double)this.explosionSize - 1.0D);
		int var30 = MathHelper.floor_double(this.explosionZ + (double)this.explosionSize + 1.0D);
		List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBoxFromPool((double)var3, (double)var5, (double)var7, (double)var4, (double)var29, (double)var30));
		Vec3D var31 = Vec3D.createVector(this.explosionX, this.explosionY, this.explosionZ);

		for(int var11 = 0; var11 < var9.size(); ++var11) {
			Entity var33 = (Entity)var9.get(var11);
			double var13 = var33.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)this.explosionSize;
			if(var13 <= 1.0D) {
				var15 = var33.posX - this.explosionX;
				var17 = var33.posY - this.explosionY;
				var19 = var33.posZ - this.explosionZ;
				double var34 = (double)MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
				var15 /= var34;
				var17 /= var34;
				var19 /= var34;
				double var35 = (double)this.worldObj.getBlockDensity(var31, var33.boundingBox);
				double var36 = (1.0D - var13) * var35;
				var33.attackEntityFrom(DamageSource.explosion, (int)((var36 * var36 + var36) / 2.0D * 8.0D * (double)this.explosionSize + 1.0D));
				var33.motionX += var15 * var36;
				var33.motionY += var17 * var36;
				var33.motionZ += var19 * var36;
			}
		}

		this.explosionSize = var1;
		ArrayList var32 = new ArrayList();
		var32.addAll(this.destroyedBlockPositions);
	}

	public void doExplosionB(boolean var1) {
		this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
		this.worldObj.spawnParticle("hugeexplosion", this.explosionX, this.explosionY, this.explosionZ, 0.0D, 0.0D, 0.0D);
		ArrayList var2 = new ArrayList();
		var2.addAll(this.destroyedBlockPositions);

		int var3;
		ChunkPosition var4;
		int var5;
		int var6;
		int var7;
		int var8;
		for(var3 = var2.size() - 1; var3 >= 0; --var3) {
			var4 = (ChunkPosition)var2.get(var3);
			var5 = var4.x;
			var6 = var4.y;
			var7 = var4.z;
			var8 = this.worldObj.getBlockId(var5, var6, var7);
			if(var1) {
				double var9 = (double)((float)var5 + this.worldObj.rand.nextFloat());
				double var11 = (double)((float)var6 + this.worldObj.rand.nextFloat());
				double var13 = (double)((float)var7 + this.worldObj.rand.nextFloat());
				double var15 = var9 - this.explosionX;
				double var17 = var11 - this.explosionY;
				double var19 = var13 - this.explosionZ;
				double var21 = (double)MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
				var15 /= var21;
				var17 /= var21;
				var19 /= var21;
				double var23 = 0.5D / (var21 / (double)this.explosionSize + 0.1D);
				var23 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
				var15 *= var23;
				var17 *= var23;
				var19 *= var23;
				this.worldObj.spawnParticle("explode", (var9 + this.explosionX * 1.0D) / 2.0D, (var11 + this.explosionY * 1.0D) / 2.0D, (var13 + this.explosionZ * 1.0D) / 2.0D, var15, var17, var19);
				this.worldObj.spawnParticle("smoke", var9, var11, var13, var15, var17, var19);
			}

			if(var8 > 0) {
				Block.blocksList[var8].dropBlockAsItemWithChance(this.worldObj, var5, var6, var7, this.worldObj.getBlockMetadata(var5, var6, var7), 0.3F, 0);
				this.worldObj.setBlockWithNotify(var5, var6, var7, 0);
				Block.blocksList[var8].onBlockDestroyedByExplosion(this.worldObj, var5, var6, var7);
			}
		}

		if(this.isFlaming) {
			for(var3 = var2.size() - 1; var3 >= 0; --var3) {
				var4 = (ChunkPosition)var2.get(var3);
				var5 = var4.x;
				var6 = var4.y;
				var7 = var4.z;
				var8 = this.worldObj.getBlockId(var5, var6, var7);
				int var25 = this.worldObj.getBlockId(var5, var6 - 1, var7);
				if(var8 == 0 && Block.opaqueCubeLookup[var25] && this.explosionRNG.nextInt(3) == 0) {
					this.worldObj.setBlockWithNotify(var5, var6, var7, Block.fire.blockID);
				}
			}
		}

	}
}
