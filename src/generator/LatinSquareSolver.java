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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Solver for Latin squares of variable size.
 * @author robert
 *
 */
class LatinSquareSolver {
	
	private static List<LatinSquareSolution> solutions = 
			new ArrayList<LatinSquareSolution>(12);
	
	private static int[][] currentSolution;
	private static int squareSize;
	
	protected static void findSolutions(int size) {
		currentSolution = new int[size][size];
		squareSize = size;
		LatinSquareCellList emptyCells = new LatinSquareCellList(size);
		backtrack(emptyCells);
	}
	
	private static void backtrack(LatinSquareCellList emptyCells) {
		if(emptyCells.isEmpty()) { // no more empty cells. solution found.
			solutions.add(new LatinSquareSolution(currentSolution, squareSize));
			return;
		}
		
		LatinSquareCell currentCell = emptyCells.removeMostConstrainedCell();
		List<Integer> allowedValues = currentCell.getAllowedValues();
		List<LatinSquareCell> neighbors = emptyCells.getNeighbors(currentCell);
		
		for(int value : allowedValues) {
			// keep track of cells that were affected by the current assignment
			List<LatinSquareCell> affectedNeighbors = 
					new LinkedList<LatinSquareCell>();
			for(LatinSquareCell neighbor : neighbors)
				if(neighbor.forbid(value)) // false if value already forbidden
					affectedNeighbors.add(neighbor);
			
			// extend the current solution by the current assignment
			currentSolution[currentCell.getRow()][currentCell.getCol()] = value;
			backtrack(emptyCells);
			
			// before finding solutions for the next value, allow the current
			// value in the neighbor cells
			for(LatinSquareCell affectedNeighbor : affectedNeighbors)
				affectedNeighbor.allow(value);
		} // every possible solution for the current configuration found
		
		// clear this cell and go back to the previous cell
		emptyCells.add(currentCell);
	}
	
	protected static LatinSquareSolution[] getSample(int size) {
		LatinSquareSolution[] sample = new LatinSquareSolution[size];
		Random random = new Random();
		int solutionCount = solutions.size();
		for(int i = 0; i < size; i++) {
			int solutionIndex = random.nextInt(solutionCount);
			sample[i] = solutions.get(solutionIndex);
		}
		return sample;
	}
	
	protected static LatinSquareSolution getSample() {
		LatinSquareSolution[] sample = getSample(1);
		return sample[0];
	}
}
