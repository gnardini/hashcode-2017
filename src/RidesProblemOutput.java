import java.util.List;

public class RidesProblemOutput {
	public List<List<Integer>> rides;

	public RidesProblemOutput(List<List<Integer>> rides) {
		this.rides = rides;
	}

	@Override
	public String toString() {
		return "RidesProblemOutput{" +
				"rides=" + rides +
				'}';
	}
}
