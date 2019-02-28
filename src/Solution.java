import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Solution implements Problem {

    @Override
    public Output solve(Input input) {
        Map<Integer, Photo> photosLeft = input.photos.stream().collect(Collectors.toMap(p -> p.id, Function.identity()));
        Map<String, Set<Integer>> tagsToPhotoId = calculateTagsMap(input.photos);

        List<Slide> slides = new ArrayList<>();
        Slide initialSlide = findFirstSlide(photosLeft);
        initialSlide.markAll();
        for (String tag : initialSlide.tags) {
            for (Photo photo : initialSlide.photos) {
                tagsToPhotoId.get(tag).remove(photo.id);
            }
        }
        slides.add(initialSlide);
        for (Photo photo : initialSlide.photos) {
            photosLeft.remove(photo.id);
        }

        List<List<Slide>> listOfListOfSlides = new ArrayList<>();

        while (!photosLeft.isEmpty()) {
            int photosLeftCount = photosLeft.size();
            if (photosLeftCount % 50 == 0) {
                System.out.println(photosLeftCount);
            }
            Slide firstSlide = findNextSlide(slides.get(0), tagsToPhotoId, input.photos);
            Slide lastSlide = findNextSlide(slides.get(slides.size() - 1), tagsToPhotoId, input.photos);

            Slide bestSlide = null;
            if (firstSlide != null && lastSlide != null) {
                int firstScore = firstSlide.transitionScoreTo(slides.get(0));
                int lastScore = lastSlide.transitionScoreTo(slides.get(slides.size() - 1));
                if (firstScore > lastScore) {
                    slides.add(0, firstSlide);
                    bestSlide = firstSlide;
                } else {
                    slides.add(lastSlide);
                    bestSlide = lastSlide;
                }
            } else if (firstSlide != null) {
                bestSlide = firstSlide;
                slides.add(0, firstSlide);
            } else if (lastSlide != null) {
                bestSlide = lastSlide;
                slides.add(lastSlide);
            } else {
                listOfListOfSlides.add(slides);
                slides = new ArrayList<>();
                initialSlide = findFirstSlide(photosLeft);
                slides.add(initialSlide);
                bestSlide = initialSlide;
            }

            for (String tag : bestSlide.tags) {
                for (Photo photo : bestSlide.photos) {
                    tagsToPhotoId.get(tag).remove(photo.id);
                }
            }
            for (Photo photo : bestSlide.photos) {
                photosLeft.remove(photo.id);
            }

            bestSlide.markAll();

//            if (currentSlide != null) {
//
//            } else {
//                currentSlide = findFirstSlide(photosLeft);
//                currentSlide.markAll();
//                for (String tag : currentSlide.tags) {
//                    for (Photo photo : currentSlide.photos) {
//                        tagsToPhotoId.get(tag).remove(photo.id);
////                        if (tagsToPhotoId.get(tag).isEmpty()) {
////                            tagsToPhotoId.remove(tag);
////                        }
//                    }
//                }
//                slides.add(currentSlide);
//                for (Photo photo : currentSlide.photos) {
//                    photosLeft.remove(photo.id);
//                }
//            }
        }
        listOfListOfSlides.add(slides);

        List<Slide> resultSlides = listOfListOfSlides.stream().flatMap(list -> list.stream()).collect(Collectors.toList());

        return new Output(resultSlides);
    }

    private Slide findNextSlide(Slide slide, Map<String, Set<Integer>> tagToPhotos, List<Photo> inputPhotos) {
//        Set<Integer> photoIds = slide.tags.stream()
//                .flatMap(tag -> tagToPhotos.get(tag).stream())
//                .collect(Collectors.toSet());
        int max = -1;
        Set<Integer> photoIds = null;
        for (String tag : slide.tags.stream()
                .filter(tag -> !tagToPhotos.get(tag).isEmpty())
                .collect(Collectors.toList())) {
            if (photoIds == null) {
                photoIds = tagToPhotos.get(tag);
            } else {
                Set<Integer> previous = new HashSet<>(photoIds);
                photoIds.retainAll(tagToPhotos.get(tag));
                if (photoIds.isEmpty()) {
                    photoIds = previous;
                    break;
                }
            }
        }
        if (photoIds == null) {
            return null;
        }
//        System.out.println(photoIds.size());

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
                int otherId = verticalIds.get((i + 1) % verticalIds.size());
                Slide potentialSlide = new Slide(inputPhotos.get(verticalId), inputPhotos.get(otherId));
                int score = slide.transitionScoreTo(potentialSlide);
                if (score > maxScore) {
                    maxScore = score;
                    nextSlide = potentialSlide;
                }
//                for (int j = i + 1; j < verticalIds.size(); j++) {
//                    int otherVerticalId = verticalIds.get(j);
//                    Slide potentialSlide = new Slide(inputPhotos.get(verticalId), inputPhotos.get(otherVerticalId));
//                    int score = slide.transitionScoreTo(potentialSlide);
//                    if (score > maxScore) {
//                        maxScore = score;
//                        nextSlide = potentialSlide;
//                    }
//                }
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

    private Map<String, Set<Integer>> calculateTagsMap(List<Photo> originalPhotos) {
        Map<String, Set<Integer>> tagsToPhotoId = new HashMap<>();
        for (Photo photo : originalPhotos) {
            for (String tag : photo.tags) {
                tagsToPhotoId.computeIfPresent(tag, (key, photos) -> {
                    photos.add(photo.id);
                    return photos;
                });
                tagsToPhotoId.computeIfAbsent(tag, key -> {
                    Set<Integer> array = new HashSet<>();
                    array.add(photo.id);
                    return array;
                });
            }
        }
        return tagsToPhotoId;
    }
}
