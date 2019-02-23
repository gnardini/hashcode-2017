public class Ride {
    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;
    public int startStep;
    public int finalStep;
    public int id;

    public Ride(int startRow, int startCol, int endRow, int endCol, int startStep, int finalStep) {
        this(startRow, startCol, endRow, endCol, startStep, finalStep, 0);
    }

    public Ride(int startRow, int startCol, int endRow, int endCol, int startStep, int finalStep, int id) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.startStep = startStep;
        this.finalStep = finalStep;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("ride from [%d, %d] to [%d, %d], earliest start %d, latest finish %d\n",
                startRow, startCol, endRow, endCol, startStep, finalStep);
    }
}
