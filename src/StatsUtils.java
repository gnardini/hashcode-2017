import java.util.ArrayList;
import java.util.List;

public class StatsUtils {
	public static double mean(List<Double> data) {

		return 0.0;
	}

	public static double min(List<Double> data) {
		return data.stream().mapToDouble(x -> x).min().getAsDouble();
	}

	public static double max(List<Double> data) {
		return data.stream().mapToDouble(x -> x).max().getAsDouble();
	}

	public static List<Double> data(int[] x) {
		List<Double> list = new ArrayList<>();
		for (int a: x) {
			list.add((double) a);
		}
		return list;
	}

	public static List<Double> data(double[] x) {
		List<Double> list = new ArrayList<>();
		for (double a: x) {
			list.add(a);
		}
		return list;
	}

	public static List<Double> data(float[] x) {
		List<Double> list = new ArrayList<>();
		for (float a: x) {
			list.add((double) a);
		}
		return list;
	}
}
