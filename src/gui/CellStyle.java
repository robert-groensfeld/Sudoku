package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/** 
 * The style of a Sudoku board cell.
 * @author Robert Grönsfeld
 */
class CellStyle {

    private int cellSize;

    private Color backgroundColor;
    private Color fontColor;
    
    private Font font;

    /** Creates a default cell style. */
    protected CellStyle() {
        this.cellSize = 40;
        this.backgroundColor = Color.WHITE;
        this.fontColor = Color.BLACK;
        this.font = new Font("Monospaced", Font.PLAIN, cellSize);
    }

    /**
     * Change the cell size.
     * @param size The cell size in pixels.
     */
    protected void setCellSize(int size) {
        this.cellSize = size;
    }

    /**
     * Change the background color of the cell.
     * @param color The new background color.
     */
    protected void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /**
     * Change the color of the number within the cell.
     * @param color The new font color.
     */
    protected void setFontColor(Color color) {
        this.fontColor = color;
    }

    /**
     * Choose a new font for the number within the cell.
     * @param font The new font.
     */
    protected void setFont(Font font) {
        this.font = new Font(font.getName(), font.getStyle(), cellSize);
    }

    /**
     * Applies this style to a cell.
     * @param cell A cell of a Sudoku Board.
     */
    protected void apply(Cell cell) {
        cell.setPreferredSize(new Dimension(cellSize, cellSize));
        
        cell.setBackground(backgroundColor);
        cell.setForeground(fontColor);
        
        cell.setHorizontalAlignment(Cell.CENTER);
		cell.setFont(font);
    }
}