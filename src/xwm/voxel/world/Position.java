package xwm.voxel.world;

import xenoframium.glmath.linearalgebra.Vec3;

import static xwm.voxel.Main.CHUNK_SIZE;

public class Position {

	public int x,y,z;

	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position(Position p){
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Position position = (Position) o;

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

	public Position modulo(int mod) {
		return new Position(Math.floorMod(x, mod),Math.floorMod(y, mod),Math.floorMod(z, mod));
	}

	public Position div(int divisor) {
		return new Position(Math.floorDiv(x, divisor),Math.floorDiv(y, divisor),Math.floorDiv(z, divisor));
	}

	public Vec3 toVec3() {
		return new Vec3(x, y, z);
	}

	@Override
	public String toString() {
		return "Position (" +
				 x + ", " + y + ", " + z + ')';
	}

	public ChunkPos toChunkPos() {
		return new ChunkPos(Math.floorDiv(x, CHUNK_SIZE), Math.floorDiv(y, CHUNK_SIZE), Math.floorDiv(z, CHUNK_SIZE));
	}

	public PosInChunk toPosInChunk() {
		return new PosInChunk(Math.floorMod(x, CHUNK_SIZE), Math.floorMod(y, CHUNK_SIZE), Math.floorMod(z, CHUNK_SIZE));
	}
}
