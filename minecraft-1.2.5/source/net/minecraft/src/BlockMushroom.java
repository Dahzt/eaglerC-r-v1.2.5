package net.minecraft.src;



public class BlockMushroom extends BlockFlower {
	protected BlockMushroom(int var1, int var2) {
		super(var1, var2);
		float var3 = 0.2F;
		this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var3 * 2.0F, 0.5F + var3);
		this.setTickRandomly(true);
	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if(var5.nextInt(25) == 0) {
			byte var6 = 4;
			int var7 = 5;

			int var8;
			int var9;
			int var10;
			for(var8 = var2 - var6; var8 <= var2 + var6; ++var8) {
				for(var9 = var4 - var6; var9 <= var4 + var6; ++var9) {
					for(var10 = var3 - 1; var10 <= var3 + 1; ++var10) {
						if(var1.getBlockId(var8, var10, var9) == this.blockID) {
							--var7;
							if(var7 <= 0) {
								return;
							}
						}
					}
				}
			}

			var8 = var2 + var5.nextInt(3) - 1;
			var9 = var3 + var5.nextInt(2) - var5.nextInt(2);
			var10 = var4 + var5.nextInt(3) - 1;

			for(int var11 = 0; var11 < 4; ++var11) {
				if(var1.isAirBlock(var8, var9, var10) && this.canBlockStay(var1, var8, var9, var10)) {
					var2 = var8;
					var3 = var9;
					var4 = var10;
				}

				var8 = var2 + var5.nextInt(3) - 1;
				var9 = var3 + var5.nextInt(2) - var5.nextInt(2);
				var10 = var4 + var5.nextInt(3) - 1;
			}

			if(var1.isAirBlock(var8, var9, var10) && this.canBlockStay(var1, var8, var9, var10)) {
				var1.setBlockWithNotify(var8, var9, var10, this.blockID);
			}
		}

	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return super.canPlaceBlockAt(var1, var2, var3, var4) && this.canBlockStay(var1, var2, var3, var4);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int var1) {
		return Block.opaqueCubeLookup[var1];
	}

	public boolean canBlockStay(World var1, int var2, int var3, int var4) {
		if(var3 >= 0 && var3 < 256) {
			int var5 = var1.getBlockId(var2, var3 - 1, var4);
			return var5 == Block.mycelium.blockID || var1.getFullBlockLightValue(var2, var3, var4) < 13 && this.canThisPlantGrowOnThisBlockID(var5);
		} else {
			return false;
		}
	}

	public boolean fertilizeMushroom(World var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getBlockMetadata(var2, var3, var4);
		var1.setBlock(var2, var3, var4, 0);
		WorldGenBigMushroom var7 = null;
		if(this.blockID == Block.mushroomBrown.blockID) {
			var7 = new WorldGenBigMushroom(0);
		} else if(this.blockID == Block.mushroomRed.blockID) {
			var7 = new WorldGenBigMushroom(1);
		}

		if(var7 != null && var7.generate(var1, var5, var2, var3, var4)) {
			return true;
		} else {
			var1.setBlockAndMetadata(var2, var3, var4, this.blockID, var6);
			return false;
		}
	}
}
