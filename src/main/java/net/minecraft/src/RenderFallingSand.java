package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderFallingSand extends Render {
	private RenderBlocks renderBlocks = new RenderBlocks();

	public RenderFallingSand() {
		this.shadowSize = 0.5F;
	}

	public void doRenderFallingSand(EntityFallingSand var1, double var2, double var4, double var6, float var8, float var9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var2, (float)var4, (float)var6);
		this.loadTexture("/terrain.png");
		Block var10 = Block.blocksList[var1.blockID];
		World var11 = var1.getWorld();
		GL11.glDisable(GL11.GL_LIGHTING);
		if(var10 == Block.dragonEgg) {
			this.renderBlocks.blockAccess = var11;
			Tessellator var12 = Tessellator.instance;
			var12.startDrawingQuads();
			var12.setTranslation((double)((float)(-MathHelper.floor_double(var1.posX)) - 0.5F), (double)((float)(-MathHelper.floor_double(var1.posY)) - 0.5F), (double)((float)(-MathHelper.floor_double(var1.posZ)) - 0.5F));
			this.renderBlocks.renderBlockByRenderType(var10, MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
			var12.setTranslation(0.0D, 0.0D, 0.0D);
			var12.draw();
		} else {
			this.renderBlocks.renderBlockFallingSand(var10, var11, MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ));
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.doRenderFallingSand((EntityFallingSand)var1, var2, var4, var6, var8, var9);
	}
}
