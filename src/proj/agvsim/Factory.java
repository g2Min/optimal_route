package proj.agvsim;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Factory {
	private String factoryName;
	private Location inventory;
	private Location storage;
	private List<Port> ports;
	private List<Part> parts;
	private AGV[] AGVs;
	private int numPorts = 0;
	private int numParts = 0;
	private int numAGVs = 0;
	private Map<String, Integer> remainQuota;
	private Map<String, Integer> output;
	private int generatedParts = 0;
	private int partID = 0;
	private Port inputPort;

	public Factory(String factoryName) {
		this.factoryName = factoryName;
		this.inventory = new Location("I", 0, 0);
		this.storage = new Location("S", 0, 6);

		ports = new ArrayList<Port>();
		Location A = new Location("A", 4, 1);
		Location B = new Location("B", 6, 1);
		Location C = new Location("C", 5, 3);
		Location D = new Location("D", 4, 5);
		Location E = new Location("E", 6, 5);
		ports.add(new Port("A", "processing", 300, A));
		ports.add(new Port("B", "processing", 300, B));
		ports.add(new Port("C", "painting", 120, C));
		ports.add(new Port("D", "asembly", 180, D));
		ports.add(new Port("E", "asembly", 600, E));
		numPorts = 5;

		List<Location> path;
		parts = new ArrayList<Part>();
		path = new ArrayList<>(Arrays.asList(inventory, A, D, storage));
		parts.add(new Part("X", path));
		path = new ArrayList<>(Arrays.asList(inventory, B, C, D, storage));
		parts.add(new Part("Y", path));
		path = new ArrayList<>(Arrays.asList(inventory, B, C, E, storage));
		parts.add(new Part("Z", path));
		numParts = 3;

		AGVs = new AGV[3];
		numAGVs = 3;
		int AGVSpeed = 2;
		for (int i = 0; i < numAGVs; i++) {
			AGVs[i] = new AGV("AGV" + Integer.toString(i + 1), AGVSpeed, i + 1);
		}

		remainQuota = new HashMap<>();
		remainQuota.put("X", 100);
		remainQuota.put("Y", 100);
		remainQuota.put("Z", 100);
		output = new HashMap<>();
		output.put("X", 0);
		output.put("Y", 0);
		output.put("Z", 0);

	}


	public Location getInvLoc() {
		return inventory;
	}

	public int getRemainQuota(String partType) {
		int result = 0;
		if (remainQuota.containsKey(partType)) {
			result = remainQuota.get(partType);
		}
		else {
			throw new IllegalArgumentException("No part with partType: " + partType);
		}
		return result;
	}

	public int getNumPorts() {
		return numPorts;
	}

	public int getNumParts() {
		return numParts;
	}

	public AGV[] getAGVs() {
		return AGVs;
	}

	public Port getPort(int index) {
		return ports.get(index);
	}

	public Part getPart(String partType) {
		Part result = null;
		for (Part part : parts) {
			if (part.getPartType() == partType) {
				result = part;
			}
		}
		return result;
	}

	public Part getPart(int index) {
		return parts.get(index);
	}

	public void loadUpAGV(Part part, AGV agv) {
		String partType = part.getPartType();
		int numParts = 0;
		int capacity = agv.getCapacity();


		// agv가 inventory에 있으면 quota 감소
		if (agv.getCurrentLocation().isSame(inventory)) {
			int remain = remainQuota.get(partType);
			if (capacity <= remain) {
				numParts = capacity;
			}
			else {
				numParts = remain;
			}
			agv.loadUpPart(part, numParts);
			remainQuota.put(partType, remainQuota.get(partType) - numParts);
		}

		// agv가 port에 있으면
		else {
			for (Port port : ports) {
				if ( agv.getCurrentLocation().isSame(port.getLocation()) ) {
					Part targetPart = port.pollOutputQueue();
					// 도착했는데 할 일이 없으면
					if (targetPart == null) {
						return;
					}
					numParts = 1;
					// port outputQueue에 해당 부품을 갯수만큼 제거하고
					for (int i = 0; i < capacity - 1; i++)
						if (port.peekOutputQueue() == targetPart) {
							numParts++;
							port.pollOutputQueue();
						}
					// agv에 실음
					agv.loadUpPart(targetPart, numParts);
					break;
				}
			}
		}
	}

	public Part putDownPart(AGV agv) {
		int numParts = agv.getNumHoldingParts();
		Part part = agv.putDownPart();
		String partType = part.getPartType();

		// agv가 storage에 있으면 output 증가
		if (agv.getCurrentLocation().isSame(storage)) {
			output.put(partType, output.get(partType) + numParts);
		}

		// agv가 port에 있으면
		else {
			for (Port port : ports) {
				if ( agv.getCurrentLocation().isSame(port.getLocation()) ) {
					// port inputQueue에 해당 부품을 갯수만큼 추가
					// TODO: Queue가 꽉차는 경우 고려 안함 수정 필요
					for (int i = 0; i < numParts; i++)
						port.addToInputQueue(part);
					break;
				}
			}
		}
		return part;
	}

	// agv가 가지고 있는 부품의 path에서 현재위치의 다음 목적지를 구한다.
	public void rerouteAGV(AGV agv) {
		Part holdingPart = agv.getHoldingPart();
		// 가지고 있는 부품이 없으면 새로운 일을 찾음
		if (holdingPart == null) {
			agv.setAvailable(true);
			return;
		}
		Location currentLocation = agv.getCurrentLocation();
		List<Location> path = holdingPart.getPath();
		for (Location loc : path) {
			if (loc.isSame(currentLocation)) {
				Location newDest = holdingPart.getNextLocation(loc);
				agv.setDest(newDest);
				break;
			}
		}

	}

	public void operatePorts(int time) {
		for (Port port : ports) {
			port.doNextProcess();
			port.spendTime(time);
		}
	}

	public void printLog() {
		System.out.print("Inventory:");
		remainQuota.forEach((key, value) -> System.out.print("\t(" + key + ", " + value + ")"));
		System.out.println();
		System.out.print("Storage:");
		output.forEach((key, value) -> System.out.print("\t(" + key + ", " + value + ")"));
		System.out.println();
		System.out.print("AGVs:");
		for (AGV agv : AGVs) {
			agv.print();
		}
		System.out.println();
		System.out.print("Ports:");
		for (Port port : ports) {
			port.print();
		}
		System.out.println();
		System.out.println();
	}

}
