package net.minecraft.src;



public class WorldGenBigMushroom extends WorldGenerator {
	private int mushroomType = -1;

	public WorldGenBigMushroom(int var1) {
		super(true);
		this.mushroomType = var1;
	}

	public WorldGenBigMushroom() {
		super(false);
	}

	public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
		int var6 = var2.nextInt(2);
		if(this.mushroomType >= 0) {
			var6 = this.mushroomType;
		}

		int var7 = var2.nextInt(3) + 4;
		boolean var8 = true;
		if(var4 >= 1 && var4 + var7 + 1 < 256) {
			int var9;
			int var11;
			int var12;
			int var13;
			for(var9 = var4; var9 <= var4 + 1 + var7; ++var9) {
				byte var10 = 3;
				if(var9 == var4) {
					var10 = 0;
				}

				for(var11 = var3 - var10; var11 <= var3 + var10 && var8; ++var11) {
					for(var12 = var5 - var10; var12 <= var5 + var10 && var8; ++var12) {
						if(var9 >= 0 && var9 < 256) {
							var13 = var1.getBlockId(var11, var9, var12);
							if(var13 != 0 && var13 != Block.leaves.blockID) {
								var8 = false;
							}
						} else {
							var8 = false;
						}
					}
				}
			}

			if(!var8) {
				return false;
			} else {
				var9 = var1.getBlockId(var3, var4 - 1, var5);
				if(var9 != Block.dirt.blockID && var9 != Block.grass.blockID && var9 != Block.mycelium.blockID) {
					return false;
				} else if(!Block.mushroomBrown.canPlaceBlockAt(var1, var3, var4, var5)) {
					return false;
				} else {
					this.setBlockAndMetadata(var1, var3, var4 - 1, var5, Block.dirt.blockID, 0);
					int var16 = var4 + var7;
					if(var6 == 1) {
						var16 = var4 + var7 - 3;
					}

					for(var11 = var16; var11 <= var4 + var7; ++var11) {
						var12 = 1;
						if(var11 < var4 + var7) {
							++var12;
						}

						if(var6 == 0) {
							var12 = 3;
						}

						for(var13 = var3 - var12; var13 <= var3 + var12; ++var13) {
							for(int var14 = var5 - var12; var14 <= var5 + var12; ++var14) {
								int var15 = 5;
								if(var13 == var3 - var12) {
									--var15;
								}

								if(var13 == var3 + var12) {
									++var15;
								}

								if(var14 == var5 - var12) {
									var15 -= 3;
								}

								if(var14 == var5 + var12) {
									var15 += 3;
								}

								if(var6 == 0 || var11 < var4 + var7) {
									if((var13 == var3 - var12 || var13 == var3 + var12) && (var14 == var5 - var12 || var14 == var5 + var12)) {
										continue;
									}

									if(var13 == var3 - (var12 - 1) && var14 == var5 - var12) {
										var15 = 1;
									}

									if(var13 == var3 - var12 && var14 == var5 - (var12 - 1)) {
										var15 = 1;
									}

									if(var13 == var3 + (var12 - 1) && var14 == var5 - var12) {
										var15 = 3;
									}

									if(var13 == var3 + var12 && var14 == var5 - (var12 - 1)) {
										var15 = 3;
									}

									if(var13 == var3 - (var12 - 1) && var14 == var5 + var12) {
										var15 = 7;
									}

									if(var13 == var3 - var12 && var14 == var5 + (var12 - 1)) {
										var15 = 7;
									}

									if(var13 == var3 + (var12 - 1) && var14 == var5 + var12) {
										var15 = 9;
									}

									if(var13 == var3 + var12 && var14 == var5 + (var12 - 1)) {
										var15 = 9;
									}
								}

								if(var15 == 5 && var11 < var4 + var7) {
									var15 = 0;
								}

								if((var15 != 0 || var4 >= var4 + var7 - 1) && !Block.opaqueCubeLookup[var1.getBlockId(var13, var11, var14)]) {
									this.setBlockAndMetadata(var1, var13, var11, var14, Block.mushroomCapBrown.blockID + var6, var15);
								}
							}
						}
					}

					for(var11 = 0; var11 < var7; ++var11) {
						var12 = var1.getBlockId(var3, var4 + var11, var5);
						if(!Block.opaqueCubeLookup[var12]) {
							this.setBlockAndMetadata(var1, var3, var4 + var11, var5, Block.mushroomCapBrown.blockID + var6, 10);
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}
}
