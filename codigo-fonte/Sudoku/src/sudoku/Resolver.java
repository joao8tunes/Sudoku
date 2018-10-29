package sudoku;

import java.util.ArrayList;

public class Resolver 
{

	public static int MATRIX_DIMENSION = 9;
	public static int SUBSET_DIMENSION = MATRIX_DIMENSION/3;
	public boolean MVR;
	public boolean FC;
	protected long maxTime;
	protected int maxAssignments;
	protected long startTime;

	public float getMaxTime() 
	{
		return maxTime;
	}

	public void setMaxTime(long maxTime) 
	{
		this.maxTime = maxTime;
	}

	public int getMaxAssignments() 
	{
		return maxAssignments;
	}

	public void setMaxAssignments(int maxAssignments) 
	{
		this.maxAssignments = maxAssignments;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isMVR() 
	{
		return MVR;
	}

	public void setMVR(boolean mVR) 
	{
		MVR = mVR;
	}

	public boolean isFC() 
	{
		return FC;
	}

	public void setFC(boolean fC) 
	{
		FC = fC;
	}
	
	public ArrayList<Result> resolveAll(ArrayList<int[][]> problems, long maxTime, int maxAssignments)
	{
		ArrayList<Result> results = new ArrayList<Result>();
		
		setMaxTime(maxTime);
		setMaxAssignments(maxAssignments);
		
		for (int[][] problem : problems) {    //Resolving all the problems from the input.
			results.add(new Result());
			results.get(results.size()-1).setProblem(problem);
			results.get(results.size()-1).setResult(problem);    //Will be modified during the process.
			setStartTime(System.currentTimeMillis());
			resolve(results.get(results.size()-1));    //Resolving the last problem added.
		}
		
		return results;
	}
	
	private boolean resolve(Result result)
	{	
		int[] holeLocation = null;
		
		//Selecting the hole cell of accord with the algorithm:
		if (isMVR()) holeLocation = getHoleLocationMVR(result);
		else holeLocation = getHoleLocation(result);
		
		if (exceedStopCriteria(result)) return true;
		
		//If doesn't have any hole cell, and implicitaly the result is valid, the problem is solved.
		if (holeLocation == null) {
			result.setSolved(true);
			return true;
		}
		
		//Resolving the suboku's problem:
		for (int number = 1; number <= MATRIX_DIMENSION; ++number) {    //Try to identify a valid value to a hole cell.
			if (allDiff(result, holeLocation[0], holeLocation[1], number)) {    //If the identified value do not cause inconsistence, then assign this value to the hole cell
				result.getResult()[holeLocation[0]][holeLocation[1]] = number;
				if (isFC() || isMVR()) updateVR(result);    //Updating the table of remaining values.
					
				if (isFC() && needBacktrack(result)) {    //Avoiding unnecessary calls.
					result.getResult()[holeLocation[0]][holeLocation[1]] = 0;    //If the current solution is invalid, undo and try again with other value.
					updateVR(result);    //Updating the table of remaining values.
					continue;    //Triggering the backtracking.
				}
				
				if (resolve(result)) return true;    //If find a valid solution, end the process.
				
				result.getResult()[holeLocation[0]][holeLocation[1]] = 0;    //If the current solution is invalid, undo and try again with other value.
				if (isFC() || isMVR()) updateVR(result);    //Updating the table of remaining values.
			}
		}
		
		return false;    //Triggering the backtracking.
	}
	
	private boolean exceedStopCriteria(Result result)
	{
		//Controlling the criterias of stop:
		result.setTime(System.currentTimeMillis()-getStartTime());    //Updating the time.
		result.setAssignments(result.getAssignments()+1);    //Updating the assignments.
				
		if (getMaxTime() > 0 && result.getTime() > getMaxTime()) return true;
		if (getMaxAssignments() > 0 && result.getAssignments() > getMaxAssignments()) return true;
		return false;
	}
	
	public int[] getHoleLocation(Result result)    //Verify if has one cell with a hole, i.e., a '0'.
	{
		int[] holeLocation = new int[2];    //'holeLine' and 'holeColumn', respectively.
		
		for (holeLocation[0] = 0; holeLocation[0] < MATRIX_DIMENSION; ++holeLocation[0]) {
			for (holeLocation[1] = 0; holeLocation[1] < MATRIX_DIMENSION; ++holeLocation[1]) {
				if (result.getResult()[holeLocation[0]][holeLocation[1]] == 0) return holeLocation;
			}
		}
		
		return null;
	}
	
	private int[] getHoleLocationMVR(Result result)
	{
		int mVR = Integer.MAX_VALUE;
		int[] holeLocation = new int[2];    //'holeLine' and 'holeColumn', respectively.
		
		holeLocation[0] = -1;
		
		for (int i = 0; i < MATRIX_DIMENSION; ++i) {
			for (int j = 0; j < MATRIX_DIMENSION; ++j) {
				if (result.getResult()[i][j] == 0 && result.getRemainingValues()[i][j] < mVR) {
					mVR = result.getRemainingValues()[i][j];
					holeLocation[0] = i;
					holeLocation[1] = j;
				}
			}
		}
		
		if (holeLocation[0] == -1) return null;
		return holeLocation;
	}	

	private boolean needBacktrack(Result result)
	{
		for (int i = 0; i < MATRIX_DIMENSION; ++i) {
			for (int j = 0; j < MATRIX_DIMENSION; ++j) {
				if (result.getResult()[i][j] == 0 && result.getRemainingValues()[i][j] == 0) {
					return true;
				}
			}
		}
		
		return false;
	}	
	
	private void updateVR(Result result)
	{
		for (int i = 0; i < MATRIX_DIMENSION; ++i) {
			for (int j = 0; j < MATRIX_DIMENSION; ++j) {
				result.getRemainingValues()[i][j] = getNumberVR(result, i, j);
			}
		}
	}
	
	private int getNumberVR(Result result, int line, int column) 
	{
		int vr = 0;
		
		if (result.getResult()[line][column] == 0) {    //Verifying if the cell is clear.
			for (int validNumber = 1; validNumber <= MATRIX_DIMENSION; ++validNumber) {
				if (allDiff(result, line, column, validNumber)) ++vr;				
			}
		}
		
		return vr;
	}
	
	private boolean allDiff(Result result, int line, int column, int number)    //Check if the current instance is a valid solution ('all different').
	{	
		//Verifying if all the LINES has different numbers:
		for (int i = 0; i < MATRIX_DIMENSION; ++i) {
			if (result.getResult()[line][i] == number) return false;
		}

		//Verifying if all the COLUMNS has different numbers:
		for (int i = 0; i < MATRIX_DIMENSION; ++i) {
			if (result.getResult()[i][column] == number) return false;
		}
		
		int lr = line/SUBSET_DIMENSION, cr = column/SUBSET_DIMENSION;
		
		//Verifying if all the SUBSETS has different numbers:
		for (int l = lr*SUBSET_DIMENSION; l < (lr+1)*SUBSET_DIMENSION; ++l) {
			for (int c = cr*SUBSET_DIMENSION; c < (cr+1)*SUBSET_DIMENSION; ++c) {
				if (result.getResult()[l][c] == number) return false;
			}
		}
		
		return true;
	}

}
