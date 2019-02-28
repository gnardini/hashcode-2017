import java.util.*;

public class SlidesSolution implements Problem {

    @Override
    public Output solve(Input input) {
        List<Slide> output = new ArrayList<>();
        Set<Slide> slides = new HashSet<>();
        List<Photo> verticals = new ArrayList<>();
        input.photos.forEach(photo -> {
            if (photo.orientation.equals(Photo.Orientation.H)) {
                slides.add(new Slide(Arrays.asList(photo)));
            } else {
                verticals.add(photo);
            }
        });

        Photo best;
        for (Photo vertical : verticals) {
            if (vertical.marked) {
                continue;
            }
            best = getBestMatchingVertical(verticals, vertical);
            vertical.marked = true;
            best.marked = true;
            slides.add(new Slide(best, vertical));
        }

        Slide current = slides.iterator().next();
        current.marked = true;
        output.add(current);

        while (output.size() != slides.size()) {
            Slide nextBestSlide = getNextBestSlide(slides, current);
            output.add(nextBestSlide);
            nextBestSlide.marked = true;
            current = nextBestSlide;
        }

        return new Output(output);
    }

    private Slide getNextBestSlide(Set<Slide> slides, Slide slide) {
        Slide best = null;
        int score = 0;
        for (Slide other : slides) {
            if (other.marked) {
                continue;
            }
            int transitionScoreTo = slide.transitionScoreTo(other);
            if (score < transitionScoreTo) {
                best = other;
                score = transitionScoreTo;
            }
        }
        return best;
    }

    private Photo getBestMatchingVertical(List<Photo> verticals, Photo vertical) {
        Photo bestPhoto = null;
        int bestTags = 0;
        for (Photo photo : verticals) {
            if (photo.marked || photo.id == vertical.id) {
                continue;
            }
            int size = new Slide(photo, vertical).tags.size();
            if (bestTags < size) {
                bestPhoto = photo;
                bestTags = size;
            }
        }
        return bestPhoto;
    }
}
