import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution implements Problem {

    @Override
    public Output solve(Input input) {
        Map<String, List<Integer>> tagsToPhotoId = new HashMap<>();
        for (Photo photo : input.photos) {
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
        int mostAmount = -1;
        String tag = null;
        for (Map.Entry<String, List<Integer>> entry : tagsToPhotoId.entrySet()) {
            if (entry.getValue().size() > mostAmount) {
                mostAmount = entry.getValue().size();
                tag = entry.getKey();
            }
        }
        List<Slide> slides = new ArrayList<>();
        List<Integer> photoIds = tagsToPhotoId.get(tag);
        int maxScore = -1;
        int firstId = -1;
        int secondId = -1;
        for (Integer photoId : photoIds) {
            for (Integer otherId : photoIds) {
                if (photoId < otherId) {
                    int score = Validator.score(input.photos.get(photoId), input.photos.get(otherId));
                    if (score > maxScore) {
                        maxScore = score;
                        firstId = photoId;
                        secondId = otherId;
                    }
                }
            }
        }
        if
        slides.add(new Slide());

        return new Output(slides);
    }
}
