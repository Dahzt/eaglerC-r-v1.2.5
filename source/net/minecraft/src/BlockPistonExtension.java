package net.minecraft.src;

import java.util.ArrayList;


public class BlockPistonExtension extends Block {
	private int headTexture = -1;

	public BlockPistonExtension(int var1, int var2) {
		super(var1, var2, Material.piston);
		this.setStepSound(soundStoneFootstep);
		this.setHardness(0.5F);
	}

	public void setHeadTexture(int var1) {
		this.headTexture = var1;
	}

	public void clearHeadTexture() {
		this.headTexture = -1;
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		super.onBlockRemoval(var1, var2, var3, var4);
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		int var6 = Facing.faceToSide[getDirectionMeta(var5)];
		var2 += Facing.offsetsXForSide[var6];
		var3 += Facing.offsetsYForSide[var6];
		var4 += Facing.offsetsZForSide[var6];
		int var7 = var1.getBlockId(var2, var3, var4);
		if(var7 == Block.pistonBase.blockID || var7 == Block.pistonStickyBase.blockID) {
			var5 = var1.getBlockMetadata(var2, var3, var4);
			if(BlockPistonBase.isExtended(var5)) {
				Block.blocksList[var7].dropBlockAsItem(var1, var2, var3, var4, var5, 0);
				var1.setBlockWithNotify(var2, var3, var4, 0);
			}
		}

	}

	public int getBlockTextureFromSideAndMetadata(int var1, int var2) {
		int var3 = getDirectionMeta(var2);
		return var1 == var3 ? (this.headTexture >= 0 ? this.headTexture : ((var2 & 8) != 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture)) : (var1 == Facing.faceToSide[var3] ? 107 : 108);
	}

	public int getRenderType() {
		return 17;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		return false;
	}

	public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
		return false;
	}

	public int quantityDropped(Random var1) {
		return 0;
	}

	public void getCollidingBoundingBoxes(World var1, int var2, int var3, int var4, AxisAlignedBB var5, ArrayList var6) {
		int var7 = var1.getBlockMetadata(var2, var3, var4);
		switch(getDirectionMeta(var7)) {
		case 0:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(6.0F / 16.0F, 0.25F, 6.0F / 16.0F, 10.0F / 16.0F, 1.0F, 10.0F / 16.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			break;
		case 1:
			this.setBlockBounds(0.0F, 12.0F / 16.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(6.0F / 16.0F, 0.0F, 6.0F / 16.0F, 10.0F / 16.0F, 12.0F / 16.0F, 10.0F / 16.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			break;
		case 2:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(0.25F, 6.0F / 16.0F, 0.25F, 12.0F / 16.0F, 10.0F / 16.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			break;
		case 3:
			this.setBlockBounds(0.0F, 0.0F, 12.0F / 16.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(0.25F, 6.0F / 16.0F, 0.0F, 12.0F / 16.0F, 10.0F / 16.0F, 12.0F / 16.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			break;
		case 4:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(6.0F / 16.0F, 0.25F, 0.25F, 10.0F / 16.0F, 12.0F / 16.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			break;
		case 5:
			this.setBlockBounds(12.0F / 16.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
			this.setBlockBounds(0.0F, 6.0F / 16.0F, 0.25F, 12.0F / 16.0F, 10.0F / 16.0F, 12.0F / 16.0F);
			super.getCollidingBoundingBoxes(var1, var2, var3, var4, var5, var6);
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
		int var5 = var1.getBlockMetadata(var2, var3, var4);
		switch(getDirectionMeta(var5)) {
		case 0:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
			break;
		case 1:
			this.setBlockBounds(0.0F, 12.0F / 16.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case 2:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
			break;
		case 3:
			this.setBlockBounds(0.0F, 0.0F, 12.0F / 16.0F, 1.0F, 1.0F, 1.0F);
			break;
		case 4:
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
			break;
		case 5:
			this.setBlockBounds(12.0F / 16.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}

	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		int var6 = getDirectionMeta(var1.getBlockMetadata(var2, var3, var4));
		int var7 = var1.getBlockId(var2 - Facing.offsetsXForSide[var6], var3 - Facing.offsetsYForSide[var6], var4 - Facing.offsetsZForSide[var6]);
		if(var7 != Block.pistonBase.blockID && var7 != Block.pistonStickyBase.blockID) {
			var1.setBlockWithNotify(var2, var3, var4, 0);
		} else {
			Block.blocksList[var7].onNeighborBlockChange(var1, var2 - Facing.offsetsXForSide[var6], var3 - Facing.offsetsYForSide[var6], var4 - Facing.offsetsZForSide[var6], var5);
		}

	}

	public static int getDirectionMeta(int var0) {
		return var0 & 7;
	}
}
