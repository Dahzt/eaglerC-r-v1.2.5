package net.minecraft.src;



public class BlockGravel extends BlockSand {
	public BlockGravel(int var1, int var2) {
		super(var1, var2);
	}

	public int idDropped(int var1, Random var2, int var3) {
		return var2.nextInt(10 - var3 * 3) == 0 ? Item.flint.shiftedIndex : this.blockID;
	}
}
