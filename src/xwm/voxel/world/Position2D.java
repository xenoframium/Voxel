package xwm.voxel.world;

public class Position2D {

	public int x,y;

	public Position2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position2D(Position2D p){
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Position2D position = (Position2D) o;

		if (x != position.x) return false;
		return y == position.y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	public Position2D modulo(int mod) {
		return new Position2D(Math.floorMod(x, mod),Math.floorMod(y, mod));
	}

	public Position2D div(int divisor) {
		return new Position2D(Math.floorDiv(x, divisor),Math.floorDiv(y, divisor));
	}

	@Override
	public String toString() {
		return "Position (" +
				 x + ", " + y + ')';
	}
}
