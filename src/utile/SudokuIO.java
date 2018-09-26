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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import rating.Rater;

/**
 * Permits to save and load 9x9 Sudoku problems and solutions. Problems and
 * solutions are represented as 9x9 integer arrays. 
 * @author robert
 *
 */
public class SudokuIO {
	
	/**
	 * Save a Sudoku instance to disk.
	 * @param target Indicates where the instance should be stored.
	 * @param sudoku 9x9 integer array representing the instance. Each entry
	 * has to contain a value from zero to nine, zero meaning that the cell
	 * corresponding to the entry is empty.
	 */
	public static void saveSudoku(File target, int[][] sudoku) {
		try {
			BufferedWriter saver = new BufferedWriter(new FileWriter(target));
			for(int row = 0; row < 9; row++) {
				for(int col = 0; col < 9; col++) {
					saver.write(sudoku[row][col] + " ");
					if(col % 3 == 2) // separate blocks horizontally
						saver.write(" ");
				}
				saver.write("\n");
				if(row % 3 == 2) // separate blocks vertically
					saver.write("\n");
			}
			saver.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a Sudoku file from disk.
	 * @param source The Sudoku file.
	 * @return A 9x9 integer array. For further specification see 
	 * {@link SudokuIO#saveSudoku(File, int[][])}
	 */
	public static int[][] loadSudoku(File source) {
		try {
			Scanner valueReader = new Scanner(source);
			int[][] sudoku = new int[9][9];
			int currentRow = 0;
			int currentCol = 0;
			while(valueReader.hasNextInt()) {
				sudoku[currentRow][currentCol] = valueReader.nextInt();
				currentCol++;		 	// go to next value in current row
				if(currentCol > 8) {	// last value in current row visited
					currentCol = 0; 	// proceed with first value ...
					currentRow++;		// ... in the next row
				}
			}
			valueReader.close();
			return sudoku;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		// LETS HOPE THAT THIS NEVER HAPPENS
		return new int[9][9];
	}
	
	/**
	 * Load a Sudoku given in the short format: <br/>
	 * 8...4...6.7........451.6.2.9..4....1.3..9..7.6
	 * ....8..4.2.6.194........6.7...3...2 <br/>
	 * Rows written one after another. Points stand for blank fields.
	 * @param shortSudoku A Sudoku in the short format.
	 * @return A 9x9 integer array representing the given Sudoku.
	 */
	public static int[][] loadSudoku(String shortSudoku) {
		return toGrid(shortSudoku);
	}
	
	/**
	 * Use this method to get the compact version of a Sudoku instance.
	 * @param sudoku A 9x9 integer array that represents a Sudoku instance.
	 * @return A line of text representing the same instance. See
	 * {@link SudokuIO#loadSudoku(String)} to get more detailed information
	 * on the output format.
	 */
	public static String getShort(int[][] sudoku) {
		String shortSudoku = "";
		for(int row = 0; row < 9; row++)
			for(int col = 0; col < 9; col++)
				if(sudoku[row][col] == 0)
					shortSudoku = shortSudoku.concat(".");
				else
					shortSudoku = shortSudoku.concat(
							Integer.toString(sudoku[row][col]));
		return shortSudoku;
	}
	
	public static void createTrainingTable(String puzzleFile) {
		List<Puzzle> puzzles = parsePuzzleFile(puzzleFile);
		System.out.println("tme\tref\tdep");
		for(Puzzle puzzle : puzzles) {
			Rater rater = new Rater(puzzle.getGivens(), puzzle.getSolution());
			System.out.println(
				puzzle.getAvgSolveTime() + "\t" +
				rater.getRefutationSum() + "\t" +
				rater.getDependencyMetric());
		}
	}

	private static List<Puzzle> parsePuzzleFile(String path) {
		List<Puzzle> puzzles = new LinkedList<Puzzle>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			while((line = reader.readLine()) != null) {
				int solveTime = getAvgSolveTime(line);
				line = reader.readLine();
				int[][] puzzle = toGrid(line);
				line = reader.readLine();
				int[][] solution = toGrid(line);
				puzzles.add(new Puzzle(puzzle, solution, solveTime));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File " + path + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read a line of file" + path);
			e.printStackTrace();
		}
		return puzzles;
	}

	private static int getAvgSolveTime(String line) {
		String[] words = line.split("\\s");
		String lastWord = words[words.length - 1];
		int solveTime = Integer.parseInt(lastWord);
		return solveTime;
	}

	private static int[][] toGrid(String line) {
		int[][] grid = new int[9][9];
		for(int i = 0; i < 81; i++) {
			int row = i / 9;
			int col = i % 9;
			char currentChar = line.charAt(i);
			if(currentChar == '.')
				grid[row][col] = 0;
			else
				grid[row][col] = (int) (currentChar - '0');
		}
		return grid;
	}
}
