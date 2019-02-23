import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GreedySolution implements RidesProblem {

    @Override
    public RidesProblemOutput solve(RidesProblemInput input) {
        VehicleState[] vehicles = new VehicleState[input.vehicleCount];
        for (int i = 0; i < vehicles.length; i++) {
            vehicles[i] = new VehicleState();
            vehicles[i].row = 0;
            vehicles[i].col = 0;
            vehicles[i].finalStep = 0;
            vehicles[i].rides = new ArrayList<>();
        }
        PriorityQueue<Integer> stepsToCheck = new PriorityQueue<>();
        stepsToCheck.add(0);
        while (!stepsToCheck.isEmpty()) {
            int currentStep = stepsToCheck.poll();
            for (VehicleState vehicle : vehicles) {
                if (vehicle.finalStep == currentStep) {
                    findBestRide(input.rides, vehicle, currentStep, input.bonus);
                    if (vehicle.finalStep != Integer.MAX_VALUE) {
                        stepsToCheck.add(vehicle.finalStep);
                    }
                }
            }
        }
        List<List<Integer>> ridesOutput = Stream.of(vehicles)
                .map(v -> v.rides)
                .collect(Collectors.toList());
        return new RidesProblemOutput(ridesOutput);
    }

    private void findBestRide(List<Ride> rides, VehicleState vehicle, int step, int bonus) {
        int bestScore = Integer.MIN_VALUE;
        Ride bestRide = null;
        int finalStep = -1;
        int bestTime = Integer.MAX_VALUE;
        Ride bestRideWithBonus = null;
        int finalStepWithBonus = -1;
        for (int i = 0; i < rides.size(); i++) {
            Ride ride = rides.get(i);
            if (ride.used) {
                continue;
            }
            int distanceOfRide = Math.abs(ride.endCol - ride.startCol) + Math.abs(ride.endRow - ride.startRow);
            int distanceToRide = Math.abs(ride.startCol - vehicle.col) + Math.abs(ride.startRow - vehicle.row);
            if (distanceToRide + distanceOfRide + step > ride.finalStep) {
                continue;
            }
            boolean hasBonus = distanceToRide + step <= ride.startStep;

            int timeToStartRide = Math.max(ride.startStep - step, distanceToRide);
            int score = distanceOfRide + (hasBonus ? bonus : 0) - timeToStartRide;
            if (score > bestScore) {
                bestScore = score;
                bestRide = ride;
                finalStep = Math.max(ride.startStep, step + distanceToRide) + distanceOfRide;
            }
            if (bonus > 1000 && hasBonus) {
                int timeOfRide = timeToStartRide + distanceOfRide;
                if (timeOfRide < bestTime) {
                    bestTime = timeOfRide;
                    bestRideWithBonus = ride;
                    finalStepWithBonus = Math.max(ride.startStep, step + distanceToRide) + distanceOfRide;
                }
            }
        }
        if (bestRideWithBonus != null) {
            bestRide = bestRideWithBonus;
            finalStep = finalStepWithBonus;
        }
        if (bestRide == null) {
            vehicle.finalStep = Integer.MAX_VALUE;
            return;
        }
        vehicle.row = bestRide.endRow;
        vehicle.col = bestRide.endCol;
        vehicle.rides.add(bestRide.index);
        vehicle.finalStep = finalStep;
        bestRide.used = true;
    }

    static class VehicleState {
        int row;
        int col;
        List<Integer> rides;
        int finalStep;
    }

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
