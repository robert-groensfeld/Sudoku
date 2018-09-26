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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utile.SudokuIO;

/**
 * Provides functionality to permute Sudoku instances.
 * @author robert
 *
 */
public class Transformer {
	
	private int[][] instance;
	private Random random = new Random();

	public Transformer(int[][] sudokuInstance) {
		instance = sudokuInstance;
	}
	
	public Transformer(String shortSudoku) {
		instance = SudokuIO.loadSudoku(shortSudoku);
	}

	public int[][] getInstance() {
		return instance;
	}

	public void transform() {
		transformRows();
		transformCols();
		transformBands();
		transformStacks();
		transformSymbols();
		if(random.nextBoolean())
			transpose();
	}
	
	private void transformRows() {
		for(int band = 0; band < 3; band++)
			for(int swap = 1; swap <= 2; swap++) { // 2 swaps per band
				int aRow = random.nextInt(3) + band * 3;
				int anotherRow = random.nextInt(3) + band * 3;
				swapRows(aRow, anotherRow);
			}
	}
	
	private void swapRows(int row1, int row2) {
		for(int col = 0; col < 9; col++) {
			int temp = instance[row1][col];
			instance[row1][col] = instance[row2][col];
			instance[row2][col] = temp;
		}
	}
	
	private void transformCols() {
		for(int stack = 0; stack < 3; stack++)
			for(int swap = 1; swap <= 2; swap++) { // 2 swaps per band
				int aCol = random.nextInt(3) + stack * 3;
				int anotherCol = random.nextInt(3) + stack * 3;
				swapCols(aCol, anotherCol);
			}
	}
	
	private void swapCols(int col1, int col2) {
		for(int row = 0; row < 9; row++) {
			int temp = instance[row][col1];
			instance[row][col1] = instance[row][col2];
			instance[row][col2] = temp;
		}
	}
	
	private void transformBands() {
		for(int swap = 1; swap <= 2; swap++) { // swap bands 2 times
			int aBand = random.nextInt(3);
			int anotherBand = random.nextInt(3);
			swapBands(aBand, anotherBand);
		}
	}
	
	private void swapBands(int band1, int band2) {
		int row1 = band1 * 3;
		int row2 = band2 * 3;
		for(int i = 0; i < 3; i++)
			swapRows(row1 + i, row2 + i);
	}
	
	private void transformStacks() {
		for(int swap = 1; swap <= 2; swap++) { // swap stacks 2 times
			int aStack = random.nextInt(3);
			int anotherStack = random.nextInt(3);
			swapStacks(aStack, anotherStack);
		}
	}
	
	private void swapStacks(int stack1, int stack2) {
		int col1 = stack1 * 3;
		int col2 = stack2 * 3;
		for(int i = 0; i < 3; i++)
			swapCols(col1 + i, col2 + i);
	}
	
	private void transformSymbols() {
		ArrayList<Integer> newSymbols = new ArrayList<Integer>();
		for(int symbol = 1; symbol <= 9; symbol++)
			newSymbols.add(symbol);
		Collections.shuffle(newSymbols);
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(instance[row][col] != 0)
					instance[row][col] = newSymbols.get(instance[row][col] - 1);
	}
	
	private void transpose() {
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < row; col++) {
				// mirror values along the diagonal from top left to bottom right
				int temp = instance[row][col];
				instance[row][col] =
						instance[col][row];
				instance[col][row] = temp;
			}
	}
}
