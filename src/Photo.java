import java.util.Set;

public class Photo {
	public int id;
	public Orientation orientation;
	public Set<String> tags;

	boolean marked;

	public Photo(int id, Orientation orientation, Set<String> tags) {
		this.id = id;
		this.orientation = orientation;
		this.tags = tags;
	}

	enum Orientation {
		V,H
	}

	boolean vertical() {
		return orientation == Orientation.V;
	}

	boolean horizontal() {
		return orientation == Orientation.H;
	}

	@Override
	public String toString() {
		return "Photo{" +
				"id=" + id +
				", orientation=" + orientation +
				", tags=" + tags +
				'}';
	}
}