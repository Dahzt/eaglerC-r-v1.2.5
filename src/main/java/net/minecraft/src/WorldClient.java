package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class WorldClient extends World {
	private LinkedList blocksToReceive = new LinkedList();
	private NetClientHandler sendQueue;
	private ChunkProviderClient field_20915_C;
	private IntHashMap entityHashSet = new IntHashMap();
	private Set entityList = new HashSet();
	private Set entitySpawnQueue = new HashSet();

	public WorldClient(NetClientHandler var1, WorldSettings var2, int var3, int var4) {
		super(new SaveHandlerMP(), "MpServer", (WorldProvider)WorldProvider.getProviderForDimension(var3), (WorldSettings)var2);
		this.sendQueue = var1;
		this.difficultySetting = var4;
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
		this.mapStorage = var1.mapStorage;
	}

	public void tick() {
		this.setWorldTime(this.getWorldTime() + 1L);

		int var1;
		for(var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1) {
			Entity var2 = (Entity)this.entitySpawnQueue.iterator().next();
			this.entitySpawnQueue.remove(var2);
			if(!this.loadedEntityList.contains(var2)) {
				this.spawnEntityInWorld(var2);
			}
		}

		this.sendQueue.processReadPackets();

		for(var1 = 0; var1 < this.blocksToReceive.size(); ++var1) {
			WorldBlockPositionType var3 = (WorldBlockPositionType)this.blocksToReceive.get(var1);
			if(--var3.acceptCountdown == 0) {
				super.setBlockAndMetadata(var3.posX, var3.posY, var3.posZ, var3.blockID, var3.metadata);
				super.markBlockNeedsUpdate(var3.posX, var3.posY, var3.posZ);
				this.blocksToReceive.remove(var1--);
			}
		}

		this.field_20915_C.unload100OldestChunks();
		this.tickBlocksAndAmbiance();
	}

	public void invalidateBlockReceiveRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
		for(int var7 = 0; var7 < this.blocksToReceive.size(); ++var7) {
			WorldBlockPositionType var8 = (WorldBlockPositionType)this.blocksToReceive.get(var7);
			if(var8.posX >= var1 && var8.posY >= var2 && var8.posZ >= var3 && var8.posX <= var4 && var8.posY <= var5 && var8.posZ <= var6) {
				this.blocksToReceive.remove(var7--);
			}
		}

	}

	protected IChunkProvider createChunkProvider() {
		this.field_20915_C = new ChunkProviderClient(this);
		return this.field_20915_C;
	}

	public void setSpawnLocation() {
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
	}

	protected void tickBlocksAndAmbiance() {
		this.func_48461_r();
		Iterator var1 = this.activeChunkSet.iterator();

		while(var1.hasNext()) {
			ChunkCoordIntPair var2 = (ChunkCoordIntPair)var1.next();
			int var3 = var2.chunkXPos * 16;
			int var4 = var2.chunkZPos * 16;
			Profiler.startSection("getChunk");
			Chunk var5 = this.getChunkFromChunkCoords(var2.chunkXPos, var2.chunkZPos);
			this.func_48458_a(var3, var4, var5);
			Profiler.endSection();
		}

	}

	public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
	}

	public boolean tickUpdates(boolean var1) {
		return false;
	}

	public void doPreChunk(int var1, int var2, boolean var3) {
		if(var3) {
			this.field_20915_C.loadChunk(var1, var2);
		} else {
			this.field_20915_C.func_539_c(var1, var2);
		}

		if(!var3) {
			this.markBlocksDirty(var1 * 16, 0, var2 * 16, var1 * 16 + 15, 256, var2 * 16 + 15);
		}

	}

	public boolean spawnEntityInWorld(Entity var1) {
		boolean var2 = super.spawnEntityInWorld(var1);
		this.entityList.add(var1);
		if(!var2) {
			this.entitySpawnQueue.add(var1);
		}

		return var2;
	}

	public void setEntityDead(Entity var1) {
		super.setEntityDead(var1);
		this.entityList.remove(var1);
	}

	protected void obtainEntitySkin(Entity var1) {
		super.obtainEntitySkin(var1);
		if(this.entitySpawnQueue.contains(var1)) {
			this.entitySpawnQueue.remove(var1);
		}

	}

	protected void releaseEntitySkin(Entity var1) {
		super.releaseEntitySkin(var1);
		if(this.entityList.contains(var1)) {
			if(var1.isEntityAlive()) {
				this.entitySpawnQueue.add(var1);
			} else {
				this.entityList.remove(var1);
			}
		}

	}

	public void addEntityToWorld(int var1, Entity var2) {
		Entity var3 = this.getEntityByID(var1);
		if(var3 != null) {
			this.setEntityDead(var3);
		}

		this.entityList.add(var2);
		var2.entityId = var1;
		if(!this.spawnEntityInWorld(var2)) {
			this.entitySpawnQueue.add(var2);
		}

		this.entityHashSet.addKey(var1, var2);
	}

	public Entity getEntityByID(int var1) {
		return (Entity)this.entityHashSet.lookup(var1);
	}

	public Entity removeEntityFromWorld(int var1) {
		Entity var2 = (Entity)this.entityHashSet.removeObject(var1);
		if(var2 != null) {
			this.entityList.remove(var2);
			this.setEntityDead(var2);
		}

		return var2;
	}

	public boolean setBlockAndMetadataAndInvalidate(int var1, int var2, int var3, int var4, int var5) {
		this.invalidateBlockReceiveRegion(var1, var2, var3, var1, var2, var3);
		return super.setBlockAndMetadataWithNotify(var1, var2, var3, var4, var5);
	}

	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.quitWithPacket(new Packet255KickDisconnect("Quitting"));
	}

	protected void updateWeather() {
		if(!this.worldProvider.hasNoSky) {
			if(this.lastLightningBolt > 0) {
				--this.lastLightningBolt;
			}

			this.prevRainingStrength = this.rainingStrength;
			if(this.worldInfo.isRaining()) {
				this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
			} else {
				this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
			}

			if(this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if(this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if(this.worldInfo.isThundering()) {
				this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
			} else {
				this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
			}

			if(this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if(this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}

		}
	}
}
