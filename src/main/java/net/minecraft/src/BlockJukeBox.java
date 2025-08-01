package net.minecraft.src;

public class BlockJukeBox extends BlockContainer {
	protected BlockJukeBox(int var1, int var2) {
		super(var1, var2, Material.wood);
	}

	public int getBlockTextureFromSide(int var1) {
		return this.blockIndexInTexture + (var1 == 1 ? 1 : 0);
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if(var1.getBlockMetadata(var2, var3, var4) == 0) {
			return false;
		} else {
			this.ejectRecord(var1, var2, var3, var4);
			return true;
		}
	}

	public void insertRecord(World var1, int var2, int var3, int var4, int var5) {
		if(!var1.isRemote) {
			TileEntityRecordPlayer var6 = (TileEntityRecordPlayer)var1.getBlockTileEntity(var2, var3, var4);
			if(var6 != null) {
				var6.record = var5;
				var6.onInventoryChanged();
				var1.setBlockMetadataWithNotify(var2, var3, var4, 1);
			}
		}
	}

	public void ejectRecord(World var1, int var2, int var3, int var4) {
		if(!var1.isRemote) {
			TileEntityRecordPlayer var5 = (TileEntityRecordPlayer)var1.getBlockTileEntity(var2, var3, var4);
			if(var5 != null) {
				int var6 = var5.record;
				if(var6 != 0) {
					var1.playAuxSFX(1005, var2, var3, var4, 0);
					var1.playRecord((String)null, var2, var3, var4);
					var5.record = 0;
					var5.onInventoryChanged();
					var1.setBlockMetadataWithNotify(var2, var3, var4, 0);
					float var8 = 0.7F;
					double var9 = (double)(var1.rand.nextFloat() * var8) + (double)(1.0F - var8) * 0.5D;
					double var11 = (double)(var1.rand.nextFloat() * var8) + (double)(1.0F - var8) * 0.2D + 0.6D;
					double var13 = (double)(var1.rand.nextFloat() * var8) + (double)(1.0F - var8) * 0.5D;
					EntityItem var15 = new EntityItem(var1, (double)var2 + var9, (double)var3 + var11, (double)var4 + var13, new ItemStack(var6, 1, 0));
					var15.delayBeforeCanPickup = 10;
					var1.spawnEntityInWorld(var15);
				}
			}
		}
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		this.ejectRecord(var1, var2, var3, var4);
		super.onBlockRemoval(var1, var2, var3, var4);
	}

	public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
		if(!var1.isRemote) {
			super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, 0);
		}
	}

	public TileEntity getBlockEntity() {
		return new TileEntityRecordPlayer();
	}
}
