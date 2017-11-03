package xwm.voxel.world;

import xwm.voxel.gfx.ChunkMesh;
import xwm.voxel.world.generation.ChunkGenerator;
import xwm.voxel.Main;
import xenoframium.ecs.Component;
import xenoframium.ecs.Entity;

import java.util.*;


public class WorldComponent implements Component {

	private Map<ChunkPos, Entity> chunks = new HashMap<>();
	private Set<ChunkPos> loaded = new HashSet<>();

	public Entity getChunk(ChunkPos pos) {
		if(!chunks.containsKey(pos)){
			createChunk(pos);
		}
		return chunks.get(pos);
	}

	public boolean isChunkLoaded(ChunkPos p) {
		return loaded.contains(p);
	}

	public Set<ChunkPos> getLoadedChunks() {
		return new HashSet<>(loaded);
	}

	public void loadChunks(Set<ChunkPos> chunksToLoad) {
		for (ChunkPos p : chunksToLoad) {
			if (!isChunkLoaded(p)) {
				loadChunk(p);
			}
		}
	}

	private Entity createChunk(ChunkPos pos) {
		Entity chunk = Main.em.createEntity();
		BlocksComponent cc = new BlocksComponent(pos);
		chunk.addComponents(cc);
		chunks.put(pos, chunk);
		ChunkGenerator.generateChunk(cc);
		return chunk;
	}


	public void loadChunk(ChunkPos pos) {
		if (isChunkLoaded(pos)) {
			return;
		}

		Entity chunk = getChunk(pos);
		BlocksComponent bc = getChunk(pos).getComponent(BlocksComponent.class);
		loaded.add(pos);
		if(bc.isLoaded) {
			return;
		}
		bc.isLoaded = true;
		ChunkMesh.makeRenderable(chunk);
	}

	public void updateRenderingStatus(ChunkPos pos) {
		Entity chunk = getChunk(pos);
		ChunkMesh.makeNonrenderable(chunk);
		ChunkMesh.makeRenderable(chunk);
	}

	public void unloadChunk(ChunkPos pos) {
		Entity chunk = getChunk(pos);
		BlocksComponent bc = chunk.getComponent(BlocksComponent.class);
		if(!bc.isLoaded) {
			return;
		}
		bc.isLoaded = false;
		loaded.remove(pos);
		ChunkMesh.makeNonrenderable(chunk);
	}




}
