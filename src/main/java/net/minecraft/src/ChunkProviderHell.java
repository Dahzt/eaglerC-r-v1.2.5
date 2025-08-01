package net.minecraft.src;

import java.util.List;


public class ChunkProviderHell implements IChunkProvider {
	private Random hellRNG;
	private NoiseGeneratorOctaves netherNoiseGen1;
	private NoiseGeneratorOctaves netherNoiseGen2;
	private NoiseGeneratorOctaves netherNoiseGen3;
	private NoiseGeneratorOctaves slowsandGravelNoiseGen;
	private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
	public NoiseGeneratorOctaves netherNoiseGen6;
	public NoiseGeneratorOctaves netherNoiseGen7;
	private World worldObj;
	private double[] field_4163_o;
	public MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
	private double[] slowsandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] netherrackExclusivityNoise = new double[256];
	private MapGenBase netherCaveGenerator = new MapGenCavesHell();
	double[] noiseData1;
	double[] noiseData2;
	double[] noiseData3;
	double[] noiseData4;
	double[] noiseData5;

	public ChunkProviderHell(World var1, long var2) {
		this.worldObj = var1;
		this.hellRNG = new Random(var2);
		this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
		this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
		this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
		this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
		this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
		this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
		this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
	}

	public void generateNetherTerrain(int var1, int var2, byte[] var3) {
		byte var4 = 4;
		byte var5 = 32;
		int var6 = var4 + 1;
		byte var7 = 17;
		int var8 = var4 + 1;
		this.field_4163_o = this.func_4057_a(this.field_4163_o, var1 * var4, 0, var2 * var4, var6, var7, var8);

		for(int var9 = 0; var9 < var4; ++var9) {
			for(int var10 = 0; var10 < var4; ++var10) {
				for(int var11 = 0; var11 < 16; ++var11) {
					double var12 = 0.125D;
					double var14 = this.field_4163_o[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 0];
					double var16 = this.field_4163_o[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 0];
					double var18 = this.field_4163_o[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 0];
					double var20 = this.field_4163_o[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 0];
					double var22 = (this.field_4163_o[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 1] - var14) * var12;
					double var24 = (this.field_4163_o[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
					double var26 = (this.field_4163_o[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 1] - var18) * var12;
					double var28 = (this.field_4163_o[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;

					for(int var30 = 0; var30 < 8; ++var30) {
						double var31 = 0.25D;
						double var33 = var14;
						double var35 = var16;
						double var37 = (var18 - var14) * var31;
						double var39 = (var20 - var16) * var31;

						for(int var41 = 0; var41 < 4; ++var41) {
							int var42 = var41 + var9 * 4 << 11 | 0 + var10 * 4 << 7 | var11 * 8 + var30;
							short var43 = 128;
							double var44 = 0.25D;
							double var46 = var33;
							double var48 = (var35 - var33) * var44;

							for(int var50 = 0; var50 < 4; ++var50) {
								int var51 = 0;
								if(var11 * 8 + var30 < var5) {
									var51 = Block.lavaStill.blockID;
								}

								if(var46 > 0.0D) {
									var51 = Block.netherrack.blockID;
								}

								var3[var42] = (byte)var51;
								var42 += var43;
								var46 += var48;
							}

							var33 += var37;
							var35 += var39;
						}

						var14 += var22;
						var16 += var24;
						var18 += var26;
						var20 += var28;
					}
				}
			}
		}

	}

	public void func_4058_b(int var1, int var2, byte[] var3) {
		byte var4 = 64;
		double var5 = 1.0D / 32.0D;
		this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, var1 * 16, var2 * 16, 0, 16, 16, 1, var5, var5, 1.0D);
		this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, var1 * 16, 109, var2 * 16, 16, 1, 16, var5, 1.0D, var5);
		this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, var1 * 16, var2 * 16, 0, 16, 16, 1, var5 * 2.0D, var5 * 2.0D, var5 * 2.0D);

		for(int var7 = 0; var7 < 16; ++var7) {
			for(int var8 = 0; var8 < 16; ++var8) {
				boolean var9 = this.slowsandNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
				boolean var10 = this.gravelNoise[var7 + var8 * 16] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
				int var11 = (int)(this.netherrackExclusivityNoise[var7 + var8 * 16] / 3.0D + 3.0D + this.hellRNG.nextDouble() * 0.25D);
				int var12 = -1;
				byte var13 = (byte)Block.netherrack.blockID;
				byte var14 = (byte)Block.netherrack.blockID;

				for(int var15 = 127; var15 >= 0; --var15) {
					int var16 = (var8 * 16 + var7) * 128 + var15;
					if(var15 >= 127 - this.hellRNG.nextInt(5)) {
						var3[var16] = (byte)Block.bedrock.blockID;
					} else if(var15 <= 0 + this.hellRNG.nextInt(5)) {
						var3[var16] = (byte)Block.bedrock.blockID;
					} else {
						byte var17 = var3[var16];
						if(var17 == 0) {
							var12 = -1;
						} else if(var17 == Block.netherrack.blockID) {
							if(var12 == -1) {
								if(var11 <= 0) {
									var13 = 0;
									var14 = (byte)Block.netherrack.blockID;
								} else if(var15 >= var4 - 4 && var15 <= var4 + 1) {
									var13 = (byte)Block.netherrack.blockID;
									var14 = (byte)Block.netherrack.blockID;
									if(var10) {
										var13 = (byte)Block.gravel.blockID;
									}

									if(var10) {
										var14 = (byte)Block.netherrack.blockID;
									}

									if(var9) {
										var13 = (byte)Block.slowSand.blockID;
									}

									if(var9) {
										var14 = (byte)Block.slowSand.blockID;
									}
								}

								if(var15 < var4 && var13 == 0) {
									var13 = (byte)Block.lavaStill.blockID;
								}

								var12 = var11;
								if(var15 >= var4 - 1) {
									var3[var16] = var13;
								} else {
									var3[var16] = var14;
								}
							} else if(var12 > 0) {
								--var12;
								var3[var16] = var14;
							}
						}
					}
				}
			}
		}

	}

	public Chunk loadChunk(int var1, int var2) {
		return this.provideChunk(var1, var2);
	}

	public Chunk provideChunk(int var1, int var2) {
		this.hellRNG.setSeed((long)var1 * 341873128712L + (long)var2 * 132897987541L);
		byte[] var3 = new byte[-Short.MIN_VALUE];
		this.generateNetherTerrain(var1, var2, var3);
		this.func_4058_b(var1, var2, var3);
		this.netherCaveGenerator.generate(this, this.worldObj, var1, var2, var3);
		this.genNetherBridge.generate(this, this.worldObj, var1, var2, var3);
		Chunk var4 = new Chunk(this.worldObj, var3, var1, var2);
		BiomeGenBase[] var5 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, var1 * 16, var2 * 16, 16, 16);
		byte[] var6 = var4.getBiomeArray();

		for(int var7 = 0; var7 < var6.length; ++var7) {
			var6[var7] = (byte)var5[var7].biomeID;
		}

		var4.resetRelightChecks();
		return var4;
	}

	private double[] func_4057_a(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		if(var1 == null) {
			var1 = new double[var5 * var6 * var7];
		}

		double var8 = 684.412D;
		double var10 = 2053.236D;
		this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, var2, var3, var4, var5, 1, var7, 1.0D, 0.0D, 1.0D);
		this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, var2, var3, var4, var5, 1, var7, 100.0D, 0.0D, 100.0D);
		this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, var2, var3, var4, var5, var6, var7, var8 / 80.0D, var10 / 60.0D, var8 / 80.0D);
		this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, var2, var3, var4, var5, var6, var7, var8, var10, var8);
		this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, var2, var3, var4, var5, var6, var7, var8, var10, var8);
		int var12 = 0;
		int var13 = 0;
		double[] var14 = new double[var6];

		int var15;
		for(var15 = 0; var15 < var6; ++var15) {
			var14[var15] = Math.cos((double)var15 * Math.PI * 6.0D / (double)var6) * 2.0D;
			double var16 = (double)var15;
			if(var15 > var6 / 2) {
				var16 = (double)(var6 - 1 - var15);
			}

			if(var16 < 4.0D) {
				var16 = 4.0D - var16;
				var14[var15] -= var16 * var16 * var16 * 10.0D;
			}
		}

		for(var15 = 0; var15 < var5; ++var15) {
			for(int var36 = 0; var36 < var7; ++var36) {
				double var17 = (this.noiseData4[var13] + 256.0D) / 512.0D;
				if(var17 > 1.0D) {
					var17 = 1.0D;
				}

				double var19 = 0.0D;
				double var21 = this.noiseData5[var13] / 8000.0D;
				if(var21 < 0.0D) {
					var21 = -var21;
				}

				var21 = var21 * 3.0D - 3.0D;
				if(var21 < 0.0D) {
					var21 /= 2.0D;
					if(var21 < -1.0D) {
						var21 = -1.0D;
					}

					var21 /= 1.4D;
					var21 /= 2.0D;
					var17 = 0.0D;
				} else {
					if(var21 > 1.0D) {
						var21 = 1.0D;
					}

					var21 /= 6.0D;
				}

				var17 += 0.5D;
				var21 = var21 * (double)var6 / 16.0D;
				++var13;

				for(int var23 = 0; var23 < var6; ++var23) {
					double var24 = 0.0D;
					double var26 = var14[var23];
					double var28 = this.noiseData2[var12] / 512.0D;
					double var30 = this.noiseData3[var12] / 512.0D;
					double var32 = (this.noiseData1[var12] / 10.0D + 1.0D) / 2.0D;
					if(var32 < 0.0D) {
						var24 = var28;
					} else if(var32 > 1.0D) {
						var24 = var30;
					} else {
						var24 = var28 + (var30 - var28) * var32;
					}

					var24 -= var26;
					double var34;
					if(var23 > var6 - 4) {
						var34 = (double)((float)(var23 - (var6 - 4)) / 3.0F);
						var24 = var24 * (1.0D - var34) + -10.0D * var34;
					}

					if((double)var23 < var19) {
						var34 = (var19 - (double)var23) / 4.0D;
						if(var34 < 0.0D) {
							var34 = 0.0D;
						}

						if(var34 > 1.0D) {
							var34 = 1.0D;
						}

						var24 = var24 * (1.0D - var34) + -10.0D * var34;
					}

					var1[var12] = var24;
					++var12;
				}
			}
		}

		return var1;
	}

	public boolean chunkExists(int var1, int var2) {
		return true;
	}

	public void populate(IChunkProvider var1, int var2, int var3) {
		BlockSand.fallInstantly = true;
		int var4 = var2 * 16;
		int var5 = var3 * 16;
		this.genNetherBridge.generateStructuresInChunk(this.worldObj, this.hellRNG, var2, var3);

		int var6;
		int var7;
		int var8;
		int var9;
		for(var6 = 0; var6 < 8; ++var6) {
			var7 = var4 + this.hellRNG.nextInt(16) + 8;
			var8 = this.hellRNG.nextInt(120) + 4;
			var9 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenHellLava(Block.lavaMoving.blockID)).generate(this.worldObj, this.hellRNG, var7, var8, var9);
		}

		var6 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1;

		int var10;
		for(var7 = 0; var7 < var6; ++var7) {
			var8 = var4 + this.hellRNG.nextInt(16) + 8;
			var9 = this.hellRNG.nextInt(120) + 4;
			var10 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenFire()).generate(this.worldObj, this.hellRNG, var8, var9, var10);
		}

		var6 = this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1);

		for(var7 = 0; var7 < var6; ++var7) {
			var8 = var4 + this.hellRNG.nextInt(16) + 8;
			var9 = this.hellRNG.nextInt(120) + 4;
			var10 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenGlowStone1()).generate(this.worldObj, this.hellRNG, var8, var9, var10);
		}

		for(var7 = 0; var7 < 10; ++var7) {
			var8 = var4 + this.hellRNG.nextInt(16) + 8;
			var9 = this.hellRNG.nextInt(128);
			var10 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenGlowStone2()).generate(this.worldObj, this.hellRNG, var8, var9, var10);
		}

		if(this.hellRNG.nextInt(1) == 0) {
			var7 = var4 + this.hellRNG.nextInt(16) + 8;
			var8 = this.hellRNG.nextInt(128);
			var9 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(this.worldObj, this.hellRNG, var7, var8, var9);
		}

		if(this.hellRNG.nextInt(1) == 0) {
			var7 = var4 + this.hellRNG.nextInt(16) + 8;
			var8 = this.hellRNG.nextInt(128);
			var9 = var5 + this.hellRNG.nextInt(16) + 8;
			(new WorldGenFlowers(Block.mushroomRed.blockID)).generate(this.worldObj, this.hellRNG, var7, var8, var9);
		}

		BlockSand.fallInstantly = false;
	}

	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "HellRandomLevelSource";
	}

	public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
		if(var1 == EnumCreatureType.monster && this.genNetherBridge.func_40483_a(var2, var3, var4)) {
			return this.genNetherBridge.getSpawnList();
		} else {
			BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(var2, var4);
			return var5 == null ? null : var5.getSpawnableList(var1);
		}
	}

	public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) {
		return null;
	}
}
