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
 * @author Robert Grönsfeld
 */
@SuppressWarnings("serial")
public class Board extends JPanel {

	protected Cell[][] cells;

	private CellFactory factory;
	private BoardStyle style;

	/**
	 * Creates an empty Sudoku board.
	 * @throws Exception Unable to create board due to an internal error.
	 */
	protected Board() throws Exception {
		this(new int[9][9]);
	}

	/**
	 * Creates a new Sudoku board with the default look.
	 * @param puzzle A row x column array with numbers from 0 to 9, 0 
	 * representing empty cells.
	 * @throws Exception A board could not be created from the given puzzle.
	 */
	protected Board(int[][] puzzle) throws Exception {
		this(puzzle, new CellFactory(), new BoardStyle());
	}
	
	/**
	 * Creates a new Sudoku board with a custom look.
	 * @param puzzle A 9 x 9, row x column Sudoku puzzle with cell values from 0 
	 * to 9, 0 representing empty cells.
	 * @param factory A factory creating cells with the desired style.
	 * @param style The style of this board.
	 * @throws Exception A cell could not be created.
	 */
	protected Board(int[][] puzzle, CellFactory factory, BoardStyle style)
	throws Exception {
		this.factory = factory;
		this.style = style;

		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		cells = new Cell[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++) {
				int value = puzzle[row][col];

				if (value == 0)
					cells[row][col] = factory.createCell(row, col);
				else
					try {
						cells[row][col] = factory.createCell(row, col, value);
					} catch (Exception e) {
						throw new Exception("Unable to create a cell", e);
					}

				cells[row][col].setBorder(style.getBorder(cells[row][col]));

				constraints.gridx = col;
				constraints.gridy = row;
				this.add(cells[row][col], constraints);
			}
	}

	/**
	 * Overwrite this board with a new Sudoku puzzle.
	 * @param puzzle A 9 x 9, row x column Sudoku puzzle containing values from
	 * 0 to 9, 0 representing empty cells.
	 * @throws Exception The puzzle contains invalid values.
	 */
	protected void setPuzzle(int[][] puzzle) throws Exception {
		for (int row = 0; row < 9; ++row)
			for (int col = 0; col < 9; ++col) {
				cells[row][col].clear();
				try {
					if (puzzle[row][col] != 0)
						cells[row][col].occupy(puzzle[row][col]);
				} catch (Exception e) {
					throw new Exception("Malformed puzzle", e);
				}
				factory.getStyle(cells[row][col]).apply(cells[row][col]);
			}
	}

	/**
	 * Gets the puzzle within this board.
	 * @return The board as 9 x 9, row x column array of numbers, 0 representing
	 * empty cells.
	 */
	protected int[][] getPuzzle() {
		int[][] puzzle = new int[9][9];
		for (int row = 0; row < 9; ++row)
			for (int col = 0; col < 9; ++col)
				if (cells[row][col].isOccupied())
					puzzle[row][col] = cells[row][col].getValue();
		return puzzle;
	}
	
	/**
	 * Converts this board to a 2D row x column integer array.
	 * @return An array containing the cell values. 0 represents an empty cell.
	 */
	public int[][] toIntArray() {
		int[][] board = new int[9][9];
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				board[row][col] = cells[row][col].getValue();
		return board;
	}

	/**
	 * Fills remaining empty cells with the solution of the puzzle.
	 * @param solution A solution for the puzzle in this board.
	 * @throws Exception The solution contains invalid values.
	 */
	protected void solve(int[][] solution) throws Exception {
		for (int row = 0; row < 9; ++row)
			for (int col = 0; col < 9; ++col)
				if (!cells[row][col].isOccupied())
					try {
						cells[row][col].setValue(solution[row][col]);
					} catch (Exception e) {
						throw new Exception("Malformed solution", e);
					}
	}
	
	@Override
	public Dimension getPreferredSize() {
		Cell arbitraryCell = cells[0][0];
		return style.getBoardSize(arbitraryCell);
	}
}
