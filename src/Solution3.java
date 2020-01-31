import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution3 implements Problem {

	public static double THRESHOLD = 20.0;

	public static final double penalty(double score, int size) {
		double p = score/size;
		return p;
	}

	@Override
	public Output solve(Input input) {
		Arrays.sort(input.Rs, (r1, r2) -> r2.R - r1.R);
		Map<Integer, Integer> load = new HashMap<>();
		Map<Integer, Set<Integer>> output = new HashMap<>();
		Map<Integer, Integer> video_bestlaten;
		for (Input.Request request: input.Rs) {
//			find best cache for request.videoId
			int videoSize = input.videoSizes[request.videoId];
			Input.Endpoint endpoint = input.Es[request.endpointId];

			List<Integer> sortedCaches = new ArrayList<>(endpoint.caches.keySet());

//			sortedCaches.sort(Comparator.comparingInt(endpoint.caches::get));
			sortedCaches.sort(Comparator.comparingDouble(c -> ((double) endpoint.latency - endpoint.caches.get(c)) * request.R));

			for (int k = 0; k < Math.min(sortedCaches.size(), sortedCaches.size()); k++) {
				int c = sortedCaches.get(k);
				if (!load.containsKey(c)) {
					load.put(c, 0);
				}
				if (load.get(c) + videoSize <= input.X) {

//					double score = ((double) endpoint.latency - endpoint.caches.get(c)) * request.R;
//					double p = penalty(score, videoSize*1000);
//					if (p > THRESHOLD) {
//						continue;
//					}

					load.put(c, load.get(c) + videoSize);
					if (!output.containsKey(c)) {
						output.put(c, new HashSet<>());
					}
					output.get(c).add(request.videoId);
					break;
				}
			}

		}

		return new Output(output);
	}
}
