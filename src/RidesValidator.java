public class RidesValidator {

	public static boolean isValidSolution(RidesProblemInput input, RidesProblemOutput output) {
		return false;
	}

	public static int score(RidesProblemInput input, RidesProblemOutput output) {
		if (!isValidSolution(input, output)) {
			throw new IllegalArgumentException("invalid solution");
		}
		
		return 0;
	}
}
