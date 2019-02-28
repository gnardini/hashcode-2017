import java.util.List;

public class Input {
	public int photosInCollection;
	public List<Photo> photos;

	public Input(int photosInCollection, List<Photo> photos) {
		this.photosInCollection = photosInCollection;
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "Input{" +
				"photosInCollection=" + photosInCollection +
				", photos=" + photos +
				'}';
	}
}