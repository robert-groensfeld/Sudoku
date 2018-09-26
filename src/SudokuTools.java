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

import java.io.File;

import generator.Generator;
import generator.Transformer;
import rating.Rater;
import solver.SudokuProblem;
import utile.SudokuIO;
import utile.SudokuPrinter;

/**
 * This class is responsible for launching the program.
 * @author robert
 *
 */
public class SudokuTools {
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("SudokuTools  Copyright (C) 2014 Robert Grönsfeld");
			System.out.println("This program comes with ABSOLUTELY NO WARRANTY; "
					+ "for details type \"sudokutools -w\". This is free software, "
					+ "and you are welcome to redistribute it under certain "
					+ "conditions; type \"sudokutools -c\" for details.");
			System.out.println();
			System.out.println("Please provide an argument:");
			
			System.out.println("-p PATH to print a .sdk file");
			System.out.println("-ps SHORT to print a Sudoku given in the short format");
			System.out.println("-s PATH to solve a .sdk file");
			System.out.println("-ss SHORT to solve a Sudoku given in the short format");
			System.out.println("-g to generate a Sudoku problem");
			System.out.println("-gs to generate a Sudoku in the short format");
			System.out.println("-r PATH to rate a .sdk file");
			System.out.println("-rs SHORT to rate a Sudoku in the short format");
			System.out.println("-tf PATH to get the permutation of a .sdk file");
			System.out.println("-tfs SHORT to get the permutation of a Sudoku in the short format");
			System.out.println();
			System.out.println("-t PATH to create a training table");
		}
		else if(args[0].equals("-p"))
			SudokuPrinter.print(
					SudokuIO.loadSudoku(new File(args[1])));
		else if(args[0].equals("-ps"))
			SudokuPrinter.print(SudokuIO.loadSudoku(args[1]));
		else if(args[0].equals("-s")) {
			SudokuProblem problem = new SudokuProblem(
					SudokuIO.loadSudoku(new File(args[1])));
			SudokuPrinter.print(problem.getSolution());
			int solutions = problem.getNumberOfSolutions();
			if(solutions == 1)
				System.out.println("\nThe Sudoku is unique.");
			else
				System.out.println("\nInvalid Sudoku. " + solutions + 
						" solutions found.");
		}
		else if(args[0].equals("-ss")) {
			SudokuProblem problem = new SudokuProblem(SudokuIO.loadSudoku(args[1]));
			SudokuPrinter.printShort(problem.getSolution());
			int solutions = problem.getNumberOfSolutions();
			if(solutions == 1)
				System.out.println("\nThe Sudoku is unique");
			else
				System.out.println("\nInvalid Sudoku. " + solutions + " solutions found.");
		}
		else if(args[0].equals("-g"))
			SudokuPrinter.print(Generator.generatePuzzle());
		else if(args[0].equals("-gs"))
			SudokuPrinter.printShort(Generator.generatePuzzle());
		else if(args[0].equals("-r"))
			generateRating(SudokuIO.loadSudoku(new File(args[1])));
		else if(args[0].equals("-rs"))
			generateRating(SudokuIO.loadSudoku(args[1]));
		else if(args[0].equals("-tf")) {
			Transformer transformer = new Transformer(
					SudokuIO.loadSudoku(new File(args[1])));
			transformer.transform();
			SudokuPrinter.print(transformer.getInstance());
		}
		else if(args[0].equals("-tfs")) {
			Transformer transformer = new Transformer(args[1]);
			transformer.transform();
			SudokuPrinter.printShort(transformer.getInstance());
		} 
		else if(args[0].equals("-t"))
			SudokuIO.createTrainingTable(args[1]);
		else if(args[0].equals("-w"))
			System.out.println("THERE IS NO WARRANTY FOR THE PROGRAM, TO THE "
					+ "EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN "
					+ "OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS "
					+ "AND/OR OTHER PARTIES PROVIDE THE PROGRAM \"AS IS\" "
					+ "WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR "
					+ "IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED "
					+ "WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A "
					+ "PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY "
					+ "AND PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE "
					+ "PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL "
					+ "NECESSARY SERVICING, REPAIR OR CORRECTION.");
		else if(args[0].equals("-c"))
			System.out.println("For more information on the redestribution "
					+ "of this software see enclosed license.txt");
	}

	private static void generateRating(int[][] sudoku) {
		System.out.println("Calculating rating...");
		Rater r = new Rater(sudoku);
		System.out.println("Rating terminated.");
		System.out.println("---------------------");
		System.out.println("Difficulty of advanced" +
				" techniques: " + r.getRefutationSum());
		System.out.println("Average number of hidden/naked" +
				" singles per step: " + r.getDependencyMetric());
		System.out.println("Estimated time to solve: " + r.getEstimatedTime() + "min");
	}
}
