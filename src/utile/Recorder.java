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

package utile;

/**
 * A class that provides functionality to track changes made to a Sudoku board.
 * @author robert
 *
 */
public class Recorder {

	private final char insert		= 'I';
	private final char delete		= 'D';
	private final char highlight	= 'H';
	private final char separator	= '|';
	
	private StringBuilder changes;
	
	public Recorder() {
		changes = new StringBuilder();
	}
	
	public void trackInsertion(int row, int column, int value) {
		changes.append(separator);
		changes.append(insert);
		changes.append(row);
		changes.append(column);
		changes.append(value);
	}
	
	public void trackDeletion(int row, int column) {
		changes.append(separator);
		changes.append(delete);
		changes.append(row);
		changes.append(column);
	}
	
	public void trackHighlight(int row, int column) {
		changes.append(separator);
		changes.append(highlight);
		changes.append(row);
		changes.append(column);
	}
	
	public String getRecording() {
		return changes.toString();
	}
}
