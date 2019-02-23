import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionSorting implements RidesProblem {

    @Override
    public RidesProblemOutput solve(RidesProblemInput input) {
        List<List<Integer>> result = IntStream.range(0, input.vehicleCount)
                .mapToObj(i -> new ArrayList())
                .collect(Collectors.toList());

        List<Car> cars = IntStream.range(0, input.vehicleCount)
                .mapToObj(i -> new Car(0, 0, 0, i))
                .collect(Collectors.toList());

        input.rides.sort(Comparator.comparingInt(o -> o.startStep));

        int time = 0;
        int currentRide = 0;

        while (time < input.steps && currentRide < input.rides.size()) {
            Ride ride = input.rides.get(currentRide);
            Car car = getAvailableCar(time, cars, ride);
            if (car != null) {
                result.get(car.i).add(ride.id);
            }
            time = cars.stream().mapToInt(car1 -> car1.t).min().orElseThrow(RuntimeException::new);
        }
        return new RidesProblemOutput(result);
    }

    private Car getAvailableCar(int time, List<Car> cars, Ride ride) {
        return cars.stream()
                .filter(car -> car.t <= time)
                .min(Comparator.comparingInt(car2 -> distance(car2.i, car2.j, ride.startRow, ride.startCol)))
                .orElse(null);
    }

    public static int distance(int i1, int j1, int i2, int j2) {
        return Math.abs(i2 - i1) + Math.abs(j2 - j1);
    }

}
