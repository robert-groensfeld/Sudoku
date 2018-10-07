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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.lang.model.util.ElementScanner6;

/**
 * Factory class to create cells of a standard 9x9 Sudoku board. Provides
 * methods to customize the look of cells and attaches listeners to them.
 * @author robert
 *
 */
class CellFactory {

	/** Styles for editable, fixed and focused cells. */
	CellStyle editable, fixed, focused;

	/** Creates a cell factory producing cells with the standard look. */
	protected CellFactory() {
		CellStyle editable = new CellStyle();
		editable.setFontColor(Color.GRAY);

		CellStyle fixed = new CellStyle();

		CellStyle focused = new CellStyle();
		focused.setBackgroundColor(Color.LIGHT_GRAY);
		focused.setFontColor(Color.GRAY);

		init(editable, fixed, focused);
	}
	
	/**
	 * Creates a cell factory producing cells with the given styles.
	 * @param editable Style of an initially blank, user editable cell.
	 * @param fixed Style of a cell with a number belonging to the puzzle. 
	 * @param focused Style of a cell that is focused by the user.
	 */
	protected CellFactory(CellStyle editable, 
						  CellStyle fixed, 
						  CellStyle focused) {
		init(editable, fixed, focused);
	}

	private void init(CellStyle editable, CellStyle fixed, CellStyle focused) {
		this.editable = editable;
		this.fixed = fixed;
		this.focused = focused;
	}

	/**
	 * Creates a new editable cell. 
	 * @param row The row of the cell.
	 * @param column The column of the cell.
	 * @return An empty, editable cell.
	 */
	protected Cell createCell(int row, int column) {
		Cell cell = new Cell(row, column);
		getStyle(cell).apply(cell);
		addListeners(cell);
		return cell;
	}
	
	/**
	 * Creates a new non editable cell containing a given value.
	 * @param row The row of this cell.
	 * @param column The column of this cell.
	 * @param value The given value. Has to be a value within the range 1..9.
	 * @throws Exception The puzzle cell could not be created.
	 * @return A cell that contains the given value and is not editable.
	 */
	protected Cell createCell(int row, int column, int value) throws Exception {
		try {
			Cell cell = new Cell(row, column, value);
			getStyle(cell).apply(cell);
			addListeners(cell); 
			return cell;
		} catch (Exception cause) {
			String message = "Unable to create puzzle cell";
			throw new Exception(message, cause);
		}
	}
	
	private void addListeners(Cell cell) {
		cell.addFocusListener(new CellFocusListener(focused, editable));
		cell.addKeyListener(new CellKeyListener());
	}

	protected CellStyle getStyle(Cell cell) {
		if (cell.isOccupied())
			return fixed;
		if (cell.isFocusOwner())
			return focused;
		return editable;
	}
}
