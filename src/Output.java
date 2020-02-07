import java.util.List;

public class Output {
    final List<Command> commands;

    public Output(List<Command> commands) {
        this.commands = commands;
    }

    interface Command {
        void print(StringBuilder out);
    }

    static class Load implements Command {
        public final int drone;
        public final int warehouse;
        public final int productType;
        public final int productCount;
        public final boolean isLoad;

        public Load(int drone, int warehouse, int productType, int productCount, boolean isLoad) {
            this.drone = drone;
            this.warehouse = warehouse;
            this.productType = productType;
            this.productCount = productCount;
            this.isLoad = isLoad;
        }

        @Override
        public void print(StringBuilder out) {
            out.append(drone);
            out.append(' ');
            out.append(isLoad ? 'L' : 'U');
            out.append(' ');
            out.append(warehouse);
            out.append(' ');
            out.append(productType);
            out.append(' ');
            out.append(productCount);
            out.append('\n');
        }
    }

    static class Deliver implements Command {
        final int drone;
        final int order;
        final int productType;
        final int productCount;

        public Deliver(int drone, int order, int productType, int productCount) {
            this.drone = drone;
            this.order = order;
            this.productType = productType;
            this.productCount = productCount;
        }

        @Override
        public void print(StringBuilder out) {
            out.append(drone);
            out.append(' ');
            out.append('D');
            out.append(' ');
            out.append(order);
            out.append(' ');
            out.append(productType);
            out.append(' ');
            out.append(productCount);
            out.append('\n');
        }
    }

    static class Wait implements Command {
        final int drone;
        final int turns;

        public Wait(int drone, int turns) {
            this.drone = drone;
            this.turns = turns;
        }

        @Override
        public void print(StringBuilder out) {
            out.append(drone);
            out.append(' ');
            out.append('W');
            out.append(' ');
            out.append(turns);
            out.append('\n');
        }
    }
}
