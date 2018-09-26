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

/**
 * Wrapper class for integer arrays representing solutions to Latin squares
 * of variable sizes.
 * @author robert
 *
 */
class LatinSquareSolution {

	private int[][] square;
	private int size;
	
	protected LatinSquareSolution(int[][] square, int size) {
		this.square = new int[size][size];
		for(int row = 0; row < size; row++)
			for(int col = 0; col < size; col++)
				this.square[row][col] = square[row][col];
		this.size = size;
	}
	
	protected int[][] getSquare() {
		return square;
	}
	
	protected int getSize() {
		return size;
	}
}
