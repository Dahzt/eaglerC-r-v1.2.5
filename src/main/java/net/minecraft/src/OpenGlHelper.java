package net.minecraft.src;

import org.lwjgl.opengl.GL13;

public class OpenGlHelper {
	public static int defaultTexUnit;
	public static int lightmapTexUnit;

	public static void initializeTextures() {
		defaultTexUnit = GL13.GL_TEXTURE0;
		lightmapTexUnit = GL13.GL_TEXTURE1;
	}

	public static void setActiveTexture(int var0) {
		GL13.glActiveTexture(var0);
	}

	public static void setClientActiveTexture(int var0) {
		GL13.glClientActiveTexture(var0);
	}

	public static void setLightmapTextureCoords(int var0, float var1, float var2) {
		GL13.glMultiTexCoord2f(var0, var1, var2);
	}
}
