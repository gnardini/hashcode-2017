import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static List<String> files = Arrays.asList("busy_day.in", "mother_of_all_warehouses.in", "redundancy.in");
    static StringTokenizer st;

    public static void main(String[] args) throws IOException {
//        Problem problem = new Solution2();
//        runAll(new VideoCacheSolution());
//        runOne(new VideoCacheSolution(), files.get(3));
    }

    private static void runOne(Problem problem, String file) throws IOException {
        Input input = read(file);
        Output out = problem.solve(input);
        Validator.validateSolution(input, out);
        write(out, file);
        Validator.validateSolution(input, out);

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

    private static int readInt() {
        return Integer.parseInt(st.nextToken());
    }


    public static Input read(String inputFile) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("./input/" + inputFile)));
        st = new StringTokenizer(contents);

        return new Input();
    }

    public static void write(Output out, String file) throws IOException {
        StringBuilder builder = new StringBuilder();


        BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + file + ".txt"));
        writer.write(builder.toString());
        writer.close();
    }
}
