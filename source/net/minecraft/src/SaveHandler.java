package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SaveHandler implements ISaveHandler {
	private static final Logger logger = Logger.getLogger("Minecraft");
	private final File saveDirectory;
	private final File playersDirectory;
	private final File mapDataDir;
	private final long initializationTime = System.currentTimeMillis();
	private final String saveDirectoryName;

	public SaveHandler(File var1, String var2, boolean var3) {
		this.saveDirectory = new File(var1, var2);
		this.saveDirectory.mkdirs();
		this.playersDirectory = new File(this.saveDirectory, "players");
		this.mapDataDir = new File(this.saveDirectory, "data");
		this.mapDataDir.mkdirs();
		this.saveDirectoryName = var2;
		if(var3) {
			this.playersDirectory.mkdirs();
		}

		this.setSessionLock();
	}

	private void setSessionLock() {
		try {
			File var1 = new File(this.saveDirectory, "session.lock");
			DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));

			try {
				var2.writeLong(this.initializationTime);
			} finally {
				var2.close();
			}

		} catch (IOException var7) {
			var7.printStackTrace();
			throw new RuntimeException("Failed to check session lock, aborting");
		}
	}

	protected File getSaveDirectory() {
		return this.saveDirectory;
	}

	public void checkSessionLock() {
		try {
			File var1 = new File(this.saveDirectory, "session.lock");
			DataInputStream var2 = new DataInputStream(new FileInputStream(var1));

			try {
				if(var2.readLong() != this.initializationTime) {
					throw new MinecraftException("The save is being accessed from another location, aborting");
				}
			} finally {
				var2.close();
			}

		} catch (IOException var7) {
			throw new MinecraftException("Failed to check session lock, aborting");
		}
	}

	public IChunkLoader getChunkLoader(WorldProvider var1) {
		throw new RuntimeException("Old Chunk Storage is no longer supported.");
	}

	public WorldInfo loadWorldInfo() {
		File var1 = new File(this.saveDirectory, "level.dat");
		NBTTagCompound var2;
		NBTTagCompound var3;
		if(var1.exists()) {
			try {
				var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
				var3 = var2.getCompoundTag("Data");
				return new WorldInfo(var3);
			} catch (Exception var5) {
				var5.printStackTrace();
			}
		}

		var1 = new File(this.saveDirectory, "level.dat_old");
		if(var1.exists()) {
			try {
				var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
				var3 = var2.getCompoundTag("Data");
				return new WorldInfo(var3);
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
		NBTTagCompound var3 = var1.getNBTTagCompoundWithPlayers(var2);
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setTag("Data", var3);

		try {
			File var5 = new File(this.saveDirectory, "level.dat_new");
			File var6 = new File(this.saveDirectory, "level.dat_old");
			File var7 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(var4, new FileOutputStream(var5));
			if(var6.exists()) {
				var6.delete();
			}

			var7.renameTo(var6);
			if(var7.exists()) {
				var7.delete();
			}

			var5.renameTo(var7);
			if(var5.exists()) {
				var5.delete();
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

	}

	public void saveWorldInfo(WorldInfo var1) {
		NBTTagCompound var2 = var1.getNBTTagCompound();
		NBTTagCompound var3 = new NBTTagCompound();
		var3.setTag("Data", var2);

		try {
			File var4 = new File(this.saveDirectory, "level.dat_new");
			File var5 = new File(this.saveDirectory, "level.dat_old");
			File var6 = new File(this.saveDirectory, "level.dat");
			CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));
			if(var5.exists()) {
				var5.delete();
			}

			var6.renameTo(var5);
			if(var6.exists()) {
				var6.delete();
			}

			var4.renameTo(var6);
			if(var4.exists()) {
				var4.delete();
			}
		} catch (Exception var7) {
			var7.printStackTrace();
		}

	}

	public File getMapFileFromName(String var1) {
		return new File(this.mapDataDir, var1 + ".dat");
	}

	public String getSaveDirectoryName() {
		return this.saveDirectoryName;
	}
}
