import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    static List<String> files = Arrays.asList("a_example", "b_lovely_landscapes", "d_pet_pictures", "d_pet_pictures", "e_shiny_selfies");

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
        try (BufferedReader br = new BufferedReader(new FileReader("./input/" + inputFile + ".txt"))) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int photosInCollection = Integer.parseInt(st.nextToken());
            List<Photo> photos = new ArrayList<>(photosInCollection);
            for (int i = 0; i < photosInCollection; i++) {
                st = new StringTokenizer(br.readLine());
                Photo.Orientation orientation = st.nextToken().equals("V") ? Photo.Orientation.V : Photo.Orientation.H;
                int numberOfTags = Integer.parseInt(st.nextToken());
                HashSet<String> tags = new HashSet<>();
                for (int j = 0; j < numberOfTags; j++) {
                    tags.add(st.nextToken());
                }
                photos.add(new Photo(i, orientation, tags));
            }
            return new Input(photosInCollection, photos);
        }
    }

    public static void write(Output out, String file) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append(out.slides.size());
        builder.append('\n');

        for (Slide slide : out.slides) {
            if (slide.photos.size() == 1) {
                builder.append(slide.photos.get(0));
                builder.append('\n');
            } else {
                builder.append(slide.photos.get(0));
                builder.append(' ');
                builder.append(slide.photos.get(1));
                builder.append('\n');
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("./output/" + file + ".txt"));
        writer.write(builder.toString());
        writer.close();
    }
}
