import java.util.Arrays;
import java.util.Collections;
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

    public SlideScore tagComparison(Slide otherSlide) {
		Set<String> union = new HashSet<>(this.tags);
		union.addAll(new HashSet<>(otherSlide.tags));
		Set<String> onlyInCurrentPhoto = new HashSet<>(this.tags);
		Set<String> onlyInNextPhoto = otherSlide.tags;
		onlyInCurrentPhoto.removeAll(onlyInNextPhoto);
    	return new SlideScore(onlyInCurrentPhoto.size(), onlyInNextPhoto.size(), union.size());
	}

	public int transitionScoreTo(Slide otherSlide) {
    	return tagComparison(otherSlide).min();
	}

	class SlideScore {
		public int tagsOnlyInCurrentPhoto;
		public int tagsOnlyInNextPhoto;
		public int tagsInBothPhotos;

		public SlideScore(int tagsOnlyInCurrentPhoto, int tagsOnlyInNextPhoto, int tagsInBothPhotos) {
			this.tagsOnlyInCurrentPhoto = tagsOnlyInCurrentPhoto;
			this.tagsOnlyInNextPhoto = tagsOnlyInNextPhoto;
			this.tagsInBothPhotos = tagsInBothPhotos;
		}

		public int min() {
			return Collections.min(Arrays.asList(tagsOnlyInCurrentPhoto, tagsInBothPhotos, tagsOnlyInNextPhoto));
		}
	}
}
