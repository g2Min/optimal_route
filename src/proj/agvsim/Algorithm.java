package proj.agvsim;

abstract class AlgorithmBase {
	abstract void calEventPriority(Event event);

}

class QueueRemainPartAlgorithm extends AlgorithmBase {

	@Override
	void calEventPriority(Event event) {
		Factory factory = new Factory("MyFactory");  // 적절한 Factory 인스턴스를 생성해야 함
		String partType = event.getPartType();
		int remainQuota = factory.getRemainQuota(partType);

		// 남은 부품 수량을 기반으로 우선순위를 계산하여 Event에 설정
		event.setPriority(remainQuota);
	}
}