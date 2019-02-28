import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    static List<String> files = Arrays.asList("a_example", "b_should_be_easy", "c_no_hurry", "d_metropolis", "e_high_bonus");

    public static void main(String[] args) throws IOException {
        Problem problem = new Solution();
//        runAll(problem);
        runOne(problem, files.get(0));
    }

    private static void runOne(Problem problem, String file) throws IOException {
        Input input = read(file);
        Output out = problem.solve(input);
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


    public static Input read(String inputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/" + inputFile + ".in"))) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            // TODO

            return new Input();
        }
    }

    public static void write(Output out, String file) throws IOException {
        StringBuilder builder = new StringBuilder();

        // TODO: Write to builder.

        BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + file + ".txt"));
        writer.write(builder.toString());
        writer.close();
    }
}
