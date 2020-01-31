import java.util.*;

public class VideoCacheSolution implements Problem {

    static int videoAverage;
    static int[][] endpointVideoRequests;
    static Map<Integer, List<Input.Endpoint>> cachesToEndpoints;

    static class VideoCacheTimeSaved implements Comparable<VideoCacheTimeSaved> {
        final int videoId;
        final int cacheId;
        final double timeSaved;

        public VideoCacheTimeSaved(int videoId, int cacheId, double timeSaved) {
            this.videoId = videoId;
            this.cacheId = cacheId;
            this.timeSaved = timeSaved;
        }

        @Override
        public int compareTo(VideoCacheTimeSaved o) {
            return Double.compare(o.timeSaved, timeSaved);
        }
    }

    @Override
    public Output solve(Input input) {
        int totalVideoSizes = 0;
        for (int videoSize : input.videoSizes) {
            totalVideoSizes += videoSize;
        }
        videoAverage = totalVideoSizes / input.V;
        endpointVideoRequests = new int[input.E][input.V];
        for (Input.Request r : input.Rs) {
            endpointVideoRequests[r.endpointId][r.videoId] += r.R;
        }
        cachesToEndpoints = new HashMap<>();
        for (int i = 0; i < input.Es.length; i++) {
            for (Integer cacheId : input.Es[i].caches.keySet()) {
                List<Input.Endpoint> endpoints = cachesToEndpoints.getOrDefault(cacheId, new ArrayList<>());
                endpoints.add(input.Es[i]);
                cachesToEndpoints.put(cacheId, endpoints);
            }
        }

        PriorityQueue<VideoCacheTimeSaved> orderedStuff = new PriorityQueue<>();


        double[][] m = new double[input.V][input.C];
        for (int i = 0; i < input.V; i++) {
            for (int j = 0; j < input.C; j++) {
                m[i][j] = timeSavedVideoInCache(input, i, j);
                orderedStuff.add(new VideoCacheTimeSaved(i, j, m[i][j]));
            }
            if ( i % 100 == 0) {
                System.out.println(i);
            }
        }

        Map<Integer, Set<Integer>> output = new HashMap<>();

        int[] totalCachesSizes = new int[input.V];
        for (int i = 0; i < input.V; i++) {
            int totalInLine = 0;
            for (int j = 0; j < input.C; j++) {
                totalInLine += m[i][j];
            }
            totalCachesSizes[i] = totalInLine;
        }

        int[] cacheSizes = new int[input.C];
        while (!orderedStuff.isEmpty()) {
//            System.out.println(orderedStuff.size());
            VideoCacheTimeSaved next = orderedStuff.poll();
//            double currentValue = m[next.videoId][next.cacheId];
//            if (next.timeSaved != currentValue) {
//                orderedStuff.add(new VideoCacheTimeSaved(next.videoId, next.cacheId, currentValue));
//                continue;
//            }
            if (cacheSizes[next.cacheId] + input.videoSizes[next.videoId] < input.X) {
                double multiplier = videoAverage*videoAverage > input.videoSizes[next.videoId]*input.videoSizes[next.videoId] ? 2 : .8;
                if ((next.timeSaved / totalCachesSizes[next.videoId]) * multiplier > Math.random()) {
                    cacheSizes[next.cacheId] += input.videoSizes[next.videoId];
                    Set<Integer> videos = output.getOrDefault(next.cacheId, new HashSet<>());
                    videos.add(next.videoId);
                    output.put(next.cacheId, videos);
//                    for (Input.Endpoint endpoint : cachesToEndpoints.get(next.cacheId)) {
//                        for (Integer cache : endpoint.caches.keySet()) {
//                            m[next.videoId][cache] = timeSavedVideoInCache(input, next.videoId, cache);
//                        }
//                    }
                }
            }
        }

        return new Output(output);
    }

    private double timeSavedVideoInCache(Input input, int videoId, int cacheId) {
        double timeSaved = 0;
        List<Input.Endpoint> endpoints = cachesToEndpoints.get(cacheId);
        if (endpoints == null || endpoints.isEmpty()) {
            return 0;
        }
        for (Input.Endpoint endpoint : endpoints) {
            timeSaved += endpointVideoRequests[endpoint.id][videoId] * (endpoint.latency - endpoint.caches.get(cacheId));
        }
        int videoSize = input.videoSizes[videoId];
        return timeSaved / Math.log(videoSize);
    }
}
