package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import sudoku.Resolver;
import sudoku.Result;

public class MainWindow extends JFrame 
{

	private static final long serialVersionUID = 5936802167406026028L;
	private JPanel contentPane;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MainWindow() 
	{
		setFont(new Font("Monospaced", Font.PLAIN, 11));
		setTitle("Sudoku 3x3 Resolver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 605, 366);
		setLocationRelativeTo(null);
		
		JTextPane textPane = new JTextPane();
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Monospaced", Font.PLAIN, 11));
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(new Font("Monospaced", Font.PLAIN, 11));
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_MASK));
		mntmOpen.setFont(new Font("Monospaced", Font.PLAIN, 11));
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(null);
                
                if (result == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    try {
						BufferedReader br = new BufferedReader(new FileReader(file));
						
						textPane.setText("");
						
						while (br.ready()) {
							textPane.setText(textPane.getText() + br.readLine() + "\n");
						}
						
						br.close();
					} 
                    catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} 
                    catch (IOException e1) {
						e1.printStackTrace();
					}
                    
                }
                else {
                	JOptionPane.showMessageDialog(null, "File not found!", "Error Message", JOptionPane.ERROR_MESSAGE);
            	}
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
		mntmSave.setFont(new Font("Monospaced", Font.PLAIN, 11));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
			    int result = fc.showSaveDialog(null);
			    
			    if (result == JFileChooser.APPROVE_OPTION) {
			    	File file = fc.getSelectedFile();
			    	try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						String[] linesContent = textPane.getText().split("\n");
						
						for (int i = 0; i < linesContent.length; ++i) {
							bw.write(linesContent[i] + "\n");
						}
						
						bw.close();
			    	} 
			    	catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			    else {
			    	JOptionPane.showMessageDialog(null, "File not found!", "Error Message", JOptionPane.ERROR_MESSAGE);
			    }
			}
		});
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mntmClear.setFont(new Font("Monospaced", Font.PLAIN, 11));
		mnFile.add(mntmClear);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		mntmClear.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				textPane.setText("");
			}
		});
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
		);
		
		JPanel panel_1 = new JPanel();
		
		JLabel lblAlgorithm = new JLabel("Algorithm");
		lblAlgorithm.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Monospaced", Font.PLAIN, 11));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"BT", "BT + MRV", "BT + FC", "BT + MRV + FC"}));
		
		JLabel lblLimitAssignments = new JLabel("Limit Assignments");
		lblLimitAssignments.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1000000, 1, 2147483647, 1));
		spinner.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		JCheckBox chckbxLimit = new JCheckBox("Limit");
		
		chckbxLimit.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent arg0) 
			{
				spinner.setEnabled(chckbxLimit.isSelected());    //Enable the edition of max number of assignments only when the 'Limit' flag is enable.
				lblLimitAssignments.setEnabled(chckbxLimit.isSelected());
			}
		});
		chckbxLimit.setSelected(true);
		chckbxLimit.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		JButton btnResolv = new JButton("Resolv");
		
		btnResolv.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 13));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Monospaced", Font.BOLD, 11));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
		);
		
		JLabel lblLimitTime = new JLabel("Limit Time (ms)");
		lblLimitTime.setEnabled(false);
		lblLimitTime.setFont(new Font("Monospaced", Font.BOLD, 11));
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setEnabled(false);
		spinner_1.setModel(new SpinnerNumberModel(new Long(60000), new Long(1), new Long(2147483647), new Long(1)));
		spinner_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		
		JCheckBox chckbxLimit_1 = new JCheckBox("Limit");
		
		chckbxLimit_1.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent arg0) 
			{
				spinner_1.setEnabled(chckbxLimit_1.isSelected());    //Enable the edition of max time only when the 'Limit' flag is enable.
				lblLimitTime.setEnabled(chckbxLimit_1.isSelected());
			}
		});
		chckbxLimit_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(spinner_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
						.addComponent(comboBox, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addContainerGap(55, Short.MAX_VALUE)
							.addComponent(lblLimitAssignments))
						.addComponent(spinner, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addContainerGap(69, Short.MAX_VALUE)
							.addComponent(lblLimitTime))
						.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
							.addContainerGap(111, Short.MAX_VALUE)
							.addComponent(lblAlgorithm))
						.addComponent(chckbxLimit)
						.addComponent(chckbxLimit_1)
						.addComponent(btnResolv, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblAlgorithm)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLimitTime)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinner_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxLimit_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLimitAssignments, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxLimit)
					.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
					.addComponent(btnResolv))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Input", null, panel_2, null);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
		);
		
		textPane.setCaretColor(Color.BLACK);
		textPane.setBackground(Color.WHITE);
		textPane.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textPane.setForeground(Color.BLACK);
		scrollPane.setViewportView(textPane);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Output", null, panel_4, null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
		);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setForeground(Color.GREEN);
		textPane_2.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textPane_2.setEditable(false);
		textPane_2.setCaretColor(Color.GREEN);
		textPane_2.setBackground(Color.BLACK);
		scrollPane_2.setViewportView(textPane_2);
		panel_4.setLayout(gl_panel_4);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Formatted Output", null, panel_3, null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
		);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setEditable(false);
		textPane_1.setCaretColor(Color.GREEN);
		textPane_1.setForeground(Color.GREEN);
		textPane_1.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textPane_1.setBackground(Color.BLACK);
		scrollPane_1.setViewportView(textPane_1);
		panel_3.setLayout(gl_panel_3);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
		
		btnResolv.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				ArrayList<int[][]> problems = new ArrayList<int[][]>(); 
				String lines[] = textPane.getText().split("\n");    //Split the input content into lines.
				int numInputs, currentLine = 0;
				
				try {
					numInputs = Integer.valueOf(lines[currentLine].trim());
					++currentLine;
					
					if (numInputs <= 0) JOptionPane.showMessageDialog(null, "Number of inputs not accepted!", "Error Message", JOptionPane.ERROR_MESSAGE);
					else if (numInputs + numInputs*Resolver.MATRIX_DIMENSION > lines.length) JOptionPane.showMessageDialog(null, "Number of problems differs from the specified!", "Error Message", JOptionPane.ERROR_MESSAGE);
					else {
						for (int i = 0; i < numInputs; ++i) {    //Reading all the problems from the input.
							problems.add(new int[Resolver.MATRIX_DIMENSION][Resolver.MATRIX_DIMENSION]);    //Adding a new sudoku problem.
							
							for (int j = 0; j < Resolver.MATRIX_DIMENSION; ++j) {    //Reading the lines of the current problem.
								String data[] = lines[currentLine].trim().split(" ");    //Reading the columns of the current problem.
								
								++currentLine;
								
								if (data.length != Resolver.MATRIX_DIMENSION) JOptionPane.showMessageDialog(null, "Malformed problem (line " + currentLine + ")!", "Error Message", JOptionPane.ERROR_MESSAGE);
								else {
									for (int k = 0; k < Resolver.MATRIX_DIMENSION; ++k) {
										try {
											problems.get(problems.size()-1)[j][k] = Integer.valueOf(data[k].trim());
											
											if (problems.get(problems.size()-1)[j][k] < 0 || problems.get(problems.size()-1)[j][k] > 9) {
												JOptionPane.showMessageDialog(null, "Malformed problem (line " + currentLine + ")!", "Error Message", JOptionPane.ERROR_MESSAGE);
												return;
											}
										}
										catch (NumberFormatException e3) {
											JOptionPane.showMessageDialog(null, "Malformed problem (line " + currentLine + ")!", "Error Message", JOptionPane.ERROR_MESSAGE);
											return;
										}
									}
								}
							}
							
							++currentLine;
						}
						
						Resolver resolver = new Resolver();
						ArrayList<Result> results;
						long maxTime = -1; 
						int maxAssignments = -1, numberOfProblem = 1;
						StyledDocument styledDocument = textPane_1.getStyledDocument();
				        Style style = textPane_1.addStyle("", null);
				        String output = numInputs + "\n";
						
						if (spinner.isEnabled()) maxAssignments = (int) spinner.getValue();
						if (spinner_1.isEnabled()) maxTime = (long) spinner_1.getValue();
						
						//Selecting the algorithm:
						switch (comboBox.getSelectedIndex()) {
							case 0: {    //Simple Backtracking.
								resolver.setMVR(false);
								resolver.setFC(false);
							} break;
							case 1: {    //Backtracking with MRV.
								resolver.setMVR(true);
								resolver.setFC(false);
							} break;
							case 2: {    //Backtracking with FC.
								resolver.setMVR(false);
								resolver.setFC(true);
							} break;
							default: {    //Backtracking with MRV+FC.
								resolver.setMVR(true);
								resolver.setFC(true);
							}
						}
						
						//Callback to resolve the problems:
						results = resolver.resolveAll(problems, maxTime, maxAssignments);
						
						//Showing the results:
						textPane_1.setText("");
						textPane_2.setText("");
						tabbedPane.setSelectedIndex(2);    //Redirect the user to the 'Output' tab pane.
						
						for (Result result : results) {
							String newFormattedOutput = " --------------------------------------------------\n ";
							
							if (result.isSolved()) {
								StyleConstants.setForeground(style, Color.GREEN);
								newFormattedOutput += numberOfProblem++ + " - SOLUTION FOUND!\n INPUT:\t\t\t OUTPUT:";
							}
							else {
								StyleConstants.setForeground(style, Color.RED);
								newFormattedOutput += numberOfProblem++ + " - SOLUTION NOT FOUND!\n INPUT:\t\t\t OUTPUT:";
							}
							
							for (int i = 0; i < Resolver.MATRIX_DIMENSION; ++i) {
								if (i%Resolver.SUBSET_DIMENSION == 0) newFormattedOutput += "\n";
								
								//Printing the problem:
								for (int j = 0; j < Resolver.MATRIX_DIMENSION; ++j) {
									if (j%Resolver.SUBSET_DIMENSION == 0) newFormattedOutput += " ";
									
									newFormattedOutput += result.getProblem()[i][j] + " ";
								}
								
								newFormattedOutput += "\t";
								
								//Printing the result:
								for (int j = 0; j < Resolver.MATRIX_DIMENSION; ++j) {
									if (j%Resolver.SUBSET_DIMENSION == 0) newFormattedOutput += " ";
									
									newFormattedOutput += result.getResult()[i][j] + " ";
									output += result.getResult()[i][j] + " ";
								}
								
								newFormattedOutput += "\n";
								output += "\n";
							}
							
							newFormattedOutput += " --------------------------------------------------\n *Time: " + result.getTime() +"ms";
							
							if (maxTime > 0 && result.getTime() > maxTime) newFormattedOutput += " (time exceeded)"; 
							
							newFormattedOutput += "\n *Assignments: " + result.getAssignments();
							
							if (maxAssignments > 0 && result.getAssignments() > maxAssignments) newFormattedOutput += " (assignments exceeded)";
							
							newFormattedOutput += "\n --------------------------------------------------\n\n\n\n";
							
							styledDocument.insertString(styledDocument.getLength(), newFormattedOutput, style);
							output += "\n";
						}
						
						textPane_2.setText(output);
					}
				}
				catch (NumberFormatException | BadLocationException e2) {
					JOptionPane.showMessageDialog(null, "Wrong number of inputs!", "Error Message", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
