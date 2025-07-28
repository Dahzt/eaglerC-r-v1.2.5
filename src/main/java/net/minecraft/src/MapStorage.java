package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage {
	private ISaveHandler saveHandler;
	private Map loadedDataMap = new HashMap();
	private List loadedDataList = new ArrayList();
	private Map idCounts = new HashMap();

	public MapStorage(ISaveHandler var1) {
		this.saveHandler = var1;
		this.loadIdCounts();
	}

	public WorldSavedData loadData(Class var1, String var2) {
		return (WorldSavedData) this.loadedDataMap.get(var2);
	}

	public void setData(String var1, WorldSavedData var2) {
		if (var2 == null) {
			throw new RuntimeException("Can\'t set null data");
		} else {
			if (this.loadedDataMap.containsKey(var1)) {
				this.loadedDataList.remove(this.loadedDataMap.remove(var1));
			}

			this.loadedDataMap.put(var1, var2);
			this.loadedDataList.add(var2);
		}
	}

	public void saveAllData() {
		for(int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
			WorldSavedData var2 = (WorldSavedData)this.loadedDataList.get(var1);
			if(var2.isDirty()) {
				this.saveData(var2);
				var2.setDirty(false);
			}
		}

	}

	private void saveData(WorldSavedData var1) {
	}

	private void loadIdCounts() {
		try {
			this.idCounts.clear();
		} catch (Exception var9) {
			var9.printStackTrace();
		}
	}

	public int getUniqueDataId(String var1) {
		Short var2 = (Short) this.idCounts.get(var1);

		if (var2 == null) {
			var2 = Short.valueOf((short) 0);
		} else {
			var2 = Short.valueOf((short) (var2.shortValue() + 1));
		}

		this.idCounts.put(var1, var2);

		return var2.shortValue();
	}
}
