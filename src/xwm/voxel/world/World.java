package xwm.voxel.world;

import xwm.voxel.Main;
import xenoframium.ecs.Entity;

import static xwm.voxel.Main.CHUNK_SIZE;

public class World {

	private static Entity world;
	private static WorldComponent worldComponent;

	public static Entity getWorld() {
		if (world == null) {
			world = Main.em.createEntity();
			worldComponent = new WorldComponent();
			world.addComponents(worldComponent);
		}
		return world;
	}

	public static short getBlock(Position pos) {
		return worldComponent.getChunk(pos.toChunkPos())
				.getComponent(BlocksComponent.class).getBlock(pos.toPosInChunk());
	}

	public static void setBlock(Position pos, String blockID) {
		setBlock(pos, BlockCreation.blockFromString(blockID));
	}

	public static void setBlock(Position pos, short blockID) {
		ChunkPos chunkPos = pos.toChunkPos();
		PosInChunk posInChunk = pos.toPosInChunk();
		BlocksComponent c = worldComponent.getChunk(chunkPos).getComponent(BlocksComponent.class);
		c.setBlock(posInChunk, blockID);
		worldComponent.updateRenderingStatus(chunkPos);
		if(posInChunk.x == 0) {//chunk borders
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x - 1, chunkPos.y, chunkPos.z));
		} else if(posInChunk.x == CHUNK_SIZE - 1) {
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x + 1, chunkPos.y, chunkPos.z));
		}
		if(posInChunk.y == 0) {
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x, chunkPos.y - 1, chunkPos.z));
		}  else if(posInChunk.y == CHUNK_SIZE - 1) {
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x, chunkPos.y + 1, chunkPos.z));
		}
		if(posInChunk.z == 0) {
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x, chunkPos.y, chunkPos.z - 1));
		} else if(posInChunk.z == CHUNK_SIZE - 1) {
			worldComponent.updateRenderingStatus(new ChunkPos(chunkPos.x, chunkPos.y, chunkPos.z + 1));
		}
	}




}
