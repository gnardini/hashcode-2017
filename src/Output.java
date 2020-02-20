import java.util.List;

public class Output {

    final List<OutputLibrary> libraries;

    public Output(List<OutputLibrary> libraries) {
        this.libraries = libraries;
    }

    static final class OutputLibrary {
        final int libraryId;
        final List[] booksToScan;

        public OutputLibrary(int libraryId, List[] booksToScan) {
            this.libraryId = libraryId;
            this.booksToScan = booksToScan;
        }
    }
}
