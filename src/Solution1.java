import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Solution1 implements Problem {

	public static void calculateStats(Input input) {

	}

	@Override
	public Output solve(Input input) {
		calculateStats(input);
		List<Output.OutputLibrary> outputLibraries = new ArrayList<>();

		List<Input.Library> libraries = orderLibraries(input);
		HashSet<Integer> mem = new HashSet<>();
		for (Input.Library library: libraries) {
			List<Integer> books = getBooks(input, library, mem);
			outputLibraries.add(new Output.OutputLibrary(library.id, books));
		}

		return new Output(outputLibraries);
	}

	public static List<Input.Library> orderLibraries(Input input) {

		return null;
	}

	public static List<Integer> getBooks(Input input, Input.Library library, HashSet<Integer> mem) {

		return null;
	}

	public static class LibraryScore {
		public final int id;
		public int score;

		public LibraryScore(int id) {
			this.id = id;
			this.score = 0;
		}
	}
}
