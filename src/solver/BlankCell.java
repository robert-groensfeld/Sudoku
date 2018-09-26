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
 * Represents a blank cell in a standard 9x9 Sudoku game. Blank cells are 
 * variable and can take any value from 1 to 9.
 * @author robert
 *
 */
class BlankCell {

	private int row;
	private int col;
	private int block;
	
	// validationTable[v - 1] is false when value v is valid for this cell
	private boolean[] validationTable = new boolean[9];
	private int validValueCount = 0;
	
	/**
	 * A blank cell is identified by its row, column and the block it resides
	 * in.
	 * @param row The row of the cell in the corresponding Sudoku grid.
	 * @param col The column of the cell in the corresponding Sudoku grid.
	 * @param block The Block of the cell in the corresponding Sudoku grid.
	 * Blocks are numbered from 0 to 8, counting from left to right and top to
	 * bottom.
	 */
	protected BlankCell(int row, int col, int block) {
		this.row = row;
		this.col = col;
		this.block = block;
	}
	
	protected int getRow() {
		return row;
	}
	
	protected int getCol() {
		return col;
	}
	
	protected int getBlock() {
		return block;
	}
	
	/**
	 * Gets a list of valid values for this cell. Initially every value from 1
	 * to 9 is a valid value. The list of valid values can be altered via
	 * {@link BlankCell#addValidValue(int)} and 
	 * {@link BlankCell#invalidateValue(int)}.
	 * @return Returns a {@link List} containing all valid values for this
	 * cell.
	 */
	protected List<Integer> getValidValues() {
		List<Integer> validValues = new LinkedList<Integer>();
		for(int value = 1; value <= 9; value++)
			if(!validationTable[value - 1])
				validValues.add(value);
		return validValues;
	}
	
	/**
	 * Add a valid value from 1 to 9 to the list of valid values. Valid values
	 * should be those that don't already occur in the same row, column or
	 * block.
	 * @param value A value from 1 to 9.
	 */
	protected void addValidValue(int value) {
		validationTable[value - 1] = false;
		validValueCount++;
	}
	
	/**
	 * Removes a value from the list of valid values. This method should be 
	 * called if another cell in the same row, column or block is filled with
	 * a value and that value thus becomes invalid for this cell.
	 * @param value A value from 1 to 9.
	 * @return Returns {@code true} if the list of valid values contained the
	 * provided value and thus was changed and {@code false} otherwise.
	 */
	protected boolean invalidateValue(int value) {
		if(validationTable[value - 1]) // value already invalid
			return false;
		validationTable[value - 1] = true;
		validValueCount--;
		return true;
	}
	
	/**
	 * Determines the number of valid values for this cell.
	 * @return The length of the list of valid values.
	 */
	protected int getValidValueCount() {
		return validValueCount;
	}

	@Override
	public String toString() {
		List<Integer> validValues = new LinkedList<Integer>();
		for(int value = 1; value <= 9; value++)
			if(!validationTable[value - 1])
				validValues.add(value);
		return "(" + row + ", " 
					+ col + ", " 
					+ block + "): " 
					+ validValues.toString();
	}
}
