package net.minecraft.src;



public class BlockVine extends Block {
	public BlockVine(int var1) {
		super(var1, 143, Material.vine);
		this.setTickRandomly(true);
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public int getRenderType() {
		return 20;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		int var6 = var1.getBlockMetadata(var2, var3, var4);
		float var7 = 1.0F;
		float var8 = 1.0F;
		float var9 = 1.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = var6 > 0;
		if((var6 & 2) != 0) {
			var10 = Math.max(var10, 1.0F / 16.0F);
			var7 = 0.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if((var6 & 8) != 0) {
			var7 = Math.min(var7, 15.0F / 16.0F);
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if((var6 & 4) != 0) {
			var12 = Math.max(var12, 1.0F / 16.0F);
			var9 = 0.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if((var6 & 1) != 0) {
			var9 = Math.min(var9, 15.0F / 16.0F);
			var12 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if(!var13 && this.canBePlacedOn(var1.getBlockId(var2, var3 + 1, var4))) {
			var8 = Math.min(var8, 15.0F / 16.0F);
			var11 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
		}

		this.setBlockBounds(var7, var8, var9, var10, var11, var12);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
		return null;
	}

	public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
		switch(var5) {
		case 1:
			return this.canBePlacedOn(var1.getBlockId(var2, var3 + 1, var4));
		case 2:
			return this.canBePlacedOn(var1.getBlockId(var2, var3, var4 + 1));
		case 3:
			return this.canBePlacedOn(var1.getBlockId(var2, var3, var4 - 1));
		case 4:
			return this.canBePlacedOn(var1.getBlockId(var2 + 1, var3, var4));
		case 5:
			return this.canBePlacedOn(var1.getBlockId(var2 - 1, var3, var4));
		default:
			return false;
		}
	}

	private boolean canBePlacedOn(int var1) {
		if(var1 == 0) {
			return false;
		} else {
			Block var2 = Block.blocksList[var1];
			return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
		}
	}

	private boolean canVineStay(World var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		int var6 = var5;
		if(var5 > 0) {
			for(int var7 = 0; var7 <= 3; ++var7) {
				int var8 = 1 << var7;
				if((var5 & var8) != 0 && !this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var7], var3, var4 + Direction.offsetZ[var7])) && (var1.getBlockId(var2, var3 + 1, var4) != this.blockID || (var1.getBlockMetadata(var2, var3 + 1, var4) & var8) == 0)) {
					var6 &= ~var8;
				}
			}
		}

		if(var6 == 0 && !this.canBePlacedOn(var1.getBlockId(var2, var3 + 1, var4))) {
			return false;
		} else {
			if(var6 != var5) {
				var1.setBlockMetadataWithNotify(var2, var3, var4, var6);
			}

			return true;
		}
	}

	public int getBlockColor() {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int getRenderColor(int var1) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	public int colorMultiplier(IBlockAccess var1, int var2, int var3, int var4) {
		return var1.getBiomeGenForCoords(var2, var4).getBiomeFoliageColor();
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.isRemote && !this.canVineStay(var1, var2, var3, var4)) {
			this.dropBlockAsItem(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if(!var1.isRemote && var1.rand.nextInt(4) == 0) {
			byte var6 = 4;
			int var7 = 5;
			boolean var8 = false;

			int var9;
			int var10;
			int var11;
			label141:
			for(var9 = var2 - var6; var9 <= var2 + var6; ++var9) {
				for(var10 = var4 - var6; var10 <= var4 + var6; ++var10) {
					for(var11 = var3 - 1; var11 <= var3 + 1; ++var11) {
						if(var1.getBlockId(var9, var11, var10) == this.blockID) {
							--var7;
							if(var7 <= 0) {
								var8 = true;
								break label141;
							}
						}
					}
				}
			}

			var9 = var1.getBlockMetadata(var2, var3, var4);
			var10 = var1.rand.nextInt(6);
			var11 = Direction.vineGrowth[var10];
			int var12;
			int var13;
			if(var10 == 1 && var3 < 255 && var1.isAirBlock(var2, var3 + 1, var4)) {
				if(var8) {
					return;
				}

				var12 = var1.rand.nextInt(16) & var9;
				if(var12 > 0) {
					for(var13 = 0; var13 <= 3; ++var13) {
						if(!this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var13], var3 + 1, var4 + Direction.offsetZ[var13]))) {
							var12 &= ~(1 << var13);
						}
					}

					if(var12 > 0) {
						var1.setBlockAndMetadataWithNotify(var2, var3 + 1, var4, this.blockID, var12);
					}
				}
			} else {
				int var14;
				if(var10 >= 2 && var10 <= 5 && (var9 & 1 << var11) == 0) {
					if(var8) {
						return;
					}

					var12 = var1.getBlockId(var2 + Direction.offsetX[var11], var3, var4 + Direction.offsetZ[var11]);
					if(var12 != 0 && Block.blocksList[var12] != null) {
						if(Block.blocksList[var12].blockMaterial.isOpaque() && Block.blocksList[var12].renderAsNormalBlock()) {
							var1.setBlockMetadataWithNotify(var2, var3, var4, var9 | 1 << var11);
						}
					} else {
						var13 = var11 + 1 & 3;
						var14 = var11 + 3 & 3;
						if((var9 & 1 << var13) != 0 && this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var11] + Direction.offsetX[var13], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var13]))) {
							var1.setBlockAndMetadataWithNotify(var2 + Direction.offsetX[var11], var3, var4 + Direction.offsetZ[var11], this.blockID, 1 << var13);
						} else if((var9 & 1 << var14) != 0 && this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var11] + Direction.offsetX[var14], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var14]))) {
							var1.setBlockAndMetadataWithNotify(var2 + Direction.offsetX[var11], var3, var4 + Direction.offsetZ[var11], this.blockID, 1 << var14);
						} else if((var9 & 1 << var13) != 0 && var1.isAirBlock(var2 + Direction.offsetX[var11] + Direction.offsetX[var13], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var13]) && this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var13], var3, var4 + Direction.offsetZ[var13]))) {
							var1.setBlockAndMetadataWithNotify(var2 + Direction.offsetX[var11] + Direction.offsetX[var13], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var13], this.blockID, 1 << (var11 + 2 & 3));
						} else if((var9 & 1 << var14) != 0 && var1.isAirBlock(var2 + Direction.offsetX[var11] + Direction.offsetX[var14], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var14]) && this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var14], var3, var4 + Direction.offsetZ[var14]))) {
							var1.setBlockAndMetadataWithNotify(var2 + Direction.offsetX[var11] + Direction.offsetX[var14], var3, var4 + Direction.offsetZ[var11] + Direction.offsetZ[var14], this.blockID, 1 << (var11 + 2 & 3));
						} else if(this.canBePlacedOn(var1.getBlockId(var2 + Direction.offsetX[var11], var3 + 1, var4 + Direction.offsetZ[var11]))) {
							var1.setBlockAndMetadataWithNotify(var2 + Direction.offsetX[var11], var3, var4 + Direction.offsetZ[var11], this.blockID, 0);
						}
					}
				} else if(var3 > 1) {
					var12 = var1.getBlockId(var2, var3 - 1, var4);
					if(var12 == 0) {
						var13 = var1.rand.nextInt(16) & var9;
						if(var13 > 0) {
							var1.setBlockAndMetadataWithNotify(var2, var3 - 1, var4, this.blockID, var13);
						}
					} else if(var12 == this.blockID) {
						var13 = var1.rand.nextInt(16) & var9;
						var14 = var1.getBlockMetadata(var2, var3 - 1, var4);
						if(var14 != (var14 | var13)) {
							var1.setBlockMetadataWithNotify(var2, var3 - 1, var4, var14 | var13);
						}
					}
				}
			}
		}

	}

	public void onBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
		byte var6 = 0;
		switch(var5) {
		case 2:
			var6 = 1;
			break;
		case 3:
			var6 = 4;
			break;
		case 4:
			var6 = 8;
			break;
		case 5:
			var6 = 2;
		}

		if(var6 != 0) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, var6);
		}

	}

	public int idDropped(int var1, Random var2, int var3) {
		return 0;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public void harvestBlock(World var1, EntityPlayer var2, int var3, int var4, int var5, int var6) {
		if(!var1.isRemote && var2.getCurrentEquippedItem() != null && var2.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			var2.addStat(StatList.mineBlockStatArray[this.blockID], 1);
			this.dropBlockAsItem_do(var1, var3, var4, var5, new ItemStack(Block.vine, 1, 0));
		} else {
			super.harvestBlock(var1, var2, var3, var4, var5, var6);
		}

	}
}
