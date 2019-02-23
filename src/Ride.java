public class Ride {
    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;
    public int startStep;
    public int finalStep;
	public int index;
	public boolean used;

    public Ride(int startRow, int startCol, int endRow, int endCol, int startStep, int finalStep, int index) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.startStep = startStep;
        this.finalStep = finalStep;
        this.index = index;
    }

    @Override
    public String toString() {
        return String.format("ride with id %d from [%d, %d] to [%d, %d], earliest start %d, latest finish %d\n",
                index, startRow, startCol, endRow, endCol, startStep, finalStep);
    }
}
