package net.minecraft.src;

import java.util.List;

public class SaveHandlerMP implements ISaveHandler {
	public WorldInfo loadWorldInfo() {
		return null;
	}

	public void checkSessionLock() {
	}

	public IChunkLoader getChunkLoader(WorldProvider var1) {
		return null;
	}

	public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
	}

	public void saveWorldInfo(WorldInfo var1) {
	}

	public String getMapFileFromName(String var1) {
		return null;
	}

	public String getSaveDirectoryName() {
		return "none";
	}
}
