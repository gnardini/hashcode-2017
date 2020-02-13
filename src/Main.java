import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static List<String> files = Arrays.asList("busy_day.in", "mother_of_all_warehouses.in", "redundancy.in");

    public static void main(String[] args) throws IOException {
        Problem problem = new Solution2();
//        runAll(problem);
        runOne(problem, "redundancy.in");
    }

    private static void runOne(Problem problem, String file) throws IOException {
        Input input = read(file);
        Output out = problem.solve(input);
        Validator.validateSolution(input, out);
        write(out, file);
        System.out.println(Validator.score(input, out));
    }

    public static void runAll(Problem problem) throws IOException {
        int score = 0;
        for (String file : files) {
            Input input = read(file);
            Output out = problem.solve(input);
            Validator.validateSolution(input, out);
            int partialScore = Validator.score(input, out);
            System.out.println("File " + file + " score " + partialScore);
            write(out, file);
            score += partialScore;
        }
        System.out.println(score);
    }


    public static Input read(String inputFile) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("./input/" + inputFile)));
        Scanner sc = new Scanner(contents);
        int rows = sc.nextInt();
        int columns = sc.nextInt();
        int droneCount = sc.nextInt();
        int T = sc.nextInt();
        int maxLoad = sc.nextInt();
        int productTypesCount = sc.nextInt();
        int[] productTypeWeights = na(sc, productTypesCount);
        int warehouseCount = sc.nextInt();
        Input.Warehouse[] warehouses = new Input.Warehouse[warehouseCount];
        for (int i = 0; i < warehouseCount; i++) {
            int row = sc.nextInt();
            int column = sc.nextInt();
            int[] products = na(sc, productTypesCount);
            warehouses[i] = new Input.Warehouse(i, row, column, products);
        }
        int orderCount = sc.nextInt();
        Input.Order[] orders = new Input.Order[orderCount];
        for (int i = 0; i < orderCount; i++) {
            int row = sc.nextInt();
            int column = sc.nextInt();
            int productsCount = sc.nextInt();
            Map<Integer, Integer> products = new HashMap<>();
            for (int j = 0; j < productsCount; j++) {
                int type = sc.nextInt();
                if (!products.containsKey(type)) {
                    products.put(type, 0);
                }
                products.put(type, products.get(type) + 1);
            }
            orders[i] = new Input.Order(i, row, column, productsCount, products);
        }

        return new Input(rows, columns, droneCount, T, maxLoad, productTypesCount, productTypeWeights, warehouseCount, warehouses, orderCount, orders);
    }

    public static int[] na(Scanner sc, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        return a;
    }

    public static void write(Output out, String file) throws IOException {
        try(PrintWriter writer = new PrintWriter(new FileOutputStream("./output/" + file + ".txt"))) {
            writer.println(out.commands.size());
            for (int i = 0; i < out.commands.size(); i++) {
                Output.Command command = out.commands.get(i);
                StringBuilder sb = new StringBuilder();
                command.print(sb);
                writer.println(sb.toString());
            }
        }
    }
}
