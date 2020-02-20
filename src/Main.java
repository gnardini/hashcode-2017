import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static List<String> files = Arrays.asList("a_example.txt", "b_read_on.txt", "c_incunabula.txt", "d_tough_choices.txt",
            "e_so_many_books.txt", "f_libraries_of_the_world.txt");
    static List<String> files2 = Arrays.asList("a_example.txt", "b_read_on.txt", "c_incunabula.txt",
            "e_so_many_books.txt", "f_libraries_of_the_world.txt");

    public static void main(String[] args) throws IOException {
        Problem problem = new SolutionN();
//        runAll(problem);
        runOne(problem, "c_incunabula.txt");
//        runOne(problem, "d_tough_choices.txt");
        runOne(problem, "e_so_many_books.txt");
        runOne(problem, "f_libraries_of_the_world.txt");
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
        Scanner scanner = new Scanner(contents);

        int books = scanner.nextInt();
        int libraryCount = scanner.nextInt();
        int days = scanner.nextInt();

        int[] bookScores = na(scanner, books);
        Input.Library[] libraries = new Input.Library[libraryCount];
        for (int i = 0; i < libraryCount; i++) {
            int libraryBooks = scanner.nextInt();
            libraries[i] = new Input.Library(i, libraryBooks, scanner.nextInt(), scanner.nextInt(), na(scanner, libraryBooks));
        }

        return new Input(books, libraryCount, days, bookScores, libraries);
    }

    public static int ni(Scanner scanner) {
        return scanner.nextInt();
    }

    public static int[] na(Scanner sc, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        return a;
    }

    public static void write(Output out, String file) throws IOException {
        try(PrintWriter writer = new PrintWriter(new FileOutputStream("./output/" + file))) {
            writer.println(out.libraries.size());
            for (Output.OutputLibrary library : out.libraries) {
                writer.println(library.libraryId + " " + library.booksToScan.size());
                String line = String.join(" ", library.booksToScan.stream().map(x -> x + "").collect(Collectors.toList()));
                writer.println(line);
            }
        }
    }
}
