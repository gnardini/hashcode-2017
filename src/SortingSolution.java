import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SortingSolution implements RidesProblem {

    @Override
    public RidesProblemOutput solve(RidesProblemInput input) {
        List<List<Integer>> result = new ArrayList<>(input.vehicleCount);
        for (int i = 0; i < input.vehicleCount; i++) {
            result.add(i, new ArrayList<>());
        }

        List<Car> cars = IntStream.range(0, input.vehicleCount)
                .mapToObj(i -> new Car(0, 0, 0, i))
                .collect(Collectors.toList());

        Set<Ride> rides = new HashSet<>(input.rides);
        Car maxCar = null;
        Ride maxRide = null;
        while (!rides.isEmpty()) {
            int maxScore = Integer.MIN_VALUE;
            for (Ride ride : rides) {
                for (Car car : cars) {
                    int score = score(car, ride, input.bonus);
                    if (score > maxScore) {
                        maxRide = ride;
                        maxCar = car;
                        maxScore = score;
                    }
                }
            }

            result.get(maxCar.id).add(maxRide.index);
            maxCar.t += distance(maxCar.i, maxCar.j, maxRide.startRow, maxRide.startCol);
            if (maxCar.t < maxRide.startStep) {
                maxCar.t = maxRide.startStep;
            }
            maxCar.t += distance(maxRide.startRow, maxRide.startCol, maxRide.endRow, maxRide.endCol);
            maxCar.i = maxRide.endRow;
            maxCar.j = maxRide.endCol;

            rides.remove(maxRide);
        }
        return new RidesProblemOutput(result);
    }

    private int timeToRide(Car car, Ride ride) {
        return distance(car.i, car.j, ride.startRow, ride.startCol) + Math.max(0, car.t - ride.startStep);
    }

    private int score(Car car, Ride ride, int bonus) {
        int bonusScore = car.t + distance(car.i, car.j, ride.startRow, ride.startCol) <= ride.startStep ? bonus : 0;
        int distanceScore = distance(ride.startRow, ride.startCol, ride.endRow, ride.endCol);
        int timeToRide = timeToRide(car, ride);
        return bonusScore * bonusScore + distanceScore - timeToRide;
    }

    public static int distance(int i1, int j1, int i2, int j2) {
        return Math.abs(i2 - i1) + Math.abs(j2 - j1);
    }

}
