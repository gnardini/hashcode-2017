import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution implements Problem {

    @Override
    public Output solve(Input input) {
        Map<Integer, Photo> photosLeft = input.photos.stream().collect(Collectors.toMap(p -> p.id, Function.identity()));
        Map<String, List<Integer>> tagsToPhotoId = calculateTagsMap(input.photos);

        List<Slide> slides = new ArrayList<>();
        Slide currentSlide = findFirstSlide(photosLeft);
        currentSlide.markAll();
        slides.add(currentSlide);
        for (Photo photo : currentSlide.photos) {
            photosLeft.remove(photo.id);
        }

        while (!photosLeft.isEmpty()) {
            int photosLeftCount = photosLeft.size();
            if (photosLeftCount % 50 == 0) {
                System.out.println(photosLeft);
            }
            currentSlide = findNextSlide(currentSlide, tagsToPhotoId, input.photos);
            if (currentSlide != null) {
                currentSlide.markAll();
                slides.add(currentSlide);
                for (Photo photo : currentSlide.photos) {
                    photosLeft.remove(photo.id);
                }
            } else {
                currentSlide = findFirstSlide(photosLeft);
                currentSlide.markAll();
                slides.add(currentSlide);
                for (Photo photo : currentSlide.photos) {
                    photosLeft.remove(photo.id);
                }
            }
        }

        return new Output(slides);
    }

    private Slide findNextSlide(Slide slide, Map<String, List<Integer>> tagToPhotos, List<Photo> inputPhotos) {
        System.out.println(slide.tags.size());
        Set<Integer> photoIds = slide.tags.stream()
                .flatMap(tag -> tagToPhotos.get(tag).stream())
                .collect(Collectors.toSet());
        

        int maxScore = -1;
        Slide nextSlide = null;
        for (Integer otherId : photoIds) {
            Photo otherPhoto = inputPhotos.get(otherId);
            if (otherPhoto.marked || otherPhoto.vertical()) {
                continue;
            }
            int score = slide.transitionScoreTo(new Slide(otherPhoto));
            if (score > maxScore) {
                maxScore = score;
                nextSlide = new Slide(otherPhoto);
            }
        }

        List<Integer> verticalIds = photoIds.stream()
                .filter(id -> {
                    Photo photo = inputPhotos.get(id);
                    return photo.vertical() && !photo.marked;
                })
                .collect(Collectors.toList());
        if (verticalIds.size() > 1) {
            for (int i = 0; i < verticalIds.size(); i++) {
                int verticalId = verticalIds.get(i);
                for (int j = i + 1; j < verticalIds.size(); j++) {
                    int otherVerticalId = verticalIds.get(j);
                    Slide potentialSlide = new Slide(inputPhotos.get(verticalId), inputPhotos.get(otherVerticalId));
                    int score = slide.transitionScoreTo(potentialSlide);
                    if (score > maxScore) {
                        maxScore = score;
                        nextSlide = potentialSlide;
                    }
                }
            }
        }

        return nextSlide;
    }

    public Slide findFirstSlide(Map<Integer, Photo> photos) {
        int maxTags = -1;
        Photo bestPhoto = null;
        for (Photo photo : photos.values()) {
            if (photo.tags.size() > maxTags) {
                maxTags = photo.tags.size();
                bestPhoto = photo;
            }
        }
        if (bestPhoto.vertical()) {
            final int bestPhotoId = bestPhoto.id;
            List<Photo> verticalPhotos = photos.values().stream()
                    .filter(Photo::vertical)
                    .filter(photo -> photo.id != bestPhotoId)
                    .collect(Collectors.toList());
            for (Photo verticalPhoto : verticalPhotos) {
                return new Slide(bestPhoto, verticalPhoto);
            }
        }
        return new Slide(bestPhoto);
    }

    private Map<String, List<Integer>> calculateTagsMap(List<Photo> originalPhotos) {
        Map<String, List<Integer>> tagsToPhotoId = new HashMap<>();
        for (Photo photo : originalPhotos) {
            for (String tag : photo.tags) {
                tagsToPhotoId.computeIfPresent(tag, (key, photos) -> {
                    photos.add(photo.id);
                    return photos;
                });
                tagsToPhotoId.computeIfAbsent(tag, key -> {
                    ArrayList<Integer> array = new ArrayList<>();
                    array.add(photo.id);
                    return array;
                });
            }
        }
        return tagsToPhotoId;
    }
}
