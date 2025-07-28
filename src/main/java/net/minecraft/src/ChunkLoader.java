package net.minecraft.src;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.PeytonPlayz585.fileutils.File;

public class ChunkLoader implements IChunkLoader {
	
	private String saveDir;

	public ChunkLoader(String var1, String var2) {
		this.saveDir = var1;
		
		if(this.saveDir.endsWith("/")) {
			this.saveDir = this.saveDir + var2;
		} else {
			this.saveDir = this.saveDir + "/" + var2;
		}
		
		System.out.println(this.saveDir);
	}
	
	private static final String hex = "0123456789ABCDEF";
	
	public String chunkFileForXZOld(int x, int z) {
		int unsignedX = x + 1900000;
		int unsignedZ = z + 1900000;
		
		char[] path = new char[12];
		for(int i = 5; i >= 0; --i) {
			path[i] = hex.charAt((unsignedX >> (i * 4)) & 0xF);
			path[i + 6] = hex.charAt((unsignedZ >> (i * 4)) & 0xF);
		}
		
		String s = new String(path);
		String s1 = saveDir + "/" + s;
		return s1;
	}
	
	public String chunkFileForXZ(int x, int z) {
		int unsignedX = x + 1900000;
		int unsignedZ = z + 1900000;
		
		char[] path = new char[12];
		for(int i = 5; i >= 0; --i) {
			path[i] = hex.charAt((unsignedX >> (i * 4)) & 0xF);
			path[i + 6] = hex.charAt((unsignedZ >> (i * 4)) & 0xF);
		}
		
		String s = new String(path);
		String s1 = saveDir + "/newChunk/" + s;
		return s1;
	}

	@Override
	public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
		boolean old = false;
		String var4;
		String s;
		
		if(File.exists((s=chunkFileForXZOld(var2, var3)))) {
			old = true;
			var4 = s;
		} else {
			var4 = chunkFileForXZ(var2, var3);
		}
		
		if(!File.exists(var4)) {
			return null;
		}
		
		try {
			NBTTagCompound nbt = CompressedStreamTools.decompress(File.readFile(var4));
			if(old) {
				nbt = nbt.getCompoundTag("Level");
				return readChunkFromNBTOld(var1, nbt, var2, var3);
			} else {
				if(!nbt.hasKey("Level")) {
					System.out.println("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
					return null;
				} else if(!nbt.getCompoundTag("Level").hasKey("Sections")) {
					System.out.println("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
					return null;
				} else {
					Chunk var5 = this.readChunkFromNBT(var1, nbt.getCompoundTag("Level"));
					if(!var5.isAtLocation(var2, var3)) {
						System.out.println("Chunk file at " + var2 + "," + var3 + " is in the wrong location; relocating. (Expected " + var2 + ", " + var3 + ", got " + var5.xPosition + ", " + var5.zPosition + ")");
						nbt.setInteger("xPos", var2);
						nbt.setInteger("zPos", var3);
						var5 = this.readChunkFromNBT(var1, nbt.getCompoundTag("Level"));
					}

					var5.removeUnknownBlocks();
					return var5;
				}
			}
		} catch(IOException e) {
			System.err.println("Corrupted chunk has been found at [" + var2 + ", " + var3 + "]");
			System.err.println("Chunk will be deleted");
			File.deleteFile(var4);
			return null;
		}
	}

	@Override
	public void saveChunk(World var1, Chunk var2) throws IOException {
		NBTTagCompound chunkNBT = new NBTTagCompound();
		this.writeChunkToNBT(var2, var1, chunkNBT);
		
		byte[] data;
		try {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setCompoundTag("Level", chunkNBT);
			data = CompressedStreamTools.compress(nbt);
		} catch(IOException e) {
			System.err.println("Chunk [" + var2.xPosition + ", " + var2.zPosition + "] could not be serialized into a byte array!");
			return;
		}
		
		try {
			String file = this.chunkFileForXZ(var2.xPosition, var2.zPosition);
			String fileOld = this.chunkFileForXZOld(var2.xPosition, var2.zPosition);
			boolean old = File.exists(fileOld);
			
			if(old) {
				File.deleteFile(fileOld);
			}
			
			File.writeFile(file, data);
		} catch(Exception e) {
			System.err.println("Chunk [" + var2.xPosition + ", " + var2.zPosition + "] could not be written to file!");
		}
	}

	@Override
	public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
	}

	@Override
	public void chunkTick() {
	}

	@Override
	public void saveExtraData() {
	}
	
	private Chunk readChunkFromNBTOld(World par1World, NBTTagCompound par2NBTTagCompound, int x, int z) {
		int var3 = x;
		int var4 = z;
		Chunk var5 = new Chunk(par1World, var3, var4);
		var5.heightMap = par2NBTTagCompound.getIntArray("HeightMap");
		var5.isTerrainPopulated = par2NBTTagCompound.getBoolean("TerrainPopulated");
		NBTTagList var6 = par2NBTTagCompound.getTagList("Sections");
		byte var7 = 16;
		ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];
		boolean var9 = !par1World.worldProvider.hasNoSky;

		for (int var10 = 0; var10 < var6.tagCount(); ++var10) {
			NBTTagCompound var11 = (NBTTagCompound) var6.tagAt(var10);
			byte var12 = var11.getByte("Y");
			ExtendedBlockStorage var13 = new ExtendedBlockStorage(var12 << 4, var9);
			var13.setBlockLSBArray(var11.getByteArray("Blocks"));

			if (var11.hasKey("Add")) {
				var13.setBlockMSBArray(new NibbleArray(var11.getByteArray("Add"), 4));
			}

			var13.setBlockMetadataArray(new NibbleArray(var11.getByteArray("Data"), 4));
			var13.setBlocklightArray(new NibbleArray(var11.getByteArray("BlockLight"), 4));

			if (var9) {
				var13.setSkylightArray(new NibbleArray(var11.getByteArray("SkyLight"), 4));
			}

			var13.removeInvalidBlocks();
			var8[var12] = var13;
		}

		var5.setStorageArrays(var8);

		if (par2NBTTagCompound.hasKey("Biomes")) {
			var5.setBiomeArray(par2NBTTagCompound.getByteArray("Biomes"));
		}

		NBTTagList var17 = par2NBTTagCompound.getTagList("Entities");

		if (var17 != null) {
			for (int var18 = 0; var18 < var17.tagCount(); ++var18) {
				NBTTagCompound var20 = (NBTTagCompound) var17.tagAt(var18);
				Entity var22 = EntityList.createEntityFromNBT(var20, par1World);
				var5.hasEntities = true;

				if (var22 != null) {
					var5.addEntity(var22);
					Entity var14 = var22;

					for (NBTTagCompound var15 = var20; var15.hasKey("Riding"); var15 = var15.getCompoundTag("Riding")) {
						Entity var16 = EntityList.createEntityFromNBT(var15.getCompoundTag("Riding"), par1World);

						if (var16 != null) {
							var5.addEntity(var16);
							var14.mountEntity(var16);
						}

						var14 = var16;
					}
				}
			}
		}

		NBTTagList var19 = par2NBTTagCompound.getTagList("TileEntities");

		if (var19 != null) {
			for (int var21 = 0; var21 < var19.tagCount(); ++var21) {
				NBTTagCompound var24 = (NBTTagCompound) var19.tagAt(var21);
				TileEntity var26 = TileEntity.createAndLoadEntity(var24);

				if (var26 != null) {
					var5.addTileEntity(var26);
				}
			}
		}

		if (par2NBTTagCompound.hasKey("TileTicks")) {
			NBTTagList var23 = par2NBTTagCompound.getTagList("TileTicks");

			if (var23 != null) {
				for (int var25 = 0; var25 < var23.tagCount(); ++var25) {
					NBTTagCompound var27 = (NBTTagCompound) var23.tagAt(var25);
					par1World.scheduleBlockUpdateFromLoad(var27.getInteger("x"), var27.getInteger("y"),
							var27.getInteger("z"), var27.getInteger("i"), var27.getInteger("t"));
				}
			}
		}

		return var5;
	}
	
	private void writeChunkToNBT(Chunk var1, World var2, NBTTagCompound var3) {
		var2.checkSessionLock();
		var3.setInteger("xPos", var1.xPosition);
		var3.setInteger("zPos", var1.zPosition);
		var3.setLong("LastUpdate", var2.getWorldTime());
		var3.func_48183_a("HeightMap", var1.heightMap);
		var3.setBoolean("TerrainPopulated", var1.isTerrainPopulated);
		ExtendedBlockStorage[] var4 = var1.getBlockStorageArray();
		NBTTagList var5 = new NBTTagList("Sections");
		ExtendedBlockStorage[] var6 = var4;
		int var7 = var4.length;

		NBTTagCompound var10;
		for(int var8 = 0; var8 < var7; ++var8) {
			ExtendedBlockStorage var9 = var6[var8];
			if(var9 != null && var9.func_48700_f() != 0) {
				var10 = new NBTTagCompound();
				var10.setByte("Y", (byte)(var9.getYLocation() >> 4 & 255));
				var10.setByteArray("Blocks", var9.func_48692_g());
				if(var9.getBlockMSBArray() != null) {
					var10.setByteArray("Add", var9.getBlockMSBArray().data);
				}

				var10.setByteArray("Data", var9.func_48697_j().data);
				var10.setByteArray("SkyLight", var9.getSkylightArray().data);
				var10.setByteArray("BlockLight", var9.getBlocklightArray().data);
				var5.appendTag(var10);
			}
		}

		var3.setTag("Sections", var5);
		var3.setByteArray("Biomes", var1.getBiomeArray());
		var1.hasEntities = false;
		NBTTagList var15 = new NBTTagList();

		Iterator var17;
		for(var7 = 0; var7 < var1.entityLists.length; ++var7) {
			var17 = var1.entityLists[var7].iterator();

			while(var17.hasNext()) {
				Entity var19 = (Entity)var17.next();
				var1.hasEntities = true;
				var10 = new NBTTagCompound();
				if(var19.addEntityID(var10)) {
					var15.appendTag(var10);
				}
			}
		}

		var3.setTag("Entities", var15);
		NBTTagList var16 = new NBTTagList();
		var17 = var1.chunkTileEntityMap.values().iterator();

		while(var17.hasNext()) {
			TileEntity var20 = (TileEntity)var17.next();
			var10 = new NBTTagCompound();
			var20.writeToNBT(var10);
			var16.appendTag(var10);
		}

		var3.setTag("TileEntities", var16);
		List var18 = var2.getPendingBlockUpdates(var1, false);
		if(var18 != null) {
			long var21 = var2.getWorldTime();
			NBTTagList var11 = new NBTTagList();
			Iterator var12 = var18.iterator();

			while(var12.hasNext()) {
				NextTickListEntry var13 = (NextTickListEntry)var12.next();
				NBTTagCompound var14 = new NBTTagCompound();
				var14.setInteger("i", var13.blockID);
				var14.setInteger("x", var13.xCoord);
				var14.setInteger("y", var13.yCoord);
				var14.setInteger("z", var13.zCoord);
				var14.setInteger("t", (int)(var13.scheduledTime - var21));
				var11.appendTag(var14);
			}

			var3.setTag("TileTicks", var11);
		}

	}
	
	private Chunk readChunkFromNBT(World var1, NBTTagCompound var2) {
		int var3 = var2.getInteger("xPos");
		int var4 = var2.getInteger("zPos");
		Chunk var5 = new Chunk(var1, var3, var4);
		var5.heightMap = var2.func_48182_l("HeightMap");
		var5.isTerrainPopulated = var2.getBoolean("TerrainPopulated");
		NBTTagList var6 = var2.getTagList("Sections");
		byte var7 = 16;
		ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];

		for(int var9 = 0; var9 < var6.tagCount(); ++var9) {
			NBTTagCompound var10 = (NBTTagCompound)var6.tagAt(var9);
			byte var11 = var10.getByte("Y");
			ExtendedBlockStorage var12 = new ExtendedBlockStorage(var11 << 4);
			var12.setBlockLSBArray(var10.getByteArray("Blocks"));
			if(var10.hasKey("Add")) {
				var12.setBlockMSBArray(new NibbleArray(var10.getByteArray("Add"), 4));
			}

			var12.setBlockMetadataArray(new NibbleArray(var10.getByteArray("Data"), 4));
			var12.setSkylightArray(new NibbleArray(var10.getByteArray("SkyLight"), 4));
			var12.setBlocklightArray(new NibbleArray(var10.getByteArray("BlockLight"), 4));
			var12.func_48708_d();
			var8[var11] = var12;
		}

		var5.setStorageArrays(var8);
		if(var2.hasKey("Biomes")) {
			var5.setBiomeArray(var2.getByteArray("Biomes"));
		}

		NBTTagList var14 = var2.getTagList("Entities");
		if(var14 != null) {
			for(int var15 = 0; var15 < var14.tagCount(); ++var15) {
				NBTTagCompound var17 = (NBTTagCompound)var14.tagAt(var15);
				Entity var19 = EntityList.createEntityFromNBT(var17, var1);
				var5.hasEntities = true;
				if(var19 != null) {
					var5.addEntity(var19);
				}
			}
		}

		NBTTagList var16 = var2.getTagList("TileEntities");
		if(var16 != null) {
			for(int var18 = 0; var18 < var16.tagCount(); ++var18) {
				NBTTagCompound var21 = (NBTTagCompound)var16.tagAt(var18);
				TileEntity var13 = TileEntity.createAndLoadEntity(var21);
				if(var13 != null) {
					var5.addTileEntity(var13);
				}
			}
		}

		if(var2.hasKey("TileTicks")) {
			NBTTagList var20 = var2.getTagList("TileTicks");
			if(var20 != null) {
				for(int var22 = 0; var22 < var20.tagCount(); ++var22) {
					NBTTagCompound var23 = (NBTTagCompound)var20.tagAt(var22);
					var1.scheduleBlockUpdateFromLoad(var23.getInteger("x"), var23.getInteger("y"), var23.getInteger("z"), var23.getInteger("i"), var23.getInteger("t"));
				}
			}
		}

		return var5;
	}
	
}
