package xwm.voxel.world;


import com.sun.istack.internal.Nullable;

public class Blocks {

	private short[][][] blocks; //TODO Octree this?
	short xDimension;
	short yDimension;
	short zDimension;

	public Blocks(short xlength, short ylength, short zlength) {
		xDimension = xlength;
		yDimension = ylength;
		zDimension = zlength;
		blocks = new short[xlength][ylength][zlength];
	}

	@Nullable
	public short getBlock(PosInChunk pos) {
		if(outOfBounds(pos)){
			throw new IllegalArgumentException("Setting block of bounds " + pos);
		}
		return blocks[pos.x][pos.y][pos.z];
	}

	public boolean outOfBounds(PosInChunk pos){
		return (pos.x < 0 || pos.y < 0 || pos.z < 0 || pos.x > xDimension || pos.x > xDimension || pos.x > xDimension);
	}

	public void setBlock(PosInChunk pos, short block) {
		if(outOfBounds(pos)){
			throw new IllegalArgumentException("Setting block of bounds " + pos);
		}
		blocks[pos.x][pos.y][pos.z] = block;
	}
}
