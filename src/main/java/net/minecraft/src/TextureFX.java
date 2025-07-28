package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class TextureFX {
	public byte[] imageData;
	public int iconIndex;
	public boolean anaglyphEnabled;
	public int textureId;
	public int tileSize;
	public int tileImage;

	public TextureFX(int var1) {
		imageData = new byte[1024];
		anaglyphEnabled = false;
		textureId = 0;
		tileSize = 1;
		tileImage = 0;
		iconIndex = var1;
	}

	public void onTick() {
	}

	public void bindImage(RenderEngine var1) {
		if(this.tileImage == 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.getTexture("/terrain.png"));
		} else if(this.tileImage == 1) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1.getTexture("/gui/items.png"));
		}

	}
}
