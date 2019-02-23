public class Ride {
	public int startRow;
	public int startCol;
	public int endRow;
	public int endCol;
	public int startStep;
	public int finalStep;

	public Ride(int startRow, int startCol, int endRow, int endCol, int startStep, int finalStep) {
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		this.startStep = startStep;
		this.finalStep = finalStep;
	}
}
