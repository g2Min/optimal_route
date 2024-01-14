package proj.agvsim;

import java.util.ArrayList;
import java.util.List;

public class Part {
	private String partType;
	private List<Location> path;

	public Part(String partType, List<Location> path) {
		this.partType = partType;
		this.path=path;
	}

	public String getPartType() {
		return partType;
	}

	public List<Location> getPath() {
		return path;
	}

	public Location getNextLocation(Location location) {
		int index = path.indexOf(location);
		Location result = new Location(path.get(index + 1).getX(), path.get(index + 1).getY());
		return result;
	}
}
