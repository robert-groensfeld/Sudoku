/* SudokuTools - Tools for generating, solving and rating Sudoku puzzles.
 * Copyright (C) 2014 Robert Grönsfeld
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package solver;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Sudoku problem.
 * @author robert
 *
 */
public class SudokuProblem {

	private int solutionCount;
	private int[][] problem;
	private int[][] solution;
	
	/**
	 * Creates a new Sudoku problem for an unsolved Sudoku instance.
	 * @param sudoku The unsolved instance as 9x9 integer array with values
	 * from 0 to 9, 0 indicating that the cell at the corresponding position
	 * is blank.
	 */
	public SudokuProblem(int[][] sudoku) {
		solutionCount = 0;
		problem = copy(sudoku);
		solution = problem;
		BlankCellList blankCells = new BlankCellList(problem);
		this.backtrack(blankCells);
	}
	
	private int[][] copy(int[][] sudoku) {
		int[][] copy = new int[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				copy[row][col] = sudoku[row][col];
		return copy;
	}

	private void backtrack(BlankCellList blankCells) {
		if(blankCells.isEmpty()) { // value assigned to every cell. solution found
			solutionCount++;
			return;
		}
		
		// assign a value to the most constrained cell and delete it from the
		// list of valid values in the neighboring cells. then backtrack over
		// the resulting list of cells.
		
		BlankCell currentCell = blankCells.removeMostConstrainedCell();
		List<Integer> validValues = currentCell.getValidValues();
		List<BlankCell> neighbors = blankCells.getNeighbors(currentCell);
		
		for(int value : validValues) {
			
			// remove the assigned value from the neighboring cells and keep
			// track of the cells that were affected by the removal
			List<BlankCell> affectedNeighbors = new LinkedList<BlankCell>();
			for(BlankCell neighbor : neighbors) {
				boolean neighborAffected = neighbor.invalidateValue(value);
				if(neighborAffected)
					affectedNeighbors.add(neighbor);
			}
			
			int lastSolutionCount = solutionCount;
			backtrack(blankCells);
			boolean newSolutionFound = solutionCount > lastSolutionCount;
			boolean firstSolutionFound = newSolutionFound && lastSolutionCount == 0;
			if(firstSolutionFound) {
				// fill in the value that led to the first solution
				int row = currentCell.getRow();
				int col = currentCell.getCol();
				solution[row][col] = value;
			}
			//if(solutionCount > 1)
			//	return; // we don't need to explore the rest of the solutions
			
			for(BlankCell affectedNeighbor : affectedNeighbors)
				// before trying the next value, reinsert the value that
				// was invalidated in the (affected) neighboring cells
				affectedNeighbor.addValidValue(value);
		}
		
		// no solution could be found for any of the valid values. So remove
		// any assignments from this cell and reinsert it into the list of
		// unassigned cells.
		blankCells.add(currentCell);
	}
	
	public boolean hasSolution() {
		return solutionCount > 0;
	}
	
	public boolean hasUniqueSolution() {
		return solutionCount == 1;
	}
	
	public int[][] getSolution() {
		return solution;
	}
	
	public int[][] toIntArray() {
		return problem;
	}
	
	public int getNumberOfSolutions() {
		return solutionCount;
	}
}
