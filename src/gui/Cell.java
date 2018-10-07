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

package gui;

import javax.swing.JTextField;

/**
 * Represents a cell on a standard 9x9 Sudoku board. Cells can contain values
 * from 1 to 9. A cell is either editable or occupied with a given value.
 * @author robert
 */
@SuppressWarnings("serial")
class Cell extends JTextField {

	protected final int row, col;
	private boolean occupied;
	
	/** 
	 * Creates an empty, editable cell.
	 * @param row The row of this cell.
	 * @param column The column of this cell.
	 */
	protected Cell(int row, int column) {
		this.row = row;
		this.col = column;
		this.occupied = false;
		
		// Override the standard input behavior.
		this.setEditable(false);
	}
	
	/**
	 * Creates a non-editable puzzle cell with a given value.
	 * @param row The row of this cell.
	 * @param column The column of this cell.
	 * @param value The given value.
	 * @throws Exception The given value is not a number between 1 and 9.
	 */
	protected Cell(int row, int column, int value) throws Exception {
		this(row, column);
		this.occupy(value);
	}

	/**
	 * Determines whether the cell contains a puzzle value or is editable.
	 * @return {@code true} for puzzle cells, {@code false} for editable cells.
	 */
	public boolean isOccupied() {
		return this.occupied;
	}

	/**
	 * Occupies this cell with a value belonging to the Sudoku puzzle.
	 * @param value A value from between 1 and 9.
	 * @throws Exception The given value is invalid.
	 */
	public void occupy(int value) throws Exception {
		this.setValue(value);
		this.occupied = true;
	}

	/** Makes the cell editable. */
	public void makeEditable() {
		this.occupied = false;
	}
	
	/**
	 * Returns the value of this cell.
	 * @return The value of the cell or 0 if the cell is empty.
	 */
	public int getValue() {
		if (this.isEmpty())
			return 0;
		else
			return Integer.parseInt(this.getText());
	}

	/**
	 * Determines if this cell contains a value or is empty.
	 * @return {@code true} for an empty cell, otherwise {@code false}
	 */
	public boolean isEmpty() {
		return this.getText().equals("");
	}
	
	/**
	 * Alters the value of this cell.
	 * @param value A number  between 1 and 9.
	 * @throws Exception This cell is occupied and its value can not be changed.
 	 * @throws Exception The given value is invalid.
	 */
	public void setValue(int value) throws Exception {
		if (this.isOccupied())
			throw new Exception("Value of puzzle cell " + this + " immutable.");
		else if (1 <= value && value <= 9)
			this.setText(Integer.toString(value));
		else
			throw new Exception(value + " invalid value for cell " + this);
	}

	/** Removes the value of this cell. */
	public void clear() {
		this.setText("");
		this.occupied = false;
	}

	@Override
	public String toString() {
		return "{row: " + row + ", col: " + col + "}";
	}
}
