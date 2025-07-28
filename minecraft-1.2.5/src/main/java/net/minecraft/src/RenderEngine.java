package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import net.PeytonPlayz585.awt.image.BufferedImage;
import net.PeytonPlayz585.awt.image.ImageIO;
import net.lax1dude.eaglercraft.beta.SpriteSheetTexture;

public class RenderEngine {
	private HashMap textureMap = new HashMap();
	private HashMap textureContentsMap = new HashMap();
	private IntHashMap textureNameToImageMap = new IntHashMap();
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
	private IntBuffer imageData = GLAllocation.createDirectIntBuffer(16777216);
	private ByteBuffer imageDataPano = GLAllocation.createDirectByteBuffer(16777216);
	private GameSettings options;
	public boolean clampTexture = false;
	public boolean blurTexture = false;
	private BufferedImage missingTextureImage = new BufferedImage(64, 64, 2);
	private int field_48512_n = 16;
	private int boundTexture;
	
	private java.util.List<TextureFX> textureList = new ArrayList<TextureFX>();
	private java.util.List<SpriteSheetTexture> textureSpriteList = new ArrayList<SpriteSheetTexture>();

	public RenderEngine(GameSettings var2) {
		this.options = var2;
		int[] missingTextureImage = new int[256];
		for(int i = 0; i < 256; ++i) {
			missingTextureImage [i] = ((i / 16 + (i % 16)) % 2 == 0) ? 0xffff00ff : 0xff000000;
		}
		this.missingTextureImage = new BufferedImage(missingTextureImage, 16, 16, true);
	}

	public int[] getTextureContents(String var1) {
		int[] var3 = (int[])this.textureContentsMap.get(var1);
		if(var3 != null) {
			return var3;
		} else {
			try {
				Object var6 = null;
				if(var1.startsWith("##")) {
					throw new UnsupportedOperationException();
				} else if(var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					var3 = this.getImageContentsAndAllocate(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(7))));
					this.clampTexture = false;
				} else if(var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var3 = this.getImageContentsAndAllocate(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(6))));
					this.clampTexture = false;
					this.blurTexture = false;
				} else {
					InputStream var7 = ImageIO.getResourceAsStream(var1);
					if(var7 == null) {
						var3 = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						var3 = this.getImageContentsAndAllocate(this.readTextureImage(var7));
					}
				}

				this.textureContentsMap.put(var1, var3);
				return var3;
			} catch (IOException var5) {
				var5.printStackTrace();
				int[] var4 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(var1, var4);
				return var4;
			}
		}
	}

	private int[] getImageContentsAndAllocate(BufferedImage var1) {
		int var2 = var1.getWidth();
		int var3 = var1.getHeight();
		int[] var4 = new int[var2 * var3];
		var1.getRGB(0, 0, var2, var3, var4, 0, var2);
		return var4;
	}

	private int[] getImageContents(BufferedImage var1, int[] var2) {
		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		var1.getRGB(0, 0, var3, var4, var2, 0, var3);
		return var2;
	}
	
	public void resetBoundTexture() {
		this.boundTexture = -1;
	}

	public int getTexture(String var1) {
		Integer var3 = (Integer)this.textureMap.get(var1);
		if(var3 != null) {
			return var3.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var6 = this.singleIntBuffer.get(0);
				if(var1.startsWith("##")) {
					throw new UnsupportedOperationException();
				} else if(var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(7))), var6);
					this.clampTexture = false;
				} else if(var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.setupTexture(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(6))), var6);
					this.blurTexture = false;
				} else if(var1.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(11))), var6);
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					InputStream var7 = ImageIO.getResourceAsStream(var1);
					if(var7 == null) {
						this.setupTexture(this.missingTextureImage, var6);
					} else {
						this.setupTexture(this.readTextureImage(var7), var6);
					}
				}

				this.textureMap.put(var1, Integer.valueOf(var6));
				return var6;
			} catch (Exception var5) {
				var5.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				this.setupTexture(this.missingTextureImage, var4);
				this.textureMap.put(var1, Integer.valueOf(var4));
				return var4;
			}
		}
	}

	public int allocateAndSetupTexture(BufferedImage var1) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int var2 = this.singleIntBuffer.get(0);
		this.setupTexture(var1, var2);
		this.textureNameToImageMap.addKey(var2, var1);
		return var2;
	}
	
	public void setupTexture(BufferedImage par1BufferedImage, int par2) {
		this.bindTexture(par2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		if (blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if (clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		int var5 = par1BufferedImage.getWidth();
		int var6 = par1BufferedImage.getHeight();
		int[] var7 = par1BufferedImage.getData();
		
		if (this.options != null && this.options.anaglyph) {
			var7 = this.colorToAnaglyph(var7);
		}

		this.imageData.clear();
		this.imageData.put(var7);
		this.imageData.position(0).limit(var7.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var5, var6, 0, GL11.GL_BGRA, GL11.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}
	
	private int[] colorToAnaglyph(int[] par1ArrayOfInteger) {
		int[] var2 = new int[par1ArrayOfInteger.length];

		for (int var3 = 0; var3 < par1ArrayOfInteger.length; ++var3) {
			int var4 = par1ArrayOfInteger[var3] >> 24 & 255;
			int var5 = par1ArrayOfInteger[var3] >> 16 & 255;
			int var6 = par1ArrayOfInteger[var3] >> 8 & 255;
			int var7 = par1ArrayOfInteger[var3] & 255;
			int var8 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
			int var9 = (var5 * 30 + var6 * 70) / 100;
			int var10 = (var5 * 30 + var7 * 70) / 100;
			var2[var3] = var4 << 24 | var8 << 16 | var9 << 8 | var10;
		}

		return var2;
	}

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		this.bindTexture(par4);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		if (this.options != null && this.options.anaglyph) {
			par1ArrayOfInteger = this.colorToAnaglyph(par1ArrayOfInteger);
		}

		this.imageData.clear();
		this.imageData.put(par1ArrayOfInteger);
		this.imageData.position(0).limit(par1ArrayOfInteger.length);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, par2, par3, GL11.GL_BGRA, GL11.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
	}

	public void deleteTexture(int var1) {
		this.textureNameToImageMap.removeObject(var1);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(var1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	public int getTextureForDownloadableImage(String var1, String var2) {
		int i = this.getTexture(var2);
		return i;
	}

	public void releaseImageData(String var1) {
	}

	public void registerSpriteSheet(String name, int iconIndex, int iconTileSize) {
		textureSpriteList.add(new SpriteSheetTexture(name, iconIndex, iconTileSize));
	}
	
	public void registerTextureFX(TextureFX texturefx) {
		textureList.add(texturefx);
		texturefx.onTick();
	}

	public void updateDynamicTextures() {
		for (int i = 0; i < textureList.size(); i++) {
			GL11.glEnable(GL11.EAG_SWAP_RB);
			TextureFX texturefx = (TextureFX) textureList.get(i);
			texturefx.anaglyphEnabled = options.anaglyph;
			texturefx.onTick();
			int tileSize = 16 * 16 * 4;
			imageDataPano.clear();
			imageDataPano.put(texturefx.imageData);
			imageDataPano.position(0).limit(tileSize);
			texturefx.bindImage(this);
			GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, 0, (texturefx.iconIndex % 16) * 16, (texturefx.iconIndex / 16) * 16, 16, 16, GL11.GL_BGRA, 5121 /* GL_UNSIGNED_BYTE */, imageDataPano);
			GL11.glDisable(GL11.EAG_SWAP_RB);
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture("/terrain.png"));
//		for(int i = 0, l = textureSpriteList.size(); i < l; ++i) {
//			SpriteSheetTexture sp = textureSpriteList.get(i);
//			sp.update();
//			int w = 16;
//			for(int j = 0; j < 5; ++j) {
//				imageDataPano.clear();
//				imageDataPano.put(sp.grabFrame(j));
//				imageDataPano.position(0).limit(sp.iconTileSize);
//				GL11.glTexSubImage2D(3553 /* GL_TEXTURE_2D */, j, (sp.iconIndex % 16) * w, (sp.iconIndex / 16) * w, w * sp.iconTileSize, w * sp.iconTileSize,
//						GL11.GL_BGRA, GL11.GL_UNSIGNED_INT_8_8_8_8_REV, imageDataPano);
//				w /= 2;
//			}
//		}
	}

	private int alphaBlend(int var1, int var2) {
		int var3 = (var1 & -16777216) >> 24 & 255;
		int var4 = (var2 & -16777216) >> 24 & 255;
		short var5 = 255;
		short var15;
		short var16;
		if(var3 + var4 < 255) {
			var5 = 0;
			var15 = 1;
			var16 = 1;
		} else if(var3 > var4) {
			var15 = 255;
			var16 = 1;
		} else {
			var15 = 1;
			var16 = 255;
		}

		int var6 = (var1 >> 16 & 255) * var15;
		int var7 = (var1 >> 8 & 255) * var15;
		int var8 = (var1 & 255) * var15;
		int var9 = (var2 >> 16 & 255) * var16;
		int var10 = (var2 >> 8 & 255) * var16;
		int var11 = (var2 & 255) * var16;
		int var12 = (var6 + var9) / (var15 + var16);
		int var13 = (var7 + var10) / (var15 + var16);
		int var14 = (var8 + var11) / (var15 + var16);
		return var5 << 24 | var12 << 16 | var13 << 8 | var14;
	}

	public void refreshTextures() {
		Iterator var2 = this.textureNameToImageMap.getKeySet().iterator();

		BufferedImage var4;
		while(var2.hasNext()) {
			int var3 = ((Integer)var2.next()).intValue();
			var4 = (BufferedImage)this.textureNameToImageMap.lookup(var3);
			this.setupTexture(var4, var3);
		}

		var2 = this.textureMap.keySet().iterator();

		String var9;
		while(var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				if(var9.startsWith("##")) {
					throw new UnsupportedOperationException();
				} else if(var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9.substring(7)));
				} else if(var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9.substring(6)));
				} else if(var9.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9.substring(11)));
				} else {
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9));
				}

				int var5 = ((Integer)this.textureMap.get(var9)).intValue();
				this.setupTexture(var4, var5);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while(var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				if(var9.startsWith("##")) {
					throw new UnsupportedOperationException();
				} else if(var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9.substring(7)));
				} else if(var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9.substring(6)));
				} else {
					var4 = this.readTextureImage(ImageIO.getResourceAsStream(var9));
				}

				this.getImageContents(var4, (int[])this.textureContentsMap.get(var9));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				var6.printStackTrace();
			}
		}
		
		for(int j = 0, l = textureSpriteList.size(); j < l; ++j) {
			textureSpriteList.get(j).reloadData();
		}

	}

	private BufferedImage readTextureImage(InputStream var1) throws IOException {
		BufferedImage var2 = ImageIO.read(var1);
		var1.close();
		return var2;
	}

	public void bindTexture(int var1) {
		if(var1 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
		}
	}
	
	public int getTexturePanorama(String var1) {
		Integer var3 = (Integer)this.textureMap.get(var1);
		if(var3 != null) {
			return var3.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var6 = this.singleIntBuffer.get(0);
				if(var1.startsWith("##")) {
					throw new UnsupportedOperationException();
				} else if(var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					this.setupTexturePanorama(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(7))), var6);
					this.clampTexture = false;
				} else if(var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.setupTexturePanorama(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(6))), var6);
					this.blurTexture = false;
				} else if(var1.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexturePanorama(this.readTextureImage(ImageIO.getResourceAsStream(var1.substring(11))), var6);
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					InputStream var7 = ImageIO.getResourceAsStream(var1);
					if(var7 == null) {
						this.setupTexturePanorama(this.missingTextureImage, var6);
					} else {
						this.setupTexturePanorama(this.readTextureImage(var7), var6);
					}
				}

				this.textureMap.put(var1, Integer.valueOf(var6));
				return var6;
			} catch (Exception var5) {
				var5.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				this.setupTexturePanorama(this.missingTextureImage, var4);
				this.textureMap.put(var1, Integer.valueOf(var4));
				return var4;
			}
		}
	}
	
	private void setupTexturePanorama(BufferedImage par1BufferedImage, int par2) {
		
		par1BufferedImage = par1BufferedImage.swapRB();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		int var3 = par1BufferedImage.getWidth();
		int var4 = par1BufferedImage.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 * 4];
		par1BufferedImage.getRGB(0, 0, var3, var4, var5, 0, var3);

		int var7;
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;
		int var13;
		int var14;
		for(var7 = 0; var7 < var5.length; ++var7) {
			var8 = var5[var7] >> 24 & 255;
			var9 = var5[var7] >> 16 & 255;
			var10 = var5[var7] >> 8 & 255;
			var11 = var5[var7] & 255;
			if(this.options != null && this.options.anaglyph) {
				var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				var13 = (var9 * 30 + var10 * 70) / 100;
				var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			var6[var7 * 4 + 0] = (byte)var9;
			var6[var7 * 4 + 1] = (byte)var10;
			var6[var7 * 4 + 2] = (byte)var11;
			var6[var7 * 4 + 3] = (byte)var8;
		}

		this.imageDataPano.clear();
		this.imageDataPano.put(var6);
		this.imageDataPano.position(0).limit(var6.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var3, var4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageDataPano);
	}
	
	public int makeViewport(int w, int h) {
		int t = GL11.glGenTextures();
		this.bindTexture(t);
		this.imageData.position(0).limit(w * h);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w, h, 0, GL11.GL_BGRA, GL11.GL_UNSIGNED_INT_8_8_8_8_REV, this.imageData);
		return t;
	}
}

