import java.util.List;

public class Output {

    final List<OutputLibrary> libraries;

    public Output(List<OutputLibrary> libraries) {
        this.libraries = libraries;
    }

    static final class OutputLibrary {
        final int libraryId;
        final List<Integer> booksToScan;

        public OutputLibrary(int libraryId, List<Integer> booksToScan) {
            this.libraryId = libraryId;
            this.booksToScan = booksToScan;
        }
    }
}
