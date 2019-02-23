import java.util.*;
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
                    boolean foundRide = false;
                    Set<Ride> markedRides = new HashSet<>();
                    while (!foundRide) {
                        BestRide bestRide = findBestRide(input.rides, vehicle, currentStep, input.bonus);
                        if (bestRide.ride == null) {
                            vehicle.finalStep = Integer.MAX_VALUE;
                            foundRide = true;
                        } else {
                            int score = Integer.MIN_VALUE; // bestScoreForRide(bestRide.ride, vehicles, input.rides, currentStep, input.bonus);
                            if (score > bestRide.score) {
                                markedRides.add(bestRide.ride);
                                bestRide.ride.used = true;
                            } else {
                                vehicle.row = bestRide.ride.endRow;
                                vehicle.col = bestRide.ride.endCol;
                                vehicle.rides.add(bestRide.ride.index);
                                vehicle.finalStep = bestRide.finalStep;
                                bestRide.ride.used = true;
                                foundRide = true;
                            }
                        }
                    }
                    markedRides.forEach(ride -> ride.used = false);

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

    private int bestScoreForRide(Ride ride, VehicleState[] vehicles, List<Ride> allRides, int step, int bonus) {
        int bestScore = Integer.MIN_VALUE;
        for (VehicleState vehicle : vehicles) {
            int score = scoreRide(vehicle, ride, allRides, step, bonus);
            if (score > bestScore) {
                bestScore = score;
            }
        }
        return bestScore;
    }

    private BestRide findBestRide(List<Ride> rides, VehicleState vehicle, int step, int bonus) {
        int bestScore = Integer.MIN_VALUE;
        Ride bestRide = null;
        int finalStep = -1;
//        int bestTime = Integer.MAX_VALUE;
//        Ride bestRideWithBonus = null;
//        int finalStepWithBonus = -1;
        for (int i = 0; i < rides.size(); i++) {
            Ride ride = rides.get(i);
            int score = scoreRide(vehicle, ride, rides, step, bonus);
            if (score > bestScore) {
                bestScore = score;
                bestRide = ride;

                int distanceOfRide = Math.abs(ride.endCol - ride.startCol) + Math.abs(ride.endRow - ride.startRow);
                int distanceToRide = Math.abs(ride.startCol - vehicle.col) + Math.abs(ride.startRow - vehicle.row);
                finalStep = Math.max(ride.startStep, step + distanceToRide) + distanceOfRide;
            }
//            if (bonus > 999 && hasBonus) {
//                int timeOfRide = timeToStartRide + distanceOfRide;
//                if (timeOfRide < bestTime) {
//                    bestTime = timeOfRide;
//                    bestRideWithBonus = ride;
//                    finalStepWithBonus = Math.max(ride.startStep, step + distanceToRide) + distanceOfRide;
//                }
//            }
        }
//        if (bestRideWithBonus != null && bestScore < bonus * 2) {
//            bestRide = bestRideWithBonus;
//            finalStep = finalStepWithBonus;
//        }
        return BestRide.of(bestScore, bestRide, finalStep);
    }

    int scoreRide(VehicleState vehicle, Ride ride, List<Ride> allRides, int step, int bonus) {
        return scoreRide(vehicle, ride, allRides, step, bonus, false);
    }

    int scoreRide(VehicleState vehicle, Ride ride, List<Ride> allRides, int step, int bonus, boolean recursive) {
        if (ride.used) {
            return Integer.MIN_VALUE;
        }
        int distanceOfRide = Math.abs(ride.endCol - ride.startCol) + Math.abs(ride.endRow - ride.startRow);
        int distanceToRide = Math.abs(ride.startCol - vehicle.col) + Math.abs(ride.startRow - vehicle.row);
        if (distanceToRide + distanceOfRide + vehicle.finalStep > ride.finalStep) {
            return Integer.MIN_VALUE;
        }
        boolean hasBonus = distanceToRide + vehicle.finalStep <= ride.startStep;

        int timeToStartRide = vehicle.finalStep - step + Math.max(ride.startStep - vehicle.finalStep, distanceToRide);
        int score = distanceOfRide + (hasBonus ? bonus : 0) - timeToStartRide;

        if (recursive) {
            int row = vehicle.row;
            int col = vehicle.col;
            int finalStep = vehicle.finalStep;
            ride.used = true;

            int bestOtherScore = Integer.MIN_VALUE;

            for (Ride otherRide : allRides) {
                vehicle.row = otherRide.endRow;
                vehicle.col = otherRide.endCol;
                vehicle.finalStep = otherRide.finalStep;
                int otherScore = scoreRide(vehicle, otherRide, allRides, finalStep, bonus, false);
                if (otherScore > bestOtherScore) {
                    bestOtherScore = otherScore;
                }
            }
            score += bestOtherScore;

            vehicle.row = row;
            vehicle.col = col;
            vehicle.finalStep = finalStep;
            ride.used = false;
        }

        return score;
    }

    static class VehicleState {
        int row;
        int col;
        List<Integer> rides;
        int finalStep;
    }

    static class BestRide {
        int score;
        int finalStep;
        Ride ride;

        static BestRide of(int score, Ride ride, int finalStep) {
            BestRide bestRide = new BestRide();
            bestRide.score = score;
            bestRide.finalStep = finalStep;
            bestRide.ride = ride;
            return bestRide;
        }
    }

    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
