package xwm.voxel.world;

import xenoframium.glmath.linearalgebra.Vec3;

import static xwm.voxel.Main.CHUNK_SIZE;

public class ChunkPos {

	public int x,y,z;

	public ChunkPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ChunkPos(ChunkPos p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public ChunkPos(Position p){
		this.x = Math.floorDiv(p.x, CHUNK_SIZE);
		this.y = Math.floorDiv(p.y, CHUNK_SIZE);
		this.z = Math.floorDiv(p.z, CHUNK_SIZE);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ChunkPos position = (ChunkPos) o;

		if (x != position.x) return false;
		if (y != position.y) return false;
		return z == position.z;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}

	public ChunkPos modulo(int mod) {
		return new ChunkPos(Math.floorMod(x, mod),Math.floorMod(y, mod),Math.floorMod(z, mod));
	}

	public ChunkPos div(int divisor) {
		return new ChunkPos(Math.floorDiv(x, divisor),Math.floorDiv(y, divisor),Math.floorDiv(z, divisor));
	}

	public Vec3 toVec3() {
		return new Vec3(x, y, z);
	}

	public Position toPosition() {
		return new Position(x*CHUNK_SIZE, y*CHUNK_SIZE, z*CHUNK_SIZE);
	}

	@Override
	public String toString() {
		return "Position (" +
				 x + ", " + y + ", " + z + ')';
	}
}
