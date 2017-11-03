package xwm.voxel.world;

import com.sun.istack.internal.Nullable;
import xwm.voxel.Main;
import xenoframium.ecs.Component;

public class BlocksComponent implements Component{

	boolean isLoaded = false;

	public Blocks blocks = new Blocks((short)Main.CHUNK_SIZE, (short)Main.CHUNK_SIZE, (short)Main.CHUNK_SIZE);

	public ChunkPos pos;

	public BlocksComponent(ChunkPos pos) {
		this.pos = pos;
	}

	@Nullable
	public short getBlock(PosInChunk pos) {
		return blocks.getBlock(pos);
	}

	public void setBlock(PosInChunk pos, short block) {
		blocks.setBlock(pos, block);
	}

}
