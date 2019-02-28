import java.io.IOException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Tests {

    public static void main(String[] args) throws IOException {
        validateValidation1();
        validateValidation2();
        validateValidation3();
        test1();
    }

    public static void validateValidation1() throws IOException {
        Input input = Main.read("a_example");
        List<Slide> slides = Arrays.asList(
                new Slide(Arrays.asList(new Photo(3, Photo.Orientation.H, new HashSet<>(Arrays.asList("cat", "beach", " sun"))))),
                new Slide(Arrays.asList(new Photo(3, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "cat"))))),
                new Slide(Arrays.asList(new Photo(1, Photo.Orientation.V, new HashSet<>(Arrays.asList("garden", "selfie"))),
                        new Photo(2, Photo.Orientation.V, new HashSet<>(Arrays.asList("selfie", "smile")))))
        );

        Output output = new Output(slides);

        try {
            Validator.validateSolution(input, output);
            throw new RuntimeException("expected duplicated photo ids exception");
        } catch (IllegalArgumentException e) {

        }
    }

    public static void validateValidation2() throws IOException {
        Input input = Main.read("a_example");
        List<Slide> slides = Arrays.asList(
                new Slide(Arrays.asList(new Photo(0, Photo.Orientation.H, new HashSet<>(Arrays.asList("cat", "beach", " sun"))))),
                new Slide(Arrays.asList(new Photo(3, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "cat"))))),
                new Slide(Arrays.asList(new Photo(1, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "selfie"))),
                        new Photo(2, Photo.Orientation.V, new HashSet<>(Arrays.asList("selfie", "smile")))))
        );

        Output output = new Output(slides);

        try {
            Validator.validateSolution(input, output);
            throw new RuntimeException("expected a vertical photo in the same slide as an horizontal one");
        } catch (IllegalStateException e) {

        }
    }

    public static void validateValidation3() throws IOException {
        Input input = Main.read("a_example");
        List<Slide> slides = Arrays.asList(
                new Slide(Arrays.asList(new Photo(0, Photo.Orientation.H, new HashSet<>(Arrays.asList("cat", "beach", " sun"))))),
                new Slide(Arrays.asList(new Photo(3, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "cat"))))),
                new Slide(Arrays.asList(new Photo(4, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "selfie"))),
                        new Photo(2, Photo.Orientation.V, new HashSet<>(Arrays.asList("selfie", "smile")))))
        );

        Output output = new Output(slides);

        try {
            Validator.validateSolution(input, output);
            throw new RuntimeException("expected a photo with id bigger than the number of photos in the problem");
        } catch (IllegalStateException e) {

        }
    }

    public static void test1() throws IOException {
        Input input = Main.read("a_example");

        List<Slide> slides = Arrays.asList(
                new Slide(Arrays.asList(new Photo(0, Photo.Orientation.H, new HashSet<>(Arrays.asList("cat", "beach", " sun"))))),
                new Slide(Arrays.asList(new Photo(3, Photo.Orientation.H, new HashSet<>(Arrays.asList("garden", "cat"))))),
                new Slide(Arrays.asList(new Photo(1, Photo.Orientation.V, new HashSet<>(Arrays.asList("garden", "selfie"))),
                        new Photo(2, Photo.Orientation.V, new HashSet<>(Arrays.asList("selfie", "smile")))))
        );

        Output output = new Output(slides);
        assertEquals(2, Validator.score(input, output));
    }

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new IllegalStateException("expected " + expected + " but got " + actual);
        }
    }
}