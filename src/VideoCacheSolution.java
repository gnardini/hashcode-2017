public class VideoCacheSolution implements Problem {

    @Override
    public Output solve(Input input) {
        int[][] m = new int[input.V][input.C];
        for (int i = 0; i < input.V; i++) {
            for (int j = 0; j < input.C; j++) {
                m[i][j] = timeSavedVideoInCache(input, i, j);
            }
        }
        return null;
    }

    private int timeSavedVideoInCache(Input input, int video, int cache) {
        input.
        return 0;
    }
}
