package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class Gui {
	protected float zLevel = 0.0F;

	protected void drawHorizontalLine(int var1, int var2, int var3, int var4) {
		if(var2 < var1) {
			int var5 = var1;
			var1 = var2;
			var2 = var5;
		}

		drawRect(var1, var3, var2 + 1, var3 + 1, var4);
	}

	protected void drawVerticalLine(int var1, int var2, int var3, int var4) {
		if(var3 < var2) {
			int var5 = var2;
			var2 = var3;
			var3 = var5;
		}

		drawRect(var1, var2 + 1, var1 + 1, var3, var4);
	}

	public static void drawRect(int var0, int var1, int var2, int var3, int var4) {
		int var5;
		if(var0 < var2) {
			var5 = var0;
			var0 = var2;
			var2 = var5;
		}

		if(var1 < var3) {
			var5 = var1;
			var1 = var3;
			var3 = var5;
		}

		float var10 = (float)(var4 >> 24 & 255) / 255.0F;
		float var6 = (float)(var4 >> 16 & 255) / 255.0F;
		float var7 = (float)(var4 >> 8 & 255) / 255.0F;
		float var8 = (float)(var4 & 255) / 255.0F;
		Tessellator var9 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(var6, var7, var8, var10);
		var9.startDrawingQuads();
		var9.addVertex((double)var0, (double)var3, 0.0D);
		var9.addVertex((double)var2, (double)var3, 0.0D);
		var9.addVertex((double)var2, (double)var1, 0.0D);
		var9.addVertex((double)var0, (double)var1, 0.0D);
		var9.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = (float)(var5 >> 24 & 255) / 255.0F;
		float var8 = (float)(var5 >> 16 & 255) / 255.0F;
		float var9 = (float)(var5 >> 8 & 255) / 255.0F;
		float var10 = (float)(var5 & 255) / 255.0F;
		float var11 = (float)(var6 >> 24 & 255) / 255.0F;
		float var12 = (float)(var6 >> 16 & 255) / 255.0F;
		float var13 = (float)(var6 >> 8 & 255) / 255.0F;
		float var14 = (float)(var6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator var15 = Tessellator.instance;
		var15.startDrawingQuads();
		var15.setColorRGBA_F(var8, var9, var10, var7);
		var15.addVertex((double)var3, (double)var2, (double)this.zLevel);
		var15.addVertex((double)var1, (double)var2, (double)this.zLevel);
		var15.setColorRGBA_F(var12, var13, var14, var11);
		var15.addVertex((double)var1, (double)var4, (double)this.zLevel);
		var15.addVertex((double)var3, (double)var4, (double)this.zLevel);
		var15.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
		var1.drawStringWithShadow(var2, var3 - var1.getStringWidth(var2) / 2, var4, var5);
	}

	public void drawString(FontRenderer var1, String var2, int var3, int var4, int var5) {
		var1.drawStringWithShadow(var2, var3, var4, var5);
	}

	public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + var6), (double)this.zLevel, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + var6), (double)this.zLevel, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + var6) * var8));
		var9.addVertexWithUV((double)(var1 + var5), (double)(var2 + 0), (double)this.zLevel, (double)((float)(var3 + var5) * var7), (double)((float)(var4 + 0) * var8));
		var9.addVertexWithUV((double)(var1 + 0), (double)(var2 + 0), (double)this.zLevel, (double)((float)(var3 + 0) * var7), (double)((float)(var4 + 0) * var8));
		var9.draw();
	}
}
