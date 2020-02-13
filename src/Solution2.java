import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Solution2 implements Problem {

	public static double score(Input input, Input.Order order) {
//		return order.products.keySet().stream().map(type -> input.productTypeWeights[type] * order.products.get(type)).mapToDouble(x -> x).sum();
		return order.productsCount;
//		return order.products.size();
	}

//	public static double score(Input input, Drone drone, Task task) {
//
//
//	}

	@Override
	public Output solve(Input input2) {
		final Input input = Input.clone(input2);

		List<List<Double>> ordersData = Arrays.stream(input.orders).map(w -> Arrays.asList((double)w.column, (double)w.row)).collect(Collectors.toList());
		List<Clustering.Centroid> centroids = Clustering.kmeans(6, ordersData);

		char[] colors = new char[]{ 'b', 'g', 'r', 'c', 'm', 'y', 'k' };
		List<FileUtils.Datapoint> clusterData = new ArrayList<>();
		for (Clustering.Centroid centroid: centroids) {
			for (List<Double> point: centroid.points) {
				clusterData.add(new FileUtils.Datapoint(point, colors[centroid.id % colors.length] + "o"));
			}
		}

		FileUtils.dumpDatapoints("cluster.csv", clusterData);
		FileUtils.dumpCsv("warehouses.csv", Arrays.stream(input.warehouses).map(w -> Arrays.asList((double)w.column, (double)w.row)).collect(Collectors.toList()));
		FileUtils.dumpCsv("orders.csv", ordersData);

		Map<Integer, Integer> allOrderProducts = new HashMap<>();
		List<Double> orderSizes = new ArrayList<>();
		for (Input.Order order : input.orders) {
			orderSizes.add((double) order.products.size());
			for (Integer type : order.products.keySet()) {
				if (!allOrderProducts.containsKey(type)) {
					allOrderProducts.put(type, 0);
				}
				allOrderProducts.put(type, allOrderProducts.get(type) + order.products.get(type));
			}
		}

		Map<Integer, Integer> allWarehouseProducts = new HashMap<>();
		for (Input.Warehouse warehouse: input.warehouses) {
			for (int i = 0; i < warehouse.products.length; i++) {
				if (!allWarehouseProducts.containsKey(i)) {
					allWarehouseProducts.put(i, 0);
				}
				allWarehouseProducts.put(i, allWarehouseProducts.get(i) + warehouse.products[i]);
			}
		}

//		sort orders. O(C*log(C))
		Arrays.sort(input.orders, Comparator.comparingDouble(c -> score(input, c)));

//		map to tasks
		List<Task> tasks = Arrays.stream(input.orders).flatMap(order -> toTasks(input, order).stream()).collect(Collectors.toList());

		Input.Warehouse firstWarehouse = input.warehouses[0];
		PriorityQueue<Drone> drones = new PriorityQueue<>(Comparator.comparingInt(c -> c.availableAt));
		for (int i = 0; i < input.droneCount; i++) {
			drones.offer(new Drone(i, firstWarehouse.row, firstWarehouse.column));
		}

		List<Output.Command> commands = new ArrayList<>();

		double totalLoad = 0;
		double numTasks = 0;
		List<Double> pathSizes = new ArrayList<>();

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
				}

				if (left == 0) {
					break;
				}
			}

			Collections.reverse(path);
			for (PathItem item: path) {
				Input.Warehouse warehouse = item.warehouse;
				timeForTask += distance(droneRow, droneColumn, warehouse.row, warehouse.column);
				droneRow = warehouse.row;
				droneColumn = warehouse.column;
			}

			timeForTask += distance(droneRow, droneColumn, task.row, task.column);

			if (t + timeForTask > input.T) {
				drones.offer(drone);
			} else {
				for (PathItem pathItem : path) {
					pathItem.warehouse.products[task.productType] -= pathItem.count;
					commands.add(new Output.Load(drone.id, pathItem.warehouse.id, task.productType, pathItem.count, true));
				}
				totalLoad += task.count * input.productTypeWeights[task.productType];
				pathSizes.add((double) path.size());
				numTasks++;
				commands.add(new Output.Deliver(drone.id, task.orderId, task.productType, task.count));
				drone.availableAt += timeForTask;
				drone.row = task.row;
				drone.column = task.column;
				drones.offer(drone);
			}
		}


		double averageLoadPerTask = totalLoad/numTasks;
		double efficiency = (averageLoadPerTask/input.maxLoad) * 100;

		List<Integer> keys = new ArrayList<>(allOrderProducts.keySet());
		Collections.sort(keys, (k1, k2) -> allOrderProducts.get(k2) - allOrderProducts.get(k1));

		List<Integer> warehouseKeys = new ArrayList<>(allWarehouseProducts.keySet());
		Collections.sort(warehouseKeys, (k1, k2) -> allWarehouseProducts.get(k2) - allWarehouseProducts.get(k1));
		System.out.println();
		System.out.println(keys.stream().map(k -> allOrderProducts.get(k)).collect(Collectors.toList()));
		System.out.println(warehouseKeys.stream().map(k -> allWarehouseProducts.get(k)).collect(Collectors.toList()));
		System.out.println(String.format("tasks %d/%d", nextTask, tasks.size()));
		System.out.println(String.format("simulation time: %d/%d", t, input.T));
		System.out.println(String.format("efficiency: %.2f%%", efficiency));
		System.out.println("path stats:");
		stats(pathSizes);
		System.out.println("order sizes stats");
		stats(orderSizes);
		return new Output(commands);
	}

	public static void stats(List<Double> data) {
		double min = Collections.min(data);
		double max = Collections.max(data);
		double mean = data.stream().reduce(0.0, (a, b) -> a + b)/data.size();
		System.out.println(String.format("min: %.2f, max: %.2f, mean: %.2f", min, max, mean));
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
