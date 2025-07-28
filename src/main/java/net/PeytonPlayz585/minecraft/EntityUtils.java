package net.PeytonPlayz585.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class EntityUtils {
	
	public static Entity newInstance(Class t) {
		if(t == EntityArrow.class) {
			return new EntityArrow(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityBlaze.class) {
			return new EntityBlaze(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityBoat.class) {
			return new EntityBoat(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityCaveSpider.class) {
			return new EntityCaveSpider(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityChicken.class) {
			return new EntityChicken(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityCow.class) {
			return new EntityCow(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityCreeper.class) {
			return new EntityCreeper(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityDragon.class) {
			return new EntityDragon(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityDragonBase.class) {
			return new EntityDragonBase(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityEgg.class) {
			return new EntityEgg(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityEnderCrystal.class) {
			return new EntityEnderCrystal(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityEnderEye.class) {
			return new EntityEnderEye(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityEnderman.class) {
			return new EntityEnderman(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityEnderPearl.class) {
			return new EntityEnderPearl(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityExpBottle.class) {
			return new EntityExpBottle(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityFallingSand.class) {
			return new EntityFallingSand(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityFireball.class) {
			return new EntityFireball(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityFishHook.class) {
			return new EntityFishHook(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityGhast.class) {
			return new EntityGhast(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityGiantZombie.class) {
			return new EntityGiantZombie(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityIronGolem.class) {
			return new EntityIronGolem(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityItem.class) {
			return new EntityItem(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityMagmaCube.class) {
			return new EntityMagmaCube(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityMinecart.class) {
			return new EntityMinecart(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityMooshroom.class) {
			return new EntityMooshroom(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityOcelot.class) {
			return new EntityOcelot(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityPainting.class) {
			return new EntityPainting(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityPig.class) {
			return new EntityPig(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityPigZombie.class) {
			return new EntityPigZombie(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityPotion.class) {
			return new EntityPotion(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySheep.class) {
			return new EntitySheep(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySilverfish.class) {
			return new EntitySilverfish(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySkeleton.class) {
			return new EntitySkeleton(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySlime.class) {
			return new EntitySlime(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySmallFireball.class) {
			return new EntitySmallFireball(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySnowball.class) {
			return new EntitySnowball(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySnowman.class) {
			return new EntitySnowman(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySpider.class) {
			return new EntitySpider(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntitySquid.class) {
			return new EntityCow(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityTNTPrimed.class) {
			return new EntityTNTPrimed(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityVillager.class) {
			return new EntityVillager(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityWolf.class) {
			return new EntityWolf(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityXPOrb.class) {
			return new EntityXPOrb(Minecraft.getMinecraft().theWorld);
		}
		
		if(t == EntityZombie.class) {
			return new EntityZombie(Minecraft.getMinecraft().theWorld);
		}
		
		return null;
	}
	
	public static TileEntity newInstanceTileEntity(Class t) {
		if(t == TileEntityBrewingStand.class) {
			return new TileEntityBrewingStand();
		}
		
		if(t == TileEntityChest.class) {
			return new TileEntityChest();
		}
		
		if(t == TileEntityDispenser.class) {
			return new TileEntityDispenser();
		}
		
		if(t == TileEntityEnchantmentTable.class) {
			return new TileEntityEnchantmentTable();
		}
		
		if(t == TileEntityEndPortal.class) {
			return new TileEntityEndPortal();
		}
		
		if(t == TileEntityFurnace.class) {
			return new TileEntityFurnace();
		}
		
		if(t == TileEntityMobSpawner.class) {
			return new TileEntityMobSpawner();
		}
		
		if(t == TileEntityNote.class) {
			return new TileEntityNote();
		}
		
		if(t == TileEntityPiston.class) {
			return new TileEntityPiston();
		}
		
		if(t == TileEntityRecordPlayer.class) {
			return new TileEntityRecordPlayer();
		}
		
		if(t == TileEntityFurnace.class) {
			return new TileEntitySign();
		}
		
		return null;
	}

}
