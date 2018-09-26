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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 * A typical 9x9 Sudoku board that contains 81 cells grouped in nine 3x3 blocks.
 * @author robert
 *
 */
@SuppressWarnings("serial")
public class Board extends JPanel {

	protected Cell[][] cells;
	private Color borderColor = Color.BLACK; // Default color is black.
	// Border 'strength'
	final int thinborder = 1;
	final int thickborder = 3;
	
	/** Creates a completely empty board with default look. */
	public Board() {
		initCells(new CellFactory());
		addCells();
		drawBorders();
	}
	
	/**
	 * Creates a board with given values using the default look.
	 * @param givenValues A two dimensional array with given values. The array
	 * should be a 9x9 grid with values in the range of 0..9, 0 meaning that
	 * at this point of the grid no value is given. The first index of the array
	 * denotes the row, the second index the column of its value.
	 */
	public Board(int[][] givenValues) {
		CellFactory freeCellFactory = new CellFactory();
		freeCellFactory.setFontColor(Color.GRAY);
		CellFactory givenCellFactory = new CellFactory();
		givenCellFactory.setFontColor(Color.BLACK);
		
		initCells(givenValues, freeCellFactory, givenCellFactory);
		addCells();
		drawBorders();
	}

	/**
	 * Create a customized empty board. The appearance of the cells is
	 * determined by the provided cell factory.
	 * @param factory A cell factory that determines the appearance of the
	 * cells.
	 * @param borderColor The color of the border around the cells and between
	 * blocks.
	 */
	public Board(CellFactory factory, Color borderColor) {
		this.borderColor = borderColor;
		initCells(factory);
		addCells();
		drawBorders();
	}
	
	/**
	 * Create a board with given values using a custom look.
	 * @param givenValues The given values. For a more detailed description
	 * see {@link Board#Board(int[][])}.
	 * @param occupiedCellFactory A cell factory that determines the look of
	 * occupied cells (the player should be able to distinguish between variable
	 * and given cells).
	 * @param emptyCellFactory A cell factory that determines the look of
	 * variable cells.
	 * @param borderColor The color of the borders between cells and blocks.
	 */
	public Board(
			int[][] givenValues, CellFactory occupiedCellFactory, 
			CellFactory emptyCellFactory, Color borderColor) {
		this.borderColor = borderColor;
		initCells(givenValues, emptyCellFactory, occupiedCellFactory);
		addCells();
		drawBorders();
	}

	private void initCells(CellFactory factory) {
		cells = new Cell[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				cells[row][col] = factory.createCell(row, col);
	}
	
	private void initCells(
			int[][] values, CellFactory free, CellFactory given) {
		cells = new Cell[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++) {
				if(values[row][col] == 0)
					cells[row][col] = free.createCell(row, col);
				else
					cells[row][col] = 
						given.createCell(row, col, values[row][col]);
			}
	}
	
	private void addCells() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++) {
				constraints.gridx = col;
				constraints.gridy = row;
				this.add(cells[row][col], constraints);
			}
	}

	private void drawBorders() {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++) {
				Cell currentCell = cells[row][col];
				MatteBorder currentBorder = getBorder(row, col);
				currentCell.setBorder(currentBorder);
			}
	}
	
	/**
	 * Determine the border of a cell based on its position in the grid.
	 * @param row Row of the cell (starting with 0).
	 * @param column Column of the cell (starting with 0).
	 * @return A fitting border for the cell at the specified position.
	 */
	private MatteBorder getBorder(int row, int column) {
		
		int top = thinborder;
		int bottom = thinborder;
		int left = thinborder;
		int right = thinborder;
		
		if(row % 3 == 0) // top border and horizontal block borders
			top = thickborder;
		if(row == 8) // bottom border
			bottom = thickborder;
		if(column % 3 == 0) // left border and vertical block borders
			left = thickborder;
		if(column == 8) // right border
			right = thickborder;

		return BorderFactory.createMatteBorder(
				top, left, bottom, right, borderColor);
	}
	
	public Dimension getPreferredSize() {
		Cell arbitraryCell = cells[0][0];
		Dimension cellDimension = arbitraryCell.getPreferredSize();
		int cellSize = cellDimension.width;
		int boardSize =
				cellSize * 9 +		// nine cells in a row/column
				4 * thickborder +	// four thick borders
				6 * thinborder;		// six thin borders (1px)
		return new Dimension(boardSize, boardSize);
	}
	
	public int[][] toIntArray() {
		int[][] board = new int[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				board[row][col] = cells[row][col].getValue();
		return board;
	}
	
	/**
	 * Alters this board according to the provided integer array.
	 * @param newInstance A 9x9 integer array with values from 0 to 9, 0
	 * meaning that the cell at the according position should be left empty.
	 */
	public void alter(int[][] newInstance) {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				cells[row][col].setValue(newInstance[row][col]);
	}
}
