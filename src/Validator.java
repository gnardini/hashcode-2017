import java.util.HashSet;
import java.util.Set;

public class Validator {

    public static void validateSolution(Input input, Output output) {
        Set<Integer> photoIds = new HashSet<>();
        int photosInOutput = 0;
        // Each photo only once
        for (Slide slide : output.slides) {
            for (Photo photo : slide.photos) {
                photoIds.add(photo.id);
                photosInOutput++;
            }
        }

        if (photoIds.size() != photosInOutput) {
            throw new IllegalArgumentException("invalid photo numbers");
        }

        // Each slide contains one H or 2
        for (Slide slide : output.slides) {
            if (slide.photos.size() == 1) {
                assertEquals(Photo.Orientation.H, slide.photos.get(0).orientation);
            } else {
                assertEquals(Photo.Orientation.V, slide.photos.get(0).orientation);
                assertEquals(Photo.Orientation.V, slide.photos.get(1).orientation);
            }
        }

        // All ids are valid
        if (!photoIds.stream().allMatch(x -> x >= 0 && x < input.photosInCollection)) {
            throw new IllegalArgumentException("invalid photo numbers");
        }
    }

    public static int score(Input input, Output output) {
        int score = 0;
        for (int i = 0; i < output.slides.size() - 1; i++) {
            score += output.slides.get(i).transitionScoreTo(output.slides.get(i + 1));
        }
        return score;
    }

    public static int score(Slide current, Slide next) {
        Set<String> intersection = new HashSet<>(current.tags);
        intersection.retainAll(next.tags);
        return intersection.size();
    }

    public static int score(Photo photo, Photo other) {
        Set<String> intersection = new HashSet<>(photo.tags);
        intersection.retainAll(other.tags);
        int intersect = intersection.size();
        return Math.min(intersect, Math.min(photo.tags.size() - intersect, other.tags.size() - intersect));
    }

    public static void assertEquals(Photo.Orientation expected, Photo.Orientation actual) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException("expected " + expected + " but got " + actual);
        }
    }
}
