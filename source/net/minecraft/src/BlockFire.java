package net.minecraft.src;



public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int var1, int var2) {
		super(var1, var2, Material.fire);
		this.setTickRandomly(true);
	}

	public void initializeBlock() {
		this.setBurnRate(Block.planks.blockID, 5, 20);
		this.setBurnRate(Block.fence.blockID, 5, 20);
		this.setBurnRate(Block.stairCompactPlanks.blockID, 5, 20);
		this.setBurnRate(Block.wood.blockID, 5, 5);
		this.setBurnRate(Block.leaves.blockID, 30, 60);
		this.setBurnRate(Block.bookShelf.blockID, 30, 20);
		this.setBurnRate(Block.tnt.blockID, 15, 100);
		this.setBurnRate(Block.tallGrass.blockID, 60, 100);
		this.setBurnRate(Block.cloth.blockID, 30, 60);
		this.setBurnRate(Block.vine.blockID, 15, 100);
	}

	private void setBurnRate(int var1, int var2, int var3) {
		this.chanceToEncourageFire[var1] = var2;
		this.abilityToCatchFire[var1] = var3;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public int tickRate() {
		return 30;
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		boolean var6 = var1.getBlockId(var2, var3 - 1, var4) == Block.netherrack.blockID;
		if(var1.worldProvider instanceof WorldProviderEnd && var1.getBlockId(var2, var3 - 1, var4) == Block.bedrock.blockID) {
			var6 = true;
		}

		if(!this.canPlaceBlockAt(var1, var2, var3, var4)) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

		if(var6 || !var1.isRaining() || !var1.canLightningStrikeAt(var2, var3, var4) && !var1.canLightningStrikeAt(var2 - 1, var3, var4) && !var1.canLightningStrikeAt(var2 + 1, var3, var4) && !var1.canLightningStrikeAt(var2, var3, var4 - 1) && !var1.canLightningStrikeAt(var2, var3, var4 + 1)) {
			int var7 = var1.getBlockMetadata(var2, var3, var4);
			if(var7 < 15) {
				var1.setBlockMetadata(var2, var3, var4, var7 + var5.nextInt(3) / 2);
			}

			var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate() + var5.nextInt(10));
			if(!var6 && !this.canNeighborBurn(var1, var2, var3, var4)) {
				if(!var1.isBlockNormalCube(var2, var3 - 1, var4) || var7 > 3) {
					var1.setBlockWithNotify(var2, var3, var4, 0);
				}

			} else if(!var6 && !this.canBlockCatchFire(var1, var2, var3 - 1, var4) && var7 == 15 && var5.nextInt(4) == 0) {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			} else {
				boolean var8 = var1.isBlockHighHumidity(var2, var3, var4);
				byte var9 = 0;
				if(var8) {
					var9 = -50;
				}

				this.tryToCatchBlockOnFire(var1, var2 + 1, var3, var4, 300 + var9, var5, var7);
				this.tryToCatchBlockOnFire(var1, var2 - 1, var3, var4, 300 + var9, var5, var7);
				this.tryToCatchBlockOnFire(var1, var2, var3 - 1, var4, 250 + var9, var5, var7);
				this.tryToCatchBlockOnFire(var1, var2, var3 + 1, var4, 250 + var9, var5, var7);
				this.tryToCatchBlockOnFire(var1, var2, var3, var4 - 1, 300 + var9, var5, var7);
				this.tryToCatchBlockOnFire(var1, var2, var3, var4 + 1, 300 + var9, var5, var7);

				for(int var10 = var2 - 1; var10 <= var2 + 1; ++var10) {
					for(int var11 = var4 - 1; var11 <= var4 + 1; ++var11) {
						for(int var12 = var3 - 1; var12 <= var3 + 4; ++var12) {
							if(var10 != var2 || var12 != var3 || var11 != var4) {
								int var13 = 100;
								if(var12 > var3 + 1) {
									var13 += (var12 - (var3 + 1)) * 100;
								}

								int var14 = this.getChanceOfNeighborsEncouragingFire(var1, var10, var12, var11);
								if(var14 > 0) {
									int var15 = (var14 + 40) / (var7 + 30);
									if(var8) {
										var15 /= 2;
									}

									if(var15 > 0 && var5.nextInt(var13) <= var15 && (!var1.isRaining() || !var1.canLightningStrikeAt(var10, var12, var11)) && !var1.canLightningStrikeAt(var10 - 1, var12, var4) && !var1.canLightningStrikeAt(var10 + 1, var12, var11) && !var1.canLightningStrikeAt(var10, var12, var11 - 1) && !var1.canLightningStrikeAt(var10, var12, var11 + 1)) {
										int var16 = var7 + var5.nextInt(5) / 4;
										if(var16 > 15) {
											var16 = 15;
										}

										var1.setBlockAndMetadataWithNotify(var10, var12, var11, this.blockID, var16);
									}
								}
							}
						}
					}
				}

			}
		} else {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
	}

	private void tryToCatchBlockOnFire(World var1, int var2, int var3, int var4, int var5, Random var6, int var7) {
		int var8 = this.abilityToCatchFire[var1.getBlockId(var2, var3, var4)];
		if(var6.nextInt(var5) < var8) {
			boolean var9 = var1.getBlockId(var2, var3, var4) == Block.tnt.blockID;
			if(var6.nextInt(var7 + 10) < 5 && !var1.canLightningStrikeAt(var2, var3, var4)) {
				int var10 = var7 + var6.nextInt(5) / 4;
				if(var10 > 15) {
					var10 = 15;
				}

				var1.setBlockAndMetadataWithNotify(var2, var3, var4, this.blockID, var10);
			} else {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}

			if(var9) {
				Block.tnt.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
			}
		}

	}

	private boolean canNeighborBurn(World var1, int var2, int var3, int var4) {
		return this.canBlockCatchFire(var1, var2 + 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2 - 1, var3, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 - 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3 + 1, var4) ? true : (this.canBlockCatchFire(var1, var2, var3, var4 - 1) ? true : this.canBlockCatchFire(var1, var2, var3, var4 + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World var1, int var2, int var3, int var4) {
		byte var5 = 0;
		if(!var1.isAirBlock(var2, var3, var4)) {
			return 0;
		} else {
			int var6 = this.getChanceToEncourageFire(var1, var2 + 1, var3, var4, var5);
			var6 = this.getChanceToEncourageFire(var1, var2 - 1, var3, var4, var6);
			var6 = this.getChanceToEncourageFire(var1, var2, var3 - 1, var4, var6);
			var6 = this.getChanceToEncourageFire(var1, var2, var3 + 1, var4, var6);
			var6 = this.getChanceToEncourageFire(var1, var2, var3, var4 - 1, var6);
			var6 = this.getChanceToEncourageFire(var1, var2, var3, var4 + 1, var6);
			return var6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess var1, int var2, int var3, int var4) {
		return this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)] > 0;
	}

	public int getChanceToEncourageFire(World var1, int var2, int var3, int var4, int var5) {
		int var6 = this.chanceToEncourageFire[var1.getBlockId(var2, var3, var4)];
		return var6 > var5 ? var6 : var5;
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return var1.isBlockNormalCube(var2, var3 - 1, var4) || this.canNeighborBurn(var1, var2, var3, var4);
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canNeighborBurn(var1, var2, var3, var4)) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		if(var1.worldProvider.worldType > 0 || var1.getBlockId(var2, var3 - 1, var4) != Block.obsidian.blockID || !Block.portal.tryToCreatePortal(var1, var2, var3, var4)) {
			if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !this.canNeighborBurn(var1, var2, var3, var4)) {
				var1.setBlockWithNotify(var2, var3, var4, 0);
			} else {
				var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate() + var1.rand.nextInt(10));
			}
		}
	}

	public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(24) == 0) {
			var1.playSoundEffect((double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F), "fire.fire", 1.0F + var5.nextFloat(), var5.nextFloat() * 0.7F + 0.3F);
		}

		int var6;
		float var7;
		float var8;
		float var9;
		if(!var1.isBlockNormalCube(var2, var3 - 1, var4) && !Block.fire.canBlockCatchFire(var1, var2, var3 - 1, var4)) {
			if(Block.fire.canBlockCatchFire(var1, var2 - 1, var3, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat() * 0.1F;
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2 + 1, var3, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)(var2 + 1) - var5.nextFloat() * 0.1F;
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3, var4 - 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)var4 + var5.nextFloat() * 0.1F;
					var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3, var4 + 1)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)var3 + var5.nextFloat();
					var9 = (float)(var4 + 1) - var5.nextFloat() * 0.1F;
					var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}

			if(Block.fire.canBlockCatchFire(var1, var2, var3 + 1, var4)) {
				for(var6 = 0; var6 < 2; ++var6) {
					var7 = (float)var2 + var5.nextFloat();
					var8 = (float)(var3 + 1) - var5.nextFloat() * 0.1F;
					var9 = (float)var4 + var5.nextFloat();
					var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
				}
			}
		} else {
			for(var6 = 0; var6 < 3; ++var6) {
				var7 = (float)var2 + var5.nextFloat();
				var8 = (float)var3 + var5.nextFloat() * 0.5F + 0.5F;
				var9 = (float)var4 + var5.nextFloat();
				var1.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
			}
		}

	}
}
