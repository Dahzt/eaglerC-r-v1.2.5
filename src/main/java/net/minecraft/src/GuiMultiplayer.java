package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer extends GuiScreen {
	private static int threadsPending = 0;
	private static Object lock = new Object();
	private GuiScreen parentScreen;
	private GuiSlotServer serverSlotContainer;
	private List<ServerNBTStorage> serverList = new ArrayList<ServerNBTStorage>();
	private int selectedServer = -1;
	private GuiButton buttonEdit;
	private GuiButton buttonSelect;
	private GuiButton buttonDelete;
	private boolean deleteClicked = false;
	private boolean addClicked = false;
	private boolean editClicked = false;
	private boolean directClicked = false;
	private String lagTooltip = null;
	private ServerNBTStorage tempServer = null;

	public GuiMultiplayer(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void updateScreen() {
	}

	public void initGui() {
		this.loadServerList();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.serverSlotContainer = new GuiSlotServer(this);
		this.initGuiControls();
	}

	private void loadServerList() {
		this.serverList.clear();
	}

	private void saveServerList() {
	}

	public void initGuiControls() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(this.buttonEdit = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
		this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
		this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
		this.controlList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
		boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
		this.buttonSelect.enabled = var2;
		this.buttonEdit.enabled = var2;
		this.buttonDelete.enabled = var2;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 2) {
				String var2 = ((ServerNBTStorage)this.serverList.get(this.selectedServer)).name;
				if(var2 != null) {
					this.deleteClicked = true;
					StringTranslate var3 = StringTranslate.getInstance();
					String var4 = var3.translateKey("selectServer.deleteQuestion");
					String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
					String var6 = var3.translateKey("selectServer.deleteButton");
					String var7 = var3.translateKey("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
					this.mc.displayGuiScreen(var8);
				}
			} else if(var1.id == 1) {
				this.joinServer(this.selectedServer);
			} else if(var1.id == 4) {
				this.directClicked = true;
				this.mc.displayGuiScreen(new GuiScreenServerList(this, this.tempServer = new ServerNBTStorage(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if(var1.id == 3) {
				this.addClicked = true;
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.tempServer = new ServerNBTStorage(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if(var1.id == 7) {
				this.editClicked = true;
				ServerNBTStorage var9 = (ServerNBTStorage)this.serverList.get(this.selectedServer);
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.tempServer = new ServerNBTStorage(var9.name, var9.host)));
			} else if(var1.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if(var1.id == 8) {
				this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
			} else {
				this.serverSlotContainer.actionPerformed(var1);
			}

		}
	}

	public void confirmClicked(boolean var1, int var2) {
		if(this.deleteClicked) {
			this.deleteClicked = false;
			if(var1) {
				this.serverList.remove(var2);
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.directClicked) {
			this.directClicked = false;
			if(var1) {
				this.joinServer(this.tempServer);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if(this.addClicked) {
			this.addClicked = false;
			if(var1) {
				this.serverList.add(this.tempServer);
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.editClicked) {
			this.editClicked = false;
			if(var1) {
				ServerNBTStorage var3 = (ServerNBTStorage)this.serverList.get(this.selectedServer);
				var3.name = this.tempServer.name;
				var3.host = this.tempServer.host;
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		}

	}

	private int parseIntWithDefault(String var1, int var2) {
		try {
			return Integer.parseInt(var1.trim());
		} catch (Exception var4) {
			return var2;
		}
	}

	protected void keyTyped(char var1, int var2) {
		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(2));
		}

	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.lagTooltip = null;
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.serverSlotContainer.drawScreen(var1, var2, var3);
		this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
		if(this.lagTooltip != null) {
			this.func_35325_a(this.lagTooltip, var1, var2);
		}

	}

	private void joinServer(int var1) {
		this.joinServer((ServerNBTStorage)this.serverList.get(var1));
	}

	private void joinServer(ServerNBTStorage var1) {
		String var2 = var1.host;
		String[] var3 = var2.split(":");
		if(var2.startsWith("[")) {
			int var4 = var2.indexOf("]");
			if(var4 > 0) {
				String var5 = var2.substring(1, var4);
				String var6 = var2.substring(var4 + 1).trim();
				if(var6.startsWith(":") && var6.length() > 0) {
					var6 = var6.substring(1);
					var3 = new String[]{var5, var6};
				} else {
					var3 = new String[]{var5};
				}
			}
		}

		if(var3.length > 2) {
			var3 = new String[]{var2};
		}

		this.mc.displayGuiScreen(new GuiConnecting(this.mc, var3[0], var3.length > 1 ? this.parseIntWithDefault(var3[1], 25565) : 25565));
	}

	private void pollServer(ServerNBTStorage var1) throws IOException {
		String var2 = var1.host;
		String[] var3 = var2.split(":");
		if(var2.startsWith("[")) {
			int var4 = var2.indexOf("]");
			if(var4 > 0) {
				String var5 = var2.substring(1, var4);
				String var6 = var2.substring(var4 + 1).trim();
				if(var6.startsWith(":") && var6.length() > 0) {
					var6 = var6.substring(1);
					var3 = new String[]{var5, var6};
				} else {
					var3 = new String[]{var5};
				}
			}
		}

		if(var3.length > 2) {
			var3 = new String[]{var2};
		}

		String var29 = var3[0];
		int var30 = var3.length > 1 ? this.parseIntWithDefault(var3[1], 25565) : 25565;
		Socket var31 = null;
		DataInputStream var7 = null;
		DataOutputStream var8 = null;

		try {
			var31 = new Socket();
			var31.setSoTimeout(3000);
			var31.setTcpNoDelay(true);
			var31.setTrafficClass(18);
			var31.connect(new InetSocketAddress(var29, var30), 3000);
			var7 = new DataInputStream(var31.getInputStream());
			var8 = new DataOutputStream(var31.getOutputStream());
			var8.write(254);
			if(var7.read() != 255) {
				throw new IOException("Bad message");
			}

			String var9 = Packet.readString(var7, 256);
			char[] var10 = var9.toCharArray();

			int var11;
			for(var11 = 0; var11 < var10.length; ++var11) {
				if(var10[var11] != 167 && ChatAllowedCharacters.allowedCharacters.indexOf(var10[var11]) < 0) {
					var10[var11] = 63;
				}
			}

			var9 = new String(var10);
			var3 = var9.split("\u00a7");
			var9 = var3[0];
			var11 = -1;
			int var12 = -1;

			try {
				var11 = Integer.parseInt(var3[1]);
				var12 = Integer.parseInt(var3[2]);
			} catch (Exception var27) {
			}

			var1.motd = "\u00a77" + var9;
			if(var11 >= 0 && var12 > 0) {
				var1.playerCount = "\u00a77" + var11 + "\u00a78/\u00a77" + var12;
			} else {
				var1.playerCount = "\u00a78???";
			}
		} finally {
			try {
				if(var7 != null) {
					var7.close();
				}
			} catch (Throwable var26) {
			}

			try {
				if(var8 != null) {
					var8.close();
				}
			} catch (Throwable var25) {
			}

			try {
				if(var31 != null) {
					var31.close();
				}
			} catch (Throwable var24) {
			}

		}

	}

	protected void func_35325_a(String var1, int var2, int var3) {
		if(var1 != null) {
			int var4 = var2 + 12;
			int var5 = var3 - 12;
			int var6 = this.fontRenderer.getStringWidth(var1);
			this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(var1, var4, var5, -1);
		}
	}

	static List<ServerNBTStorage> getServerList(GuiMultiplayer var0) {
		return var0.serverList;
	}

	static int setSelectedServer(GuiMultiplayer var0, int var1) {
		return var0.selectedServer = var1;
	}

	static int getSelectedServer(GuiMultiplayer var0) {
		return var0.selectedServer;
	}

	static GuiButton getButtonSelect(GuiMultiplayer var0) {
		return var0.buttonSelect;
	}

	static GuiButton getButtonEdit(GuiMultiplayer var0) {
		return var0.buttonEdit;
	}

	static GuiButton getButtonDelete(GuiMultiplayer var0) {
		return var0.buttonDelete;
	}

	static void joinServer(GuiMultiplayer var0, int var1) {
		var0.joinServer(var1);
	}

	static Object getLock() {
		return lock;
	}

	static int getThreadsPending() {
		return threadsPending;
	}

	static int incrementThreadsPending() {
		return threadsPending++;
	}

	static void pollServer(GuiMultiplayer var0, ServerNBTStorage var1) throws IOException {
		var0.pollServer(var1);
	}

	static int decrementThreadsPending() {
		return threadsPending--;
	}

	static String setTooltipText(GuiMultiplayer var0, String var1) {
		return var0.lagTooltip = var1;
	}
}
