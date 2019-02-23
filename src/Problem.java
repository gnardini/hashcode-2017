import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Problem {

    static List<String> files = Arrays.asList("a_example", "b_should_be_easy", "c_no_hurry", "d_metropolis", "e_high_bonus");

    public static void main(String[] args) throws IOException {
        RidesProblem problem = new GreedySolution();
        runAll(problem);
//        RidesProblemInput input = read("./inputs/e_high_bonus.in");
//        RidesProblemOutput out = problem.solve(input);
//        write(out);
//        RidesValidator.validateSolution(input, out);
//        System.out.println(RidesValidator.score(input, out));
    }

//    a_example 10
//    b_should_be_easy 176877
//    c_no_hurry 15329730
//    d_metropolis 9917897
//    e_high_bonus 21465945
//    47054084
    public static void runAll(RidesProblem problem) throws IOException {
        int score = 0;
        for (String file : files) {
            RidesProblemInput input = read("./inputs/" + file + ".in");
            RidesProblemOutput out = problem.solve(input);
            RidesValidator.validateSolution(input, out);
            int partialScore = RidesValidator.score(input, out);
            System.out.println("File " + file + " score " + partialScore);
            score += partialScore;
        }
        System.out.println(score);
    }


    public static RidesProblemInput read(String inputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int rows = Integer.parseInt(st.nextToken());
            int cols = Integer.parseInt(st.nextToken());
            int vehicleCount = Integer.parseInt(st.nextToken());
            int ridesCount = Integer.parseInt(st.nextToken());
            int bonus = Integer.parseInt(st.nextToken());
            int steps = Integer.parseInt(st.nextToken());

            List<Ride> rides = new ArrayList<>(ridesCount);
            for (int i = 0; i < ridesCount; i++) {
                st = new StringTokenizer(br.readLine());
                rides.add(new Ride(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
                        Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), i));
            }
            return new RidesProblemInput(rows, cols, vehicleCount, ridesCount, bonus, steps, rides);
        }
    }

    public static void write(RidesProblemOutput out) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (List<Integer> rides : out.rides) {
            builder.append(rides.size());
            for (Integer ride : rides) {
                builder.append(" " + ride);
            }
            builder.append('\n');
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt"));
        writer.write(builder.toString());
        writer.close();
    }
}
