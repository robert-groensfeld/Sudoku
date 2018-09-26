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
 * Represents a 9x9 Sudoku puzzle.
 * @author robert
 *
 */
public class Puzzle {

	private final int[][] givens;
	private final int[][] solution;
	private final int avgSolveTime;
	
	/**
	 * Creates a new puzzle.
	 * @param givens A 9x9 integer array that represents the puzzle.
	 * @param solution A 9x9 integer array that represents the solution to this
	 * puzzle.
	 * @param avgSolveTime The average time a human would need to solve this
	 * puzzle.
	 */
	protected Puzzle(int[][] givens, int[][] solution, int avgSolveTime) {
		this.givens = givens;
		this.solution = solution;
		this.avgSolveTime = avgSolveTime;
	}
	
	public int[][] getGivens() {
		return givens;
	}

	public int[][] getSolution() {
		return solution;
	}

	public int getAvgSolveTime() {
		return avgSolveTime;
	}
}
