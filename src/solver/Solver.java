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
 * Solves standard 9x9 Sudoku instances.
 * @author robert
 *
 */
public class Solver {
	
	private static int[][] solution = new int[9][9];

	/**
	 * Searches for a solution of the provided Sudoku instance.
	 * @param sudoku A 9x9 integer array containing values from 0 to 9, 0
	 * meaning that the cell at the corresponding position is empty.
	 * @return {@code true} if a solution to the provided instance could be
	 * found, {@code false} if there is no solution for the provided instance.
	 */
	public static boolean findSolution(int[][] sudoku) {
		// first fill the solution with the given values
		solution = sudoku;
		// then backtrack over the blank cells to obtain the complete solution
		BlankCellList blankCells = new BlankCellList(sudoku);
		return backtrack(blankCells);
	}

	private static boolean backtrack(BlankCellList cells) {
		if(cells.isEmpty()) // value assigned to every cell. solution found
			return true;
		
		// assign a value to the most constrained cell and delete it from the
		// list of valid values in the neighboring cells. then backtrack over
		// the resulting list of cells.
		
		BlankCell currentCell = cells.removeMostConstrainedCell();
		List<Integer> validValues = currentCell.getValidValues();
		List<BlankCell> neighbors = cells.getNeighbors(currentCell);
		
		for(int value : validValues) {
			// remove the assigned value from the neighboring cells and keep
			// track of the cells that were affected by the removal
			List<BlankCell> affectedNeighbors = new LinkedList<BlankCell>();
			for(BlankCell neighbor : neighbors) {
				boolean neighborAffected = neighbor.invalidateValue(value);
				if(neighborAffected)
					affectedNeighbors.add(neighbor);
			}
			
			if(backtrack(cells) == true) { // solution found
				// fill in the value that led to a solution
				int row = currentCell.getRow();
				int col = currentCell.getCol();
				solution[row][col] = value;
				return true; // propagate that a solution was found
			}
			else { // no solution found with the current value
				for(BlankCell affectedNeighbor : affectedNeighbors)
					// before trying the next value, reinsert the value that
					// was invalidated in the (affected) neighboring cells
					affectedNeighbor.addValidValue(value);
			}
		}
		
		// no solution could be found for any of the valid values. So remove
		// any assignments from this cell and reinsert it into the list of
		// unassigned cells.
		cells.add(currentCell);
		return false;
	}
	
	/**
	 * Call this method to obtain the last solution that 
	 * {@link Solver#findSolution(int[][])} has found. 
	 * @return A 9x9 integer array filled with values from 1 to 9. If 
	 * {@link Solver#findSolution(int[][]) never has been called, an array
	 * filled with 0's will be returned.
	 */
	public static int[][] getSolution() {
		return solution;
	}
}