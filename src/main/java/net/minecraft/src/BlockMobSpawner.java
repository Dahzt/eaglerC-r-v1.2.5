package net.minecraft.src;



public class BlockMobSpawner extends BlockContainer {
	protected BlockMobSpawner(int var1, int var2) {
		super(var1, var2, Material.rock);
	}

	public TileEntity getBlockEntity() {
		return new TileEntityMobSpawner();
	}

	public int idDropped(int var1, Random var2, int var3) {
		return 0;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public boolean isOpaqueCube() {
		return false;
	}
}
