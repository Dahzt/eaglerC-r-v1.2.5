package net.minecraft.src;

import java.util.Iterator;


public class BlockChest extends BlockContainer {
	private Random random = new Random();

	protected BlockChest(int var1) {
		super(var1, Material.wood);
		this.blockIndexInTexture = 26;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public int getRenderType() {
		return 22;
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		super.onBlockAdded(var1, var2, var3, var4);
		this.unifyAdjacentChests(var1, var2, var3, var4);
		int var5 = var1.getBlockId(var2, var3, var4 - 1);
		int var6 = var1.getBlockId(var2, var3, var4 + 1);
		int var7 = var1.getBlockId(var2 - 1, var3, var4);
		int var8 = var1.getBlockId(var2 + 1, var3, var4);
		if(var5 == this.blockID) {
			this.unifyAdjacentChests(var1, var2, var3, var4 - 1);
		}

		if(var6 == this.blockID) {
			this.unifyAdjacentChests(var1, var2, var3, var4 + 1);
		}

		if(var7 == this.blockID) {
			this.unifyAdjacentChests(var1, var2 - 1, var3, var4);
		}

		if(var8 == this.blockID) {
			this.unifyAdjacentChests(var1, var2 + 1, var3, var4);
		}

	}

	public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5) {
		int var6 = var1.getBlockId(var2, var3, var4 - 1);
		int var7 = var1.getBlockId(var2, var3, var4 + 1);
		int var8 = var1.getBlockId(var2 - 1, var3, var4);
		int var9 = var1.getBlockId(var2 + 1, var3, var4);
		byte var10 = 0;
		int var11 = MathHelper.floor_double((double)(var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(var11 == 0) {
			var10 = 2;
		}

		if(var11 == 1) {
			var10 = 5;
		}

		if(var11 == 2) {
			var10 = 3;
		}

		if(var11 == 3) {
			var10 = 4;
		}

		if(var6 != this.blockID && var7 != this.blockID && var8 != this.blockID && var9 != this.blockID) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, var10);
		} else {
			if((var6 == this.blockID || var7 == this.blockID) && (var10 == 4 || var10 == 5)) {
				if(var6 == this.blockID) {
					var1.setBlockMetadataWithNotify(var2, var3, var4 - 1, var10);
				} else {
					var1.setBlockMetadataWithNotify(var2, var3, var4 + 1, var10);
				}

				var1.setBlockMetadataWithNotify(var2, var3, var4, var10);
			}

			if((var8 == this.blockID || var9 == this.blockID) && (var10 == 2 || var10 == 3)) {
				if(var8 == this.blockID) {
					var1.setBlockMetadataWithNotify(var2 - 1, var3, var4, var10);
				} else {
					var1.setBlockMetadataWithNotify(var2 + 1, var3, var4, var10);
				}

				var1.setBlockMetadataWithNotify(var2, var3, var4, var10);
			}
		}

	}

	public void unifyAdjacentChests(World var1, int var2, int var3, int var4) {
		if(!var1.isRemote) {
			int var5 = var1.getBlockId(var2, var3, var4 - 1);
			int var6 = var1.getBlockId(var2, var3, var4 + 1);
			int var7 = var1.getBlockId(var2 - 1, var3, var4);
			int var8 = var1.getBlockId(var2 + 1, var3, var4);
			boolean var9 = true;
			int var10;
			int var11;
			boolean var12;
			byte var13;
			int var14;
			if(var5 != this.blockID && var6 != this.blockID) {
				if(var7 != this.blockID && var8 != this.blockID) {
					var13 = 3;
					if(Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
						var13 = 3;
					}

					if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
						var13 = 2;
					}

					if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
						var13 = 5;
					}

					if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
						var13 = 4;
					}
				} else {
					var10 = var1.getBlockId(var7 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 - 1);
					var11 = var1.getBlockId(var7 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 + 1);
					var13 = 3;
					var12 = true;
					if(var7 == this.blockID) {
						var14 = var1.getBlockMetadata(var2 - 1, var3, var4);
					} else {
						var14 = var1.getBlockMetadata(var2 + 1, var3, var4);
					}

					if(var14 == 2) {
						var13 = 2;
					}

					if((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11]) {
						var13 = 3;
					}

					if((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var10]) {
						var13 = 2;
					}
				}
			} else {
				var10 = var1.getBlockId(var2 - 1, var3, var5 == this.blockID ? var4 - 1 : var4 + 1);
				var11 = var1.getBlockId(var2 + 1, var3, var5 == this.blockID ? var4 - 1 : var4 + 1);
				var13 = 5;
				var12 = true;
				if(var5 == this.blockID) {
					var14 = var1.getBlockMetadata(var2, var3, var4 - 1);
				} else {
					var14 = var1.getBlockMetadata(var2, var3, var4 + 1);
				}

				if(var14 == 4) {
					var13 = 4;
				}

				if((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
					var13 = 5;
				}

				if((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var10]) {
					var13 = 4;
				}
			}

			var1.setBlockMetadataWithNotify(var2, var3, var4, var13);
		}
	}

	public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			return this.blockIndexInTexture - 1;
		} else if(var5 == 0) {
			return this.blockIndexInTexture - 1;
		} else {
			int var6 = var1.getBlockId(var2, var3, var4 - 1);
			int var7 = var1.getBlockId(var2, var3, var4 + 1);
			int var8 = var1.getBlockId(var2 - 1, var3, var4);
			int var9 = var1.getBlockId(var2 + 1, var3, var4);
			int var10;
			int var11;
			int var12;
			byte var13;
			if(var6 != this.blockID && var7 != this.blockID) {
				if(var8 != this.blockID && var9 != this.blockID) {
					byte var14 = 3;
					if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var7]) {
						var14 = 3;
					}

					if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var6]) {
						var14 = 2;
					}

					if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var9]) {
						var14 = 5;
					}

					if(Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var8]) {
						var14 = 4;
					}

					return var5 == var14 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture;
				} else if(var5 != 4 && var5 != 5) {
					var10 = 0;
					if(var8 == this.blockID) {
						var10 = -1;
					}

					var11 = var1.getBlockId(var8 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 - 1);
					var12 = var1.getBlockId(var8 == this.blockID ? var2 - 1 : var2 + 1, var3, var4 + 1);
					if(var5 == 3) {
						var10 = -1 - var10;
					}

					var13 = 3;
					if((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var12]) {
						var13 = 3;
					}

					if((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11]) {
						var13 = 2;
					}

					return (var5 == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
				} else {
					return this.blockIndexInTexture;
				}
			} else if(var5 != 2 && var5 != 3) {
				var10 = 0;
				if(var6 == this.blockID) {
					var10 = -1;
				}

				var11 = var1.getBlockId(var2 - 1, var3, var6 == this.blockID ? var4 - 1 : var4 + 1);
				var12 = var1.getBlockId(var2 + 1, var3, var6 == this.blockID ? var4 - 1 : var4 + 1);
				if(var5 == 4) {
					var10 = -1 - var10;
				}

				var13 = 5;
				if((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var9] && !Block.opaqueCubeLookup[var12]) {
					var13 = 5;
				}

				if((Block.opaqueCubeLookup[var9] || Block.opaqueCubeLookup[var12]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
					var13 = 4;
				}

				return (var5 == var13 ? this.blockIndexInTexture + 16 : this.blockIndexInTexture + 32) + var10;
			} else {
				return this.blockIndexInTexture;
			}
		}
	}

	public int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? this.blockIndexInTexture - 1 : (var1 == 0 ? this.blockIndexInTexture - 1 : (var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		int var5 = 0;
		if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2, var3, var4 - 1) == this.blockID) {
			++var5;
		}

		if(var1.getBlockId(var2, var3, var4 + 1) == this.blockID) {
			++var5;
		}

		return var5 > 1 ? false : (this.isThereANeighborChest(var1, var2 - 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2 + 1, var3, var4) ? false : (this.isThereANeighborChest(var1, var2, var3, var4 - 1) ? false : !this.isThereANeighborChest(var1, var2, var3, var4 + 1))));
	}

	private boolean isThereANeighborChest(World var1, int var2, int var3, int var4) {
		return var1.getBlockId(var2, var3, var4) != this.blockID ? false : (var1.getBlockId(var2 - 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2 + 1, var3, var4) == this.blockID ? true : (var1.getBlockId(var2, var3, var4 - 1) == this.blockID ? true : var1.getBlockId(var2, var3, var4 + 1) == this.blockID)));
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		super.onNeighborBlockChange(var1, var2, var3, var4, var5);
		TileEntityChest var6 = (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4);
		if(var6 != null) {
			var6.updateContainingBlockInfo();
		}

	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		TileEntityChest var5 = (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4);
		if(var5 != null) {
			for(int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
				ItemStack var7 = var5.getStackInSlot(var6);
				if(var7 != null) {
					float var8 = this.random.nextFloat() * 0.8F + 0.1F;
					float var9 = this.random.nextFloat() * 0.8F + 0.1F;

					EntityItem var12;
					for(float var10 = this.random.nextFloat() * 0.8F + 0.1F; var7.stackSize > 0; var1.spawnEntityInWorld(var12)) {
						int var11 = this.random.nextInt(21) + 10;
						if(var11 > var7.stackSize) {
							var11 = var7.stackSize;
						}

						var7.stackSize -= var11;
						var12 = new EntityItem(var1, (double)((float)var2 + var8), (double)((float)var3 + var9), (double)((float)var4 + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));
						float var13 = 0.05F;
						var12.motionX = (double)((float)this.random.nextGaussian() * var13);
						var12.motionY = (double)((float)this.random.nextGaussian() * var13 + 0.2F);
						var12.motionZ = (double)((float)this.random.nextGaussian() * var13);
						if(var7.hasTagCompound()) {
							var12.item.setTagCompound((NBTTagCompound)var7.getTagCompound().copy());
						}
					}
				}
			}
		}

		super.onBlockRemoval(var1, var2, var3, var4);
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		Object var6 = (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4);
		if(var6 == null) {
			return true;
		} else if(var1.isBlockNormalCube(var2, var3 + 1, var4)) {
			return true;
		} else if(func_50075_j(var1, var2, var3, var4)) {
			return true;
		} else if(var1.getBlockId(var2 - 1, var3, var4) != this.blockID || !var1.isBlockNormalCube(var2 - 1, var3 + 1, var4) && !func_50075_j(var1, var2 - 1, var3, var4)) {
			if(var1.getBlockId(var2 + 1, var3, var4) != this.blockID || !var1.isBlockNormalCube(var2 + 1, var3 + 1, var4) && !func_50075_j(var1, var2 + 1, var3, var4)) {
				if(var1.getBlockId(var2, var3, var4 - 1) != this.blockID || !var1.isBlockNormalCube(var2, var3 + 1, var4 - 1) && !func_50075_j(var1, var2, var3, var4 - 1)) {
					if(var1.getBlockId(var2, var3, var4 + 1) != this.blockID || !var1.isBlockNormalCube(var2, var3 + 1, var4 + 1) && !func_50075_j(var1, var2, var3, var4 + 1)) {
						if(var1.getBlockId(var2 - 1, var3, var4) == this.blockID) {
							var6 = new InventoryLargeChest("Large chest", (TileEntityChest)var1.getBlockTileEntity(var2 - 1, var3, var4), (IInventory)var6);
						}

						if(var1.getBlockId(var2 + 1, var3, var4) == this.blockID) {
							var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)var1.getBlockTileEntity(var2 + 1, var3, var4));
						}

						if(var1.getBlockId(var2, var3, var4 - 1) == this.blockID) {
							var6 = new InventoryLargeChest("Large chest", (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4 - 1), (IInventory)var6);
						}

						if(var1.getBlockId(var2, var3, var4 + 1) == this.blockID) {
							var6 = new InventoryLargeChest("Large chest", (IInventory)var6, (TileEntityChest)var1.getBlockTileEntity(var2, var3, var4 + 1));
						}

						if(var1.isRemote) {
							return true;
						} else {
							var5.displayGUIChest((IInventory)var6);
							return true;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	public TileEntity getBlockEntity() {
		return new TileEntityChest();
	}

	private static boolean func_50075_j(World var0, int var1, int var2, int var3) {
		Iterator var4 = var0.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBoxFromPool((double)var1, (double)(var2 + 1), (double)var3, (double)(var1 + 1), (double)(var2 + 2), (double)(var3 + 1))).iterator();

		EntityOcelot var6;
		do {
			if(!var4.hasNext()) {
				return false;
			}

			Entity var5 = (Entity)var4.next();
			var6 = (EntityOcelot)var5;
		} while(!var6.isSitting());

		return true;
	}
}
