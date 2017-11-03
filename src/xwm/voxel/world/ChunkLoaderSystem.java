package xwm.voxel.world;

import xwm.voxel.player.PlayerInputSystem;
import xwm.voxel.Main;
import xenoframium.ecs.BaseSystem;
import xenoframium.ecs.Entity;
import xenoframium.glmath.linearalgebra.Vec3;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;

public class ChunkLoaderSystem implements BaseSystem {

	WorldComponent world;
	public static final int CHUNK_LOAD_DISTANCE = 150;
	public static final int MAX_MILLISECONDS_CHUNKLOADING = 10;

	@Override
	public void notifyEntityAddition(Entity entity) {
		if(world == null) {
			world = entity.getComponent(WorldComponent.class);
		} else {
			System.out.println("World already exists");
		}
	}

	@Override
	public void notifyEntityRemoval(Entity entity) {
		System.out.println("World being removed");
	}

	private double lastUpdate = 0;
	@Override
	public void update(double l, double time) {
		if (time - lastUpdate < 0.05) {
			return;
		}
		lastUpdate = time;
		int cx = (int) PlayerInputSystem.getPlayerPosition().x / Main.CHUNK_SIZE;
		int cy = (int) PlayerInputSystem.getPlayerPosition().y / Main.CHUNK_SIZE;
		int cz = (int) PlayerInputSystem.getPlayerPosition().z / Main.CHUNK_SIZE;
		Vec3 ppos = new Vec3(cx, cy, cz);
		int chunkCount = CHUNK_LOAD_DISTANCE / Main.CHUNK_SIZE;
		PriorityQueue<ChunkPos> chunks = new PriorityQueue<>(new Comparator<ChunkPos>() {
			@Override
			public int compare(ChunkPos o1, ChunkPos o2) {
				return Float.compare(o1.toVec3().subt(ppos).magSq(), o2.toVec3().subt(ppos).magSq());
			}
		});
		for(int x = -chunkCount; x <= chunkCount; x++) {
			for (int y = -chunkCount; y <= chunkCount; y++) {
				for (int z = -chunkCount; z <= chunkCount; z++) {
					ChunkPos pos = new ChunkPos(x + cx, y + cy, z + cz);
					chunks.add(pos);
				}
			}
		}
		Set<ChunkPos> toUnload = world.getLoadedChunks();
		toUnload.removeAll(chunks);

		for(ChunkPos p : toUnload){
			world.unloadChunk(p);
		}

		long startTime = System.currentTimeMillis();
		int numLoaded = 0;
		while (!chunks.isEmpty()) {
			ChunkPos pos = chunks.poll();
			if (world.isChunkLoaded(pos)) {
				continue;
			}
			world.loadChunk(pos);
			numLoaded++;
			if (System.currentTimeMillis() - startTime > MAX_MILLISECONDS_CHUNKLOADING) {
				break;
			}
		}


	}
}
