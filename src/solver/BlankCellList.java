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
 * A list that holds blank cells of a standard 9x9 Sudoku grid.
 * @author robert
 *
 */
class BlankCellList {

	private List<BlankCell> blankCells = new LinkedList<BlankCell>();
	
	/**
	 * Creates a list of the blank cells in the provided sudoku grid.
	 * @param sudoku A 9x9 Sudoku grid with values from 0 to 9, 0 meaning
	 * that the cell at the corresponding position is empty (and thus will
	 * be part of this list).
	 */
	protected BlankCellList(int[][] sudoku) {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(sudoku[row][col] == 0) { // blank cell
					int block = getBlock(row, col);
					blankCells.add(new BlankCell(row, col, block));
				}
		initValidValues(sudoku);
	}
	
	private int getBlock(int row, int col) {
		int x = (int) (col / 3);
		int y = (int) (row / 3);
		return x + y * 3;
	}
	
	private void initValidValues(int[][] sudoku) {
		// Go through each initially filled cell in the Sudoku grid and check
		// for all blank cells if they are in the same row, column or block. If 
		// that is the case, delete the value of the filled cell from the list 
		// of valid values for the blank cell.
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(sudoku[row][col] != 0) { // filled cell
					for(BlankCell c : blankCells) {
						boolean inSameRow = row == c.getRow();
						boolean inSameCol = col == c.getCol();
						boolean inSameBlock = getBlock(row, col) == c.getBlock();
						if(inSameRow || inSameCol || inSameBlock)
							c.invalidateValue(sudoku[row][col]);
					}
				}
	}
	
	protected boolean isEmpty() {
		return blankCells.isEmpty();
	}
	
	/**
	 * Removes and returns the cell with the least valid values of the list
	 * of blank cells. Make sure that this list is not empty before you call
	 * this method.
	 * @return The cell with the least valid values.
	 */
	protected BlankCell removeMostConstrainedCell() {
		BlankCell mostConstrained = blankCells.get(0);
		for(BlankCell c : blankCells) {
			if(c.getValidValueCount() < mostConstrained.getValidValueCount())
				mostConstrained = c;
		}
		blankCells.remove(mostConstrained);
		return mostConstrained;
	}
	
	/**
	 * Gets the neighbors of a given cell. Each cell that resides in the same
	 * column, row or block as the provided cell is considered as neighbor. The
	 * provided cell doesn't have to be part of this cell list.
	 * @param cell An arbitrary blank cell.
	 * @return Every neighbor of the provided cell that is part of this list.
	 */
	protected List<BlankCell> getNeighbors(BlankCell cell) {
		List<BlankCell> neighbours = new LinkedList<BlankCell>();
		for(BlankCell c : blankCells) {
			boolean inSameRow = c.getRow() == cell.getRow();
			boolean inSameCol = c.getCol() == cell.getCol();
			boolean inSameBlock = c.getBlock() == cell.getBlock();
			if(inSameRow || inSameCol || inSameBlock)
				neighbours.add(c);
		}
		return neighbours;
	}
	
	protected void add(BlankCell cell) {
		blankCells.add(cell);
	}
}