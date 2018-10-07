package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class CellKeyListener extends KeyAdapter {

	@Override
	public void keyPressed(KeyEvent event) {
		final Cell focusedCell = (Cell) event.getComponent();

        if (!focusedCell.isOccupied()) {
            final char number = event.getKeyChar();
            if (Character.isDigit(number))
                try {
                    focusedCell.setValue(Character.getNumericValue(number));
                } catch (Exception e) { /* Ignore invalid user input. */ }

            final int keyCode = event.getKeyCode();
            final boolean deletePressed = keyCode == KeyEvent.VK_DELETE;
            final boolean backSpacePressed = keyCode == KeyEvent.VK_BACK_SPACE;
            if (deletePressed || backSpacePressed)
                focusedCell.clear();
        }
	}
}