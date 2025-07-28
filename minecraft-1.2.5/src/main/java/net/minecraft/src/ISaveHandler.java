package net.minecraft.src;

import java.util.List;

public interface ISaveHandler {
	WorldInfo loadWorldInfo();

	void checkSessionLock();

	IChunkLoader getChunkLoader(WorldProvider var1);

	void saveWorldInfoAndPlayer(WorldInfo var1, List var2);

	void saveWorldInfo(WorldInfo var1);

	String getMapFileFromName(String var1);

	String getSaveDirectoryName();
}
