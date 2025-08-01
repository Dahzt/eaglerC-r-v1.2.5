//package net.minecraft.src;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//import net.PeytonPlayz585.awt.image.BufferedImage;
//import net.PeytonPlayz585.awt.image.ImageIO;
//
//public class TerrainTextureManager {
//	private float[] texCols = new float[768];
//	private int[] pixels = new int[17408];
//	private int[] zBuf = new int[17408];
//	private int[] waterBuf = new int[17408];
//	private int[] waterBr = new int[17408];
//	private int[] yBuf = new int[34];
//	private int[] textures = new int[768];
//
//	public TerrainTextureManager() {
//		try {
//			BufferedImage var1 = ImageIO.read(ImageIO.getResource("/terrain.png"));
//			int[] var2 = new int[65536];
//			var1.getRGB(0, 0, 256, 256, var2, 0, 256);
//
//			for(int var3 = 0; var3 < 256; ++var3) {
//				int var4 = 0;
//				int var5 = 0;
//				int var6 = 0;
//				int var7 = var3 % 16 * 16;
//				int var8 = var3 / 16 * 16;
//				int var9 = 0;
//
//				for(int var10 = 0; var10 < 16; ++var10) {
//					for(int var11 = 0; var11 < 16; ++var11) {
//						int var12 = var2[var11 + var7 + (var10 + var8) * 256];
//						int var13 = var12 >> 24 & 255;
//						if(var13 > 128) {
//							var4 += var12 >> 16 & 255;
//							var5 += var12 >> 8 & 255;
//							var6 += var12 & 255;
//							++var9;
//						}
//					}
//
//					if(var9 == 0) {
//						++var9;
//					}
//
//					this.texCols[var3 * 3 + 0] = (float)(var4 / var9);
//					this.texCols[var3 * 3 + 1] = (float)(var5 / var9);
//					this.texCols[var3 * 3 + 2] = (float)(var6 / var9);
//				}
//			}
//		} catch (Exception var14) {
//			var14.printStackTrace();
//		}
//
//		for(int var15 = 0; var15 < 4096; ++var15) {
//			if(Block.blocksList[var15] != null) {
//				this.textures[var15 * 3 + 0] = Block.blocksList[var15].getBlockTextureFromSide(1);
//				this.textures[var15 * 3 + 1] = Block.blocksList[var15].getBlockTextureFromSide(2);
//				this.textures[var15 * 3 + 2] = Block.blocksList[var15].getBlockTextureFromSide(3);
//			}
//		}
//
//	}
//
//	public void render(IsoImageBuffer var1) {
//		World var2 = var1.level;
//		if(var2 == null) {
//			var1.noContent = true;
//			var1.rendered = true;
//		} else {
//			int var3 = var1.x * 16;
//			int var4 = var1.y * 16;
//			int var5 = var3 + 16;
//			int var6 = var4 + 16;
//			Chunk var7 = var2.getChunkFromChunkCoords(var1.x, var1.y);
//			if(var7.isEmpty()) {
//				var1.noContent = true;
//				var1.rendered = true;
//			} else {
//				var1.noContent = false;
//				Arrays.fill(this.zBuf, 0);
//				Arrays.fill(this.waterBuf, 0);
//				Arrays.fill(this.yBuf, 544);
//
//				for(int var8 = var6 - 1; var8 >= var4; --var8) {
//					for(int var9 = var5 - 1; var9 >= var3; --var9) {
//						int var10 = var9 - var3;
//						int var11 = var8 - var4;
//						int var12 = var10 + var11;
//						boolean var13 = true;
//
//						for(int var14 = 0; var14 < 128; ++var14) {
//							int var15 = var11 - var10 - var14 + 544 - 16;
//							if(var15 < this.yBuf[var12] || var15 < this.yBuf[var12 + 1]) {
//								Block var16 = Block.blocksList[var2.getBlockId(var9, var14, var8)];
//								if(var16 == null) {
//									var13 = false;
//								} else if(var16.blockMaterial == Material.water) {
//									int var24 = var2.getBlockId(var9, var14 + 1, var8);
//									if(var24 == 0 || Block.blocksList[var24].blockMaterial != Material.water) {
//										float var25 = (float)var14 / 127.0F * 0.6F + 0.4F;
//										float var26 = var2.getLightBrightness(var9, var14 + 1, var8) * var25;
//										if(var15 >= 0 && var15 < 544) {
//											int var27 = var12 + var15 * 32;
//											if(var12 >= 0 && var12 <= 32 && this.waterBuf[var27] <= var14) {
//												this.waterBuf[var27] = var14;
//												this.waterBr[var27] = (int)(var26 * 127.0F);
//											}
//
//											if(var12 >= -1 && var12 <= 31 && this.waterBuf[var27 + 1] <= var14) {
//												this.waterBuf[var27 + 1] = var14;
//												this.waterBr[var27 + 1] = (int)(var26 * 127.0F);
//											}
//
//											var13 = false;
//										}
//									}
//								} else {
//									if(var13) {
//										if(var15 < this.yBuf[var12]) {
//											this.yBuf[var12] = var15;
//										}
//
//										if(var15 < this.yBuf[var12 + 1]) {
//											this.yBuf[var12 + 1] = var15;
//										}
//									}
//
//									float var17 = (float)var14 / 127.0F * 0.6F + 0.4F;
//									int var18;
//									int var19;
//									float var20;
//									float var22;
//									if(var15 >= 0 && var15 < 544) {
//										var18 = var12 + var15 * 32;
//										var19 = this.textures[var16.blockID * 3 + 0];
//										var20 = (var2.getLightBrightness(var9, var14 + 1, var8) * 0.8F + 0.2F) * var17;
//										if(var12 >= 0 && this.zBuf[var18] <= var14) {
//											this.zBuf[var18] = var14;
//											this.pixels[var18] = -16777216 | (int)(this.texCols[var19 * 3 + 0] * var20) << 16 | (int)(this.texCols[var19 * 3 + 1] * var20) << 8 | (int)(this.texCols[var19 * 3 + 2] * var20);
//										}
//
//										if(var12 < 31) {
//											var22 = var20 * 0.9F;
//											if(this.zBuf[var18 + 1] <= var14) {
//												this.zBuf[var18 + 1] = var14;
//												this.pixels[var18 + 1] = -16777216 | (int)(this.texCols[var19 * 3 + 0] * var22) << 16 | (int)(this.texCols[var19 * 3 + 1] * var22) << 8 | (int)(this.texCols[var19 * 3 + 2] * var22);
//											}
//										}
//									}
//
//									if(var15 >= -1 && var15 < 543) {
//										var18 = var12 + (var15 + 1) * 32;
//										var19 = this.textures[var16.blockID * 3 + 1];
//										var20 = var2.getLightBrightness(var9 - 1, var14, var8) * 0.8F + 0.2F;
//										int var21 = this.textures[var16.blockID * 3 + 2];
//										var22 = var2.getLightBrightness(var9, var14, var8 + 1) * 0.8F + 0.2F;
//										float var23;
//										if(var12 >= 0) {
//											var23 = var20 * var17 * 0.6F;
//											if(this.zBuf[var18] <= var14 - 1) {
//												this.zBuf[var18] = var14 - 1;
//												this.pixels[var18] = -16777216 | (int)(this.texCols[var19 * 3 + 0] * var23) << 16 | (int)(this.texCols[var19 * 3 + 1] * var23) << 8 | (int)(this.texCols[var19 * 3 + 2] * var23);
//											}
//										}
//
//										if(var12 < 31) {
//											var23 = var22 * 0.9F * var17 * 0.4F;
//											if(this.zBuf[var18 + 1] <= var14 - 1) {
//												this.zBuf[var18 + 1] = var14 - 1;
//												this.pixels[var18 + 1] = -16777216 | (int)(this.texCols[var21 * 3 + 0] * var23) << 16 | (int)(this.texCols[var21 * 3 + 1] * var23) << 8 | (int)(this.texCols[var21 * 3 + 2] * var23);
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//
//				this.postProcess();
//				if(var1.image == null) {
//					var1.image = new BufferedImage(32, 544, 2);
//				}
//
//				var1.image.setRGB(0, 0, 32, 544, this.pixels, 0, 32);
//				var1.rendered = true;
//			}
//		}
//	}
//
//	private void postProcess() {
//		for(int var1 = 0; var1 < 32; ++var1) {
//			for(int var2 = 0; var2 < 544; ++var2) {
//				int var3 = var1 + var2 * 32;
//				if(this.zBuf[var3] == 0) {
//					this.pixels[var3] = 0;
//				}
//
//				if(this.waterBuf[var3] > this.zBuf[var3]) {
//					int var4 = this.pixels[var3] >> 24 & 255;
//					this.pixels[var3] = ((this.pixels[var3] & 16711422) >> 1) + this.waterBr[var3];
//					if(var4 < 128) {
//						this.pixels[var3] = Integer.MIN_VALUE + this.waterBr[var3] * 2;
//					} else {
//						this.pixels[var3] |= -16777216;
//					}
//				}
//			}
//		}
//
//	}
//}
