package net.minecraft.src;

public class LongHashMap {
	private transient LongHashMapEntry[] hashArray = new LongHashMapEntry[16];
	private transient int numHashElements;
	private int capacity = 12;
	private final float percentUseable = 12.0F / 16.0F;
	private transient volatile int modCount;

	private static int getHashedKey(long var0) {
		return hash((int)(var0 ^ var0 >>> 32));
	}

	private static int hash(int var0) {
		var0 ^= var0 >>> 20 ^ var0 >>> 12;
		return var0 ^ var0 >>> 7 ^ var0 >>> 4;
	}

	private static int getHashIndex(int var0, int var1) {
		return var0 & var1 - 1;
	}

	public int getNumHashElements() {
		return this.numHashElements;
	}

	public Object getValueByKey(long var1) {
		int var3 = getHashedKey(var1);

		for(LongHashMapEntry var4 = this.hashArray[getHashIndex(var3, this.hashArray.length)]; var4 != null; var4 = var4.nextEntry) {
			if(var4.key == var1) {
				return var4.value;
			}
		}

		return null;
	}

	public boolean containsItem(long var1) {
		return this.getEntry(var1) != null;
	}

	final LongHashMapEntry getEntry(long var1) {
		int var3 = getHashedKey(var1);

		for(LongHashMapEntry var4 = this.hashArray[getHashIndex(var3, this.hashArray.length)]; var4 != null; var4 = var4.nextEntry) {
			if(var4.key == var1) {
				return var4;
			}
		}

		return null;
	}

	public void add(long var1, Object var3) {
		int var4 = getHashedKey(var1);
		int var5 = getHashIndex(var4, this.hashArray.length);

		for(LongHashMapEntry var6 = this.hashArray[var5]; var6 != null; var6 = var6.nextEntry) {
			if(var6.key == var1) {
				var6.value = var3;
			}
		}

		++this.modCount;
		this.createKey(var4, var1, var3, var5);
	}

	private void resizeTable(int var1) {
		LongHashMapEntry[] var2 = this.hashArray;
		int var3 = var2.length;
		if(var3 == 1073741824) {
			this.capacity = Integer.MAX_VALUE;
		} else {
			LongHashMapEntry[] var4 = new LongHashMapEntry[var1];
			this.copyHashTableTo(var4);
			this.hashArray = var4;
			this.capacity = (int)((float)var1 * this.percentUseable);
		}
	}

	private void copyHashTableTo(LongHashMapEntry[] var1) {
		LongHashMapEntry[] var2 = this.hashArray;
		int var3 = var1.length;

		for(int var4 = 0; var4 < var2.length; ++var4) {
			LongHashMapEntry var5 = var2[var4];
			if(var5 != null) {
				var2[var4] = null;

				LongHashMapEntry var6;
				do {
					var6 = var5.nextEntry;
					int var7 = getHashIndex(var5.hash, var3);
					var5.nextEntry = var1[var7];
					var1[var7] = var5;
					var5 = var6;
				} while(var6 != null);
			}
		}

	}

	public Object remove(long var1) {
		LongHashMapEntry var3 = this.removeKey(var1);
		return var3 == null ? null : var3.value;
	}

	final LongHashMapEntry removeKey(long var1) {
		int var3 = getHashedKey(var1);
		int var4 = getHashIndex(var3, this.hashArray.length);
		LongHashMapEntry var5 = this.hashArray[var4];

		LongHashMapEntry var6;
		LongHashMapEntry var7;
		for(var6 = var5; var6 != null; var6 = var7) {
			var7 = var6.nextEntry;
			if(var6.key == var1) {
				++this.modCount;
				--this.numHashElements;
				if(var5 == var6) {
					this.hashArray[var4] = var7;
				} else {
					var5.nextEntry = var7;
				}

				return var6;
			}

			var5 = var6;
		}

		return var6;
	}

	private void createKey(int var1, long var2, Object var4, int var5) {
		LongHashMapEntry var6 = this.hashArray[var5];
		this.hashArray[var5] = new LongHashMapEntry(var1, var2, var4, var6);
		if(this.numHashElements++ >= this.capacity) {
			this.resizeTable(2 * this.hashArray.length);
		}

	}

	static int getHashCode(long var0) {
		return getHashedKey(var0);
	}
}
