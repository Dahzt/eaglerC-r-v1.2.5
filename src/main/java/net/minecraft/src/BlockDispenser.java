package net.minecraft.src;



public class BlockDispenser extends BlockContainer {
	private Random random = new Random();

	protected BlockDispenser(int var1) {
		super(var1, Material.rock);
		this.blockIndexInTexture = 45;
	}

	public int tickRate() {
		return 4;
	}

	public int idDropped(int var1, Random var2, int var3) {
		return Block.dispenser.blockID;
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		super.onBlockAdded(var1, var2, var3, var4);
		this.setDispenserDefaultDirection(var1, var2, var3, var4);
	}

	private void setDispenserDefaultDirection(World var1, int var2, int var3, int var4) {
		if(!var1.isRemote) {
			int var5 = var1.getBlockId(var2, var3, var4 - 1);
			int var6 = var1.getBlockId(var2, var3, var4 + 1);
			int var7 = var1.getBlockId(var2 - 1, var3, var4);
			int var8 = var1.getBlockId(var2 + 1, var3, var4);
			byte var9 = 3;
			if(Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
				var9 = 3;
			}

			if(Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
				var9 = 2;
			}

			if(Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
				var9 = 5;
			}

			if(Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
				var9 = 4;
			}

			var1.setBlockMetadataWithNotify(var2, var3, var4, var9);
		}
	}

	public int getBlockTexture(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		if(var5 == 1) {
			return this.blockIndexInTexture + 17;
		} else if(var5 == 0) {
			return this.blockIndexInTexture + 17;
		} else {
			int var6 = var1.getBlockMetadata(var2, var3, var4);
			return var5 != var6 ? this.blockIndexInTexture : this.blockIndexInTexture + 1;
		}
	}

	public int getBlockTextureFromSide(int var1) {
		return var1 == 1 ? this.blockIndexInTexture + 17 : (var1 == 0 ? this.blockIndexInTexture + 17 : (var1 == 3 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture));
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if(var1.isRemote) {
			return true;
		} else {
			TileEntityDispenser var6 = (TileEntityDispenser)var1.getBlockTileEntity(var2, var3, var4);
			if(var6 != null) {
				var5.displayGUIDispenser(var6);
			}

			return true;
		}
	}

	private void dispenseItem(World var1, int var2, int var3, int var4, Random var5) {
		int var6 = var1.getBlockMetadata(var2, var3, var4);
		byte var9 = 0;
		byte var10 = 0;
		if(var6 == 3) {
			var10 = 1;
		} else if(var6 == 2) {
			var10 = -1;
		} else if(var6 == 5) {
			var9 = 1;
		} else {
			var9 = -1;
		}

		TileEntityDispenser var11 = (TileEntityDispenser)var1.getBlockTileEntity(var2, var3, var4);
		if(var11 != null) {
			ItemStack var12 = var11.getRandomStackFromInventory();
			double var13 = (double)var2 + (double)var9 * 0.6D + 0.5D;
			double var15 = (double)var3 + 0.5D;
			double var17 = (double)var4 + (double)var10 * 0.6D + 0.5D;
			if(var12 == null) {
				var1.playAuxSFX(1001, var2, var3, var4, 0);
			} else {
				if(var12.itemID == Item.arrow.shiftedIndex) {
					EntityArrow var27 = new EntityArrow(var1, var13, var15, var17);
					var27.setArrowHeading((double)var9, (double)0.1F, (double)var10, 1.1F, 6.0F);
					var27.doesArrowBelongToPlayer = true;
					var1.spawnEntityInWorld(var27);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.egg.shiftedIndex) {
					EntityEgg var26 = new EntityEgg(var1, var13, var15, var17);
					var26.setThrowableHeading((double)var9, (double)0.1F, (double)var10, 1.1F, 6.0F);
					var1.spawnEntityInWorld(var26);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.snowball.shiftedIndex) {
					EntitySnowball var25 = new EntitySnowball(var1, var13, var15, var17);
					var25.setThrowableHeading((double)var9, (double)0.1F, (double)var10, 1.1F, 6.0F);
					var1.spawnEntityInWorld(var25);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.potion.shiftedIndex && ItemPotion.isSplash(var12.getItemDamage())) {
					EntityPotion var24 = new EntityPotion(var1, var13, var15, var17, var12.getItemDamage());
					var24.setThrowableHeading((double)var9, (double)0.1F, (double)var10, 1.375F, 3.0F);
					var1.spawnEntityInWorld(var24);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.expBottle.shiftedIndex) {
					EntityExpBottle var19 = new EntityExpBottle(var1, var13, var15, var17);
					var19.setThrowableHeading((double)var9, (double)0.1F, (double)var10, 1.375F, 3.0F);
					var1.spawnEntityInWorld(var19);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.monsterPlacer.shiftedIndex) {
					ItemMonsterPlacer.func_48440_a(var1, var12.getItemDamage(), var13 + (double)var9 * 0.3D, var15 - 0.3D, var17 + (double)var10 * 0.3D);
					var1.playAuxSFX(1002, var2, var3, var4, 0);
				} else if(var12.itemID == Item.fireballCharge.shiftedIndex) {
					EntitySmallFireball var22 = new EntitySmallFireball(var1, var13 + (double)var9 * 0.3D, var15, var17 + (double)var10 * 0.3D, (double)var9 + var5.nextGaussian() * 0.05D, var5.nextGaussian() * 0.05D, (double)var10 + var5.nextGaussian() * 0.05D);
					var1.spawnEntityInWorld(var22);
					var1.playAuxSFX(1009, var2, var3, var4, 0);
				} else {
					EntityItem var23 = new EntityItem(var1, var13, var15 - 0.3D, var17, var12);
					double var20 = var5.nextDouble() * 0.1D + 0.2D;
					var23.motionX = (double)var9 * var20;
					var23.motionY = (double)0.2F;
					var23.motionZ = (double)var10 * var20;
					var23.motionX += var5.nextGaussian() * (double)0.0075F * 6.0D;
					var23.motionY += var5.nextGaussian() * (double)0.0075F * 6.0D;
					var23.motionZ += var5.nextGaussian() * (double)0.0075F * 6.0D;
					var1.spawnEntityInWorld(var23);
					var1.playAuxSFX(1000, var2, var3, var4, 0);
				}

				var1.playAuxSFX(2000, var2, var3, var4, var9 + 1 + (var10 + 1) * 3);
			}
		}

	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(var5 > 0 && Block.blocksList[var5].canProvidePower()) {
			boolean var6 = var1.isBlockIndirectlyGettingPowered(var2, var3, var4) || var1.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4);
			if(var6) {
				var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate());
			}
		}

	}

	public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
		if(!var1.isRemote && (var1.isBlockIndirectlyGettingPowered(var2, var3, var4) || var1.isBlockIndirectlyGettingPowered(var2, var3 + 1, var4))) {
			this.dispenseItem(var1, var2, var3, var4, var5);
		}

	}

	public TileEntity getBlockEntity() {
		return new TileEntityDispenser();
	}

	public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5) {
		int var6 = MathHelper.floor_double((double)(var5.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		if(var6 == 0) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, 2);
		}

		if(var6 == 1) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, 5);
		}

		if(var6 == 2) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, 3);
		}

		if(var6 == 3) {
			var1.setBlockMetadataWithNotify(var2, var3, var4, 4);
		}

	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		TileEntityDispenser var5 = (TileEntityDispenser)var1.getBlockTileEntity(var2, var3, var4);
		if(var5 != null) {
			for(int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
				ItemStack var7 = var5.getStackInSlot(var6);
				if(var7 != null) {
					float var8 = this.random.nextFloat() * 0.8F + 0.1F;
					float var9 = this.random.nextFloat() * 0.8F + 0.1F;
					float var10 = this.random.nextFloat() * 0.8F + 0.1F;

					while(var7.stackSize > 0) {
						int var11 = this.random.nextInt(21) + 10;
						if(var11 > var7.stackSize) {
							var11 = var7.stackSize;
						}

						var7.stackSize -= var11;
						EntityItem var12 = new EntityItem(var1, (double)((float)var2 + var8), (double)((float)var3 + var9), (double)((float)var4 + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));
						if(var7.hasTagCompound()) {
							var12.item.setTagCompound((NBTTagCompound)var7.getTagCompound().copy());
						}

						float var13 = 0.05F;
						var12.motionX = (double)((float)this.random.nextGaussian() * var13);
						var12.motionY = (double)((float)this.random.nextGaussian() * var13 + 0.2F);
						var12.motionZ = (double)((float)this.random.nextGaussian() * var13);
						var1.spawnEntityInWorld(var12);
					}
				}
			}
		}

		super.onBlockRemoval(var1, var2, var3, var4);
	}
}
