package net.minecraft.src;

public class ItemSoup extends ItemFood {
	public ItemSoup(int var1, int var2) {
		super(var1, var2, false);
		this.setMaxStackSize(1);
	}

	public ItemStack onFoodEaten(ItemStack var1, World var2, EntityPlayer var3) {
		super.onFoodEaten(var1, var2, var3);
		return new ItemStack(Item.bowlEmpty);
	}
}
