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

import java.util.BitSet;
import java.util.LinkedList;

/**
 * Represents a blank cell of a Sudoku puzzle.
 * @author robert
 *
 */
class BlankCell {
	
	/** 
	 * Use this constant to simulate an infinite ref_v value.
	 * This can be done because the upper bound for the refutation sum of an
	 * arbitrary cell is 496. The minimum number of givens in a Sudoku is 17.
	 * This leaves 81 - 17 = 64 empty cells in a minimal Sudoku instance. This
	 * means that after filling the first cell with a wrong candidate, we reach
	 * a contradiction in 62 steps, which is the maximum ref_v value for a
	 * candidate. We have at most 8 wrong candidates. So the maximum refutation
	 * sum is 8 * 62 = 496.
	 */
	protected static final int infinite = 1000;
	
	protected final int row, column, block;
	protected final int solution;
	
	private BitSet candidates;
	private int candidateCount;
	
	private LinkedList<BlankCell> neighbors = new LinkedList<BlankCell>();
	
	private int refutationScore;

	/**
	 * Creates a new blank cell. Blank cells are uniquely identified by their
	 * position.
	 * @param row Row of this cell. 
	 * @param column Column of this cell.
	 */
	protected BlankCell(int row, int column, int solution) {
		this.row = row;
		this.column = column;
		block = this.initBlock();
		this.solution = solution;
		
		initCandidates();
	}

	private int initBlock() {
		int x = (int) (column / 3);
		int y = (int) (row / 3);
		return x + y * 3;
	}
	
	private void initCandidates() {
		candidates = new BitSet(9);
		candidates.set(0, 9); // every candidate allowed
		candidateCount = 9;
	}
	
	/**
	 * Determines if this cell contains a "naked single", meaning that it has
	 * only one allowed value.
	 * @return {@code true} if only one value is allowed for this cell, 
	 * {@code false} otherwise.
	 */
	protected boolean hasNakedSingle() {
		return candidateCount == 1;
	}
	
	/**
	 * Determines if this cell contains a "hidden single", meaning that this
	 * cell is the only cell in its row, column or block which can be filled
	 * with some value. To return a correct value, every blank neighbor of
	 * this cell has to be registered via 
	 * {@link {@link BlankCell#assignNeighbor(BlankCell)}}
	 * @return {@code true} if this cell contains a hidden single, 
	 * {@code false} otherwise.
	 */
	protected boolean hasHiddenSingle() {
		BitSet rowCandidates = (BitSet) candidates.clone();
		BitSet colCandidates = (BitSet) candidates.clone();
		BitSet blockCandidates = (BitSet) candidates.clone();
		
		for(BlankCell neighbor : neighbors) {
			if(neighbor.row == this.row)
				rowCandidates.andNot(neighbor.candidates);
			if(neighbor.column == this.column)
				colCandidates.andNot(neighbor.candidates);
			if(neighbor.block == this.block)
				blockCandidates.andNot(neighbor.candidates);
		}

		return rowCandidates.cardinality() == 1
				|| colCandidates.cardinality() == 1
				|| blockCandidates.cardinality() == 1;
	}

	/**
	 * Use this method to let this cell know about one of its neighbors.
	 * Knowing about all neighbors is crucial to determine if this cell contains
	 * a hidden single.
	 * @param neighbor A blank cell that resides in the same row, column or
	 * block.
	 */
	protected void assignNeighbor(BlankCell neighbor) {
		neighbors.add(neighbor);
	}
	
	/**
	 * Call this method to fill a cell with its correct value. Forbids the
	 * value in every neighbor of this cell and deletes this cell from the
	 * neighbor list of every neighbor cell.
	 */
	protected void fill() {
		this.fill(solution);
	}
	
	/**
	 * Call this method to fill this cell with an arbitrary value. Forbids 
	 * the value in every neighbor of this cell and deletes this cell from the 
	 * neighbor list of every neighbor cell.
	 * @param value A value v for this cell (1 <= v <= 9).
	 */
	protected void fill(int value) {
		for(BlankCell neighbor : neighbors) {
			neighbor.forbid(value);
			neighbor.removeNeighbor(this); 
		}
	}
	
	/**
	 * Every blank cell has a set of allowed values. A value is allowed if it
	 * doesn't already occur in neighboring cells and otherwise forbidden.
	 * @param value The forbidden value. This value occurs in some neighboring
	 * cell.
	 */
	protected void forbid(int value) {
		if(candidate(value)) {
			candidates.flip(value - 1);
			--candidateCount;
		}
	}
	
	private boolean candidate(int value) {
		return candidates.get(value - 1);
	}
	
	/**
	 * Removes a cell from the neighbors of this cell. This occurs when a
	 * blank neighbor cell gets filled with a value.
	 * @param neighbor A neighbor of this cell.
	 */
	protected void removeNeighbor(BlankCell neighbor) {
		neighbors.remove(neighbor);
	}
	
	/**
	 * Checks if this cell has one or more valid candidate values.
	 * @return {@code true} if this cell contains at least one valid candidate
	 * value and thus can be filled, {@code false} otherwise.
	 */
	protected boolean canBeFilled() {
		return candidateCount > 0;
	}
	
	/**
	 * Call this method to get all wrong candidates for this cell. A candidate
	 * is a value that does not already occur in the same row, column or block
	 * as this cell. Every candidate except this cells solution is a wrong
	 * candidate.
	 * @return A list with wrong candidates for this cell.
	 */
	protected int[] getWrongCandidates() {
		int[] wrongCandidates = new int[candidateCount - 1];
		int index = 0;
		for(int value = 1; value <= 9; value++)
			if(candidate(value) && value != solution)
				wrongCandidates[index++] = value;
		return wrongCandidates;
	}
	
	/**
	 * Call this method to refute an arbitrary wrong candidate of this cell.
	 * @param ref_v The number of simple steps (applications of naked and
	 * hidden single technique) it took to refute the wrong candidate.
	 */
	protected void refute(int ref_v) {
		refutationScore += ref_v;
	}
	
	/**
	 * Use this method when you want to refute a wrong candidate, that has
	 * an infinite ref_v value.
	 */
	protected void refute() {
		refutationScore += infinite;
	}
	
	/**
	 * Call this method to get the refutation score for this cell.
	 * @return The refutation score or a number n >= {@link BlankCell#infinite} 
	 * if the refutation score of this cell is infinite.
	 */
	protected int getRefutationScore() {
		return refutationScore;
	}
	
	/**
	 * Call this method to check if this cell has a finite refutation score.
	 * @return {@code true} if the refutation score is finite, {@code false}
	 * if it is infinite.
	 */
	protected boolean refutationScoreFinite() {
		return refutationScore <= infinite;
	}
	
	protected int wrongCandidateCount() {
		return candidateCount - 1;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + column + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlankCell other = (BlankCell) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
