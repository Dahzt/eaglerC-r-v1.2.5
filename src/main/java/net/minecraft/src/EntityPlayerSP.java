package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	protected int sprintToggleTimer = 0;
	public int sprintingTicksLeft = 0;
	public float renderArmYaw;
	public float renderArmPitch;
	public float prevRenderArmYaw;
	public float prevRenderArmPitch;
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private MouseFilter field_21902_bL = new MouseFilter();

	public EntityPlayerSP(Minecraft var1, World var2, Session var3, int var4) {
		super(var2);
		this.mc = var1;
		this.dimension = var4;
		if(var3 != null && var3.username != null && var3.username.length() > 0) {
			this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + var3.username + ".png";
		}

		this.username = var3.username;
	}

	public void moveEntity(double var1, double var3, double var5) {
		super.moveEntity(var1, var3, var5);
	}

	public void updateEntityActionState() {
		super.updateEntityActionState();
		this.moveStrafing = this.movementInput.moveStrafe;
		this.moveForward = this.movementInput.moveForward;
		this.isJumping = this.movementInput.jump;
		this.prevRenderArmYaw = this.renderArmYaw;
		this.prevRenderArmPitch = this.renderArmPitch;
		this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
		this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
	}

	protected boolean isClientWorld() {
		return true;
	}

	public void onLivingUpdate() {
		if(this.sprintingTicksLeft > 0) {
			--this.sprintingTicksLeft;
			if(this.sprintingTicksLeft == 0) {
				this.setSprinting(false);
			}
		}

		if(this.sprintToggleTimer > 0) {
			--this.sprintToggleTimer;
		}

		if(this.mc.playerController.func_35643_e()) {
			this.posX = this.posZ = 0.5D;
			this.posX = 0.0D;
			this.posZ = 0.0D;
			this.rotationYaw = (float)this.ticksExisted / 12.0F;
			this.rotationPitch = 10.0F;
			this.posY = 68.5D;
		} else {
			if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
				this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
			}

			this.prevTimeInPortal = this.timeInPortal;
			boolean var1;
			if(this.inPortal) {
				if(!this.worldObj.isRemote && this.ridingEntity != null) {
					this.mountEntity((Entity)null);
				}

				if(this.mc.currentScreen != null) {
					this.mc.displayGuiScreen((GuiScreen)null);
				}

				if(this.timeInPortal == 0.0F) {
					this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
				}

				this.timeInPortal += 0.0125F;
				if(this.timeInPortal >= 1.0F) {
					this.timeInPortal = 1.0F;
					if(!this.worldObj.isRemote) {
						this.timeUntilPortal = 10;
						this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
						var1 = false;
						byte var5;
						if(this.dimension == -1) {
							var5 = 0;
						} else {
							var5 = -1;
						}

						this.mc.usePortal(var5);
						this.triggerAchievement(AchievementList.portal);
					}
				}

				this.inPortal = false;
			} else if(this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
				this.timeInPortal += 2.0F / 3.0F * 0.01F;
				if(this.timeInPortal > 1.0F) {
					this.timeInPortal = 1.0F;
				}
			} else {
				if(this.timeInPortal > 0.0F) {
					this.timeInPortal -= 0.05F;
				}

				if(this.timeInPortal < 0.0F) {
					this.timeInPortal = 0.0F;
				}
			}

			if(this.timeUntilPortal > 0) {
				--this.timeUntilPortal;
			}

			var1 = this.movementInput.jump;
			float var2 = 0.8F;
			boolean var3 = this.movementInput.moveForward >= var2;
			this.movementInput.func_52013_a();
			if(this.isUsingItem()) {
				this.movementInput.moveStrafe *= 0.2F;
				this.movementInput.moveForward *= 0.2F;
				this.sprintToggleTimer = 0;
			}

			if(this.movementInput.sneak && this.ySize < 0.2F) {
				this.ySize = 0.2F;
			}

			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			boolean var4 = (float)this.getFoodStats().getFoodLevel() > 6.0F;
			if(this.onGround && !var3 && this.movementInput.moveForward >= var2 && !this.isSprinting() && var4 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
				if(this.sprintToggleTimer == 0) {
					this.sprintToggleTimer = 7;
				} else {
					this.setSprinting(true);
					this.sprintToggleTimer = 0;
				}
			}

			if(this.isSneaking()) {
				this.sprintToggleTimer = 0;
			}

			if(this.isSprinting() && (this.movementInput.moveForward < var2 || this.isCollidedHorizontally || !var4)) {
				this.setSprinting(false);
			}

			if(this.capabilities.allowFlying && !var1 && this.movementInput.jump) {
				if(this.flyToggleTimer == 0) {
					this.flyToggleTimer = 7;
				} else {
					this.capabilities.isFlying = !this.capabilities.isFlying;
					this.func_50009_aI();
					this.flyToggleTimer = 0;
				}
			}

			if(this.capabilities.isFlying) {
				if(this.movementInput.sneak) {
					this.motionY -= 0.15D;
				}

				if(this.movementInput.jump) {
					this.motionY += 0.15D;
				}
			}

			super.onLivingUpdate();
			if(this.onGround && this.capabilities.isFlying) {
				this.capabilities.isFlying = false;
				this.func_50009_aI();
			}

		}
	}

	public void travelToTheEnd(int var1) {
		if(!this.worldObj.isRemote) {
			if(this.dimension == 1 && var1 == 1) {
				this.triggerAchievement(AchievementList.theEnd2);
				this.mc.displayGuiScreen(new GuiWinGame());
			} else {
				this.triggerAchievement(AchievementList.theEnd);
				this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
				this.mc.usePortal(1);
			}

		}
	}

	public float getFOVMultiplier() {
		float var1 = 1.0F;
		if(this.capabilities.isFlying) {
			var1 *= 1.1F;
		}

		var1 *= (this.landMovementFactor * this.getSpeedModifier() / this.speedOnGround + 1.0F) / 2.0F;
		if(this.isUsingItem() && this.getItemInUse().itemID == Item.bow.shiftedIndex) {
			int var2 = this.getItemInUseDuration();
			float var3 = (float)var2 / 20.0F;
			if(var3 > 1.0F) {
				var3 = 1.0F;
			} else {
				var3 *= var3;
			}

			var1 *= 1.0F - var3 * 0.15F;
		}

		return var1;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setInteger("Score", this.score);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.score = var1.getInteger("Score");
	}

	public void closeScreen() {
		super.closeScreen();
		this.mc.displayGuiScreen((GuiScreen)null);
	}

	public void displayGUIEditSign(TileEntitySign var1) {
		this.mc.displayGuiScreen(new GuiEditSign(var1));
	}

	public void displayGUIChest(IInventory var1) {
		this.mc.displayGuiScreen(new GuiChest(this.inventory, var1));
	}

	public void displayWorkbenchGUI(int var1, int var2, int var3) {
		this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, var1, var2, var3));
	}

	public void displayGUIEnchantment(int var1, int var2, int var3) {
		this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, var1, var2, var3));
	}

	public void displayGUIFurnace(TileEntityFurnace var1) {
		this.mc.displayGuiScreen(new GuiFurnace(this.inventory, var1));
	}

	public void displayGUIBrewingStand(TileEntityBrewingStand var1) {
		this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, var1));
	}

	public void displayGUIDispenser(TileEntityDispenser var1) {
		this.mc.displayGuiScreen(new GuiDispenser(this.inventory, var1));
	}

	public void onCriticalHit(Entity var1) {
		this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, var1));
	}

	public void onEnchantmentCritical(Entity var1) {
		EntityCrit2FX var2 = new EntityCrit2FX(this.mc.theWorld, var1, "magicCrit");
		this.mc.effectRenderer.addEffect(var2);
	}

	public void onItemPickup(Entity var1, int var2) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, var1, this, -0.5F));
	}

	public void sendChatMessage(String var1) {
	}

	public boolean isSneaking() {
		return this.movementInput.sneak && !this.sleeping;
	}

	public void setHealth(int var1) {
		int var2 = this.getHealth() - var1;
		if(var2 <= 0) {
			this.setEntityHealth(var1);
			if(var2 < 0) {
				this.heartsLife = this.heartsHalvesLife / 2;
			}
		} else {
			this.naturalArmorRating = var2;
			this.setEntityHealth(this.getHealth());
			this.heartsLife = this.heartsHalvesLife;
			this.damageEntity(DamageSource.generic, var2);
			this.hurtTime = this.maxHurtTime = 10;
		}

	}

	public void respawnPlayer() {
		this.mc.respawn(false, 0, false);
	}

	public void func_6420_o() {
	}

	public void addChatMessage(String var1) {
		this.mc.ingameGUI.addChatMessageTranslate(var1);
	}

	public void addStat(StatBase var1, int var2) {
		if(var1 != null) {
			if(var1.isAchievement()) {
				Achievement var3 = (Achievement)var1;
				if(var3.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(var3.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(var3)) {
						this.mc.guiAchievement.queueTakenAchievement(var3);
					}

					this.mc.statFileWriter.readStat(var1, var2);
				}
			} else {
				this.mc.statFileWriter.readStat(var1, var2);
			}

		}
	}

	private boolean isBlockTranslucent(int var1, int var2, int var3) {
		return this.worldObj.isBlockNormalCube(var1, var2, var3);
	}

	protected boolean pushOutOfBlocks(double var1, double var3, double var5) {
		int var7 = MathHelper.floor_double(var1);
		int var8 = MathHelper.floor_double(var3);
		int var9 = MathHelper.floor_double(var5);
		double var10 = var1 - (double)var7;
		double var12 = var5 - (double)var9;
		if(this.isBlockTranslucent(var7, var8, var9) || this.isBlockTranslucent(var7, var8 + 1, var9)) {
			boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
			boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
			boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
			boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;
			if(var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}

			if(var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}

			if(var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}

			if(var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}

			float var21 = 0.1F;
			if(var18 == 0) {
				this.motionX = (double)(-var21);
			}

			if(var18 == 1) {
				this.motionX = (double)var21;
			}

			if(var18 == 4) {
				this.motionZ = (double)(-var21);
			}

			if(var18 == 5) {
				this.motionZ = (double)var21;
			}
		}

		return false;
	}

	public void setSprinting(boolean var1) {
		super.setSprinting(var1);
		this.sprintingTicksLeft = var1 ? 600 : 0;
	}

	public void setXPStats(float var1, int var2, int var3) {
		this.experience = var1;
		this.experienceTotal = var2;
		this.experienceLevel = var3;
	}
}
