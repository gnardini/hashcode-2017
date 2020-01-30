import java.util.*;

public class VideoCacheSolution implements Problem {

    static int[][] endpointVideoRequests;
    static Map<Integer, List<Input.Endpoint>> cachesToEndpoints;

    static class VideoCacheTimeSaved implements Comparable<VideoCacheTimeSaved> {
        final int videoId;
        final int cacheId;
        final int timeSaved;

        public VideoCacheTimeSaved(int videoId, int cacheId, int timeSaved) {
            this.videoId = videoId;
            this.cacheId = cacheId;
            this.timeSaved = timeSaved;
        }

        @Override
        public int compareTo(VideoCacheTimeSaved o) {
            return Integer.compare(timeSaved, o.timeSaved);
        }
    }

    @Override
    public Output solve(Input input) {
        endpointVideoRequests = new int[input.E][input.V];
        for (Input.Request r : input.Rs) {
            endpointVideoRequests[r.videoId][r.endpointId] += r.R;
        }
        cachesToEndpoints = new HashMap<>();
        for (int i = 0; i < input.Es.length; i++) {
            for (Integer cacheId : input.Es[i].caches.keySet()) {
                cachesToEndpoints.getOrDefault(cacheId, new ArrayList<>()).add(input.Es[i]);
            }
        }

        PriorityQueue<VideoCacheTimeSaved> orderedStuff = new PriorityQueue<>();

        int[][] m = new int[input.V][input.C];
        for (int i = 0; i < input.V; i++) {
            for (int j = 0; j < input.C; j++) {
                m[i][j] = timeSavedVideoInCache(i, j);
                orderedStuff.add(new VideoCacheTimeSaved(i, j, m[i][j]));
            }
        }

        Map<Integer, Set<Integer>> output = new HashMap<>();

        int[] cacheSizes = new int[input.C];
        while (!orderedStuff.isEmpty()) {
            VideoCacheTimeSaved next = orderedStuff.poll();
            if (cacheSizes[next.cacheId] + input.videoSizes[next.videoId] < input.X) {
                cacheSizes[next.cacheId] += input.videoSizes[next.videoId];
                output.getOrDefault(next.cacheId, new HashSet<>()).add(next.videoId);
            }
        }

        return new Output(output);
    }

    private int timeSavedVideoInCache(int videoId, int cacheId) {
        int timeSaved = 0;
        for (Input.Endpoint endpoint : cachesToEndpoints.get(cacheId)) {
            timeSaved += endpointVideoRequests[endpoint.id][videoId] * (endpoint.latency - endpoint.caches.get(cacheId));
        }
        return timeSaved;
    }
}
