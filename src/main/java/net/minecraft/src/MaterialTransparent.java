package net.minecraft.src;

public class MaterialTransparent extends Material {
	public MaterialTransparent(MapColor var1) {
		super(var1);
		this.setGroundCover();
	}

	public boolean isSolid() {
		return false;
	}

	public boolean getCanBlockGrass() {
		return false;
	}

	public boolean blocksMovement() {
		return false;
	}
}
