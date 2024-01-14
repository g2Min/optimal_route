import proj.agvsim.SimEngine;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class AGVSim {
	public static void main(String[] args) {
		try {
			// 새로운 파일 출력 스트림을 생성합니다.
			PrintStream fileOut = new PrintStream(new FileOutputStream("output.txt"));
			// 표준 출력을 파일 출력 스트림으로 변경합니다.
			System.setOut(fileOut);

			// SimEngine 인스턴스 가져오기
			SimEngine engine = SimEngine.getInstance();
			// 시뮬레이션 실행 (예: 100분)
			engine.run();

			// debugging이 잡히면 inventory에 들어간 부품 개수만큼 엔터 시키는 것
			// 이 부분은 시뮬레이션 로그에 대한 추가적인 설명이 필요합니다.
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("This will be written to the output.txt file");
	}
}