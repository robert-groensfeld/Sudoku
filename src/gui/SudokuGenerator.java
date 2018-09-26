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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import solver.Solver;

@SuppressWarnings("serial")
public class SudokuGenerator extends JFrame {

	private Board instance;
	private Board solution;
	private JButton generateButton;
	private JButton solveButton;
	
	private ActionListener generator = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			instance.alter(Generator.generateInstance());
		}
	};
	
	private ActionListener solver = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Solver.findSolution(instance.toIntArray());
			solution.alter(Solver.getSolution());
		}
	};
	
	public SudokuGenerator() {
		this.setTitle("Sudoku generator");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;

		instance = new Board();
		this.add(instance, gbc);
		gbc.gridx++;
		
		solution = new Board();
		this.add(solution, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		generateButton = new JButton("generate");
		buttons.add(generateButton, gbc);
		
		solveButton = new JButton("solve");
		buttons.add(solveButton);
		
		generateButton.addActionListener(generator);
		solveButton.addActionListener(solver);
		
		gbc.gridwidth = 2;
		this.add(buttons, gbc);

		this.pack();
	}
}
