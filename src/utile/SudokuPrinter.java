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
 * This class provides functionality to print Sudoku instances.
 * @author robert
 *
 */
public class SudokuPrinter {

	/**
	 * Print a Sudoku instance to the standard output.
	 * @param sudoku A 9x9 Sudoku as integer array.
	 */
	public static void print(int[][] sudoku) {
		for(int row = 0; row < 9; row++) {
			for(int col = 0; col < 9; col++) {
				System.out.print(sudoku[row][col]);
				System.out.print(" "); // column separation
				if(col % 3 == 2) // horizontal block separation
					System.out.print(" ");
			}
			System.out.println(); // row separation
			if(row % 3 == 2 && row != 8) // vertical block separation
				System.out.println();
		}
	}
	
	/**
	 * Print a Sudoku instance to the standard output using a compact format.
	 * @param sudoku A 9x9 Sudoku as integer array.
	 */
	public static void printShort(int[][] sudoku) {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(sudoku[row][col] == 0)
					System.out.print(".");
				else
					System.out.print(sudoku[row][col]);
		System.out.println();
	}
}
