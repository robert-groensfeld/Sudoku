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

package generator;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a cell in an unsolved Latin square.
 * @author robert
 *
 */
class LatinSquareCell {

	private int row, col;
	private int value = -1; // means that this cell does not contain a value
	private List<Integer> allowedValues;
	
	protected LatinSquareCell(int row, int col, int squareSize) {
		this.row = row;
		this.col = col;
		initAllowedValues(squareSize - 1);
	}

	private void initAllowedValues(int maxValue) {
		allowedValues = new LinkedList<Integer>();
		for(int val = 0; val <= maxValue; val++)
			allowedValues.add(val);
	}
	
	protected int getRow() {
		return row;
	}
	
	protected int getCol() {
		return col;
	}
	
	protected int getValue() {
		return value;
	}
	
	protected List<Integer> getAllowedValues() {
		return allowedValues;
	}
	
	protected boolean isEmpty() {
		return value == -1;
	}
	
	protected void assign(int value) {
		this.value = value;
	}
	
	protected void clear() {
		this.value = -1;
	}
	
	protected boolean forbid(int value) {
		return this.allowedValues.remove(new Integer(value));
	}
	
	protected void allow(int value) {
		this.allowedValues.add(value);
	}
}
