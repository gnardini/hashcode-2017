import java.io.IOException;

public class Tests {

	public static void main(String[] args) throws IOException {
		// TODO
	}

	public static void assertEquals(int expected, int actual) {
		if (expected != actual) {
			throw new IllegalStateException("expected " + expected + " but got " + actual);
		}
	}
}
