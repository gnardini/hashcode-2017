import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Slide {
    public List<Photo> photos;
    public Set<String> tags;

    public Slide(List<Photo> photos) {
        this.photos = photos;
        Set<String> tags = new HashSet<>();
        if (photos.size() == 1) {
            tags.addAll(photos.get(0).tags);
        } else {
            tags.addAll(photos.get(0).tags);
            tags.addAll(photos.get(1).tags);
        }
        this.tags = tags;
    }
}
