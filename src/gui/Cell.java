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
 *
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
		
		// The standard input behavior is overridden by CellListener, which
		// implements a key listener.
		this.setEditable(false);
	}
	
	/**
	 * Creates a non-editable cell with a given value.
	 * @param row The row of this cell.
	 * @param column The column of this cell.
	 * @param value The given value.
	 * @throws Exception Throws an exception when the given value does not lie
	 * in the range of 1 to 9.
	 */
	protected Cell(int row, int column, int value) throws Exception {
		this.row = row;
		this.col = column;
		this.occupied = true;
		if(value >= 1 && value <= 9)
			this.setText(Integer.toString(value));
		else
			throw new Exception("Only values from 1 to 9 allowed as given");
		
		// The standard input behavior is overridden by CellListener, which
		// implements a key listener.
		this.setEditable(false);
	}

	/**
	 * Use this method to determine whether this cell is occupied with a given
	 * value or editable.
	 * @return {@code true} if the cell contains a given value and 
	 * {@code false} if the cell is editable.
	 */
	public boolean isOccupied() {
		return occupied;
	}
	
	/**
	 * Returns the value of this cell.
	 * @return {@code 0}, if the cell does not contain a value.
	 */
	public int getValue() {
		String text = this.getText();
		if(text.equals(""))
			return 0;
		else
			return Integer.parseInt(text);
	}
	
	/**
	 * Alters the value of this cell.
	 * @param value The new value of this cell. The cell will become empty, if
	 * {@code 0} is provided. 
	 */
	public void setValue(int value) {
		if(value == 0)
			this.setText("");
		else {
			String text = Integer.toString(value);
			this.setText(text);
		}
	}
}
