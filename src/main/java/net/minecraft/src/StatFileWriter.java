package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import net.PeytonPlayz585.fileutils.File;
import net.PeytonPlayz585.fileutils.FilesystemUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StatFileWriter {
	private Map field_25102_a = new HashMap();
	private Map field_25101_b = new HashMap();
	private boolean field_27189_c = false;
	private StatsSyncher statsSyncher;

	public StatFileWriter(Session var1) {	
		String var3 = "stats";
		
		if(!File.exists(var3)) {
			File.mkdir(var3);
		}

		this.statsSyncher = new StatsSyncher(var1, this, var3);
	}

	public void readStat(StatBase var1, int var2) {
		this.writeStatToMap(this.field_25101_b, var1, var2);
		this.writeStatToMap(this.field_25102_a, var1, var2);
		this.field_27189_c = true;
	}

	private void writeStatToMap(Map var1, StatBase var2, int var3) {
		Integer var4 = (Integer)var1.get(var2);
		int var5 = var4 == null ? 0 : var4.intValue();
		var1.put(var2, Integer.valueOf(var5 + var3));
	}

	public Map func_27176_a() {
		return new HashMap(this.field_25101_b);
	}

	public void func_27179_a(Map var1) {
		if(var1 != null) {
			this.field_27189_c = true;
			Iterator var2 = var1.keySet().iterator();

			while(var2.hasNext()) {
				StatBase var3 = (StatBase)var2.next();
				this.writeStatToMap(this.field_25101_b, var3, ((Integer)var1.get(var3)).intValue());
				this.writeStatToMap(this.field_25102_a, var3, ((Integer)var1.get(var3)).intValue());
			}

		}
	}

	public void func_27180_b(Map var1) {
		if(var1 != null) {
			Iterator var2 = var1.keySet().iterator();

			while(var2.hasNext()) {
				StatBase var3 = (StatBase)var2.next();
				Integer var4 = (Integer)this.field_25101_b.get(var3);
				int var5 = var4 == null ? 0 : var4.intValue();
				this.field_25102_a.put(var3, Integer.valueOf(((Integer)var1.get(var3)).intValue() + var5));
			}

		}
	}

	public void func_27187_c(Map var1) {
		if(var1 != null) {
			this.field_27189_c = true;
			Iterator var2 = var1.keySet().iterator();

			while(var2.hasNext()) {
				StatBase var3 = (StatBase)var2.next();
				this.writeStatToMap(this.field_25101_b, var3, ((Integer)var1.get(var3)).intValue());
			}

		}
	}

	public static Map func_27177_a(String var0) {
		HashMap var1 = new HashMap();

		try {
			String var2 = "local";
			StringBuilder var3 = new StringBuilder();
			JsonRootNode var4 = (new JdomParser()).parse(var0);
			List var5 = var4.getArrayNode(new Object[]{"stats-change"});
			Iterator var6 = var5.iterator();

			while(var6.hasNext()) {
				JsonNode var7 = (JsonNode)var6.next();
				Map var8 = var7.getFields();
				Entry var9 = (Entry)var8.entrySet().iterator().next();
				int var10 = Integer.parseInt(((JsonStringNode)var9.getKey()).getText());
				int var11 = Integer.parseInt(((JsonNode)var9.getValue()).getText());
				StatBase var12 = StatList.getOneShotStat(var10);
				if(var12 == null) {
					System.out.println(var10 + " is not a valid stat");
				} else {
					var3.append(StatList.getOneShotStat(var10).statGuid).append(",");
					var3.append(var11).append(",");
					var1.put(var12, Integer.valueOf(var11));
				}
			}

			MD5String var14 = new MD5String(var2);
			String var15 = var14.getMD5String(var3.toString());
			if(!var15.equals(var4.getStringValue(new Object[]{"checksum"}))) {
				System.out.println("CHECKSUM MISMATCH");
				return null;
			}
		} catch (InvalidSyntaxException var13) {
			var13.printStackTrace();
		}

		return var1;
	}

	public static String func_27185_a(String var0, String var1, Map var2) {
		StringBuilder var3 = new StringBuilder();
		StringBuilder var4 = new StringBuilder();
		boolean var5 = true;
		var3.append("{\r\n");
		if(var0 != null && var1 != null) {
			var3.append("  \"user\":{\r\n");
			var3.append("    \"name\":\"").append(var0).append("\",\r\n");
			var3.append("    \"sessionid\":\"").append(var1).append("\"\r\n");
			var3.append("  },\r\n");
		}

		var3.append("  \"stats-change\":[");
		Iterator var6 = var2.keySet().iterator();

		while(var6.hasNext()) {
			StatBase var7 = (StatBase)var6.next();
			if(!var5) {
				var3.append("},");
			} else {
				var5 = false;
			}

			var3.append("\r\n    {\"").append(var7.statId).append("\":").append(var2.get(var7));
			var4.append(var7.statGuid).append(",");
			var4.append(var2.get(var7)).append(",");
		}

		if(!var5) {
			var3.append("}");
		}

		MD5String var8 = new MD5String(var1);
		var3.append("\r\n  ],\r\n");
		var3.append("  \"checksum\":\"").append(var8.getMD5String(var4.toString())).append("\"\r\n");
		var3.append("}");
		return var3.toString();
	}

	public boolean hasAchievementUnlocked(Achievement var1) {
		return this.field_25102_a.containsKey(var1);
	}

	public boolean canUnlockAchievement(Achievement var1) {
		return var1.parentAchievement == null || this.hasAchievementUnlocked(var1.parentAchievement);
	}

	public int writeStat(StatBase var1) {
		Integer var2 = (Integer)this.field_25102_a.get(var1);
		return var2 == null ? 0 : var2.intValue();
	}

	public void func_27175_b() {
	}

	public void syncStats() {
		this.statsSyncher.syncStatsFileWithMap(this.func_27176_a());
	}

	public void func_27178_d() {
		if(this.field_27189_c && this.statsSyncher.func_27420_b()) {
			this.statsSyncher.beginSendStats(this.func_27176_a());
		}

		this.statsSyncher.func_27425_c();
	}
}
