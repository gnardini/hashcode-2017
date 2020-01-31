import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    static List<String> files = Arrays.asList("kittens.in", "me_at_the_zoo.in", "trending_today.in", "videos_worth_spreading.in");
    static StringTokenizer st;

    public static void main(String[] args) throws IOException {
//        Problem problem = new Solution2();
//        runAll(new VideoCacheSolution());
        runOne(new VideoCacheSolution(), files.get(3));
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

        int V = readInt();
        int E = readInt();
        int R = readInt();
        int C = readInt();
        int X = readInt();
        int[] videoSizes = new int[V];
        for (int i = 0; i < V; i++) {
            videoSizes[i] = readInt();
        }
        Input.Endpoint[] Es = new Input.Endpoint[E];
        for (int i = 0; i < E; i++) {
            int Ld = readInt();
            int k = readInt();
            Map<Integer, Integer> caches = new HashMap<>();
            for (int j = 0; j < k; j++) {
                caches.put(readInt(), readInt());
            }
            Es[i] = new Input.Endpoint(i, Ld, k, caches);
        }
        Input.Request[] Rs = new Input.Request[R];
        for (int i = 0; i < R; i++) {
            Rs[i] = new Input.Request(readInt(), readInt(), readInt());
        }
        return new Input(V, E, R, C, X, videoSizes, Es, Rs);
    }

    public static void write(Output out, String file) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append(out.caches.size());
        builder.append('\n');

        for (Map.Entry<Integer, Set<Integer>> entry : out.caches.entrySet()) {
            builder.append(entry.getKey());
            for (Integer videoId : entry.getValue()) {
                builder.append(' ');
                builder.append(videoId);
            }
            builder.append('\n');
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + file + ".txt"));
        writer.write(builder.toString());
        writer.close();
    }
}
