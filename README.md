## 개별연구
### Deep Reinforcement Learning for Load-Balancing Aware Network Control in IoT Engine System

### ✍️ 시나리오 구상
- 작업장 구성
    - 작업장(맵)은 (0, 0)부터 (6, 6)까지 좌표로 구성되어 있으며, AGV는 0.5분당 x 또는 y 방향으로 1 또는 -1만큼 움직인다.
    - 5개의 서로 다른 작업 포트 A, B, C, D, E와 서로 다른 유형의 부품 X, Y, Z가 있으며, 각 포트는 다음과 같은 부품 조립 또는 처리 작업을 담당한다.
    
    | A포트 | 부품 X | processing | 5 (min) |
    | --- | --- | --- | --- |
    | B포트 | 부품 Y, Z | processing | 5 (min) |
    | C포트 | 부품 Y, Z | painting | 2 (min) |
    | D포트 | 부품 X, Y | assembly | 3 (min) |
    | E포트 | 부품 Z | assembly | 10 (min) |
    - 각 포트는 Input Queue - Machine - Output Queue와 같은 구조를 가진다.
- 각 부품은 다음과 같은 경로를 거쳐 하나의 제품으로 완성된다.
    - X: Inventory → A → D → Storage
    - Y: Inventory → B → C → D → Storage
    - Z: Inventory → B → C → E → Storage
- 인벤토리에는 X, Y, Z 부품이 각각 50개씩 들어 있다.
- 작업장에는 총 3대의 AGV가 있으며, AGV1은 1개, AGV2는 2개, AGV3은 3개의 부품을 동시에 운반할 수 있다.
- AGV는 어떤 부품이든 적재할 수 있으며, 한 번에 여러 종류의 부품을 적재할 수 없다.

  
### 🧑‍💻 팀원

|       팀장       | 팀원 |       팀원       | 팀원 |
|:--------------:|:--:|:--------------:|:--:|
|    **김준범**     |**손혜인**|    **이지민**     |**허상운**|
|   **수학과**   |**컴퓨터공학과**|   **수학과**   |**컴퓨터공학**|
|   **2019110386**   |**2019112057**|   **2020110408**   |**2018112030**|g

