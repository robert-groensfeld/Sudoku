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

/**
 * Factory class to create cells of a standard 9x9 Sudoku board. Provides
 * methods to customize the look of cells and attaches listeners to them.
 * @author robert
 *
 */
class CellFactory {
	
	// STANDARD CELL LOOK /////////////////////////////////////////////////////

	private int size = 40;
	private final int minSize = 20;
	
	private Color notFocused = Color.WHITE;
	private Color focused = Color.LIGHT_GRAY;
	
	private Font font = new Font("Monospaced", Font.PLAIN, size);
	private Color fontColor = Color.BLACK;
	
	///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates a new editable cell. 
	 * @param row The row of the cell.
	 * @param column The column of the cell.
	 * @return An empty, editable cell.
	 */
	protected Cell createCell(int row, int column) {
		Cell cell = new Cell(row, column);
		decorateCell(cell);
		return cell;
	}
	
	/**
	 * Creates a new non editable cell containing a given value.
	 * @param row The row of this cell.
	 * @param column The column of this cell.
	 * @param value The given value. Has to be a value within the range 1..9.
	 * @return A cell that contains the given value and is not editable.
	 */
	protected Cell createCell(int row, int column, int value) {
		try {
			Cell cell = new Cell(row, column, value);
			decorateCell(cell);
			return cell;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		// GOD FORBID THIS LINE IS EVER REACHED
		return new Cell(row, column);
	}
	
	private void decorateCell(Cell cell) {
		Dimension cellSize = new Dimension(size, size);
		cell.setPreferredSize(cellSize);
		Dimension minimalSize = new Dimension(minSize, minSize);
		cell.setMinimumSize(minimalSize);
		
		cell.setBackground(notFocused);
		cell.setFont(font);
		cell.setHorizontalAlignment(Cell.CENTER);
		cell.setForeground(fontColor);
		
		CellListener listener = new CellListener(focused, notFocused);
		cell.addFocusListener(listener);
		cell.addKeyListener(listener);
	}
	
	
	/**
	 * Change the preferred size of the cells.
	 * @param newSize The new size. It has to be greater than the minimal cell
	 * size determined by this factory (20).
	 */
	public void setSize(int newSize) {
		if(newSize >= minSize)
			size = newSize;
	}
	
	/**
	 * Select a new background color for the cells that are to be created. 
	 * @param bgColor The new background color.
	 */
	protected void setBgColor(Color bgColor) {
		notFocused = bgColor;
	}
	
	/**
	 * The background color of a cell changes if it gains focus. Use this
	 * method to determine the background color under focus.
	 * @param focusColor The background color of a focused cell.
	 */
	protected void setFocusColor(Color focusColor) {
		focused = focusColor;
	}
	
	/**
	 * Change the font of the cells that are to be created.
	 * @param newFont The new font. The point size has to be smaller than the
	 * cell size.
	 */
	protected void setFont(Font newFont) {
		if(newFont.getSize() <= size)
			font = newFont;
	}
	
	/**
	 * Select a new font color.
	 * @param newColor The new font color.
	 */
	protected void setFontColor(Color newColor) {
		fontColor = newColor;
	}
}
