package net.minecraft.src;

public class ContainerDispenser extends Container {
	private TileEntityDispenser tileEntityDispenser;

	public ContainerDispenser(IInventory var1, TileEntityDispenser var2) {
		this.tileEntityDispenser = var2;

		int var3;
		int var4;
		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 3; ++var4) {
				this.addSlot(new Slot(var2, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 3; ++var3) {
			for(var4 = 0; var4 < 9; ++var4) {
				this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for(var3 = 0; var3 < 9; ++var3) {
			this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
		}

	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.tileEntityDispenser.isUseableByPlayer(var1);
	}

	public ItemStack transferStackInSlot(int var1) {
		ItemStack var2 = null;
		Slot var3 = (Slot)this.inventorySlots.get(var1);
		if(var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if(var1 < 9) {
				if(!this.mergeItemStack(var4, 9, 45, true)) {
					return null;
				}
			} else if(!this.mergeItemStack(var4, 0, 9, false)) {
				return null;
			}

			if(var4.stackSize == 0) {
				var3.putStack((ItemStack)null);
			} else {
				var3.onSlotChanged();
			}

			if(var4.stackSize == var2.stackSize) {
				return null;
			}

			var3.onPickupFromSlot(var4);
		}

		return var2;
	}
}
