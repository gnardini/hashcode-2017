import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static List<String> files = Arrays.asList();

    public static void main(String[] args) throws IOException {
        Problem problem = new Solution1();
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

        return new Input();
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

        }
    }
}
