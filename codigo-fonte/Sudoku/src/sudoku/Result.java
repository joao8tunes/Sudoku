package sudoku;

public class Result 
{

	private int[][] problem;
	private int[][] result;
	private int[][] remainingValues;
	private long time;
	private int assignments;
	private boolean solved;
	
	public Result()
	{
		problem = new int[Resolver.MATRIX_DIMENSION][Resolver.MATRIX_DIMENSION];
		result = new int[Resolver.MATRIX_DIMENSION][Resolver.MATRIX_DIMENSION];
		remainingValues = new int[Resolver.MATRIX_DIMENSION][Resolver.MATRIX_DIMENSION];
		setTime(0);
		setAssignments(0);
		setSolved(false);
	}
	
	public int[][] getProblem()
	{
		return problem;
	}
	
	public void setProblem(int[][] problem) 
	{
		for (int i = 0; i < Resolver.MATRIX_DIMENSION; ++i) {
			for (int j = 0; j < Resolver.MATRIX_DIMENSION; ++j) {
				this.problem[i][j] = problem[i][j];
			}
		}
	}
	
	public float getTime() 
	{
		return time;
	}
	
	public void setTime(long time) 
	{
		this.time = time;
	}
	
	public int getAssignments() 
	{
		return assignments;
	}
	
	public void setAssignments(int assignments) 
	{
		this.assignments = assignments;
	}

	public int[][] getResult() 
	{
		return result;
	}

	public void setResult(int[][] result) 
	{
		for (int i = 0; i < Resolver.MATRIX_DIMENSION; ++i) {
			for (int j = 0; j < Resolver.MATRIX_DIMENSION; ++j) {
				this.result[i][j] = result[i][j];
			}
		}
	}

	public boolean isSolved() 
	{
		return solved;
	}

	public void setSolved(boolean solved) 
	{
		this.solved = solved;
	}
	
	public int[][] getRemainingValues()
	{
		return remainingValues;
	}	
}
