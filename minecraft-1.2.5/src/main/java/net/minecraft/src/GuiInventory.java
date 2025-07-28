package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiInventory extends GuiContainer {
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventory(EntityPlayer var1) {
		super(var1.inventorySlots);
		this.allowUserInput = true;
		var1.addStat(AchievementList.openInventory, 1);
	}

	public void updateScreen() {
		if(this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		}

	}

	public void initGui() {
		this.controlList.clear();
		if(this.mc.playerController.isInCreativeMode()) {
			this.mc.displayGuiScreen(new GuiContainerCreative(this.mc.thePlayer));
		} else {
			super.initGui();
			if(!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
				this.guiLeft = 160 + (this.width - this.xSize - 200) / 2;
			}
		}

	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 86, 16, 4210752);
	}

	public void drawScreen(int var1, int var2, float var3) {
		super.drawScreen(var1, var2, var3);
		this.xSize_lo = (float)var1;
		this.ySize_lo = (float)var2;
	}

	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int var4 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = this.guiLeft;
		int var6 = this.guiTop;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
		this.displayDebuffEffects();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(var5 + 51), (float)(var6 + 75), 50.0F);
		float var7 = 30.0F;
		GL11.glScalef(-var7, var7, var7);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float var8 = this.mc.thePlayer.renderYawOffset;
		float var9 = this.mc.thePlayer.rotationYaw;
		float var10 = this.mc.thePlayer.rotationPitch;
		float var11 = (float)(var5 + 51) - this.xSize_lo;
		float var12 = (float)(var6 + 75 - 50) - this.ySize_lo;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(var12 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(var11 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(var11 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(var12 / 40.0F))) * 20.0F;
		this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		this.mc.thePlayer.renderYawOffset = var8;
		this.mc.thePlayer.rotationYaw = var9;
		this.mc.thePlayer.rotationPitch = var10;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
		}

	}

	private void displayDebuffEffects() {
		int var1 = this.guiLeft - 124;
		int var2 = this.guiTop;
		int var3 = this.mc.renderEngine.getTexture("/gui/inventory.png");
		Collection var4 = this.mc.thePlayer.getActivePotionEffects();
		if(!var4.isEmpty()) {
			int var5 = 33;
			if(var4.size() > 5) {
				var5 = 132 / (var4.size() - 1);
			}

			for(Iterator var6 = this.mc.thePlayer.getActivePotionEffects().iterator(); var6.hasNext(); var2 += var5) {
				PotionEffect var7 = (PotionEffect)var6.next();
				Potion var8 = Potion.potionTypes[var7.getPotionID()];
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.mc.renderEngine.bindTexture(var3);
				this.drawTexturedModalRect(var1, var2, 0, this.ySize, 140, 32);
				if(var8.hasStatusIcon()) {
					int var9 = var8.getStatusIconIndex();
					this.drawTexturedModalRect(var1 + 6, var2 + 7, 0 + var9 % 8 * 18, this.ySize + 32 + var9 / 8 * 18, 18, 18);
				}

				String var11 = StatCollector.translateToLocal(var8.getName());
				if(var7.getAmplifier() == 1) {
					var11 = var11 + " II";
				} else if(var7.getAmplifier() == 2) {
					var11 = var11 + " III";
				} else if(var7.getAmplifier() == 3) {
					var11 = var11 + " IV";
				}

				this.fontRenderer.drawStringWithShadow(var11, var1 + 10 + 18, var2 + 6, 16777215);
				String var10 = Potion.getDurationString(var7);
				this.fontRenderer.drawStringWithShadow(var10, var1 + 10 + 18, var2 + 6 + 10, 8355711);
			}

		}
	}
}
