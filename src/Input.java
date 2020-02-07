import java.util.HashMap;
import java.util.Map;

public class Input {
	final int rows;
	final int columns;
	final int droneCount;
	final int T;
	final int maxLoad;

	final int productTypesCount;
	final int[] productTypeWeights;

	final int warehouseCount;
	final Warehouse[] warehouses;

	final int orderCount;
	final Order[] orders;

    public Input(int rows, int columns, int droneCount, int t, int maxLoad, int productTypesCount,
                 int[] productTypeWeights, int warehouseCount, Warehouse[] warehouses, int orderCount,
                 Order[] orders) {
        this.rows = rows;
        this.columns = columns;
        this.droneCount = droneCount;
        T = t;
        this.maxLoad = maxLoad;
        this.productTypesCount = productTypesCount;
        this.productTypeWeights = productTypeWeights;
        this.warehouseCount = warehouseCount;
        this.warehouses = warehouses;
        this.orderCount = orderCount;
        this.orders = orders;
    }

    static class Warehouse {
    	final int id;
	    final int row;
	    final int column;

	    final int[] products;

        public Warehouse(int id, int row, int column, int[] products) {
        	this.id = id;
            this.row = row;
            this.column = column;
            this.products = products;
        }
    }

    static class Order {
    	final int id;
        final int row;
        final int column;

        final int productsCount;
        final Map<Integer, Integer> products; // product type -> count

        public Order(int id, int row, int column, int productsCount, Map<Integer, Integer> products) {
        	this.id = id;
            this.row = row;
            this.column = column;
            this.productsCount = productsCount;
            this.products = products;
        }
    }

    public static Warehouse clone(Warehouse warehouse) {
    	return new Warehouse(warehouse.id, warehouse.row, warehouse.column, warehouse.products.clone());
	}

	public static Warehouse[] clone(Warehouse[] warehouses) {
    	Warehouse[] clone = new Warehouse[warehouses.length];
		for (int i = 0; i < warehouses.length; i++) {
			clone[i] = clone(warehouses[i]);
		}
		return clone;
	}

	public static Order clone(Order order) {
		return new Order(order.id, order.row, order.column, order.productsCount, new HashMap<>(order.products));
	}

	public static Order[] clone(Order[] orders) {
		Order[] clone = new Order[orders.length];
		for (int i = 0; i < orders.length; i++) {
			clone[i] = clone(orders[i]);
		}
		return clone;
	}

    public static Input clone(Input input) {
    	return new Input(input.rows, input.columns, input.droneCount, input.T, input.maxLoad, input.productTypesCount, input.productTypeWeights, input.warehouseCount,
				clone(input.warehouses), input.orderCount, clone(input.orders));
	}
}