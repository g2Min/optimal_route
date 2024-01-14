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

		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("This will be written to the output.txt file");
	}
}