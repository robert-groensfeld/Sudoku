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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import utile.SudokuIO;

/**
 * This window provides functionality to open and create Sudoku files.
 * @author robert
 *
 */
@SuppressWarnings("serial")
public class SudokuExplorer extends JFrame {

	private Board currentBoard;
	private JButton saveButton;
	private JButton loadButton;
	
	private ActionListener saver = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser saveDialog = new JFileChooser();
			int state = saveDialog.showSaveDialog(getMainFrame());
			if(state == JFileChooser.APPROVE_OPTION) {
				File file = saveDialog.getSelectedFile();
				int[][] sudoku = currentBoard.toIntArray();
				SudokuIO.saveSudoku(file, sudoku);
				currentBoard.alter(new int[9][9]); // clear board
			}
		}
	};
	
	private ActionListener loader = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser openDialog = new JFileChooser();
			int state = openDialog.showOpenDialog(getMainFrame());
			if(state == JFileChooser.APPROVE_OPTION) {
				File file = openDialog.getSelectedFile();
				int[][] sudoku = SudokuIO.loadSudoku(file);
				currentBoard.alter(sudoku);
			}
		}
	};
	
	public SudokuExplorer() {
		this.setTitle("Sudoku explorer");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		currentBoard = new Board();
		this.add(currentBoard, gbc);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		saveButton = new JButton("save");
		buttons.add(saveButton);
		
		loadButton = new JButton("load");
		buttons.add(loadButton);
		
		gbc.gridy++;
		this.add(buttons, gbc);
		
		saveButton.addActionListener(saver);
		loadButton.addActionListener(loader);

		this.pack();
	}
	
	private SudokuExplorer getMainFrame() {
		return this;
	}
}
