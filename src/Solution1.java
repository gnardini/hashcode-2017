import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution1 implements Problem {

	@Override
	public Output solve(Input input) {
		Arrays.sort(input.Rs, (r1, r2) -> r2.R - r1.R);
		Map<Integer, Integer> load = new HashMap<>();
		Map<Integer, Set<Integer>> output = new HashMap<>();
		for (Input.Request request: input.Rs) {
//			find best cache for request.videoId
			int videoSize = input.videoSizes[request.videoId];
			Input.Endpoint endpoint = input.Es[request.endpointId];

//			int bestCache = -1;
//			int Lmin = endpoint.latency;
			List<Integer> sortedCaches = new ArrayList<>(endpoint.caches.keySet());
			sortedCaches.sort(Comparator.comparingInt(endpoint.caches::get));

//			for (int cache: endpoint.caches.keySet()) {
//				if (endpoint.caches.get(cache) < Lmin) {
//					Lmin = endpoint.caches.get(cache);
//					bestCache = cache;
//				}
//			}
			for (int k = 0; k < Math.min(sortedCaches.size(), sortedCaches.size()); k++) {
				int c = sortedCaches.get(k);
				if (!load.containsKey(c)) {
					load.put(c, 0);
				}
				if (load.get(c) + videoSize <= input.X) {
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
