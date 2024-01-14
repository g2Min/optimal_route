package proj.agvsim;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class SimEngine {
	private static SimEngine instance;
	private Queue<Event> jobQueue;
	private Factory factory;
	private int currentTime = 0;
	private int endTime = Integer.MAX_VALUE;

	// private 생성자
	private SimEngine() {
		jobQueue = new PriorityQueue<>((e1, e2) -> Double.compare(e1.getPriority(), e2.getPriority()));
		factory = new Factory("MyFactory");
	}

	// 싱글톤 인스턴스 반환 메서드
	public static SimEngine getInstance() {
		if (instance == null) {
			instance = new SimEngine();
		}
		return instance;
	}


	public void run() {
		AlgorithmBase algo = new QueueRemainPartAlgorithm(); // 사용할 우선순위 알고리즘
		AGV[] AGVs = factory.getAGVs();
		currentTime = 0;
		int unitTime = 30;

		for (int i = 0; i < factory.getNumParts(); i++) {
			Part targetPart = factory.getPart(i);

			// 생산할 부품이 남아있다면 새로 만듦
			if (factory.getRemainQuota(targetPart.getPartType()) > 0) {
				Event newEvent = new Event(factory.getInvLoc(), targetPart.getPath().get(1), targetPart.getPartType());
				algo.calEventPriority(newEvent);
				jobQueue.add(newEvent);
			}
		}

		while (currentTime <= endTime) {
			// port마다 단위 시간 만큼 작동함
			factory.operatePorts(unitTime);

			// 작업을 마친 port에서 output큐를 비우는 job을 생성
			for (int i = 0; i < factory.getNumPorts(); i++) {
				boolean isThereEvent = false;
				Port targetPort = factory.getPort(i);
				for (Event e : jobQueue) {
					// port마다 output큐를 비우는 job이 있다면 break
					if (e.getSource().getName() == targetPort.getPortID()) {
						isThereEvent = true;
						break;
					}
				}
				// 해당하는 event가 없고, port의 outputQueue가 존재하면 새로 만듦
				if (!isThereEvent && targetPort.getOutputQueueSize() > 0) {
					Part targetPart = targetPort.peekOutputQueue();
					Location portLocation = targetPort.getLocation();
					Event newEvent = new Event(portLocation, targetPart.getNextLocation(portLocation), targetPart.getPartType());
					jobQueue.add(newEvent);
				}
			}

			// 모든 부품에 대하여 inventory가 출발지인 job 생성
			for (int i = 0; i < factory.getNumParts(); i++) {
				boolean isThereEvent = false;
				Part targetPart = factory.getPart(i);
				for (Event e : jobQueue) {
					// inventory가 출발지인 event가 있다면 break
					if (e.getPartType() == targetPart.getPartType() && e.getSource().getName() == "I") {
						isThereEvent = true;
						break;
					}
				}
				// 해당하는 event가 없고, 생산할 부품이 남아있다면 새로 만듦
				if (!isThereEvent && factory.getRemainQuota(targetPart.getPartType()) > 0) {
					Event newEvent = new Event(factory.getInvLoc(), targetPart.getPath().get(1), targetPart.getPartType());
					algo.calEventPriority(newEvent);
					jobQueue.add(newEvent);
				}
			}

			// TODO: job을 생성한 후 priority를 다시 계산하는 코드가 필요함.

			// AGV마다 할 일을 지정
			for (AGV agv : AGVs) {
				// 이용가능하면 새로운 일을 배정
				if (agv.isAvailable()) {
					Event nextJob = jobQueue.poll();
					// NOTE: 모든 일이 다른 agv에게 배정됐을 경우 고려해야
					if (nextJob != null) {
						agv.setAvailable(false);
						agv.setCurrentJob(nextJob);
						if (agv.getCurrentLocation().isSame(nextJob.getSource())) {
							agv.setDest(nextJob.getDestination());
						}
						else {
							agv.setDest(nextJob.getSource());
						}
						if (agv.isDest()) {
							Part targetPart = factory.getPart(nextJob.getPartType());
							factory.loadUpAGV(targetPart, agv);
							agv.setDest(nextJob.getDestination());
						}
						agv.toDest();
					}
				}
				// 하던 일이 있다면 그 일을 계속 함
				else {
					agv.toDest();
					if (agv.isDest()) {
						// 특정 포트에 부품을 가지고 도착하면 부품을 포트에 내린 후 새로운 일을 찾는다.
						if (agv.getHoldingPart() != null){
							factory.putDownPart(agv);
							agv.setAvailable(true);
							agv.setCurrentJob(null);
						}
						// 특정 포트에 물건 없이 도착하면 부품을 싣고 다음 경로 설정
						else {
							Part targetPart = factory.getPart(agv.getCurrentJob().getPartType());
							String targetPartType = targetPart.getPartType();
							factory.loadUpAGV(targetPart, agv);
							// 다음 경로를 정해준다.
							factory.rerouteAGV(agv);
						}
					}
				}
			}
			// end AGV
			currentTime += unitTime;
			System.out.println("Current Time: " + currentTime);
			factory.printLog();
		}
		// end while
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

}
