import java.util.*;

public class Slide {
    public List<Photo> photos;
    public Set<String> tags;

    public Slide(Photo photo) {
        this.photos = new ArrayList<>();
        this.photos.add(photo);
        this.tags = new HashSet<>();
        this.tags.addAll(photo.tags);
    }

    public Slide(Photo photo, Photo other) {
        this.photos = new ArrayList<>();
        this.photos.add(photo);
        this.photos.add(other);
        this.tags = new HashSet<>();
        this.tags.addAll(photo.tags);
        this.tags.addAll(other.tags);
    }


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
		Set<String> onlyInNextPhoto = new HashSet<>(otherSlide.tags);
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
