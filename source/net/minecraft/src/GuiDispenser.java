package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer {
	public GuiDispenser(InventoryPlayer var1, TileEntityDispenser var2) {
		super(new ContainerDispenser(var1, var2));
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.dispenser"), 60, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int var4 = this.mc.renderEngine.getTexture("/gui/trap.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
}
