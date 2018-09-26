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
 * This class provides functionality to print Latin squares.
 * @author robert
 *
 */
public class LatinSquarePrinter {

	/**
	 * Print a Latin square to the standard output.
	 * @param latinSquare The Latin square as integer array.
	 * @param squareSize The side length of the provided Latin square.
	 */
	public static void print(int[][] latinSquare, int squareSize) {
		for(int row = 0; row < squareSize; row++) {
			for(int col = 0; col < squareSize; col++)
				System.out.print(latinSquare[row][col] + " ");
			System.out.println();
		}
		System.out.println();
	}
}
