package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer {
	private Minecraft mc;
	private ItemStack itemToRender = null;
	private float equippedProgress = 0.0F;
	private float prevEquippedProgress = 0.0F;
	private RenderBlocks renderBlocksInstance = new RenderBlocks();
	private MapItemRenderer mapItemRenderer;
	private int equippedItemSlot = -1;

	public ItemRenderer(Minecraft var1) {
		this.mc = var1;
		this.mapItemRenderer = new MapItemRenderer(var1.fontRenderer, var1.gameSettings, var1.renderEngine);
	}

	public void renderItem(EntityLiving var1, ItemStack var2, int var3) {
		GL11.glPushMatrix();
		if(var2.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var2.itemID].getRenderType())) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			this.renderBlocksInstance.renderBlockAsItem(Block.blocksList[var2.itemID], var2.getItemDamage(), 1.0F);
		} else {
			if(var2.itemID < 256) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			} else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/gui/items.png"));
			}

			Tessellator var4 = Tessellator.instance;
			int var5 = var1.getItemIcon(var2, var3);
			float var6 = ((float)(var5 % 16 * 16) + 0.0F) / 256.0F;
			float var7 = ((float)(var5 % 16 * 16) + 15.99F) / 256.0F;
			float var8 = ((float)(var5 / 16 * 16) + 0.0F) / 256.0F;
			float var9 = ((float)(var5 / 16 * 16) + 15.99F) / 256.0F;
			float var10 = 0.0F;
			float var11 = 0.3F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glTranslatef(-var10, -var11, 0.0F);
			float var12 = 1.5F;
			GL11.glScalef(var12, var12, var12);
			GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-(15.0F / 16.0F), -(1.0F / 16.0F), 0.0F);
			this.renderItemIn2D(var4, var7, var8, var6, var9);
			if(var2 != null && var2.hasEffect() && var3 == 0) {
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("%blur%/misc/glint.png"));
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				float var13 = 0.76F;
				GL11.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glPushMatrix();
				float var14 = 2.0F / 16.0F;
				GL11.glScalef(var14, var14, var14);
				float var15 = (float)(System.currentTimeMillis() % 3000L) / 3000.0F * 8.0F;
				GL11.glTranslatef(var15, 0.0F, 0.0F);
				GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
				this.renderItemIn2D(var4, 0.0F, 0.0F, 1.0F, 1.0F);
				GL11.glPopMatrix();
				GL11.glPushMatrix();
				GL11.glScalef(var14, var14, var14);
				var15 = (float)(System.currentTimeMillis() % 4873L) / 4873.0F * 8.0F;
				GL11.glTranslatef(-var15, 0.0F, 0.0F);
				GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
				this.renderItemIn2D(var4, 0.0F, 0.0F, 1.0F, 1.0F);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		GL11.glPopMatrix();
	}

	private void renderItemIn2D(Tessellator var1, float var2, float var3, float var4, float var5) {
		float var6 = 1.0F;
		float var7 = 1.0F / 16.0F;
		var1.startDrawingQuads();
		var1.setNormal(0.0F, 0.0F, 1.0F);
		var1.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)var2, (double)var5);
		var1.addVertexWithUV((double)var6, 0.0D, 0.0D, (double)var4, (double)var5);
		var1.addVertexWithUV((double)var6, 1.0D, 0.0D, (double)var4, (double)var3);
		var1.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)var2, (double)var3);
		var1.draw();
		var1.startDrawingQuads();
		var1.setNormal(0.0F, 0.0F, -1.0F);
		var1.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - var7), (double)var2, (double)var3);
		var1.addVertexWithUV((double)var6, 1.0D, (double)(0.0F - var7), (double)var4, (double)var3);
		var1.addVertexWithUV((double)var6, 0.0D, (double)(0.0F - var7), (double)var4, (double)var5);
		var1.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - var7), (double)var2, (double)var5);
		var1.draw();
		var1.startDrawingQuads();
		var1.setNormal(-1.0F, 0.0F, 0.0F);

		int var8;
		float var9;
		float var10;
		float var11;
		for(var8 = 0; var8 < 16; ++var8) {
			var9 = (float)var8 / 16.0F;
			var10 = var2 + (var4 - var2) * var9 - 0.001953125F;
			var11 = var6 * var9;
			var1.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)var5);
			var1.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)var5);
			var1.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)var3);
			var1.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)var3);
		}

		var1.draw();
		var1.startDrawingQuads();
		var1.setNormal(1.0F, 0.0F, 0.0F);

		for(var8 = 0; var8 < 16; ++var8) {
			var9 = (float)var8 / 16.0F;
			var10 = var2 + (var4 - var2) * var9 - 0.001953125F;
			var11 = var6 * var9 + 1.0F / 16.0F;
			var1.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)var3);
			var1.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)var3);
			var1.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)var5);
			var1.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)var5);
		}

		var1.draw();
		var1.startDrawingQuads();
		var1.setNormal(0.0F, 1.0F, 0.0F);

		for(var8 = 0; var8 < 16; ++var8) {
			var9 = (float)var8 / 16.0F;
			var10 = var5 + (var3 - var5) * var9 - 0.001953125F;
			var11 = var6 * var9 + 1.0F / 16.0F;
			var1.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)var2, (double)var10);
			var1.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)var4, (double)var10);
			var1.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)var4, (double)var10);
			var1.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)var2, (double)var10);
		}

		var1.draw();
		var1.startDrawingQuads();
		var1.setNormal(0.0F, -1.0F, 0.0F);

		for(var8 = 0; var8 < 16; ++var8) {
			var9 = (float)var8 / 16.0F;
			var10 = var5 + (var3 - var5) * var9 - 0.001953125F;
			var11 = var6 * var9;
			var1.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)var4, (double)var10);
			var1.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)var2, (double)var10);
			var1.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)var2, (double)var10);
			var1.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)var4, (double)var10);
		}

		var1.draw();
	}

	public void renderItemInFirstPerson(float var1) {
		float var2 = this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * var1;
		EntityPlayerSP var3 = this.mc.thePlayer;
		float var4 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * var1;
		GL11.glPushMatrix();
		GL11.glRotatef(var4, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * var1, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		float var6;
		float var7;
		if(var3 instanceof EntityPlayerSP) {
			EntityPlayerSP var5 = (EntityPlayerSP)var3;
			var6 = var5.prevRenderArmPitch + (var5.renderArmPitch - var5.prevRenderArmPitch) * var1;
			var7 = var5.prevRenderArmYaw + (var5.renderArmYaw - var5.prevRenderArmYaw) * var1;
			GL11.glRotatef((var3.rotationPitch - var6) * 0.1F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef((var3.rotationYaw - var7) * 0.1F, 0.0F, 1.0F, 0.0F);
		}

		ItemStack var14 = this.itemToRender;
		var6 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ));
		var6 = 1.0F;
		int var15 = this.mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(var3.posX), MathHelper.floor_double(var3.posY), MathHelper.floor_double(var3.posZ), 0);
		int var8 = var15 % 65536;
		int var9 = var15 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var8 / 1.0F, (float)var9 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var10;
		float var16;
		float var18;
		if(var14 != null) {
			var15 = Item.itemsList[var14.itemID].getColorFromDamage(var14.getItemDamage(), 0);
			var16 = (float)(var15 >> 16 & 255) / 255.0F;
			var18 = (float)(var15 >> 8 & 255) / 255.0F;
			var10 = (float)(var15 & 255) / 255.0F;
			GL11.glColor4f(var6 * var16, var6 * var18, var6 * var10, 1.0F);
		} else {
			GL11.glColor4f(var6, var6, var6, 1.0F);
		}

		float var11;
		float var13;
		if(var14 != null && var14.itemID == Item.map.shiftedIndex) {
			GL11.glPushMatrix();
			var7 = 0.8F;
			var16 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var16 * (float)Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI);
			GL11.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI * 2.0F) * 0.2F, -var18 * 0.2F);
			var16 = 1.0F - var4 / 45.0F + 0.1F;
			if(var16 < 0.0F) {
				var16 = 0.0F;
			}

			if(var16 > 1.0F) {
				var16 = 1.0F;
			}

			var16 = -MathHelper.cos(var16 * (float)Math.PI) * 0.5F + 0.5F;
			GL11.glTranslatef(0.0F, 0.0F * var7 - (1.0F - var2) * 1.2F - var16 * 0.5F + 0.04F, -0.9F * var7);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(var16 * -85.0F, 0.0F, 0.0F, 1.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));

			for(var9 = 0; var9 < 2; ++var9) {
				int var24 = var9 * 2 - 1;
				GL11.glPushMatrix();
				GL11.glTranslatef(-0.0F, -0.6F, 1.1F * (float)var24);
				GL11.glRotatef((float)(-45 * var24), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(59.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef((float)(-65 * var24), 0.0F, 1.0F, 0.0F);
				Render var22 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
				RenderPlayer var26 = (RenderPlayer)var22;
				var13 = 1.0F;
				GL11.glScalef(var13, var13, var13);
				var26.drawFirstPersonHand();
				GL11.glPopMatrix();
			}

			var18 = var3.getSwingProgress(var1);
			var10 = MathHelper.sin(var18 * var18 * (float)Math.PI);
			var11 = MathHelper.sin(MathHelper.sqrt_float(var18) * (float)Math.PI);
			GL11.glRotatef(-var10 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var11 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var11 * 80.0F, 1.0F, 0.0F, 0.0F);
			var18 = 0.38F;
			GL11.glScalef(var18, var18, var18);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-1.0F, -1.0F, 0.0F);
			var10 = 0.015625F;
			GL11.glScalef(var10, var10, var10);
			this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/misc/mapbg.png"));
			Tessellator var23 = Tessellator.instance;
			GL11.glNormal3f(0.0F, 0.0F, -1.0F);
			var23.startDrawingQuads();
			byte var27 = 7;
			var23.addVertexWithUV((double)(0 - var27), (double)(128 + var27), 0.0D, 0.0D, 1.0D);
			var23.addVertexWithUV((double)(128 + var27), (double)(128 + var27), 0.0D, 1.0D, 1.0D);
			var23.addVertexWithUV((double)(128 + var27), (double)(0 - var27), 0.0D, 1.0D, 0.0D);
			var23.addVertexWithUV((double)(0 - var27), (double)(0 - var27), 0.0D, 0.0D, 0.0D);
			var23.draw();
			MapData var25 = Item.map.getMapData(var14, this.mc.theWorld);
			this.mapItemRenderer.renderMap(this.mc.thePlayer, this.mc.renderEngine, var25);
			GL11.glPopMatrix();
		} else if(var14 != null) {
			GL11.glPushMatrix();
			var7 = 0.8F;
			float var12;
			if(var3.getItemInUseCount() > 0) {
				EnumAction var17 = var14.getItemUseAction();
				if(var17 == EnumAction.eat || var17 == EnumAction.drink) {
					var18 = (float)var3.getItemInUseCount() - var1 + 1.0F;
					var10 = 1.0F - var18 / (float)var14.getMaxItemUseDuration();
					var12 = 1.0F - var10;
					var12 = var12 * var12 * var12;
					var12 = var12 * var12 * var12;
					var12 = var12 * var12 * var12;
					var13 = 1.0F - var12;
					GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos(var18 / 4.0F * (float)Math.PI) * 0.1F) * (float)((double)var10 > 0.2D ? 1 : 0), 0.0F);
					GL11.glTranslatef(var13 * 0.6F, -var13 * 0.5F, 0.0F);
					GL11.glRotatef(var13 * 90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(var13 * 10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(var13 * 30.0F, 0.0F, 0.0F, 1.0F);
				}
			} else {
				var16 = var3.getSwingProgress(var1);
				var18 = MathHelper.sin(var16 * (float)Math.PI);
				var10 = MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI);
				GL11.glTranslatef(-var10 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI * 2.0F) * 0.2F, -var18 * 0.2F);
			}

			GL11.glTranslatef(0.7F * var7, -0.65F * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var16 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var16 * var16 * (float)Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI);
			GL11.glRotatef(-var18 * 20.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var10 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var10 * 80.0F, 1.0F, 0.0F, 0.0F);
			var16 = 0.4F;
			GL11.glScalef(var16, var16, var16);
			if(var3.getItemInUseCount() > 0) {
				EnumAction var20 = var14.getItemUseAction();
				if(var20 == EnumAction.block) {
					GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
					GL11.glRotatef(30.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-80.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(60.0F, 0.0F, 1.0F, 0.0F);
				} else if(var20 == EnumAction.bow) {
					GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
					GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
					var10 = (float)var14.getMaxItemUseDuration() - ((float)var3.getItemInUseCount() - var1 + 1.0F);
					var11 = var10 / 20.0F;
					var11 = (var11 * var11 + var11 * 2.0F) / 3.0F;
					if(var11 > 1.0F) {
						var11 = 1.0F;
					}

					if(var11 > 0.1F) {
						GL11.glTranslatef(0.0F, MathHelper.sin((var10 - 0.1F) * 1.3F) * 0.01F * (var11 - 0.1F), 0.0F);
					}

					GL11.glTranslatef(0.0F, 0.0F, var11 * 0.1F);
					GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(0.0F, 0.5F, 0.0F);
					var12 = 1.0F + var11 * 0.2F;
					GL11.glScalef(1.0F, 1.0F, var12);
					GL11.glTranslatef(0.0F, -0.5F, 0.0F);
					GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
				}
			}

			if(var14.getItem().shouldRotateAroundWhenRendering()) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if(var14.getItem().func_46058_c()) {
				this.renderItem(var3, var14, 0);
				var9 = Item.itemsList[var14.itemID].getColorFromDamage(var14.getItemDamage(), 1);
				var10 = (float)(var9 >> 16 & 255) / 255.0F;
				var11 = (float)(var9 >> 8 & 255) / 255.0F;
				var12 = (float)(var9 & 255) / 255.0F;
				GL11.glColor4f(var6 * var10, var6 * var11, var6 * var12, 1.0F);
				this.renderItem(var3, var14, 1);
			} else {
				this.renderItem(var3, var14, 0);
			}

			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			var7 = 0.8F;
			var16 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var16 * (float)Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI);
			GL11.glTranslatef(-var10 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI * 2.0F) * 0.4F, -var18 * 0.4F);
			GL11.glTranslatef(0.8F * var7, -(12.0F / 16.0F) * var7 - (1.0F - var2) * 0.6F, -0.9F * var7);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			var16 = var3.getSwingProgress(var1);
			var18 = MathHelper.sin(var16 * var16 * (float)Math.PI);
			var10 = MathHelper.sin(MathHelper.sqrt_float(var16) * (float)Math.PI);
			GL11.glRotatef(var10 * 70.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-var18 * 20.0F, 0.0F, 0.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTextureForDownloadableImage(this.mc.thePlayer.skinUrl, this.mc.thePlayer.getTexture()));
			GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
			GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(5.6F, 0.0F, 0.0F);
			Render var19 = RenderManager.instance.getEntityRenderObject(this.mc.thePlayer);
			RenderPlayer var21 = (RenderPlayer)var19;
			var10 = 1.0F;
			GL11.glScalef(var10, var10, var10);
			var21.drawFirstPersonHand();
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
	}

	public void renderOverlays(float var1) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		int var2;
		if(this.mc.thePlayer.isBurning()) {
			var2 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			this.renderFireInFirstPerson(var1);
		}

		if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
			var2 = MathHelper.floor_double(this.mc.thePlayer.posX);
			int var3 = MathHelper.floor_double(this.mc.thePlayer.posY);
			int var4 = MathHelper.floor_double(this.mc.thePlayer.posZ);
			int var5 = this.mc.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var5);
			int var6 = this.mc.theWorld.getBlockId(var2, var3, var4);
			if(this.mc.theWorld.isBlockNormalCube(var2, var3, var4)) {
				this.renderInsideOfBlock(var1, Block.blocksList[var6].getBlockTextureFromSide(2));
			} else {
				for(int var7 = 0; var7 < 8; ++var7) {
					float var8 = ((float)((var7 >> 0) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					float var9 = ((float)((var7 >> 1) % 2) - 0.5F) * this.mc.thePlayer.height * 0.2F;
					float var10 = ((float)((var7 >> 2) % 2) - 0.5F) * this.mc.thePlayer.width * 0.9F;
					int var11 = MathHelper.floor_float((float)var2 + var8);
					int var12 = MathHelper.floor_float((float)var3 + var9);
					int var13 = MathHelper.floor_float((float)var4 + var10);
					if(this.mc.theWorld.isBlockNormalCube(var11, var12, var13)) {
						var6 = this.mc.theWorld.getBlockId(var11, var12, var13);
					}
				}
			}

			if(Block.blocksList[var6] != null) {
				this.renderInsideOfBlock(var1, Block.blocksList[var6].getBlockTextureFromSide(2));
			}
		}

		if(this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			var2 = this.mc.renderEngine.getTexture("/misc/water.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
			this.renderWarpedTextureOverlay(var1);
		}

		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void renderInsideOfBlock(float var1, int var2) {
		Tessellator var3 = Tessellator.instance;
		this.mc.thePlayer.getBrightness(var1);
		float var4 = 0.1F;
		GL11.glColor4f(var4, var4, var4, 0.5F);
		GL11.glPushMatrix();
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = 0.0078125F;
		float var11 = (float)(var2 % 16) / 256.0F - var10;
		float var12 = ((float)(var2 % 16) + 15.99F) / 256.0F + var10;
		float var13 = (float)(var2 / 16) / 256.0F - var10;
		float var14 = ((float)(var2 / 16) + 15.99F) / 256.0F + var10;
		var3.startDrawingQuads();
		var3.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)var12, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)var11, (double)var14);
		var3.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)var11, (double)var13);
		var3.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)var12, (double)var13);
		var3.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private void renderWarpedTextureOverlay(float var1) {
		Tessellator var2 = Tessellator.instance;
		float var3 = this.mc.thePlayer.getBrightness(var1);
		GL11.glColor4f(var3, var3, var3, 0.5F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		float var4 = 4.0F;
		float var5 = -1.0F;
		float var6 = 1.0F;
		float var7 = -1.0F;
		float var8 = 1.0F;
		float var9 = -0.5F;
		float var10 = -this.mc.thePlayer.rotationYaw / 64.0F;
		float var11 = this.mc.thePlayer.rotationPitch / 64.0F;
		var2.startDrawingQuads();
		var2.addVertexWithUV((double)var5, (double)var7, (double)var9, (double)(var4 + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var7, (double)var9, (double)(0.0F + var10), (double)(var4 + var11));
		var2.addVertexWithUV((double)var6, (double)var8, (double)var9, (double)(0.0F + var10), (double)(0.0F + var11));
		var2.addVertexWithUV((double)var5, (double)var8, (double)var9, (double)(var4 + var10), (double)(0.0F + var11));
		var2.draw();
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFireInFirstPerson(float var1) {
		Tessellator var2 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float var3 = 1.0F;

		for(int var4 = 0; var4 < 2; ++var4) {
			GL11.glPushMatrix();
			int var5 = Block.fire.blockIndexInTexture + var4 * 16;
			int var6 = (var5 & 15) << 4;
			int var7 = var5 & 240;
			float var8 = (float)var6 / 256.0F;
			float var9 = ((float)var6 + 15.99F) / 256.0F;
			float var10 = (float)var7 / 256.0F;
			float var11 = ((float)var7 + 15.99F) / 256.0F;
			float var12 = (0.0F - var3) / 2.0F;
			float var13 = var12 + var3;
			float var14 = 0.0F - var3 / 2.0F;
			float var15 = var14 + var3;
			float var16 = -0.5F;
			GL11.glTranslatef((float)(-(var4 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
			GL11.glRotatef((float)(var4 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
			var2.startDrawingQuads();
			var2.addVertexWithUV((double)var12, (double)var14, (double)var16, (double)var9, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var14, (double)var16, (double)var8, (double)var11);
			var2.addVertexWithUV((double)var13, (double)var15, (double)var16, (double)var8, (double)var10);
			var2.addVertexWithUV((double)var12, (double)var15, (double)var16, (double)var9, (double)var10);
			var2.draw();
			GL11.glPopMatrix();
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public void updateEquippedItem() {
		this.prevEquippedProgress = this.equippedProgress;
		EntityPlayerSP var1 = this.mc.thePlayer;
		ItemStack var2 = var1.inventory.getCurrentItem();
		boolean var4 = this.equippedItemSlot == var1.inventory.currentItem && var2 == this.itemToRender;
		if(this.itemToRender == null && var2 == null) {
			var4 = true;
		}

		if(var2 != null && this.itemToRender != null && var2 != this.itemToRender && var2.itemID == this.itemToRender.itemID && var2.getItemDamage() == this.itemToRender.getItemDamage()) {
			this.itemToRender = var2;
			var4 = true;
		}

		float var5 = 0.4F;
		float var6 = var4 ? 1.0F : 0.0F;
		float var7 = var6 - this.equippedProgress;
		if(var7 < -var5) {
			var7 = -var5;
		}

		if(var7 > var5) {
			var7 = var5;
		}

		this.equippedProgress += var7;
		if(this.equippedProgress < 0.1F) {
			this.itemToRender = var2;
			this.equippedItemSlot = var1.inventory.currentItem;
		}

	}

	public void func_9449_b() {
		this.equippedProgress = 0.0F;
	}

	public void func_9450_c() {
		this.equippedProgress = 0.0F;
	}
}
