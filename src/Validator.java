public class Validator {

    public static void validateSolution(Input input, Output output) {

    }

    private static int turnsDistance(Drone drone, int x, int y) {
        return (int) Math.ceil(Math.sqrt(Math.pow(drone.x - x, 2) + Math.pow(drone.y - y, 2)));
    }

    public static int score(Input input, Output output) {
        int score = 0;

        Drone[] drones = new Drone[input.droneCount];
        for (int i = 0; i < input.droneCount; i++) {
            drones[i] = new Drone(input.warehouses[0].row, input.warehouses[0].column, 0);
        }
        for (Output.Command command : output.commands) {
            if (command instanceof Output.Load) {
                Output.Load load = ((Output.Load) command);
                Drone drone = drones[load.drone];
                Input.Warehouse warehouse = input.warehouses[load.warehouse];
                drone.turn += turnsDistance(drone, warehouse.row, warehouse.column) + 1;
                drone.x = warehouse.row;
                drone.y = warehouse.column;
                warehouse.products[load.productType] -= load.productCount;
                if (warehouse.products[load.productType] < 0) {
                    throw new IllegalStateException("No quedan productos en en warehouse");
                }
                if (drone.turn > input.T) {
                    throw new IllegalStateException("Un dron se pasó del límite");
                }
            } else if (command instanceof Output.Deliver) {
                Output.Deliver deliver = ((Output.Deliver) command);
                Drone drone = drones[deliver.drone];
                Input.Order order = input.orders[deliver.order];
                if (order.products.isEmpty()) {
                    continue;
                }
                int productCount = order.products.get(deliver.productType);
                int remainingProductCount = productCount - deliver.productCount;
                if (remainingProductCount <= 0) {
                    order.products.remove(deliver.productType);
                } else {
                    order.products.put(deliver.productType, remainingProductCount);
                }
                drone.turn += turnsDistance(drone, order.row, order.column) + 1;
                drone.x = order.row;
                drone.y = order.column;
                int newScore = (int) Math.ceil(((double) input.T - drone.turn) * 100 / input.T);
                score += newScore;
            } else if (command instanceof Output.Wait) {
                Output.Wait wait = ((Output.Wait) command);
                Drone drone = drones[wait.drone];
                drone.turn += wait.turns;
            }
        }

        return score;
    }

    static class Drone {
        int x;
        int y;
        int turn;

        public Drone(int x, int y, int turn) {
            this.x = x;
            this.y = y;
            this.turn = turn;
        }
    }
}
