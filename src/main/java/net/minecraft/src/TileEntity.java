package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.PeytonPlayz585.minecraft.EntityUtils;

public class TileEntity {
	private static Map nameToClassMap = new HashMap();
	private static Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	protected boolean tileEntityInvalid;
	public int blockMetadata = -1;
	public Block blockType;

	private static void addMapping(Class var0, String var1) {
		if(classToNameMap.containsKey(var1)) {
			throw new IllegalArgumentException("Duplicate id: " + var1);
		} else {
			nameToClassMap.put(var1, var0);
			classToNameMap.put(var0, var1);
		}
	}

	public void readFromNBT(NBTTagCompound var1) {
		this.xCoord = var1.getInteger("x");
		this.yCoord = var1.getInteger("y");
		this.zCoord = var1.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound var1) {
		String var2 = (String)classToNameMap.get(this.getClass());
		if(var2 == null) {
			throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
		} else {
			var1.setString("id", var2);
			var1.setInteger("x", this.xCoord);
			var1.setInteger("y", this.yCoord);
			var1.setInteger("z", this.zCoord);
		}
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound var0) {
		TileEntity var1 = null;

		try {
			Class var2 = (Class)nameToClassMap.get(var0.getString("id"));
			if(var2 != null) {
				var1 = EntityUtils.newInstanceTileEntity(var2);
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}

		if(var1 != null) {
			var1.readFromNBT(var0);
		} else {
			System.out.println("Skipping TileEntity with id " + var0.getString("id"));
		}

		return var1;
	}

	public int getBlockMetadata() {
		if(this.blockMetadata == -1) {
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		}

		return this.blockMetadata;
	}

	public void onInventoryChanged() {
		if(this.worldObj != null) {
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
		}

	}

	public double getDistanceFrom(double var1, double var3, double var5) {
		double var7 = (double)this.xCoord + 0.5D - var1;
		double var9 = (double)this.yCoord + 0.5D - var3;
		double var11 = (double)this.zCoord + 0.5D - var5;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public Block getBlockType() {
		if(this.blockType == null) {
			this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
		}

		return this.blockType;
	}

	public boolean isInvalid() {
		return this.tileEntityInvalid;
	}

	public void invalidate() {
		this.tileEntityInvalid = true;
	}

	public void validate() {
		this.tileEntityInvalid = false;
	}

	public void onTileEntityPowered(int var1, int var2) {
	}

	public void updateContainingBlockInfo() {
		this.blockType = null;
		this.blockMetadata = -1;
	}

	static {
		addMapping(TileEntityFurnace.class, "Furnace");
		addMapping(TileEntityChest.class, "Chest");
		addMapping(TileEntityRecordPlayer.class, "RecordPlayer");
		addMapping(TileEntityDispenser.class, "Trap");
		addMapping(TileEntitySign.class, "Sign");
		addMapping(TileEntityMobSpawner.class, "MobSpawner");
		addMapping(TileEntityNote.class, "Music");
		addMapping(TileEntityPiston.class, "Piston");
		addMapping(TileEntityBrewingStand.class, "Cauldron");
		addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
		addMapping(TileEntityEndPortal.class, "Airportal");
	}
}
