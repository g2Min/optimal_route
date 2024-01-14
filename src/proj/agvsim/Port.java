package proj.agvsim;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

public class Port {
	private String portID;
	private String portType;
	private int processingTime;	// 단위: 초
	private Location location;
	private int remainTime = 0;
	private Part currentPart;
	private boolean available = true;
	private int maxQueueSize = 5;
	private Queue<Part> inputQueue;
	private Queue<Part> outputQueue;
	private List<Part> parts;

	public Port(String portID, String portType, int processingTime, Location location) {
		this.portID = portID;
		this.portType = portType;
		this.processingTime = processingTime;
		this.location = location;
		this.maxQueueSize = 5;  // 추가된 부분
		this.parts = new ArrayList<>();
		inputQueue = new ArrayDeque<>(this.maxQueueSize);
		outputQueue = new ArrayDeque<>(this.maxQueueSize);
	}

	public Port(String portID, String portType, int processingTime, int queueSize, Location location) {
		this.portID = portID;
		this.portType = portType;
		this.processingTime = processingTime;
		this.location = location;
		this.maxQueueSize = queueSize;
		inputQueue = new ArrayDeque<>(this.maxQueueSize);
		outputQueue = new ArrayDeque<>(this.maxQueueSize);
	}

	public Port() {
		this.parts = new ArrayList<>();
	}
	public String getPortID() {
		return portID;
	}

	public void addPart(Part part) {
		parts.add(part);
	}
	public void setPortID(String portID) {
		this.portID = portID;
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public int getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	public Location getLocation() {
		return location;
	}

	public void addToInputQueue(Part part) {
		inputQueue.add(part);
	}

	public int getInputQueueSize() {
		return inputQueue.size();
	}

	public int getOutputQueueSize() {
		return outputQueue.size();
	}

	public Part peekOutputQueue() {
		return outputQueue.peek();
	}

	public Part pollOutputQueue() {
		return outputQueue.poll();
	}

	public int getMaxQueueSize() {
		return maxQueueSize;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void doNextProcess() {
		if (available && !inputQueue.isEmpty() && outputQueue.size() < maxQueueSize) {
			Part nextPart = inputQueue.poll();
			available = false;
			remainTime = processingTime;
			currentPart = nextPart;
		}
	}

	public void completeProcess() {
		if (currentPart != null && remainTime == 0) {
			outputQueue.add(currentPart);
			available = true;
			currentPart = null;
		}
	}

	// 입력받은 시간만큼 작업하고 남은 시간 반환
	public int spendTime(int time) {
		if (remainTime > 0) {
			remainTime -= time;
			if (remainTime <= 0) {
				completeProcess();
				return 0;
			}
			return time;
		}
		return 0;
	}

	public void print() {
		System.out.print("\t" + portID);
		System.out.print("(" + remainTime + ")");
		System.out.print("(");
		System.out.print(inputQueue.size());
		System.out.print(", ");
		System.out.print(outputQueue.size());
		System.out.print(")");
	}
}
