package net.minecraft.src;

public class ChunkLoader {
	public static AnvilConverterData load(NBTTagCompound var0) {
		int var1 = var0.getInteger("xPos");
		int var2 = var0.getInteger("zPos");
		AnvilConverterData var3 = new AnvilConverterData(var1, var2);
		var3.blocks = var0.getByteArray("Blocks");
		var3.data = new NibbleArrayReader(var0.getByteArray("Data"), 7);
		var3.skyLight = new NibbleArrayReader(var0.getByteArray("SkyLight"), 7);
		var3.blockLight = new NibbleArrayReader(var0.getByteArray("BlockLight"), 7);
		var3.heightmap = var0.getByteArray("HeightMap");
		var3.terrainPopulated = var0.getBoolean("TerrainPopulated");
		var3.entities = var0.getTagList("Entities");
		var3.tileEntities = var0.getTagList("TileEntities");
		var3.tileTicks = var0.getTagList("TileTicks");

		try {
			var3.lastUpdated = var0.getLong("LastUpdate");
		} catch (ClassCastException var5) {
			var3.lastUpdated = (long)var0.getInteger("LastUpdate");
		}

		return var3;
	}

	public static void convertToAnvilFormat(AnvilConverterData var0, NBTTagCompound var1, WorldChunkManager var2) {
		var1.setInteger("xPos", var0.x);
		var1.setInteger("zPos", var0.z);
		var1.setLong("LastUpdate", var0.lastUpdated);
		int[] var3 = new int[var0.heightmap.length];

		for(int var4 = 0; var4 < var0.heightmap.length; ++var4) {
			var3[var4] = var0.heightmap[var4];
		}

		var1.func_48183_a("HeightMap", var3);
		var1.setBoolean("TerrainPopulated", var0.terrainPopulated);
		NBTTagList var16 = new NBTTagList("Sections");

		int var7;
		for(int var5 = 0; var5 < 8; ++var5) {
			boolean var6 = true;

			for(var7 = 0; var7 < 16 && var6; ++var7) {
				for(int var8 = 0; var8 < 16 && var6; ++var8) {
					for(int var9 = 0; var9 < 16; ++var9) {
						int var10 = var7 << 11 | var9 << 7 | var8 + (var5 << 4);
						byte var11 = var0.blocks[var10];
						if(var11 != 0) {
							var6 = false;
							break;
						}
					}
				}
			}

			if(!var6) {
				byte[] var19 = new byte[4096];
				NibbleArray var20 = new NibbleArray(var19.length, 4);
				NibbleArray var21 = new NibbleArray(var19.length, 4);
				NibbleArray var22 = new NibbleArray(var19.length, 4);

				for(int var23 = 0; var23 < 16; ++var23) {
					for(int var12 = 0; var12 < 16; ++var12) {
						for(int var13 = 0; var13 < 16; ++var13) {
							int var14 = var23 << 11 | var13 << 7 | var12 + (var5 << 4);
							byte var15 = var0.blocks[var14];
							var19[var12 << 8 | var13 << 4 | var23] = (byte)(var15 & 255);
							var20.set(var23, var12, var13, var0.data.get(var23, var12 + (var5 << 4), var13));
							var21.set(var23, var12, var13, var0.skyLight.get(var23, var12 + (var5 << 4), var13));
							var22.set(var23, var12, var13, var0.blockLight.get(var23, var12 + (var5 << 4), var13));
						}
					}
				}

				NBTTagCompound var24 = new NBTTagCompound();
				var24.setByte("Y", (byte)(var5 & 255));
				var24.setByteArray("Blocks", var19);
				var24.setByteArray("Data", var20.data);
				var24.setByteArray("SkyLight", var21.data);
				var24.setByteArray("BlockLight", var22.data);
				var16.appendTag(var24);
			}
		}

		var1.setTag("Sections", var16);
		byte[] var17 = new byte[256];

		for(int var18 = 0; var18 < 16; ++var18) {
			for(var7 = 0; var7 < 16; ++var7) {
				var17[var7 << 4 | var18] = (byte)(var2.getBiomeGenAt(var0.x << 4 | var18, var0.z << 4 | var7).biomeID & 255);
			}
		}

		var1.setByteArray("Biomes", var17);
		var1.setTag("Entities", var0.entities);
		var1.setTag("TileEntities", var0.tileEntities);
		if(var0.tileTicks != null) {
			var1.setTag("TileTicks", var0.tileTicks);
		}

	}
}
