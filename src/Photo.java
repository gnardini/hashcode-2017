import java.util.HashSet;

public class Photo {
	public int id;
	public Orientation orientation;
	public HashSet<String> tags;

	public Photo(int id, Orientation orientation, HashSet<String> tags) {
		this.id = id;
		this.orientation = orientation;
		this.tags = tags;
	}

	enum Orientation {
		V,H
	}
}