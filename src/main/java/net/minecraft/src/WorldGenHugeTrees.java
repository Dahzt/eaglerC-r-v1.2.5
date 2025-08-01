package net.minecraft.src;



public class WorldGenHugeTrees extends WorldGenerator {
	private final int field_48195_a;
	private final int woodMetadata;
	private final int leavesMetadata;

	public WorldGenHugeTrees(boolean var1, int var2, int var3, int var4) {
		super(var1);
		this.field_48195_a = var2;
		this.woodMetadata = var3;
		this.leavesMetadata = var4;
	}

	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		int var6 = var2.nextInt(3) + this.field_48195_a;
		boolean var7 = true;
		if(var4 >= 1 && var4 + var6 + 1 <= 256) {
			int var8;
			int var10;
			int var11;
			int var12;
			for(var8 = var4; var8 <= var4 + 1 + var6; ++var8) {
				byte var9 = 2;
				if(var8 == var4) {
					var9 = 1;
				}

				if(var8 >= var4 + 1 + var6 - 2) {
					var9 = 2;
				}

				for(var10 = var3 - var9; var10 <= var3 + var9 && var7; ++var10) {
					for(var11 = var5 - var9; var11 <= var5 + var9 && var7; ++var11) {
						if(var8 >= 0 && var8 < 256) {
							var12 = var1.getBlockId(var10, var8, var11);
							if(var12 != 0 && var12 != Block.leaves.blockID && var12 != Block.grass.blockID && var12 != Block.dirt.blockID && var12 != Block.wood.blockID && var12 != Block.sapling.blockID) {
								var7 = false;
							}
						} else {
							var7 = false;
						}
					}
				}
			}

			if(!var7) {
				return false;
			} else {
				var8 = var1.getBlockId(var3, var4 - 1, var5);
				if((var8 == Block.grass.blockID || var8 == Block.dirt.blockID) && var4 < 256 - var6 - 1) {
					var1.setBlock(var3, var4 - 1, var5, Block.dirt.blockID);
					var1.setBlock(var3 + 1, var4 - 1, var5, Block.dirt.blockID);
					var1.setBlock(var3, var4 - 1, var5 + 1, Block.dirt.blockID);
					var1.setBlock(var3 + 1, var4 - 1, var5 + 1, Block.dirt.blockID);
					this.func_48192_a(var1, var3, var5, var4 + var6, 2, var2);

					for(int var14 = var4 + var6 - 2 - var2.nextInt(4); var14 > var4 + var6 / 2; var14 -= 2 + var2.nextInt(4)) {
						float var15 = var2.nextFloat() * (float)Math.PI * 2.0F;
						var11 = var3 + (int)(0.5F + MathHelper.cos(var15) * 4.0F);
						var12 = var5 + (int)(0.5F + MathHelper.sin(var15) * 4.0F);
						this.func_48192_a(var1, var11, var12, var14, 0, var2);

						for(int var13 = 0; var13 < 5; ++var13) {
							var11 = var3 + (int)(1.5F + MathHelper.cos(var15) * (float)var13);
							var12 = var5 + (int)(1.5F + MathHelper.sin(var15) * (float)var13);
							this.setBlockAndMetadata(var1, var11, var14 - 3 + var13 / 2, var12, Block.wood.blockID, this.woodMetadata);
						}
					}

					for(var10 = 0; var10 < var6; ++var10) {
						var11 = var1.getBlockId(var3, var4 + var10, var5);
						if(var11 == 0 || var11 == Block.leaves.blockID) {
							this.setBlockAndMetadata(var1, var3, var4 + var10, var5, Block.wood.blockID, this.woodMetadata);
							if(var10 > 0) {
								if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 - 1, var4 + var10, var5)) {
									this.setBlockAndMetadata(var1, var3 - 1, var4 + var10, var5, Block.vine.blockID, 8);
								}

								if(var2.nextInt(3) > 0 && var1.isAirBlock(var3, var4 + var10, var5 - 1)) {
									this.setBlockAndMetadata(var1, var3, var4 + var10, var5 - 1, Block.vine.blockID, 1);
								}
							}
						}

						if(var10 < var6 - 1) {
							var11 = var1.getBlockId(var3 + 1, var4 + var10, var5);
							if(var11 == 0 || var11 == Block.leaves.blockID) {
								this.setBlockAndMetadata(var1, var3 + 1, var4 + var10, var5, Block.wood.blockID, this.woodMetadata);
								if(var10 > 0) {
									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 + 2, var4 + var10, var5)) {
										this.setBlockAndMetadata(var1, var3 + 2, var4 + var10, var5, Block.vine.blockID, 2);
									}

									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 + 1, var4 + var10, var5 - 1)) {
										this.setBlockAndMetadata(var1, var3 + 1, var4 + var10, var5 - 1, Block.vine.blockID, 1);
									}
								}
							}

							var11 = var1.getBlockId(var3 + 1, var4 + var10, var5 + 1);
							if(var11 == 0 || var11 == Block.leaves.blockID) {
								this.setBlockAndMetadata(var1, var3 + 1, var4 + var10, var5 + 1, Block.wood.blockID, this.woodMetadata);
								if(var10 > 0) {
									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 + 2, var4 + var10, var5 + 1)) {
										this.setBlockAndMetadata(var1, var3 + 2, var4 + var10, var5 + 1, Block.vine.blockID, 2);
									}

									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 + 1, var4 + var10, var5 + 2)) {
										this.setBlockAndMetadata(var1, var3 + 1, var4 + var10, var5 + 2, Block.vine.blockID, 4);
									}
								}
							}

							var11 = var1.getBlockId(var3, var4 + var10, var5 + 1);
							if(var11 == 0 || var11 == Block.leaves.blockID) {
								this.setBlockAndMetadata(var1, var3, var4 + var10, var5 + 1, Block.wood.blockID, this.woodMetadata);
								if(var10 > 0) {
									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3 - 1, var4 + var10, var5 + 1)) {
										this.setBlockAndMetadata(var1, var3 - 1, var4 + var10, var5 + 1, Block.vine.blockID, 8);
									}

									if(var2.nextInt(3) > 0 && var1.isAirBlock(var3, var4 + var10, var5 + 2)) {
										this.setBlockAndMetadata(var1, var3, var4 + var10, var5 + 2, Block.vine.blockID, 4);
									}
								}
							}
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	private void func_48192_a(World var1, int var2, int var3, int var4, int var5, Random var6) {
		byte var7 = 2;

		for(int var8 = var4 - var7; var8 <= var4; ++var8) {
			int var9 = var8 - var4;
			int var10 = var5 + 1 - var9;

			for(int var11 = var2 - var10; var11 <= var2 + var10 + 1; ++var11) {
				int var12 = var11 - var2;

				for(int var13 = var3 - var10; var13 <= var3 + var10 + 1; ++var13) {
					int var14 = var13 - var3;
					if((var12 >= 0 || var14 >= 0 || var12 * var12 + var14 * var14 <= var10 * var10) && (var12 <= 0 && var14 <= 0 || var12 * var12 + var14 * var14 <= (var10 + 1) * (var10 + 1)) && (var6.nextInt(4) != 0 || var12 * var12 + var14 * var14 <= (var10 - 1) * (var10 - 1)) && !Block.opaqueCubeLookup[var1.getBlockId(var11, var8, var13)]) {
						this.setBlockAndMetadata(var1, var11, var8, var13, Block.leaves.blockID, this.leavesMetadata);
					}
				}
			}
		}

	}
}
