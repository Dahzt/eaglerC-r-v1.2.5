package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AchievementList {
	public static int minDisplayColumn;
	public static int minDisplayRow;
	public static int maxDisplayColumn;
	public static int maxDisplayRow;
	public static List achievementList = new ArrayList();
	public static Achievement openInventory = (new Achievement(0, "openInventory", 0, 0, Item.book, (Achievement)null)).setIndependent().registerAchievement();
	public static Achievement mineWood = (new Achievement(1, "mineWood", 2, 1, Block.wood, openInventory)).registerAchievement();
	public static Achievement buildWorkBench = (new Achievement(2, "buildWorkBench", 4, -1, Block.workbench, mineWood)).registerAchievement();
	public static Achievement buildPickaxe = (new Achievement(3, "buildPickaxe", 4, 2, Item.pickaxeWood, buildWorkBench)).registerAchievement();
	public static Achievement buildFurnace = (new Achievement(4, "buildFurnace", 3, 4, Block.stoneOvenActive, buildPickaxe)).registerAchievement();
	public static Achievement acquireIron = (new Achievement(5, "acquireIron", 1, 4, Item.ingotIron, buildFurnace)).registerAchievement();
	public static Achievement buildHoe = (new Achievement(6, "buildHoe", 2, -3, Item.hoeWood, buildWorkBench)).registerAchievement();
	public static Achievement makeBread = (new Achievement(7, "makeBread", -1, -3, Item.bread, buildHoe)).registerAchievement();
	public static Achievement bakeCake = (new Achievement(8, "bakeCake", 0, -5, Item.cake, buildHoe)).registerAchievement();
	public static Achievement buildBetterPickaxe = (new Achievement(9, "buildBetterPickaxe", 6, 2, Item.pickaxeStone, buildPickaxe)).registerAchievement();
	public static Achievement cookFish = (new Achievement(10, "cookFish", 2, 6, Item.fishCooked, buildFurnace)).registerAchievement();
	public static Achievement onARail = (new Achievement(11, "onARail", 2, 3, Block.rail, acquireIron)).setSpecial().registerAchievement();
	public static Achievement buildSword = (new Achievement(12, "buildSword", 6, -1, Item.swordWood, buildWorkBench)).registerAchievement();
	public static Achievement killEnemy = (new Achievement(13, "killEnemy", 8, -1, Item.bone, buildSword)).registerAchievement();
	public static Achievement killCow = (new Achievement(14, "killCow", 7, -3, Item.leather, buildSword)).registerAchievement();
	public static Achievement flyPig = (new Achievement(15, "flyPig", 8, -4, Item.saddle, killCow)).setSpecial().registerAchievement();
	public static Achievement snipeSkeleton = (new Achievement(16, "snipeSkeleton", 7, 0, Item.bow, killEnemy)).setSpecial().registerAchievement();
	public static Achievement diamonds = (new Achievement(17, "diamonds", -1, 5, Item.diamond, acquireIron)).registerAchievement();
	public static Achievement portal = (new Achievement(18, "portal", -1, 7, Block.obsidian, diamonds)).registerAchievement();
	public static Achievement ghast = (new Achievement(19, "ghast", -4, 8, Item.ghastTear, portal)).setSpecial().registerAchievement();
	public static Achievement blazeRod = (new Achievement(20, "blazeRod", 0, 9, Item.blazeRod, portal)).registerAchievement();
	public static Achievement potion = (new Achievement(21, "potion", 2, 8, Item.potion, blazeRod)).registerAchievement();
	public static Achievement theEnd = (new Achievement(22, "theEnd", 3, 10, Item.eyeOfEnder, blazeRod)).setSpecial().registerAchievement();
	public static Achievement theEnd2 = (new Achievement(23, "theEnd2", 4, 13, Block.dragonEgg, theEnd)).setSpecial().registerAchievement();
	public static Achievement enchantments = (new Achievement(24, "enchantments", -4, 4, Block.enchantmentTable, diamonds)).registerAchievement();
	public static Achievement overkill = (new Achievement(25, "overkill", -4, 1, Item.swordDiamond, enchantments)).setSpecial().registerAchievement();
	public static Achievement bookcase = (new Achievement(26, "bookcase", -3, 6, Block.bookShelf, enchantments)).registerAchievement();

	public static void init() {
	}

	static {
		System.out.println(achievementList.size() + " achievements");
	}
}
