package net.minecraft.src;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class GLAllocation {
	private static List<Integer> displayLists = new ArrayList<Integer>();
	private static List<Integer> textureNames = new ArrayList<Integer>();

	public static synchronized int generateDisplayLists(int var0) {
		int var1 = GL11.glGenLists(var0);
		displayLists.add(Integer.valueOf(var1));
		displayLists.add(Integer.valueOf(var0));
		return var1;
	}

	public static synchronized void generateTextureNames(IntBuffer var0) {
		GL11.glGenTextures(var0);

		for(int var1 = var0.position(); var1 < var0.limit(); ++var1) {
			textureNames.add(Integer.valueOf(var0.get(var1)));
		}

	}

	public static synchronized void deleteDisplayLists(int var0) {
		int var1 = displayLists.indexOf(Integer.valueOf(var0));
		GL11.glDeleteLists(((Integer)displayLists.get(var1)).intValue(), ((Integer)displayLists.get(var1 + 1)).intValue());
		displayLists.remove(var1);
		displayLists.remove(var1);
	}

	public static synchronized void deleteTexturesAndDisplayLists() {
		for(int var0 = 0; var0 < displayLists.size(); var0 += 2) {
			GL11.glDeleteLists(((Integer)displayLists.get(var0)).intValue(), ((Integer)displayLists.get(var0 + 1)).intValue());
		}


		for(int var1 = 0; var1 < textureNames.size(); ++var1) {
			GL11.glDeleteTextures(((Integer) textureNames.get(var1)).intValue());
		}

		displayLists.clear();
		textureNames.clear();
	}

	public static synchronized ByteBuffer createDirectByteBuffer(int var0) {
		return ByteBuffer.wrap(new byte[var0]);
	}

	public static IntBuffer createDirectIntBuffer(int var0) {
		return IntBuffer.wrap(new int[var0]);
	}

	public static FloatBuffer createDirectFloatBuffer(int var0) {
		return FloatBuffer.wrap(new float[var0]);
	}
}
