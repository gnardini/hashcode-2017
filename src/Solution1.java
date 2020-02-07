import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Solution1 implements Problem {
	@Override
	public Output solve(Input input2) {
		final Input input = Input.clone(input2);
//		sort orders. O(C*log(C))
		Arrays.sort(input.orders, Comparator.comparingInt(c -> c.productsCount));

//		map to tasks
		List<Task> tasks = Arrays.stream(input.orders).flatMap(order -> toTasks(input, order).stream()).collect(Collectors.toList());

		Input.Warehouse firstWarehouse = input.warehouses[0];
		PriorityQueue<Drone> drones = new PriorityQueue<>(Comparator.comparingInt(c -> c.availableAt));
		for (int i = 0; i < input.droneCount; i++) {
			drones.offer(new Drone(i, firstWarehouse.row, firstWarehouse.column));
		}

		List<Output.Command> commands = new ArrayList<>();

		int t = 0;
		int nextTask = 0;
		while(t < input.T && nextTask < tasks.size()) {
			Drone drone = drones.poll();
			t = drone.availableAt;
			Task task = tasks.get(nextTask++);
			Arrays.sort(input.warehouses, Comparator.comparingInt(w -> distance(task.row, task.column, w.row, w.column)));

			int left = task.count;

			List<PathItem> path = new ArrayList<>();
			int timeForTask = 0;
			int droneRow = drone.row;
			int droneColumn = drone.column;
			for (Input.Warehouse warehouse: input.warehouses) {
//				TODO: prioritize warehouses with more availability
				if (warehouse.products[task.productType] > 0) {
					int take = Math.min(left, warehouse.products[task.productType]);
					left -= take;
					path.add(new PathItem(warehouse, take));
					timeForTask += distance(droneRow, droneColumn, warehouse.row, warehouse.column);
					droneRow = warehouse.row;
					droneColumn = warehouse.column;
					if (t + timeForTask > input.T) {
						break;
					}
				}

				if (left == 0) {
					break;
				}
			}

			timeForTask += distance(droneRow, droneColumn, task.row, task.column);

			if (t + timeForTask > input.T) {
				drones.offer(drone);
			} else {
				for (PathItem pathItem : path) {
					pathItem.warehouse.products[task.productType] -= pathItem.count;
					commands.add(new Output.Load(drone.id, pathItem.warehouse.id, task.productType, pathItem.count, true));
				}
				commands.add(new Output.Deliver(drone.id, task.orderId, task.productType, task.count));
				drone.availableAt += timeForTask;
				drone.row = task.row;
				drone.column = task.column;
				drones.offer(drone);
			}
		}

		return new Output(commands);
	}


	public static int distance(int r1, int c1, int r2, int c2) {
		return (int) Math.ceil(Math.sqrt((r1 - r2) * (r1 - r2) + (c1 - c2) * (c1 - c2))) + 1;
	}

	public static List<Task> toTasks(Input input, Input.Order order) {
		List<Task> tasks = new ArrayList<>();

		for (int type: order.products.keySet()) {
			int count = order.products.get(type);
			int w = input.productTypeWeights[type];
			int capacity = input.maxLoad;
			int maxPerDrone = (int) Math.floor(capacity/((double) w));
			int requiredTasks = (int) Math.ceil((count) / ((double) maxPerDrone));
			for (int i = 0; i < requiredTasks; i++) {
				int c = Math.min(maxPerDrone, count - i*maxPerDrone);
				tasks.add(new Task(order.id, type, c, order.row, order.column));
			}
		}

		return tasks;
	}

	public static List<Output.Command> toCommands(Input input, Task task) {
		return null;
	}


	public static class PathItem {
		final Input.Warehouse warehouse;
		final int count;

		public PathItem(Input.Warehouse warehouse, int count) {
			this.warehouse = warehouse;
			this.count = count;
		}

		@Override
		public String toString() {
			return "PathItem{" +
					"warehouse=" + warehouse +
					", count=" + count +
					'}';
		}
	}

	public static class Drone {
		public final int id;
		public int row;
		public int column;
		public int availableAt;

		public Drone(int id, int row, int column) {
			this.id = id;
			this.row = row;
			this.column = column;
			this.availableAt = 0;
		}

		@Override
		public String toString() {
			return "Drone{" +
					"id=" + id +
					", row=" + row +
					", column=" + column +
					", availableAt=" + availableAt +
					'}';
		}
	}

	public static class Task {
		public final int orderId;
		public final int productType;
		public final int count;
		public final int row;
		public final int column;

		public Task(int orderId, int productType, int count, int row, int column) {
			this.orderId = orderId;
			this.productType = productType;
			this.count = count;
			this.row = row;
			this.column = column;
		}

		@Override
		public String toString() {
			return "Task{" +
					"orderId=" + orderId +
					", productType=" + productType +
					", count=" + count +
					", row=" + row +
					", column=" + column +
					'}';
		}
	}
}
