package net.minecraft.src;

import java.io.File;
import java.net.URI;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

public class GuiTexturePacks extends GuiScreen {
	protected GuiScreen guiScreen;
	private int refreshTimer = -1;
	private String fileLocation = "";
	private GuiTexturePackSlot guiTexturePackSlot;

	public GuiTexturePacks(GuiScreen var1) {
		this.guiScreen = var1;
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(new GuiSmallButton(5, this.width / 2 - 154, this.height - 48, var1.translateKey("texturePack.openFolder")));
		this.controlList.add(new GuiSmallButton(6, this.width / 2 + 4, this.height - 48, var1.translateKey("gui.done")));
		this.mc.texturePackList.updateAvaliableTexturePacks();
		this.fileLocation = (new File(Minecraft.getMinecraftDir(), "texturepacks")).getAbsolutePath();
		this.guiTexturePackSlot = new GuiTexturePackSlot(this);
		this.guiTexturePackSlot.registerScrollButtons(this.controlList, 7, 8);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 5) {
				boolean var2 = false;

				try {
					Class var3 = Class.forName("java.awt.Desktop");
					Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
					var3.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{(new File(Minecraft.getMinecraftDir(), "texturepacks")).toURI()});
				} catch (Throwable var5) {
					var5.printStackTrace();
					var2 = true;
				}

				if(var2) {
					System.out.println("Opening via Sys class!");
					Sys.openURL("file://" + this.fileLocation);
				}
			} else if(var1.id == 6) {
				this.mc.renderEngine.refreshTextures();
				this.mc.displayGuiScreen(this.guiScreen);
			} else {
				this.guiTexturePackSlot.actionPerformed(var1);
			}

		}
	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
	}

	protected void mouseMovedOrUp(int var1, int var2, int var3) {
		super.mouseMovedOrUp(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.guiTexturePackSlot.drawScreen(var1, var2, var3);
		if(this.refreshTimer <= 0) {
			this.mc.texturePackList.updateAvaliableTexturePacks();
			this.refreshTimer += 20;
		}

		StringTranslate var4 = StringTranslate.getInstance();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("texturePack.title"), this.width / 2, 16, 16777215);
		this.drawCenteredString(this.fontRenderer, var4.translateKey("texturePack.folderInfo"), this.width / 2 - 77, this.height - 26, 8421504);
		super.drawScreen(var1, var2, var3);
	}

	public void updateScreen() {
		super.updateScreen();
		--this.refreshTimer;
	}

	static Minecraft func_22124_a(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22126_b(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22119_c(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22122_d(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22117_e(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_35307_f(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_35308_g(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22118_f(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22116_g(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22121_h(GuiTexturePacks var0) {
		return var0.mc;
	}

	static Minecraft func_22123_i(GuiTexturePacks var0) {
		return var0.mc;
	}

	static FontRenderer func_22127_j(GuiTexturePacks var0) {
		return var0.fontRenderer;
	}

	static FontRenderer func_22120_k(GuiTexturePacks var0) {
		return var0.fontRenderer;
	}

	static FontRenderer func_22125_l(GuiTexturePacks var0) {
		return var0.fontRenderer;
	}
}
