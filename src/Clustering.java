import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Clustering {

	public static List<Centroid> kmeans(int k, List<List<Double>> data) {
		int N = 5; // iterations
		Collections.shuffle(data);
		List<Centroid> centroids = new ArrayList<>();
		int id = 0;
		for (List<Double> point: data.subList(0, k)) {
			centroids.add(new Centroid(id++, point));
		}

 		for (int i = 0; i < N; i++) {
 			for (Centroid centroid: centroids) {
 				centroid.points = new ArrayList<>();
			}
 			for (List<Double> point: data) {
 				Collections.sort(centroids, Comparator.comparingDouble(c -> distance(c.center, point)));
 				Centroid centroid = centroids.get(0);
 				centroid.points.add(point);
			}
 			for (Centroid centroid: centroids) {
 				centroid.center = mean(centroid.points);
			}
		}

 		return centroids;
	}

	public static List<Double> mean(List<List<Double>> data) {
		List<Double> mean = new ArrayList<>();
		for (Double x: data.get(0)) {
			mean.add(0.0);
		}

		for (List<Double> point: data) {
			for (int i = 0; i < point.size(); i++) {
				mean.set(i, mean.get(i) + point.get(i));
			}
		}

		for (int i = 0; i < mean.size(); i++) {
			mean.set(i, mean.get(i)/data.size());
		}
		return mean;
	}

	public static double distance(List<Double> x1, List<Double> x2) {
		double sum = 0;
		for (int i = 0; i < x1.size(); i++) {
			double a = x1.get(i);
			double b = x2.get(i);
			sum += (a - b) * (a - b);
		}
		return Math.sqrt(sum);
	}

	public static class Centroid {
		public final int id;
		public List<Double> center;
		public List<List<Double>> points;

		public Centroid(int id, List<Double> center) {
			this.id = id;
			this.center = center;
		}

		@Override
		public String toString() {
			return "Centroid{" +
					"id=" + id +
					", center=" + center +
					", points=" + points +
					'}';
		}
	}

	public static class Cluster {
		public List<Double> center;
		public int count;
		public double radius;

		public Cluster(List<Double> center, int count, double radius) {
			this.center = center;
			this.count = count;
			this.radius = radius;
		}
	}
}
