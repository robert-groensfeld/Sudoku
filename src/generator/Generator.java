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

import java.util.Random;
import java.util.Stack;

import solver.SudokuProblem;

/**
 * This class provides functionality to generate Sudoku problems.
 * @author robert
 *
 */
public class Generator {
	
	private static int[][] solution = new int[9][9];
	private static int[][] instance = new int[9][9];
	private static Stack<Position> givenPositions = new Stack<Position>();
	
	public static int[][] generateInstance() {
		generateSolution();
		placeValues(40);
		SudokuProblem problem = new SudokuProblem(instance);
		while(!problem.hasUniqueSolution()) {
			instance = new int[9][9];
			placeValues(40);
			problem = new SudokuProblem(instance);
		}
		minimizeGivens();
		Transformer transformer = new Transformer(instance);
		transformer.transform();
		return transformer.getInstance();
	}

	/**
	 * Generates a 9x9 solved Sudoku instance.
	 */
	private static void generateSolution() {
		// Fetch nine 3x3 Latin squares:
		LatinSquareSolver.findSolutions(3);
		LatinSquareSolution[] squares = LatinSquareSolver.getSample(9);
		insertSquares(squares);
		toBase10(LatinSquareSolver.getSample());
		swapRows(1, 3);
		swapRows(2, 6);
		swapRows(5, 7);
	}
	
	private static void insertSquares(LatinSquareSolution[] solutions) {
		// block index increases from left to right and top to bottom
		for(int blockIndex = 0; blockIndex < 9; blockIndex++) {
			// pick the 3x3 Latin square corresponding to the current block
			int[][] square = solutions[blockIndex].getSquare();
			for(int row = 0; row < 3; row++)
				for(int col = 0; col < 3; col++) {
					// find the position of the current Latin square cell 
					// in the Sudoku grid and copy the value over
					int sudokuRow = ((int) (blockIndex / 3)) * 3 + row;
					int sudokuCol = 3 * (blockIndex%3) + col;
					solution[sudokuRow][sudokuCol] = square[row][col];
				}
		}
	}
	
	private static void toBase10(LatinSquareSolution overlay) {
		int[][] square = overlay.getSquare();
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++) {
				int lsRow = (int) row / 3;
				int lsCol = (int) col / 3;
				int base10Value = 
						square[lsRow][lsCol] * 3  +
						solution[row][col] + 1;
				solution[row][col] = base10Value;
			}
	}
	
	private static void swapRows(int row1, int row2) {
		for(int col = 0; col < 9; col++) {
			int temp = solution[row1][col];
			solution[row1][col] = solution[row2][col];
			solution[row2][col] = temp;
		}
	}
	
	private static void placeValues(int numberOfValues) {
		int placedValues = 0;
		Random rowGenerator = new Random();
		Random colGenerator = new Random();
		while(placedValues < numberOfValues) {
			int row = rowGenerator.nextInt(9);
			int col = colGenerator.nextInt(9);
			if(instance[row][col] == 0) {
				instance[row][col] = solution[row][col];
				givenPositions.push(new Position(row, col));
				placedValues++;
			}
		}
	}
	
	private static void minimizeGivens() {
		while(!givenPositions.empty()) {
			Position pos = givenPositions.pop();
			int currentValue = instance[pos.row][pos.column];
			instance[pos.row][pos.column] = 0; // remove given
			SudokuProblem problem = new SudokuProblem(instance);
			if(!problem.hasUniqueSolution()) // reinsert value if not unique
				instance[pos.row][pos.column] = currentValue;
		}
	}
}
