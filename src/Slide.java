import java.util.*;

public class Slide {
    public List<Photo> photos;
    public Set<String> tags;
    public boolean marked;

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

    public void markAll() {
        for (Photo photo : photos) {
            photo.marked = true;
        }
    }

    public SlideScore tagComparison(Slide otherSlide) {
		Set<String> intersection = new HashSet<>(this.tags);
		intersection.retainAll(new HashSet<>(otherSlide.tags));

		Set<String> onlyInCurrentPhoto = new HashSet<>(this.tags);
		onlyInCurrentPhoto.removeAll(new HashSet<>(intersection));

		Set<String> onlyInNextPhoto = new HashSet<>(otherSlide.tags);
        onlyInNextPhoto.removeAll(new HashSet<>(intersection));

    	return new SlideScore(onlyInCurrentPhoto.size(), onlyInNextPhoto.size(), intersection.size());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slide slide = (Slide) o;
        return marked == slide.marked &&
                Objects.equals(photos, slide.photos) &&
                Objects.equals(tags, slide.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(photos, tags, marked);
    }

    public String toString() {
        return "Slide{" +
                "photos=" + photos +
                ", tags=" + tags +
                '}';
    }
}
