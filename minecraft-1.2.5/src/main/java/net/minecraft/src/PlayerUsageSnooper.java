package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class PlayerUsageSnooper {
	private Map field_52025_a = new HashMap();

	public PlayerUsageSnooper(String var1) {
	}

	public void func_52022_a(String var1, Object var2) {
		this.field_52025_a.put(var1, var2);
	}

	public void func_52021_a() {
		PlayerUsageSnooperThread var1 = new PlayerUsageSnooperThread(this, "reporter");
		var1.setDaemon(true);
		var1.start();
	}

	static Map func_52020_b(PlayerUsageSnooper var0) {
		return var0.field_52025_a;
	}
}
