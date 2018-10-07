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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A wrapper class that puts the listeners attached to {@link Cell} together in 
 * one place.
 * @author robert
 *
 */
class CellListener implements FocusListener, KeyListener {
	
	// If the cell gains focus, the background color is changed to distinguish
	// it from other cells.
	private Color focused;
	private Color notFocused;
	
	/**
	 * Creates a new cell listener that implements a {@link FocusListener} and
	 * a {@link KeyListener}.
	 * @param focused Background color of a cell that has gained focus.
	 * @param notFocused Background color of a cell that is not focused.
	 */
	protected CellListener(Color focused, Color notFocused) {
		this.focused = focused;
		this.notFocused = notFocused;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		Cell correspondingCell = (Cell) arg0.getComponent();
		int keyCode = arg0.getKeyCode();
		
		// The keys 1..9 have the codes 49..57. Therefore we just have to
		// subtract 48 from the key code and assign the resulting value to the
		// corresponding cell.
		if(keyCode >= 49 && keyCode <= 57) {
			int cellValue = keyCode - 48;
			if(!correspondingCell.isOccupied())
				correspondingCell.setText(Integer.toString(cellValue));
		}
		
		// Remove a value from the cell if [delete] or [return] are pressed
		if(keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE)
			if(!correspondingCell.isOccupied())
				correspondingCell.setText("");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent arg0) {
		Cell correspondingCell = (Cell) arg0.getComponent();
		correspondingCell.setBackground(focused);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent arg0) {
		Cell correspondingCell = (Cell) arg0.getComponent();
		correspondingCell.setBackground(notFocused);
	}

}
