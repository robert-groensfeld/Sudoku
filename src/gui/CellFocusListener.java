package gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Changes the appearance of the currently focused cell.
 * @author Robert Grönsfeld
 */
class CellFocusListener implements FocusListener {

    private final CellStyle focusedCellStyle, normalCellStyle;

    /**
     * Create a new focus listener for cells.
     * @param focused Apply this style to editable cells gaining focus.
     * @param notFocused Apply this style to editable cells losing focus.
     */
    protected CellFocusListener(CellStyle focused, CellStyle notFocused) {
        this.focusedCellStyle = focused;
        this.normalCellStyle = notFocused;
    }

    @Override
	public void focusGained(FocusEvent event) {
        Cell correspondingCell = (Cell) event.getComponent();
        if (!correspondingCell.isOccupied())
            focusedCellStyle.apply(correspondingCell);
	}

	@Override
	public void focusLost(FocusEvent event) {
        Cell correspondingCell = (Cell) event.getComponent();
        if (!correspondingCell.isOccupied())
            normalCellStyle.apply(correspondingCell);
	}
}