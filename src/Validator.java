import java.util.*;

public class Validator {

    public static void validateSolution(Input input, Output output) {

    }

    public static int score(Input input, Output output) {
        int score = 0;

        Map<Integer, Input.Library> inputLibs = new HashMap<>();
        for (Input.Library library : input.libraries) {
            inputLibs.put(library.id, library);
        }
        Map<Integer, Integer> bookScores = new HashMap<>();
        for (int i = 0; i < input.bookScores.length; i++) {
            bookScores.put(i, input.bookScores[i]);
        }

        Set<Integer> scannedBooks = new HashSet<>();
        int time = 0;
        for (Output.OutputLibrary library : output.libraries) {
            if (time >= input.totalDays) {
                return score;
            }
            Input.Library inputLib = inputLibs.get(library.libraryId);
            time += inputLib.signupTime;
            if (time >= input.totalDays) {
                return score;
            }
            int daysLeft = input.totalDays - time;
            int booksToScan = daysLeft * inputLib.booksPerDay;
            for (Integer bookId: library.booksToScan) {
                if (booksToScan == 0) {
                    break;
                }
                booksToScan--;
                if (!scannedBooks.contains(bookId)) {
                    scannedBooks.add(bookId);
                    score += bookScores.get(bookId);
                }
            }
        }

        return score;
    }
}
