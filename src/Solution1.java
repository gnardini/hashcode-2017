import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution1 implements Problem {

	public static void calculateStats(Input input) {

	}

	@Override
	public Output solve(Input input) {
		calculateStats(input);
		List<Output.OutputLibrary> outputLibraries = new ArrayList<>();

		List<Input.Library> libraries = orderLibraries(input);
		HashSet<Integer> mem = new HashSet<>();
		int d = 0;
		for (Input.Library library: libraries) {
			d += library.signupTime;
			if (d > input.totalDays) break;
			// TODO: check if value of d is ok
			List<Integer> books = getBooks(input, library, mem, d);
			for (Integer id: books) {
				mem.add(id);
			}
			outputLibraries.add(new Output.OutputLibrary(library.id, books));
		}

		return new Output(outputLibraries);
	}

	public static List<Input.Library> orderLibraries(Input input) {
		Map<Integer, Integer> scores = new HashMap<>();
		for (Input.Library library: input.libraries) {
			scores.put(library.id, libraryScore(input, library));
		}
		// sort by score
		List<Integer> sorted = new ArrayList<>(scores.keySet());
		sorted.sort(Comparator.comparingInt(scores::get));

		// reverse to process highest score first
		Collections.reverse(sorted);

		return sorted.stream().map(l -> input.libraries[l]).collect(Collectors.toList());
	}

	public static int libraryScore(Input input, Input.Library library) {
		int sum = 0;
		for (int bookId: library.bookIds) {
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
		// TODO: filter those that aren't gonna make it before day D
		return bookIds;
	}
}
