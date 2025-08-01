package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Container {
	public List inventoryItemStacks = new ArrayList();
	public List inventorySlots = new ArrayList();
	public int windowId = 0;
	private short transactionID = 0;
	protected List crafters = new ArrayList();
	private Set field_20918_b = new HashSet();

	protected void addSlot(Slot var1) {
		var1.slotNumber = this.inventorySlots.size();
		this.inventorySlots.add(var1);
		this.inventoryItemStacks.add((Object)null);
	}

	public void updateCraftingResults() {
		for(int var1 = 0; var1 < this.inventorySlots.size(); ++var1) {
			ItemStack var2 = ((Slot)this.inventorySlots.get(var1)).getStack();
			ItemStack var3 = (ItemStack)this.inventoryItemStacks.get(var1);
			if(!ItemStack.areItemStacksEqual(var3, var2)) {
				var3 = var2 == null ? null : var2.copy();
				this.inventoryItemStacks.set(var1, var3);

				for(int var4 = 0; var4 < this.crafters.size(); ++var4) {
					((ICrafting)this.crafters.get(var4)).updateCraftingInventorySlot(this, var1, var3);
				}
			}
		}

	}

	public boolean enchantItem(EntityPlayer var1, int var2) {
		return false;
	}

	public Slot getSlot(int var1) {
		return (Slot)this.inventorySlots.get(var1);
	}

	public ItemStack transferStackInSlot(int var1) {
		Slot var2 = (Slot)this.inventorySlots.get(var1);
		return var2 != null ? var2.getStack() : null;
	}

	public ItemStack slotClick(int var1, int var2, boolean var3, EntityPlayer var4) {
		ItemStack var5 = null;
		if(var2 > 1) {
			return null;
		} else {
			if(var2 == 0 || var2 == 1) {
				InventoryPlayer var6 = var4.inventory;
				if(var1 == -999) {
					if(var6.getItemStack() != null && var1 == -999) {
						if(var2 == 0) {
							var4.dropPlayerItem(var6.getItemStack());
							var6.setItemStack((ItemStack)null);
						}

						if(var2 == 1) {
							var4.dropPlayerItem(var6.getItemStack().splitStack(1));
							if(var6.getItemStack().stackSize == 0) {
								var6.setItemStack((ItemStack)null);
							}
						}
					}
				} else if(var3) {
					ItemStack var7 = this.transferStackInSlot(var1);
					if(var7 != null) {
						int var8 = var7.itemID;
						var5 = var7.copy();
						Slot var9 = (Slot)this.inventorySlots.get(var1);
						if(var9 != null && var9.getStack() != null && var9.getStack().itemID == var8) {
							this.retrySlotClick(var1, var2, var3, var4);
						}
					}
				} else {
					if(var1 < 0) {
						return null;
					}

					Slot var12 = (Slot)this.inventorySlots.get(var1);
					if(var12 != null) {
						var12.onSlotChanged();
						ItemStack var13 = var12.getStack();
						ItemStack var14 = var6.getItemStack();
						if(var13 != null) {
							var5 = var13.copy();
						}

						int var10;
						if(var13 == null) {
							if(var14 != null && var12.isItemValid(var14)) {
								var10 = var2 == 0 ? var14.stackSize : 1;
								if(var10 > var12.getSlotStackLimit()) {
									var10 = var12.getSlotStackLimit();
								}

								var12.putStack(var14.splitStack(var10));
								if(var14.stackSize == 0) {
									var6.setItemStack((ItemStack)null);
								}
							}
						} else if(var14 == null) {
							var10 = var2 == 0 ? var13.stackSize : (var13.stackSize + 1) / 2;
							ItemStack var11 = var12.decrStackSize(var10);
							var6.setItemStack(var11);
							if(var13.stackSize == 0) {
								var12.putStack((ItemStack)null);
							}

							var12.onPickupFromSlot(var6.getItemStack());
						} else if(var12.isItemValid(var14)) {
							if(var13.itemID == var14.itemID && (!var13.getHasSubtypes() || var13.getItemDamage() == var14.getItemDamage()) && ItemStack.func_46154_a(var13, var14)) {
								var10 = var2 == 0 ? var14.stackSize : 1;
								if(var10 > var12.getSlotStackLimit() - var13.stackSize) {
									var10 = var12.getSlotStackLimit() - var13.stackSize;
								}

								if(var10 > var14.getMaxStackSize() - var13.stackSize) {
									var10 = var14.getMaxStackSize() - var13.stackSize;
								}

								var14.splitStack(var10);
								if(var14.stackSize == 0) {
									var6.setItemStack((ItemStack)null);
								}

								var13.stackSize += var10;
							} else if(var14.stackSize <= var12.getSlotStackLimit()) {
								var12.putStack(var14);
								var6.setItemStack(var13);
							}
						} else if(var13.itemID == var14.itemID && var14.getMaxStackSize() > 1 && (!var13.getHasSubtypes() || var13.getItemDamage() == var14.getItemDamage()) && ItemStack.func_46154_a(var13, var14)) {
							var10 = var13.stackSize;
							if(var10 > 0 && var10 + var14.stackSize <= var14.getMaxStackSize()) {
								var14.stackSize += var10;
								var13 = var12.decrStackSize(var10);
								if(var13.stackSize == 0) {
									var12.putStack((ItemStack)null);
								}

								var12.onPickupFromSlot(var6.getItemStack());
							}
						}
					}
				}
			}

			return var5;
		}
	}

	protected void retrySlotClick(int var1, int var2, boolean var3, EntityPlayer var4) {
		this.slotClick(var1, var2, var3, var4);
	}

	public void onCraftGuiClosed(EntityPlayer var1) {
		InventoryPlayer var2 = var1.inventory;
		if(var2.getItemStack() != null) {
			var1.dropPlayerItem(var2.getItemStack());
			var2.setItemStack((ItemStack)null);
		}

	}

	public void onCraftMatrixChanged(IInventory var1) {
		this.updateCraftingResults();
	}

	public void putStackInSlot(int var1, ItemStack var2) {
		this.getSlot(var1).putStack(var2);
	}

	public void putStacksInSlots(ItemStack[] var1) {
		for(int var2 = 0; var2 < var1.length; ++var2) {
			this.getSlot(var2).putStack(var1[var2]);
		}

	}

	public void updateProgressBar(int var1, int var2) {
	}

	public short getNextTransactionID(InventoryPlayer var1) {
		++this.transactionID;
		return this.transactionID;
	}

	public void func_20113_a(short var1) {
	}

	public void func_20110_b(short var1) {
	}

	public abstract boolean canInteractWith(EntityPlayer var1);

	protected boolean mergeItemStack(ItemStack var1, int var2, int var3, boolean var4) {
		boolean var5 = false;
		int var6 = var2;
		if(var4) {
			var6 = var3 - 1;
		}

		Slot var7;
		ItemStack var8;
		if(var1.isStackable()) {
			while(var1.stackSize > 0 && (!var4 && var6 < var3 || var4 && var6 >= var2)) {
				var7 = (Slot)this.inventorySlots.get(var6);
				var8 = var7.getStack();
				if(var8 != null && var8.itemID == var1.itemID && (!var1.getHasSubtypes() || var1.getItemDamage() == var8.getItemDamage()) && ItemStack.func_46154_a(var1, var8)) {
					int var9 = var8.stackSize + var1.stackSize;
					if(var9 <= var1.getMaxStackSize()) {
						var1.stackSize = 0;
						var8.stackSize = var9;
						var7.onSlotChanged();
						var5 = true;
					} else if(var8.stackSize < var1.getMaxStackSize()) {
						var1.stackSize -= var1.getMaxStackSize() - var8.stackSize;
						var8.stackSize = var1.getMaxStackSize();
						var7.onSlotChanged();
						var5 = true;
					}
				}

				if(var4) {
					--var6;
				} else {
					++var6;
				}
			}
		}

		if(var1.stackSize > 0) {
			if(var4) {
				var6 = var3 - 1;
			} else {
				var6 = var2;
			}

			while(!var4 && var6 < var3 || var4 && var6 >= var2) {
				var7 = (Slot)this.inventorySlots.get(var6);
				var8 = var7.getStack();
				if(var8 == null) {
					var7.putStack(var1.copy());
					var7.onSlotChanged();
					var1.stackSize = 0;
					var5 = true;
					break;
				}

				if(var4) {
					--var6;
				} else {
					++var6;
				}
			}
		}

		return var5;
	}
}
