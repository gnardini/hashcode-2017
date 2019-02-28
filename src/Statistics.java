import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Statistics {

    public static void main(String[] args) throws IOException {
//        System.out.println("B");
//        computeStats(Main.read("b_lovely_landscapes"));
//        System.out.println();
//
//        System.out.println("C");
//        computeStats(Main.read("c_memorable_moments"));
//        System.out.println();
//
        System.out.println("D");
        computeStats(Main.read("d_pet_pictures"));
        System.out.println();

//        System.out.println("E");
//        computeStats(Main.read("e_shiny_selfies"));
    }

    public static void computeStats(Input input) {
        System.out.println(String.format("number of photos: %d", input.photosInCollection));
        Map<String, Integer> tagMap = countTags(input.photos);
        System.out.println(String.format("number of unique tags: %d", tagMap.size()));

        List<String> tags = new ArrayList<>(tagMap.keySet());
        List<String> uselessTags = tags.stream().filter(t -> tagMap.get(t) <= 1).collect(Collectors.toList());
        System.out.println(String.format("useless tags: %d", uselessTags.size()));

        int horizontals = input.photos.stream().filter(p -> p.orientation == Photo.Orientation.H).collect(Collectors.toList()).size();
        int verticals = input.photosInCollection - horizontals;
        System.out.println(String.format("horizontals: %d, verticals: %d", horizontals, verticals));

        double[] tagsPerPhoto = toDoubleArray(input.photos.stream().map(p -> p.tags.size()).collect(Collectors.toList()));
        double meanTagsPerPhoto = mean(tagsPerPhoto);
        System.out.println(String.format("mean tags per photo: %.4f", meanTagsPerPhoto));

        double[] photosPerTag = toDoubleArray(new ArrayList<>(tagMap.values()));
        double meanPhotosPerTag = mean(photosPerTag);
        System.out.println(String.format("mean photos per tag: %.4f", meanPhotosPerTag));

        tags.sort(Comparator.comparingInt(tagMap::get));
        System.out.println("Lower 25: " + tags.subList(0, 25).stream().map(tagMap::get).collect(Collectors.toList()));
        Collections.reverse(tags);
        System.out.println("Upper 25: " + tags.subList(0, 25).stream().map(tagMap::get).collect(Collectors.toList()));
    }

    public static Map<String, Integer> countTags(List<Photo> photos) {
        Map<String, Integer> tags = new HashMap<>();

        for (Photo photo : photos) {
            for (String tag : photo.tags) {
                if (!tags.containsKey(tag)) {
                    tags.put(tag, 0);
                }
                tags.put(tag, tags.get(tag) + 1);
            }
        }

        return tags;
    }

    public static double[] map(Function<Double, Double> f, double[] data) {
        double[] ans = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            ans[i] = f.apply(data[i]);
        }
        return ans;
    }

    public static double mean(double[] data) {
        return Arrays.stream(data).sum() / data.length;
    }

    public static double[] toDoubleArray(List<Integer> list) {
        return toDoubleArray(list.toArray(new Integer[list.size()]));
    }

    public static double[] toDoubleArray(Integer[] arr) {
        double[] ans = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static double[] toDoubleArray(int[] arr) {
        double[] ans = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static double[] print(double[] arr) {
        System.out.println(Arrays.toString(arr));
        return arr;
    }
}
