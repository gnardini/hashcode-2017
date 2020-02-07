import java.util.ArrayList;
import java.util.List;

public class SolutionN implements Problem {

    @Override
    public Output solve(Input input) {
        List<Output.Command> commands = new ArrayList<>();
        commands.add(new Output.Load(0, 0, 0, 1, true));
        commands.add(new Output.Deliver(0, 0, 0, 1));
        return new Output(commands);
    }
}
