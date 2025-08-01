package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiStats extends GuiScreen {
	private static RenderItem renderItem = new RenderItem();
	protected GuiScreen parentGui;
	protected String statsTitle = "Select world";
	private GuiSlotStatsGeneral slotGeneral;
	private GuiSlotStatsItem slotItem;
	private GuiSlotStatsBlock slotBlock;
	private StatFileWriter statFileWriter;
	private GuiSlot selectedSlot = null;

	public GuiStats(GuiScreen var1, StatFileWriter var2) {
		this.parentGui = var1;
		this.statFileWriter = var2;
	}

	public void initGui() {
		this.statsTitle = StatCollector.translateToLocal("gui.stats");
		this.slotGeneral = new GuiSlotStatsGeneral(this);
		this.slotGeneral.registerScrollButtons(this.controlList, 1, 1);
		this.slotItem = new GuiSlotStatsItem(this);
		this.slotItem.registerScrollButtons(this.controlList, 1, 1);
		this.slotBlock = new GuiSlotStatsBlock(this);
		this.slotBlock.registerScrollButtons(this.controlList, 1, 1);
		this.selectedSlot = this.slotGeneral;
		this.addHeaderButtons();
	}

	public void addHeaderButtons() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, var1.translateKey("gui.done")));
		this.controlList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("stat.generalButton")));
		List var10000 = this.controlList;
		GuiButton var2 = new GuiButton(2, this.width / 2 - 46, this.height - 52, 100, 20, var1.translateKey("stat.blocksButton"));
		var10000.add(var2);
		var10000 = this.controlList;
		GuiButton var3 = new GuiButton(3, this.width / 2 + 62, this.height - 52, 100, 20, var1.translateKey("stat.itemsButton"));
		var10000.add(var3);
		if(this.slotBlock.getSize() == 0) {
			var2.enabled = false;
		}

		if(this.slotItem.getSize() == 0) {
			var3.enabled = false;
		}

	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 0) {
				this.mc.displayGuiScreen(this.parentGui);
			} else if(var1.id == 1) {
				this.selectedSlot = this.slotGeneral;
			} else if(var1.id == 3) {
				this.selectedSlot = this.slotItem;
			} else if(var1.id == 2) {
				this.selectedSlot = this.slotBlock;
			} else {
				this.selectedSlot.actionPerformed(var1);
			}

		}
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.selectedSlot.drawScreen(var1, var2, var3);
		this.drawCenteredString(this.fontRenderer, this.statsTitle, this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
	}

	private void drawItemSprite(int var1, int var2, int var3) {
		this.drawButtonBackground(var1 + 1, var2 + 1);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();
		renderItem.drawItemIntoGui(this.fontRenderer, this.mc.renderEngine, var3, 0, Item.itemsList[var3].getIconFromDamage(0), var1 + 2, var2 + 2);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	private void drawButtonBackground(int var1, int var2) {
		this.drawSprite(var1, var2, 0, 0);
	}

	private void drawSprite(int var1, int var2, int var3, int var4) {
		int var5 = this.mc.renderEngine.getTexture("/gui/slot.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var5);
		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.addVertexWithUV((double)(var1 + 0), (double)(var2 + 18), (double)this.zLevel, (double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F));
		var10.addVertexWithUV((double)(var1 + 18), (double)(var2 + 18), (double)this.zLevel, (double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F));
		var10.addVertexWithUV((double)(var1 + 18), (double)(var2 + 0), (double)this.zLevel, (double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F));
		var10.addVertexWithUV((double)(var1 + 0), (double)(var2 + 0), (double)this.zLevel, (double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F));
		var10.draw();
	}

	static Minecraft getMinecraft(GuiStats var0) {
		return var0.mc;
	}

	static FontRenderer getFontRenderer1(GuiStats var0) {
		return var0.fontRenderer;
	}

	static StatFileWriter getStatsFileWriter(GuiStats var0) {
		return var0.statFileWriter;
	}

	static FontRenderer getFontRenderer2(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer3(GuiStats var0) {
		return var0.fontRenderer;
	}

	static Minecraft getMinecraft1(GuiStats var0) {
		return var0.mc;
	}

	static void drawSprite(GuiStats var0, int var1, int var2, int var3, int var4) {
		var0.drawSprite(var1, var2, var3, var4);
	}

	static Minecraft getMinecraft2(GuiStats var0) {
		return var0.mc;
	}

	static FontRenderer getFontRenderer4(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer5(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer6(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer7(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer8(GuiStats var0) {
		return var0.fontRenderer;
	}

	static void drawGradientRect(GuiStats var0, int var1, int var2, int var3, int var4, int var5, int var6) {
		var0.drawGradientRect(var1, var2, var3, var4, var5, var6);
	}

	static FontRenderer getFontRenderer9(GuiStats var0) {
		return var0.fontRenderer;
	}

	static FontRenderer getFontRenderer10(GuiStats var0) {
		return var0.fontRenderer;
	}

	static void drawGradientRect1(GuiStats var0, int var1, int var2, int var3, int var4, int var5, int var6) {
		var0.drawGradientRect(var1, var2, var3, var4, var5, var6);
	}

	static FontRenderer getFontRenderer11(GuiStats var0) {
		return var0.fontRenderer;
	}

	static void drawItemSprite(GuiStats var0, int var1, int var2, int var3) {
		var0.drawItemSprite(var1, var2, var3);
	}
}
