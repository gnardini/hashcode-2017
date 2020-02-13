import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
	public static void dumpCsv(String path, List<List<Double>> rows) {
		try(PrintWriter writer = new PrintWriter(new FileOutputStream(path))) {
			for (List<Double> row: rows) {
				writer.println(row.stream().map(x -> String.format("%.2f", x)).collect(Collectors.joining(",")));
			}
		} catch (FileNotFoundException e) {
			System.out.println("failed to dump csv");
			e.printStackTrace();
		}
	}

	public static void dumpDatapoints(String path, List<Datapoint> rows) {
		try(PrintWriter writer = new PrintWriter(new FileOutputStream(path))) {
			for (Datapoint datapoint: rows) {
				writer.println(datapoint.point.stream().map(x -> String.format("%.2f", x)).collect(Collectors.joining(",")) + "," + datapoint.format);
			}
		} catch (FileNotFoundException e) {
			System.out.println("failed to dump csv");
			e.printStackTrace();
		}
	}

	public static class Datapoint {
		public List<Double> point;
		public String format;

		public Datapoint(List<Double> point, String format) {
			this.point = point;
			this.format = format;
		}
	}
}
