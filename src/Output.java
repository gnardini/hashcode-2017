import java.util.List;

public class Output {
    final int numberOfSlides;
    final List<List<Integer>> photoOrder;

    public Output(int numberOfSlides, List<List<Integer>> photoOrder) {
        this.numberOfSlides = numberOfSlides;
        this.photoOrder = photoOrder;
    }
}
