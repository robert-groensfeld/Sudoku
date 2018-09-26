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
	private JButton solveButton;
	private JButton loadButton;
	private JButton clearButton;
	private JButton rateButton;
	private JLabel statusArea;
	String lastPath = "";

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
				inputBoard.fill(sudoku);
				lastPath = file.getParent();
			}
		}
	};

	private ActionListener solver = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			long start = System.currentTimeMillis();
			SudokuProblem sp = new SudokuProblem(inputBoard.toIntArray());
			long time = System.currentTimeMillis() - start;
			inputBoard.fill(sp.getSolution());
			int count = sp.getNumberOfSolutions();
			if (count == 1)
				statusArea.setText("Solution found.");
			else if (count == 0)
				statusArea.setText("Unable to complete: illegal user input.");
			else if (count > 1)
				statusArea.setText("This puzzle has more than one solution.");
		}
	};

	private ActionListener clearer = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (Cell[] row : inputBoard.cells) {
				for (Cell cell : row) {
					if (!cell.isOccupied()) {
						cell.setValue(0);
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
		
		inputBoard = new Board(Generator.generatePuzzle());
		this.add(inputBoard, gbc);
		
		statusArea = new JLabel("- ~ -");
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		gbc.gridy++;
		statusPanel.add(statusArea);
		this.add(statusPanel, gbc);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());

		loadButton = new JButton("load");
		buttons.add(loadButton);
		
		solveButton = new JButton("solve");
		buttons.add(solveButton);
		
		clearButton = new JButton("clear");
		buttons.add(clearButton);
		
		rateButton = new JButton("rate");
		buttons.add(rateButton);
		
		gbc.gridy++;
		this.add(buttons, gbc);
		
		solveButton.addActionListener(solver);
		loadButton.addActionListener(loader);
		clearButton.addActionListener(clearer);
		rateButton.addActionListener(rater);
		
		this.pack();
	}

	private SudokuSolver getMainFrame() {
		return this;
	}
}
