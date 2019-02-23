import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Tests {

	public static void main(String[] args) throws IOException {
		test1();
		test2();
	}

	public static void test1() throws IOException {
		RidesProblemInput input = Problem.read("./inputs/a_example.in");

		List<List<Integer>> outputList = Arrays.asList(
				Arrays.asList(0),
				Arrays.asList(2, 1)
		);

		RidesProblemOutput output = new RidesProblemOutput(outputList);
		assertEquals(10, RidesValidator.score(input, output));
	}

	public static void test2() throws IOException {
		RidesProblemInput input = Problem.read("./inputs/a_example.in");

		List<List<Integer>> outputList = Arrays.asList(
				Arrays.asList(0),
				Arrays.asList(1, 2)
		);

		RidesProblemOutput output = new RidesProblemOutput(outputList);
		assertEquals(10, RidesValidator.score(input, output));
	}

	public static void assertEquals(int expected, int actual) {
		if (expected != actual) {
			throw new IllegalStateException("expected " + expected + " but got " + actual);
		}
	}
}
