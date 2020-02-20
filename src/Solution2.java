import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Solution2 implements Problem {

	public static void calculateStats(Input input) {

	}

	@Override
	public Output solve(Input input) {
		calculateStats(input);
		List<Output.OutputLibrary> outputLibraries = new ArrayList<>();

		HashSet<Integer> mem = new HashSet<>();
		HashSet<Integer> librariesMem = new HashSet<>();
		int d = 0;
		while(input.libraryCount != librariesMem.size() && d < input.totalDays) {
			Input.Library library = maxLibrary(input, d, mem, librariesMem);
			if (library == null) break;
			librariesMem.add(library.id);

			d += library.signupTime;
			List<Integer> books = getBooks(input, library, mem, d);
			for (Integer id: books) {
				mem.add(id);
			}
			outputLibraries.add(new Output.OutputLibrary(library.id, books));
		}

		return new Output(outputLibraries);
	}

	public static Input.Library maxLibrary(Input input, int d, HashSet<Integer> mem, HashSet<Integer> librariesMem) {
		List<Input.Library> availableLibraries = Arrays.stream(input.libraries).filter(l -> !librariesMem.contains(l.id)).collect(Collectors.toList());
		int max = 0;
		Input.Library best = null;
		for (Input.Library library: availableLibraries) {
			int score = libraryScore(input, library, d + library.signupTime, mem);
			if (score > max) {
				max = score;
				best = library;
			}
		}
//		Input.Library best = Collections.max(availableLibraries, Comparator.comparingInt(l -> libraryScore(input, l, d + l.signupTime, mem)));

		if (max <= 0) {
			return null;
		}
		return best;
	}

	public static int libraryScore(Input input, Input.Library library, int d, HashSet<Integer> mem) {
		if (d >= input.totalDays) {
			return 0;
		}

		List<Integer> bookIds = Arrays.stream(library.bookIds).filter(id -> !mem.contains(id)).mapToObj(x -> x).collect(Collectors.toList());
		Collections.sort(bookIds, Comparator.comparingInt(id -> -input.bookScores[id]));

		int N = (int) Math.min((input.totalDays - d) * ((long) library.booksPerDay), bookIds.size());

		int sum = 0;
		for (int bookId: bookIds.subList(0, N)) {
			sum += input.bookScores[bookId];
		}
		return sum;
	}

	// ignore already processed books
	// ignore 0-score books
	// maybe sort by score
	public static List<Integer> getBooks(Input input, Input.Library library, HashSet<Integer> mem, int d) {
		List<Integer> bookIds = Arrays.stream(library.bookIds).filter(id -> !mem.contains(id)).mapToObj(x -> x).collect(Collectors.toList());
		bookIds.sort(Comparator.comparingInt(i -> -input.bookScores[i]));
		if (d >= input.totalDays) {
			return Arrays.asList();
		}
//		int N = (input.totalDays - d) * library.booksPerDay;
		int N = (int) Math.min((input.totalDays - d) * ((long) library.booksPerDay), bookIds.size());
		return bookIds.subList(0, N);
	}
}
