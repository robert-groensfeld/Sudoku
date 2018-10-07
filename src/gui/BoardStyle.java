package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;

/** 
 * The style of a Sudoku board. 
 * @author Robert Grönsfeld
 */
class BoardStyle {

    private int cellBorderSize, blockBorderSize;
    private Color borderColor;

    BoardStyle() {
        cellBorderSize = 1;
        blockBorderSize = 3;
        borderColor = Color.BLACK;
    }

    /**
     * Change the size of the cell border.
     * @param cellBorderSize The new cell border thickness in pixels.
     */
    protected void setCellBorderSize(int size) {
        this.cellBorderSize = size;
    }

    /**
     * Change the size of the 3x3 blocks of the Sudoku board.
     * @param blockBorderSize The new block border thickness in pixels.
     */
    protected void setBlockBorderSize(int size) {
        this.blockBorderSize = size;
    }

    /**
     * Generate a border for a cell on the board.
     * @param cell A Sudoku board cell.
     * @return A border for the given cell.
     */
    protected MatteBorder getBorder(Cell cell) {
        final int top = cell.row % 3 == 0 ? blockBorderSize : cellBorderSize;
        final int bottom = cell.row == 8 ? blockBorderSize : cellBorderSize;
        final int left = cell.col % 3 == 0 ? blockBorderSize : cellBorderSize;
        final int right = cell.col == 8 ? blockBorderSize : cellBorderSize;
        final Color color = borderColor;
        
		return BorderFactory.createMatteBorder(top, left, bottom, right, color);
    }

    /**
     * Calculates the board size from the cell size and the size of the borders.
     * @param cell A cell within the board.
     * @return The board size.
     */
    protected Dimension getBoardSize(Cell cell) {
		int cellSize = cell.getPreferredSize().width;
		int boardWidth =
				cellSize * 9 +	    	// nine cells in a row/column
				4 * blockBorderSize +	// four thick borders
				6 * cellBorderSize;		// six thin borders (1px)
		return new Dimension(boardWidth, boardWidth);
    }
}