import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Problem {

    public static void main(String[] args) throws IOException {
        System.out.println(read());
    }


    private static RidesProblemInput read() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/a_example.in"))) {
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
                        Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
            }
            return new RidesProblemInput(rows, cols, vehicleCount, ridesCount, bonus, steps, rides);
        }
    }
}
