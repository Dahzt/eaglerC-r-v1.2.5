package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider var1) {
		return 180.0F;
	}

	protected int setSpiderEyeBrightness(EntitySpider var1, int var2, float var3) {
		if(var2 != 0) {
			return -1;
		} else {
			this.loadTexture("/mob/spider_eyes.png");
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			char var5 = '\uf0f0';
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	protected void scaleSpider(EntitySpider var1, float var2) {
		float var3 = var1.spiderScaleAmount();
		GL11.glScalef(var3, var3, var3);
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {
		this.scaleSpider((EntitySpider)var1, var2);
	}

	protected float getDeathMaxRotation(EntityLiving var1) {
		return this.setSpiderDeathMaxRotation((EntitySpider)var1);
	}

	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setSpiderEyeBrightness((EntitySpider)var1, var2, var3);
	}
}
