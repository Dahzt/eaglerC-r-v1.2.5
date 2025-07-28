package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.PeytonPlayz585.fileutils.File;

public class EaglerSaveHandler implements ISaveHandler {
	
	private String saveDir;
	
	public EaglerSaveHandler(String s) {
		this.saveDir = s;
	}

	@Override
	public WorldInfo loadWorldInfo() {
		String file = File.makePath(this.saveDir, "level.dat");
		byte[] data = File.readFile(file);
		if(data != null) {
			try {
				NBTBase nbt = NBTBase.readNamedTag(new DataInputStream(new ByteArrayInputStream(data)));
				if(nbt instanceof NBTTagCompound) {
					return new WorldInfo((NBTTagCompound)nbt);
				}
			} catch (IOException e) {
				System.err.println("Failed to load world data for '" + file + "'");
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void checkSessionLock() {
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider var1) {
		if(var1 instanceof WorldProviderHell) {
			return new ChunkLoader(saveDir, "/c1");
		}else if(var1 instanceof WorldProviderEnd) {
			return new ChunkLoader(saveDir, "/c2");
		} else {
			return new ChunkLoader(saveDir, "/c0");
		}
	}

	@Override
	public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
		DataOutputStream ds = new DataOutputStream(out);
		String file = File.makePath(this.saveDir, "level.dat");
		try {
			NBTBase.writeNamedTag(var1.getNBTTagCompoundWithPlayers(var2), ds);
		} catch (IOException e) {
			System.err.println("Failed to serialize world data for '" + file + "'");
			e.printStackTrace();
			return;
		}
		File.writeFile(file, out.toByteArray());
	}

	@Override
	public void saveWorldInfo(WorldInfo var1) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(8192);
		DataOutputStream ds = new DataOutputStream(out);
		String file = File.makePath(this.saveDir, "level.dat");
		try {
			NBTBase.writeNamedTag(var1.getNBTTagCompound(), ds);
		} catch (IOException e) {
			System.err.println("Failed to serialize world data for '" + file + "/level.dat'");
			e.printStackTrace();
			return;
		}
		File.writeFile(file, out.toByteArray());
	}

	@Override
	public String getMapFileFromName(String var1) {
		return this.saveDir + "/" + var1 + ".dat";
	}

	@Override
	public String getSaveDirectoryName() {
		return saveDir;
	}

}
