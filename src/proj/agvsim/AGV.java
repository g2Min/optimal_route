package proj.agvsim;

public class AGV {
	private String name;	// AGV 이름
	private int speed;	// 분당 이동속도
	private int capacity;	// 부품 적재량
	private Part holdingPart = null;
	private int numHoldingParts = 0;
	private boolean available = true;	// 이용가능 여부
	private Location currentLoc;	// AGV의 현재 위치
	private Location dest = null;
	private Event currentJob = null;

	public AGV(String name, int speed, int capacity) {
		this.name = name;
		this.speed = speed;
		this.capacity = capacity;
		this.currentLoc = new Location(0, 0);
		this.dest = new Location(0, 0);
	}

	public void move() {
		// 이동 로직 추가
	}
	public AGV(String name, int speed, int capacity, Location location) {
		this.name = name;
		this.speed = speed;
		this.capacity = capacity;
		this.currentLoc = location;
		this.dest = new Location(0, 0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Part getHoldingPart() {
		return holdingPart;
	}

	public int getNumHoldingParts() {
		return numHoldingParts;
	}

	public Location getCurrentLocation() {
		return currentLoc;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLoc = currentLocation;
	}

	public void setDest(Location loc) {
		dest.setX(loc.getX());
		dest.setY(loc.getY());
	}

	// AGV의 목표지점까지 단위 거리만큼 이동
	public void toDest() {
		if (dest == null) throw new IllegalArgumentException("AGV's destination is null. AGV'name: " + name);
		if (currentLoc.isSame(dest)) throw new IllegalArgumentException("AGV is destination already. AGV'name: " + name);

		if (currentLoc.getX() == dest.getX()) {
			if (currentLoc.getY() > dest.getY()) {
				currentLoc.increaseY(-1);
			}
			else {
				currentLoc.increaseY(1);
			}
		}
		else if (currentLoc.getX() > dest.getX()) {
			currentLoc.increaseX(-1);
		}
		else {
			currentLoc.increaseX(1);
		}
	}

	public boolean isDest() {
		if (currentLoc.isSame(dest)) return true;
		else return false;
	}

	// 동일한 부품만 실을 수 있다고 가정
	public void loadUpPart(Part part, int numParts) {
		holdingPart = part;
		numHoldingParts += numParts;
	}

	public Part putDownPart() {
		numHoldingParts = 0;
		Part result = holdingPart;
		if (numHoldingParts == 0) {
			holdingPart = null;
		}
		return result;
	}

	public Event getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(Event job) {
		currentJob = job;
	}

	public void print() {
		System.out.print("\t" + name);
		if (holdingPart != null) {
			System.out.print("(" + holdingPart.getPartType() + ", " + numHoldingParts + ")");
		}
		System.out.print(currentLoc.toString());

	}
}
