import java.util.List;

public class RidesValidator {

	public static boolean isValidSolution(RidesProblemInput input, RidesProblemOutput output) {
		return true;
	}

	public static int score(RidesProblemInput input, RidesProblemOutput output) {
		if (!isValidSolution(input, output)) {
			throw new IllegalArgumentException("invalid solution");
		}

		return output.rides.stream().map(rides -> score(input, rides)).reduce(0, (a, b) -> a + b);
	}

	public static int score(RidesProblemInput input, List<Integer> rides) {
		return score(input, rides, 0, 0, 0, 0);
	}

	public static int score(RidesProblemInput input, List<Integer> rides, int k, int t, int i, int j) {
		if (k >= rides.size()) {
			return 0;
		}
		int score = 0;
		int nextT = t;
		int rideNumber = rides.get(k);
		Ride ride = input.rides.get(rideNumber);
		int distanceToStartRide = distance(i, j, ride.startRow, ride.startCol);
		nextT += distanceToStartRide;
		if (nextT < ride.startStep) {
			nextT = ride.startStep;
		}
		boolean hasBonus = false;
		if (nextT == ride.startStep) {
			// consider impossible rides
			hasBonus = true;
		}
		int rideDistance = distance(ride.startRow, ride.startCol, ride.endRow, ride.endCol);
		nextT += rideDistance;
		if (nextT <= ride.finalStep) {
			score += rideDistance;
			if (hasBonus) {
				score += input.bonus;
			}
		}

		if (nextT < input.steps) {
			score += score(input, rides, k + 1, nextT, ride.endRow, ride.endCol);
		}

		return score;
	}

	public static int distance(int i1, int j1, int i2, int j2) {
		return Math.abs(i2 - i1) + Math.abs(j2 - j1);
	}
}
