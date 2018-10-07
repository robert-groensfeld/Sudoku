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

package gui;

import generator.Generator;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rating.Rater;
import solver.SudokuProblem;
import utile.ErrorFormatter;
import utile.SudokuIO;

/**
 * A window that provides functionality to solve Sudoku instances.
 * 
 * @author robert
 *
 */
@SuppressWarnings("serial")
public class SudokuSolver extends JFrame {

	public static void main(String[] args) {
		SudokuSolver solver = new SudokuSolver();

		try {
			String laf = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(solver);
		} catch (Exception e) {}

		solver.setVisible(true);
	}

	private Board inputBoard;
	private JButton generateButton;
	private JButton solveButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton clearButton;
	private JButton rateButton;
	private JLabel statusArea;
	String lastPath = "";

	private ActionListener generator = new ActionListener() {
	
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				inputBoard.setPuzzle(Generator.generatePuzzle());
			} catch (Exception internalError) {
				statusArea.setText("Broken Sudoku generator. Contact vendor.");
				ErrorFormatter formatter = new ErrorFormatter();
				System.out.println(formatter.format(internalError));
			}
		}
	};

	private ActionListener loader = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser openDialog;
			if (lastPath.equals(""))
				openDialog = new JFileChooser();
			else
				openDialog = new JFileChooser(lastPath);
			int state = openDialog.showOpenDialog(getMainFrame());
			if (state == JFileChooser.APPROVE_OPTION) {
				File file = openDialog.getSelectedFile();
				int[][] sudoku = SudokuIO.loadSudoku(file);
				try {
					inputBoard.setPuzzle(sudoku);
				} catch (Exception e) {
					ErrorFormatter formatter = new ErrorFormatter();
					statusArea.setText(e.getMessage());
					System.out.println(formatter.format(e));
				}
				lastPath = file.getParent();
			}
		}
	};

	private ActionListener saver = new ActionListener() {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser saveDialog;
			if (lastPath.equals(""))
				saveDialog = new JFileChooser();
			else
				saveDialog = new JFileChooser(lastPath);
			int state = saveDialog.showSaveDialog(getMainFrame());
			if (state == JFileChooser.APPROVE_OPTION) {
				File file = saveDialog.getSelectedFile();
				SudokuIO.saveSudoku(file, inputBoard.getPuzzle());
				lastPath = file.getParent();
			}
		}
	};

	private ActionListener solver = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				SudokuProblem sp = new SudokuProblem(inputBoard.toIntArray());
				if (sp.hasSolution())
					inputBoard.solve(sp.getSolution());
				int count = sp.getNumberOfSolutions();
				if (count == 1)
					statusArea.setText("Solution found.");
				else if (count == 0)
					statusArea.setText("Unable to solve: illegal user input.");
				else if (count > 1)
					statusArea.setText("Puzzle has more than one solution.");
			} catch (Exception e) {
				statusArea.setText("Broken solver. Contact the vendor.");
				ErrorFormatter formatter = new ErrorFormatter();
				System.out.println(formatter.format(e));
			}
		}
	};

	private ActionListener clearer = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Cell[] row : inputBoard.cells) {
				for (Cell cell : row) {
					if (!cell.isOccupied()) {
						cell.clear();
					}
				}
			}
		}
	};

	private ActionListener rater = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Rater rater = new Rater(inputBoard.toIntArray());
			int time = rater.getEstimatedTime();
			statusArea.setText("Time to solve: ca. " + time + " minutes.");
		}
	};

	public SudokuSolver() {
		this.setTitle("Sudoku Tools");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		statusArea = new JLabel("- ~ -");
		
		try {
			inputBoard = new Board(Generator.generatePuzzle());
		} catch (Exception e) {
			statusArea.setText("Broken Sudoku generator. Contact vendor.");
			try {
				inputBoard = new Board();
			} catch (Exception internalError) {
				ErrorFormatter formatter = new ErrorFormatter();
				System.out.println(formatter.format(internalError));
				System.exit(0);
			}
		}
		this.add(inputBoard, gbc);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		gbc.gridy++;
		statusPanel.add(statusArea);
		this.add(statusPanel, gbc);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());

		loadButton = new JButton("load");
		buttons.add(loadButton);

		saveButton = new JButton("save");
		buttons.add(saveButton);

		generateButton = new JButton("new");
		buttons.add(generateButton);
		
		solveButton = new JButton("solve");
		buttons.add(solveButton);
		
		clearButton = new JButton("clear");
		buttons.add(clearButton);
		
		rateButton = new JButton("rate");
		buttons.add(rateButton);
		
		gbc.gridy++;
		this.add(buttons, gbc);
		
		solveButton.addActionListener(solver);
		generateButton.addActionListener(generator);
		loadButton.addActionListener(loader);
		saveButton.addActionListener(saver);
		clearButton.addActionListener(clearer);
		rateButton.addActionListener(rater);
		
		this.pack();
	}

	private SudokuSolver getMainFrame() {
		return this;
	}
}
