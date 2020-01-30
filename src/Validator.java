import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

                load += input.Vs[0];
            }

            if(load > input.X) {
                throw new RuntimeException("cache " + cache + " exceeds max capacity: " + load + "/" + input.X);
            }
        }
    }

    public static int score(Input input, Output output) {
        int score = 0;
        int totalRequests = 0;
        for (Input.Request request: input.Rs) {
            Input.Endpoint endpoint = input.Es[request.Re];
            int Lmin = endpoint.Ld;
            for (int cache: endpoint.L.keySet()) {
                if (output.caches.containsKey(cache) && output.caches.get(cache).contains(request.Rv)) {
                    Lmin = Math.min(Lmin, endpoint.L.get(cache));
                }
            }

            score += request.Rn * (endpoint.Ld - Lmin);
            totalRequests += request.Rn;
        }
        return (int) Math.floor((score * 1000.0)/totalRequests);
    }
}
