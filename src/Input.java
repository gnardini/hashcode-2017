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
	    final int row;
	    final int column;

	    final int[] products;

        public Warehouse(int row, int column, int[] products) {
            this.row = row;
            this.column = column;
            this.products = products;
        }
    }

    static class Order {
        final int row;
        final int column;

        final int productsCount;
        final Map<Integer, Integer> products; // product type -> count

        public Order(int row, int column, int productsCount, Map<Integer, Integer> products) {
            this.row = row;
            this.column = column;
            this.productsCount = productsCount;
            this.products = products;
        }
    }
}