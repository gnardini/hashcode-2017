import java.util.HashSet;
import java.util.Set;

public class Validator {

    public static void validateSolution(Input input, Output output) {
        Set<Integer> processedCaches = new HashSet<>();
        for (int cache: output.caches.keySet()) {
            if (processedCaches.contains(cache)) {
                throw new RuntimeException("duplicated cache in output: " + cache);
            }
            processedCaches.add(cache);
            long load = 0;
            if (cache < 0 || cache >= input.C) {
                throw new RuntimeException("invalid cache id: " + cache);
            }
            for (int video: output.caches.get(cache)) {
                if (video < 0 || video >= input.V) {
                    throw new RuntimeException("invalid video id: " + video);
                }

                load += input.videoSizes[video];
            }

            if(load > input.X) {
                throw new RuntimeException("cache " + cache + " exceeds max capacity: " + load + "/" + input.X);
            }
        }
    }

    public static int score(Input input, Output output) {
        long score = 0;
        long totalRequests = 0;
        for (Input.Request request: input.Rs) {
            Input.Endpoint endpoint = input.Es[request.endpointId];
            long Lmin = endpoint.latency;
            for (int cache: endpoint.caches.keySet()) {
                if (output.caches.containsKey(cache) && output.caches.get(cache).contains(request.videoId)) {
                    Lmin = Math.min(Lmin, endpoint.caches.get(cache));
                }
            }

            score += request.R * (endpoint.latency - Lmin);
            totalRequests += request.R;
        }
        return (int) (Math.floor((score * 1000.0)/totalRequests));
    }
}
