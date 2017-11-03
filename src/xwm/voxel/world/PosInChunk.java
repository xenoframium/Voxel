package xwm.voxel.world;

import xenoframium.glmath.linearalgebra.Vec3;

import static xwm.voxel.Main.CHUNK_SIZE;

public class PosInChunk {

	public int x,y,z;

	public PosInChunk(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public PosInChunk(PosInChunk p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public PosInChunk(Position p, ChunkPos c){
		this.x = c.x*CHUNK_SIZE + p.x;
		this.y = c.y*CHUNK_SIZE + p.y;
		this.z = c.z*CHUNK_SIZE + p.z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PosInChunk position = (PosInChunk) o;

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

	public PosInChunk modulo(int mod) {
		return new PosInChunk(Math.floorMod(x, mod),Math.floorMod(y, mod),Math.floorMod(z, mod));
	}

	public PosInChunk div(int divisor) {
		return new PosInChunk(Math.floorDiv(x, divisor),Math.floorDiv(y, divisor),Math.floorDiv(z, divisor));
	}

	public Vec3 toVec3() {
		return new Vec3(x, y, z);
	}

	@Override
	public String toString() {
		return "Position (" +
				 x + ", " + y + ", " + z + ')';
	}
}
