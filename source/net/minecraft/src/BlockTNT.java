package net.minecraft.src;



public class BlockTNT extends Block {
	public BlockTNT(int var1, int var2) {
		super(var1, var2, Material.tnt);
	}

	public int getBlockTextureFromSide(int var1) {
		return var1 == 0 ? this.blockIndexInTexture + 2 : (var1 == 1 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public void onBlockAdded(World var1, int var2, int var3, int var4) {
		super.onBlockAdded(var1, var2, var3, var4);
		if(var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
			this.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		if(var5 > 0 && Block.blocksList[var5].canProvidePower() && var1.isBlockIndirectlyGettingPowered(var2, var3, var4)) {
			this.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
			var1.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public void onBlockDestroyedByExplosion(World var1, int var2, int var3, int var4) {
		if(!var1.isRemote) {
			EntityTNTPrimed var5 = new EntityTNTPrimed(var1, (double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F));
			var5.fuse = var1.rand.nextInt(var5.fuse / 4) + var5.fuse / 8;
			var1.spawnEntityInWorld(var5);
		}
	}

	public void onBlockDestroyedByPlayer(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.isRemote) {
			if((var5 & 1) == 0) {
				this.dropBlockAsItem_do(var1, var2, var3, var4, new ItemStack(Block.tnt.blockID, 1, 0));
			} else {
				EntityTNTPrimed var6 = new EntityTNTPrimed(var1, (double)((float)var2 + 0.5F), (double)((float)var3 + 0.5F), (double)((float)var4 + 0.5F));
				var1.spawnEntityInWorld(var6);
				var1.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
			}

		}
	}

	public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		super.onBlockClicked(var1, var2, var3, var4, var5);
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if(var5.getCurrentEquippedItem() != null && var5.getCurrentEquippedItem().itemID == Item.flintAndSteel.shiftedIndex) {
			this.onBlockDestroyedByPlayer(var1, var2, var3, var4, 1);
			var1.setBlockWithNotify(var2, var3, var4, 0);
			return true;
		} else {
			return super.blockActivated(var1, var2, var3, var4, var5);
		}
	}

	protected ItemStack createStackedBlock(int var1) {
		return null;
	}
}
