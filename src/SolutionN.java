import java.util.*;
import java.util.stream.Collectors;

public class SolutionN implements Problem {

    static class Book {
        int id;
        int score;

        public Book(int id, int score) {
            this.id = id;
            this.score = score;
        }
    }

    static Map<Integer, Integer> bookReps;

	@Override
	public Output solve(Input input) {
		List<Output.OutputLibrary> outputLibraries = new ArrayList<>();

		bookReps = new HashMap<>();
        for (Input.Library library : input.libraries) {
            for (int bookId : library.bookIds) {
                bookReps.compute(bookId, (k, v) -> v == null ? 1 : v + 1);
            }
        }

		HashSet<Integer> mem = new HashSet<>();
		HashSet<Integer> librariesMem = new HashSet<>();
		int d = 0;
		int scorePerDay = 1;
		while (input.libraryCount != librariesMem.size() && d < input.totalDays) {
			LibRes libRes = maxLibrary(input, d, mem, librariesMem, scorePerDay);
			if (libRes == null) break;
			Input.Library library = libRes.lib;
			librariesMem.add(library.id);

			d += library.signupTime;
			scorePerDay = libRes.score / (input.totalDays - d);
			List<Integer> books = getBooks(input, library, mem, d);
			for (Integer id: books) {
				mem.add(id);
			}
			outputLibraries.add(new Output.OutputLibrary(library.id, books));
		}

//		List<Book> books = new ArrayList<>();
//        for (int i = 0; i < input.bookScores.length; i++) {
//            books.add(new Book(i, input.bookScores[i]));
//        }
//        books.sort(Comparator.comparingInt(b -> -b.score));

//        int missing = 0;
//        for (int i = 0; i < 1000; i++) {
//            if (!mem.contains(books.get(i).id)) {
//                missing++;
//            }
//        }
//        System.out.println("missing books " + missing);

        return new Output(outputLibraries);
	}

	static class LibRes {
		Input.Library lib;
		int score;

		public LibRes(Input.Library lib, int score) {
			this.lib = lib;
			this.score = score;
		}
	}

	public static LibRes maxLibrary(Input input, int d, HashSet<Integer> mem, HashSet<Integer> librariesMem, int scorePerDay) {
		List<Input.Library> availableLibraries = Arrays.stream(input.libraries).filter(l -> !librariesMem.contains(l.id)).collect(Collectors.toList());
		int max = 0;
		Input.Library best = null;
		for (Input.Library library: availableLibraries) {
			int score = libraryScore(input, library, d + library.signupTime, mem, scorePerDay);
			if (score > max) {
				max = score;
				best = library;
			}
		}

		if (max <= 0) {
			return null;
		}
		return new LibRes(best, max);
	}

	public static int libraryScore(Input input, Input.Library library, int d, HashSet<Integer> mem, int scorePerDay) {
		if (d >= input.totalDays) {
			return 0;
		}

		List<Integer> bookIds = Arrays.stream(library.bookIds).filter(id -> !mem.contains(id)).mapToObj(x -> x).collect(Collectors.toList());
		Collections.sort(bookIds, Comparator.comparingInt(id -> -input.bookScores[id]));

		int N = (int) Math.min((input.totalDays - d) * ((long) library.booksPerDay), bookIds.size());

		double sum = 0;
		for (int bookId: bookIds.subList(0, N)) {
//            sum += input.bookScores[bookId];
            Integer reps = bookReps.get(bookId);
            int libCount = input.libraries.length;
            sum += input.bookScores[bookId] * ((double) libCount - reps + 1) / libCount;
		}
		return (int) (sum / library.signupTime);// - library.signupTime * scorePerDay * scorePerDay;
	}

	public static List<Integer> getBooks(Input input, Input.Library library, HashSet<Integer> mem, int d) {
		List<Integer> bookIds = Arrays.stream(library.bookIds).filter(id -> !mem.contains(id)).mapToObj(x -> x).collect(Collectors.toList());
		bookIds.sort(Comparator.comparingInt(i -> -input.bookScores[i]));
		if (d >= input.totalDays) {
			return Arrays.asList();
		}
		int N = (int) Math.min((input.totalDays - d) * ((long) library.booksPerDay), bookIds.size());
		return bookIds.subList(0, N);
	}
}
