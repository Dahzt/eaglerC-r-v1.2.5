package net.minecraft.src;

public class GuiIngameMenu extends GuiScreen {
	private int updateCounter2 = 0;
	private int updateCounter = 0;

	public void initGui() {
		this.updateCounter2 = 0;
		this.controlList.clear();
		byte var1 = -16;
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + var1, StatCollector.translateToLocal("menu.returnToMenu")));
		if(this.mc.isMultiplayerWorld()) {
			((GuiButton)this.controlList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
		}

		this.controlList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + var1, StatCollector.translateToLocal("menu.returnToGame")));
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + var1, StatCollector.translateToLocal("menu.options")));
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20, StatCollector.translateToLocal("gui.achievements")));
		this.controlList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20, StatCollector.translateToLocal("gui.stats")));
	}

	protected void actionPerformed(GuiButton var1) {
		switch(var1.id) {
		case 0:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;
		case 1:
			this.mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
			if(this.mc.isMultiplayerWorld()) {
				this.mc.theWorld.sendQuittingDisconnectingPacket();
			}

			this.mc.changeWorld1((World)null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		case 2:
		case 3:
		default:
			break;
		case 4:
			this.mc.displayGuiScreen((GuiScreen)null);
			this.mc.setIngameFocus();
			break;
		case 5:
			this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
			break;
		case 6:
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
		}

	}

	public void updateScreen() {
		super.updateScreen();
		++this.updateCounter;
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		boolean var4 = !this.mc.theWorld.quickSaveWorld(this.updateCounter2++);
		if(var4 || this.updateCounter < 20) {
			float var5 = ((float)(this.updateCounter % 10) + var3) / 10.0F;
			var5 = MathHelper.sin(var5 * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
			int var6 = (int)(255.0F * var5);
			this.drawString(this.fontRenderer, "Saving level..", 8, this.height - 16, var6 << 16 | var6 << 8 | var6);
		}

		this.drawCenteredString(this.fontRenderer, "Game menu", this.width / 2, 40, 16777215);
		super.drawScreen(var1, var2, var3);
	}
}
