package proj.agvsim;

public class Location {
	private String name;	// location 이름
	private int x;	// x 좌표
	private int y;	// y 좌표

	public Location(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public Location(int x, int y) {
		this.name = null;
		this.x = x;
		this.y = y;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void increaseX(int inc) {
		x += inc;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void increaseY(int inc) {
		y += inc;
	}

	public void setCoordinate(int[] coordinate) {
		if (coordinate.length != 2 )
			throw new IllegalArgumentException("illegal coordinate.");
		this.x = coordinate[0];
		this.y = coordinate[1];
	}

	public int[] getCoordinate() {
		int[] result = {x, y};
		return result;
	}

	public boolean isSame(Location loc) {
		if (x == loc.getX() && y == loc.getY())
			return true;
		else return false;
	}

	public String toString() {
		String result = new StringBuilder().append("(").append(Integer.toString(x)).append(", ").append(Integer.toString(y)).append(")").toString();
		return result;
	}
}
