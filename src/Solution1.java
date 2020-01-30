import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Solution1 implements Problem {

	@Override
	public Output solve(Input input) {
		Arrays.sort(input.Rs, Comparator.comparingInt(r -> r.R));
		Map<Integer, Integer> load = new HashMap<>();
		Map<Integer, Set<Integer>> output = new HashMap<>();
		for (Input.Request request: input.Rs) {
//			find best cache for request.videoId
			int videoSize = input.videoSizes[request.videoId];
			Input.Endpoint endpoint = input.Es[request.endpointId];
			int bestCache = -1;
			int Lmin = endpoint.latency;
			for (int cache: endpoint.caches.keySet()) {
				if (endpoint.caches.get(cache) < Lmin) {
					Lmin = endpoint.caches.get(cache);
					bestCache = cache;
				}
			}
			if (bestCache > 0) {
				if (!load.containsKey(bestCache)) {
					load.put(bestCache, 0);
				}
				if (load.get(bestCache) + videoSize <= input.X) {
					load.put(bestCache, load.get(bestCache) + videoSize);
					if (!output.containsKey(bestCache)) {
						output.put(bestCache, new HashSet<>());
					}
					output.get(bestCache).add(request.videoId);
				}
			}
		}

		return new Output(output);
	}
}
