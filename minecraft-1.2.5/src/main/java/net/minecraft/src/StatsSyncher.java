package net.minecraft.src;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import net.PeytonPlayz585.fileutils.File;

public class StatsSyncher {
	private volatile boolean isBusy = false;
	private volatile Map field_27437_b = null;
	private volatile Map field_27436_c = null;
	private StatFileWriter statFileWriter;
	private String unsentDataFile;
	private String dataFile;
	private String unsentTempFile;
	private String tempFile;
	private String unsentOldFile;
	private String oldFile;
	private Session theSession;
	private int field_27427_l = 0;
	private int field_27426_m = 0;

	public StatsSyncher(Session var1, StatFileWriter var2, String var3) {
		this.unsentDataFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + "_unsent.dat");
		this.dataFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + ".dat");
		this.unsentOldFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + "_unsent.old");
		this.oldFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + ".old");
		this.unsentTempFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + "_unsent.tmp");
		this.tempFile = File.makePath(var3, "stats_" + var1.username.toLowerCase() + ".tmp");
		if(!var1.username.toLowerCase().equals(var1.username)) {
			this.func_28214_a(var3, "stats_" + var1.username + "_unsent.dat", this.unsentDataFile);
			this.func_28214_a(var3, "stats_" + var1.username + ".dat", this.dataFile);
			this.func_28214_a(var3, "stats_" + var1.username + "_unsent.old", this.unsentOldFile);
			this.func_28214_a(var3, "stats_" + var1.username + ".old", this.oldFile);
			this.func_28214_a(var3, "stats_" + var1.username + "_unsent.tmp", this.unsentTempFile);
			this.func_28214_a(var3, "stats_" + var1.username + ".tmp", this.tempFile);
		}

		this.statFileWriter = var2;
		this.theSession = var1;
		if(File.fileExists(this.unsentDataFile)) {
			var2.func_27179_a(this.func_27415_a(this.unsentDataFile, this.unsentTempFile, this.unsentOldFile));
		}

		this.beginReceiveStats();
	}

	private void func_28214_a(String var1, String var2, String var3) {
		String var4 = File.makePath(var1, var2);
		if(File.fileExists(var4) && !File.exists(var3)) {
			File.renameFile(var4, var3);
		}

	}

	private Map func_27415_a(String var1, String var2, String var3) {
		return File.exists(var1) ? this.func_27408_a(var1) : (File.exists(var3) ? this.func_27408_a(var3) : (File.exists(var2) ? this.func_27408_a(var2) : null));
	}

	private Map func_27408_a(String var1) {
		BufferedReader var2 = null;

		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(File.readFile(var1));
			InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream, "UTF-8");
			var2 = new BufferedReader(inputStreamReader);
			String var3 = "";
			StringBuilder var4 = new StringBuilder();

			while(true) {
				var3 = var2.readLine();
				if(var3 == null) {
					Map var5 = StatFileWriter.func_27177_a(var4.toString());
					return var5;
				}

				var4.append(var3);
			}
		} catch (Exception var15) {
			var15.printStackTrace();
		} finally {
			if(var2 != null) {
				try {
					var2.close();
				} catch (Exception var14) {
					var14.printStackTrace();
				}
			}

		}

		return null;
	}

	private void func_27410_a(Map var1, String var2, String var3, String var4) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintWriter var5 = new PrintWriter(byteArrayOutputStream);

		try {
			var5.print(StatFileWriter.func_27185_a(this.theSession.username, "local", var1));
		} finally {
			var5.flush();
			byte[] data = byteArrayOutputStream.toByteArray();
			File.writeFile(var3, data);
			var5.close();
		}
		
		if(File.exists(var4)) {
			File.deleteFile(var4);
		}
		
		if(File.exists(var2)) {
			File.renameFile(var2, var4);
		}
		
		File.renameFile(var3, var2);
	}

	public void beginReceiveStats() {
		if(this.isBusy) {
			throw new IllegalStateException("Can\'t get stats from server while StatsSyncher is busy!");
		} else {
			this.field_27427_l = 100;
			this.isBusy = true;
			(new ThreadStatSyncherReceive(this)).start();
		}
	}

	public void beginSendStats(Map var1) {
		if(this.isBusy) {
			throw new IllegalStateException("Can\'t save stats while StatsSyncher is busy!");
		} else {
			this.field_27427_l = 100;
			this.isBusy = true;
			(new ThreadStatSyncherSend(this, var1)).start();
		}
	}

	public void syncStatsFileWithMap(Map var1) {
		int var2 = 30;

		while(this.isBusy) {
			--var2;
			if(var2 <= 0) {
				break;
			}

			try {
				Thread.sleep(100L);
			} catch (InterruptedException var10) {
				var10.printStackTrace();
			}
		}

		this.isBusy = true;

		try {
			this.func_27410_a(var1, this.unsentDataFile, this.unsentTempFile, this.unsentOldFile);
		} catch (Exception var8) {
			var8.printStackTrace();
		} finally {
			this.isBusy = false;
		}

	}

	public boolean func_27420_b() {
		return this.field_27427_l <= 0 && !this.isBusy && this.field_27436_c == null;
	}

	public void func_27425_c() {
		if(this.field_27427_l > 0) {
			--this.field_27427_l;
		}

		if(this.field_27426_m > 0) {
			--this.field_27426_m;
		}

		if(this.field_27436_c != null) {
			this.statFileWriter.func_27187_c(this.field_27436_c);
			this.field_27436_c = null;
		}

		if(this.field_27437_b != null) {
			this.statFileWriter.func_27180_b(this.field_27437_b);
			this.field_27437_b = null;
		}

	}

	static Map func_27422_a(StatsSyncher var0) {
		return var0.field_27437_b;
	}

	static String func_27423_b(StatsSyncher var0) {
		return var0.dataFile;
	}

	static String func_27411_c(StatsSyncher var0) {
		return var0.tempFile;
	}

	static String func_27413_d(StatsSyncher var0) {
		return var0.oldFile;
	}

	static void func_27412_a(StatsSyncher var0, Map var1, String var2, String var3, String var4) throws IOException {
		var0.func_27410_a(var1, var2, var3, var4);
	}

	static Map func_27421_a(StatsSyncher var0, Map var1) {
		return var0.field_27437_b = var1;
	}

	static Map func_27409_a(StatsSyncher var0, String var1, String var2, String var3) {
		return var0.func_27415_a(var1, var2, var3);
	}

	static boolean setBusy(StatsSyncher var0, boolean var1) {
		return var0.isBusy = var1;
	}

	static String getUnsentDataFile(StatsSyncher var0) {
		return var0.unsentDataFile;
	}

	static String getUnsentTempFile(StatsSyncher var0) {
		return var0.unsentTempFile;
	}

	static String getUnsentOldFile(StatsSyncher var0) {
		return var0.unsentOldFile;
	}
}
