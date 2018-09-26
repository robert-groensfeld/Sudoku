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

package rating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import solver.SudokuProblem;

/**
 * Represents a list of empty cells in a Sudoku instance. Provides functionality
 * to find naked singles and hidden singles.
 * @author robert
 */
class BlankCellList {

	private int[][] solution; 	// needed to fill cells
	
	// cells that can be filled using the naked or hidden single technique.
	private Stack<BlankCell> singles = new Stack<BlankCell>();
	// cells that can only be filled using a more advanced technique.
	private List<BlankCell> advanced = new ArrayList<BlankCell>(64);
	
	/**
	 * Generates a list of all blank cells in a given Sudoku puzzle.
	 * @param puzzle A 9x9 integer array representing a Sudoku puzzle.
	 */
	protected BlankCellList(int[][] puzzle) {
		SudokuProblem p = new SudokuProblem(puzzle);
		this.init(puzzle, p.getSolution());
	}
	
	/**
	 * Generates a list of all blank cells in a given Sudoku puzzle.
	 * @param puzzle A 9x9 integer array representing a Sudoku puzzle.
	 * @param solution The 9x9 integer array representing the solution to the
	 * given puzzle.
	 */
	protected BlankCellList(int[][] puzzle, int[][] solution) {
		this.init(puzzle, solution);
	}
	
	private void init(int[][] puzzle, int[][] solution) {
		this.solution = solution;
		findBlankCells(puzzle);
		initForbiddenValues(puzzle);
		assignNeighborCells();
		// shuffle cells for a random ordering of naked and hidden singles.
		Collections.shuffle(advanced);
		spotSingles();
	}

	private void findBlankCells(int[][] instance) {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(instance[row][col] == 0)
					advanced.add(new BlankCell(row, col, solution[row][col]));
	}
	
	private void initForbiddenValues(int[][] instance) {
		for(BlankCell cell : advanced) {
			for(int col = 0; col < 9; col++)
				if(instance[cell.row][col] != 0)
					cell.forbid(instance[cell.row][col]);
			
			for(int row = 0; row < 9; row++)
				if(instance[row][cell.column] != 0)
					cell.forbid(instance[row][cell.column]);
			
			int blockStartRow = (cell.row / 3) * 3;
			int blockStartCol = (cell.column / 3) * 3;
			for(int row = blockStartRow; row < blockStartRow + 3; row++)
				for(int col = blockStartCol; col < blockStartCol + 3; col++) {
					if(instance[row][col] != 0)
						cell.forbid(instance[row][col]);
				}
		}
	}

	private void assignNeighborCells() {
		for(BlankCell cell : advanced)
			for(BlankCell otherCell : advanced)
				if(cell != otherCell)
					if(cell.row == otherCell.row 
					|| cell.column == otherCell.column
					|| cell.block == otherCell.block)
						cell.assignNeighbor(otherCell);
	}
	
	private void spotSingles() {
		// check if some of the advanced cells can be filled
		// using naked or hidden singles
		int advancedCellCount = advanced.size();
		for(int i = 0; i < advancedCellCount; i++) {
			BlankCell first = advanced.remove(0);
			if(first.hasNakedSingle() || first.hasHiddenSingle())
				singles.push(first);
			else
				advanced.add(first);
		}
	}
	
	/**
	 * Checks if this list of blank cells is empty. 
	 * @return {@code true} when there are no more blank cells, {@code false}
	 * otherwise.
	 */
	protected boolean filled() {
		return (singles.size() + advanced.size()) == 0;
	}
	
	/**
	 * Call this method to determine whether there is a cell which can be
	 * filled using the naked single or hidden single technique.
	 * @return {@code true} if there is at least one cell left where one of
	 * the above mentioned techniques is applicable. {@code false} if a more
	 * advanced technique has to be used to fill any cell in this list.
	 */
	protected boolean hasSingleCell() {
		return singles.size() > 0;
	}
	
	/**
	 * Call this method to fill an advanced cell with its right value.
	 * @param cell A cell in this list.
	 */
	protected void fill(BlankCell cell) {
		fill(cell, cell.solution);
	}
	
	/**
	 * Call this method to fill an advanced cell with a specific value.
	 * @param cell A cell in this list.
	 * @param value A value from 1 to 9.
	 */
	protected void fill(BlankCell cell, int value) {
		int i = advanced.indexOf(cell);
		advanced.get(i).fill(value);
		advanced.remove(i);
		spotSingles();
	}
	
	/**
	 * Call this method to fill a cell where the naked or hidden single
	 * technique can be applied.
	 * @throws EmptyStackException When there are no cells where one of the
	 * above mentioned techniques are applicable.
	 */
	protected void fillSingleCell() {
		BlankCell cell = singles.pop();
		cell.fill(solution[cell.row][cell.column]);
		spotSingles();
	}
	
	/**
	 * Checks if the underlying Sudoku instance is solvable. The instance is
	 * solvable as long as there is at least one candidate for each cell.
	 * @return {@code true} if the instance is solvable, {@code false} if it
	 * isn't.
	 */
	protected boolean isConsistent() {
		for(BlankCell cell : advanced)
			if(!cell.canBeFilled())
				return false;
		for(BlankCell cell : singles)
			if(!cell.canBeFilled())
				return false;
		return true;
	}
	
	protected int getSinglePossibilities() {
		return singles.size();
	}
	
	protected List<BlankCell> getAdvancedCells() {
		return advanced;
	}
}
