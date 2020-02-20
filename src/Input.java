public class Input {
	final int bookCount;
	final int libraryCount;
	final int totalDays;

	final int[] bookScores;
	final Library[] libraries;

    public Input(int bookCount, int libraryCount, int totalDays, int[] bookScores, Library[] libraries) {
        this.bookCount = bookCount;
        this.libraryCount = libraryCount;
        this.totalDays = totalDays;
        this.bookScores = bookScores;
        this.libraries = libraries;
    }

    final static class Library {
	    final int bookCount;
	    final int signupTime;
	    final int booksPerDay;
	    final int[] bookIds;

        public Library(int bookCount, int signupTime, int booksPerDay, int[] bookIds) {
            this.bookCount = bookCount;
            this.signupTime = signupTime;
            this.booksPerDay = booksPerDay;
            this.bookIds = bookIds;
        }
    }
}