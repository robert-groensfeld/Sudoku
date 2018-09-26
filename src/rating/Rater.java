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

package rating;

import java.util.List;

import com.rits.cloning.Cloner;

/**
 * Provides functionality to rate a Sudoku instance according to its difficulty.
 * @author robert
 *
 */
public class Rater {

	private BlankCellList blankCells;
	
	private BlankCellList[] copies;
	private int currentCopyIndex = 0;
	
	private int refutationSum = 0;
	private int averageRefutationSum = 0;
	
	private final int DEPENDENCY_STEPS = 25;
	private int dependencyStep = 1;
	private int dependencySum = 0;
	private int averageDependencyMetric = 0;
	
	private final int RUNS = 30;
	
	/**
	 * Initializes a new rater for a given Sudoku puzzle.
	 * @param puzzle A 9x9 integer array representing an unsolved Sudoku.
	 */
	public Rater(int[][] puzzle) {
		// rate several times and build the average rating
		for(int run = 0; run < RUNS; run++) {
			blankCells = new BlankCellList(puzzle);
			ratePuzzle();
			
			averageRefutationSum += refutationSum;
			averageDependencyMetric += 
					Math.round(dependencySum / DEPENDENCY_STEPS);
			
			currentCopyIndex = 0;
			refutationSum = 0;
			dependencyStep = 1;
			dependencySum = 0;
		}
		
		averageRefutationSum = Math.round(averageRefutationSum / RUNS);
		averageDependencyMetric = Math.round(averageDependencyMetric / RUNS);
	}
	
	/**
	 * Initializes a new rater for a given Sudoku puzzle.
	 * @param puzzle A 9x9 integer array representing an unsolved Sudoku.
	 * @param solution The solution to the provided puzzle.
	 */
	public Rater(int[][] puzzle, int[][] solution) {
		// rate several times and build the average rating
		for(int run = 0; run < RUNS; run++) {
			blankCells = new BlankCellList(puzzle, solution);
			ratePuzzle();
			
			averageRefutationSum += refutationSum;
			averageDependencyMetric += 
					Math.round(dependencySum / DEPENDENCY_STEPS);
			
			currentCopyIndex = 0;
			refutationSum = 0;
			dependencyStep = 1;
			dependencySum = 0;
		}
		
		averageRefutationSum = Math.round(averageRefutationSum / RUNS);
		averageDependencyMetric = Math.round(averageDependencyMetric / RUNS);
	}

	private void ratePuzzle() {
		while(!blankCells.filled()) { // Sudoku not solved.
			fillInSingles();

			if(!blankCells.filled()) // advanced technique required.
				fillAdvancedCell();
		} // puzzle rated.
	}
	
	private void fillInSingles() {
		while(blankCells.hasSingleCell()) {
			updateDependency();
			blankCells.fillSingleCell();
		}
	}
	
	private void updateDependency() {
		if(dependencyStep++ <= DEPENDENCY_STEPS)
			dependencySum += blankCells.getSinglePossibilities();
	}
	
	private void fillAdvancedCell() {
		List<BlankCell> advancedCells = blankCells.getAdvancedCells();
		// cell with minimal refutation score. Initially the first cell.
		BlankCell minCell = advancedCells.get(0);
		
		makeCopies(); // copies of blankCells. Needed when refuting candidates.
		for(BlankCell cell : advancedCells) {
			refuteWrongCandidates(cell);

			if(cell.getRefutationScore() < minCell.getRefutationScore())
				minCell = cell;
		} // found the cell with the minimal refutation score
		blankCells.fill(minCell);
		
		++dependencyStep;
		refutationSum = refutationSum + minCell.getRefutationScore();
	}
	
	private void makeCopies() {
		int copyCount = 0;
		for(BlankCell advancedCell : blankCells.getAdvancedCells())
			copyCount += advancedCell.wrongCandidateCount();
		copies = new BlankCellList[copyCount];
		
		Cloner cloner = new Cloner();
		while(copyCount > 0)
			copies[--copyCount] = cloner.deepClone(blankCells);
		currentCopyIndex = 0;
	}
	
	private void refuteWrongCandidates(BlankCell cell) {
		int[] wrongCandidates = cell.getWrongCandidates();
		for(int candidate : wrongCandidates) {
			BlankCellList copy = getNextCopy();
			copy.fill(cell, candidate);
			
			int step = 0; 
			while(copy.isConsistent() && copy.hasSingleCell()) {
				copy.fillSingleCell();
				++step;
			}
			
			if(!copy.isConsistent())
				cell.refute(step);
			else
				cell.refute();
		}
	}
	
	private BlankCellList getNextCopy() {
		return copies[currentCopyIndex++];
	}
	
	public int getRefutationSum() {
		return averageRefutationSum;
	}
	
	public int getDependencyMetric() {
		return averageDependencyMetric;
	}

	public int getEstimatedTime() {
		if(this.getRefutationSum() == 0) { // no advanced techniques required
			// values from next line obtained via linear regression over
			// a set of 715 puzzles where no advanced techniques were
			// required.
			double solveTime = 
					26.361323 - 1.130103 * this.getDependencyMetric();
			return (int) Math.round(solveTime);
		}
		else { // advanced technique required
			// linear regression with manually removed outliers:
			double solveTime = 37.931241053 
					+ 0.09396403 * (double) this.getRefutationSum()
					- 1.08375558 * (double) this.getDependencyMetric();
			// without outlier removal:  
			// (Intercept)          ref          dep 
			// 37.931241053  0.006577984 -1.515772855 
			return (int) Math.round(solveTime);
		}
	}
}
