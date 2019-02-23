import java.util.List;

public class RidesProblemInput {
	public int rows;
	public int cols;
	public int vehicleCount;
	public int ridesCount;
	public int bonus;
	public int steps;
	public List<Ride> rides;

	public RidesProblemInput(int rows, int cols, int vehicleCount, int ridesCount, int bonus, int steps, List<Ride> rides) {
		this.rows = rows;
		this.cols = cols;
		this.vehicleCount = vehicleCount;
		this.ridesCount = ridesCount;
		this.bonus = bonus;
		this.steps = steps;
		this.rides = rides;
	}

	@Override
	public String toString() {
		return "rows=" + rows +
				", cols=" + cols +
				", vehicleCount=" + vehicleCount +
				", ridesCount=" + ridesCount +
				", bonus=" + bonus +
				", steps=" + steps +
				", rides=\n" + rides;
	}
}
