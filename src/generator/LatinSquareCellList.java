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

package generator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author robert
 *
 */
class LatinSquareCellList {

	private List<LatinSquareCell> cells = 
			new LinkedList<LatinSquareCell>();
	
	protected LatinSquareCellList(int squareSize) {
		for(int row = 0; row < squareSize; row++)
			for(int col = 0; col < squareSize; col++)
				cells.add(new LatinSquareCell(row, col, squareSize));
	}
	
	protected LatinSquareCell removeMostConstrainedCell() {
		LatinSquareCell mostConstrained = cells.get(0);
		for(LatinSquareCell currentCell : cells)
			if(currentCell.getAllowedValues().size() < 
					mostConstrained.getAllowedValues().size())
				mostConstrained = currentCell;
		cells.remove(mostConstrained);
		return mostConstrained;
	}
	
	protected List<LatinSquareCell> getNeighbors(LatinSquareCell cell) {
		List<LatinSquareCell> neighbors = new LinkedList<LatinSquareCell>();
		for(LatinSquareCell currentCell : cells)
			if(currentCell.getRow() == cell.getRow() || currentCell.getCol() == cell.getCol())
				neighbors.add(currentCell);
		return neighbors;
	}
	
	protected boolean isEmpty() {
		return cells.isEmpty();
	}
	
	protected void add(LatinSquareCell cell) {
		cells.add(cell);
	}
}
