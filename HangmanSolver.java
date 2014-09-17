import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

public class HangmanSolver {
	private static JFrame frame; // main frame
	private static JButton finddictionary; // TicTacToe button
	private static JButton build; // Tetris button
	private static JButton applyWord; //Title button
	private static JFormattedTextField wordLengthTextField;
	private static JFormattedTextField wordStringTextField;
	private static BufferedImage background; //Arcade bg image
	private static JMenuBar menuBar = new JMenuBar();
	private static JMenu helpMenu = new JMenu("Help");
	private static JMenuItem instructions = new JMenuItem("Instructions");
	private static ActionListener MAListener;



	public static int wordlength = 0;	
	public static String wordString = "";
	public static String[] alphabet = {"-", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
		"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
		"u", "v", "w", "x", "y", "z"};
	//panels
	private static JPanel panel;
	private static JPanel titlePanel;
	private static JPanel dictionaryEdits;
	private static JPanel wordEdits;
	private static JPanel directions;
	private static JPanel letterBoxes;
	//labels
	public static JLabel wordlengthlabel;
	public static JLabel wordStringLabel;
	public static JLabel bestGuess = new JLabel("");
	public static JLabel bestGuessLabel = new JLabel("Guess: ");
	public static JFormattedTextField[] letterInputs = new JFormattedTextField[0];
	public static JLabel directionsLabel = new JLabel("Directions");
	public static JTextArea directionsText = new JTextArea();

	public static JFileChooser dictionarychooser;
	public static SolverEngine engine;
	public static FileNameExtensionFilter filter;
	public static File file = null;



	//STARTING MAIN METHOD
	//-------------------------------------------------------------------------------------------------
	public static void main(String[] args) throws IOException {

		//Initialize panels
		panel = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, null);
			} };
			background = ImageIO.read(new File("background.png"));

			panel.setLayout(new BorderLayout());
			titlePanel = new JPanel();
			dictionaryEdits = new JPanel();
			wordEdits = new JPanel();
			directions = new JPanel();
			letterBoxes = new JPanel();
			
			menuBar = new JMenuBar();
			helpMenu = new JMenu("Help");

			// Create a new JFrame
			frame = new JFrame("Hangman Solver");
			// Set a minimum size for the JFrame

			frame.setMaximumSize(new Dimension(1000, 400));
			frame.setMinimumSize(new Dimension(1000, 400));
			// Give the JFrame the ability to be closed
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// Center the frame
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			//Adding frames
			frame.getContentPane().add(panel);
			panel.add(titlePanel, BorderLayout.NORTH);
			titlePanel.setOpaque(false);
			panel.add(dictionaryEdits, BorderLayout.WEST);
			dictionaryEdits.setOpaque(false);
			panel.add(wordEdits, BorderLayout.CENTER);
			wordEdits.setOpaque(false);
			panel.add(directions, BorderLayout.EAST);
			directions.setOpaque(false);
			panel.add(letterBoxes, BorderLayout.SOUTH);

			
			



			//Setting up title
			JLabel title = new JLabel("Hangman Solver", JLabel.CENTER);
			title.setFont(new Font("Helvetica", Font.PLAIN, 50));
			title.setForeground(Color.black);

			//Set up file chooser
			filter = new FileNameExtensionFilter("txt and data files", "txt", "data"); //limiting filetypes
			dictionarychooser = new JFileChooser();
			dictionarychooser.setFileFilter(filter);
			

			try { //Look for default dictionary file.
				file = new File("dictionary.data");
				Scanner scanner = null;
				scanner = new Scanner(file);
			} catch (FileNotFoundException e) { //Notify user if default not found and set file to null.
				JOptionPane.showMessageDialog(frame, "The default dictionary file (dictionary.data) was not found. Please select a dictionary file.", 
						"NO DICTIONARY FILE FOUND", JOptionPane.PLAIN_MESSAGE);
				file = null;
			}


			//Button Listener 
			class ButtonActionListener implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent event) {
					final String[] empty = new String[0];
					if(event.getSource() == finddictionary) { //Open a dictionary file
						int returnValue = dictionarychooser.showOpenDialog(titlePanel);
						if(returnValue == JFileChooser.APPROVE_OPTION) {
							file = dictionarychooser.getSelectedFile();
						}
					}
					if(event.getSource() == build) { //USER PRESSED BUILD BUTTON
						boolean buildCondition = true;
						if(wordlength == 0){ //Assert that wordlength is not 0
							JOptionPane.showMessageDialog(frame, "Please enter a valid word length!", "UNSPECIFIED WORD LENGTH", JOptionPane.PLAIN_MESSAGE);
							buildCondition = false;
						}
						if(file == null){ //Assert that a dictionary file has been selected
							JOptionPane.showMessageDialog(frame, "Please select a dictionary file.", "NO DICTIONARY FILE FOUND", JOptionPane.PLAIN_MESSAGE);
							buildCondition = false;
						}
						if(buildCondition == true){  //The user is able to build the dictionary
							System.out.println("BUILDING..");
							applyWord.setEnabled(true);
							build.setEnabled(false);
							engine = new SolverEngine(wordlength, file); //Start engine passing wordlength and dictionary file.

							letterInputs = new JFormattedTextField[wordlength];

							/* Setting uptext field space for each letter in the word */
							letterBoxes.setLayout(new GridLayout(2, wordlength));
							for(int i = 0; i < wordlength; i++){
								wordString+= "-";
								JFormattedTextField letterField = new JFormattedTextField(createFormatter("?"));
								letterInputs[i] = letterField;
								letterBoxes.add(letterInputs[i], BorderLayout.AFTER_LAST_LINE);								
							}
							letterBoxes.add(new JLabel("     "));
							for(int i = 1; i < wordlength + 1; i++){
								String x = Integer.toString(i);
								JLabel letterFieldLabel = new JLabel(x);
								letterBoxes.add(letterFieldLabel);
							}

							/* Generating first guess based on word length */
							char guess = engine.generateMove(wordString, null);
							bestGuess.setText(Character.toString(guess).toUpperCase());
							bestGuess.setFont(new Font("Helvetica", Font.PLAIN, 50));
							bestGuess.setForeground(Color.red);
							applyWord.setVisible(true); //Enable the apply word button
						}
					}
					if(event.getSource() == applyWord){ //USER PRESED "UPDATE WORD"
						String x = "";


						for(int i = 0; i < wordlength; i++){
							if(letterInputs[i].getText().equalsIgnoreCase(" ")){ //Letterbox was empty
								x+= "-";
							}else{
								x += letterInputs[i].getText(); //Letterbox contained letters
							}
						}

						if(x.equalsIgnoreCase(wordString)){ //indicates that the last guess was incorrect
							applyWord.setText("Apply Word (last guess was wrong)");
						}else{
							applyWord.setText("Apply Updated Word");
						}

						wordString = x;
						System.out.println(wordString);

						if(wordlength != wordString.length()){
							JOptionPane.showMessageDialog(frame, "Please enter a word that is the same length as the specified word length. "
									+ "Unguessed letters should be replaced by hyphens '-' ",
									"SPECIFIED WORD LENGTH MISMATCH", JOptionPane.PLAIN_MESSAGE);
						}
						char guess = engine.generateMove(wordString, null);

						bestGuess.setText(Character.toString(guess).toUpperCase());
						bestGuess.setFont(new Font("Helvetica", Font.PLAIN, 40));


						System.out.println("GUESS: " + guess);
					}
				}				
			}
			// Text Field Listener
			class FormattedTextFieldListener implements PropertyChangeListener {
				@Override
				public void propertyChange(PropertyChangeEvent e) {
					Object source = e.getSource();
					if (source == wordLengthTextField) { //User updated a new word of a new length
						build.setEnabled(true);
						wordlength = ((Number)wordLengthTextField.getValue()).intValue();
					} 
					if (source == wordStringTextField){ //User updated the word string


						System.out.println(wordString);
					}

				}
			}
			class MenuActionListener implements ActionListener {
				public void actionPerformed(ActionEvent event) {
					if(event.getSource() == instructions) { //Display instructions dialog and pause the game
						JOptionPane.showMessageDialog(frame, 
								"Welcome to the Hangman Solver\n\n"
								+ "Hangman Solver takes input for a word of a specified length, generating the best possible guess"
								+ " each time it is updated.\n"
								+ "1) 		If you are not using the default dictionary, select a dictionary file (.data or .txt).\n"
										+ "	Make sure  words in the dictionary are separated by new lines only.\n"
										+ "2) Specify the length of the word you are trying to solve, then select 'build'.\n "
										+ "You have now narrowed down to the words of that length and generated the best guess.\n"
										+ "3) After guessing the first letter, enter solved letters for the word in the corresponding\n"
										+ " textbox. Make sure the letters that have been solved are in the correct position. \n"
										+ "If the generated guess was incorrect, leave the word string as it was. \n"
										+ "This indicates to the solver that the word does not contain that letter.\n"
										+ "4) Press 'Apply Updated Word' once you have put any correctly guessed letters in their place.\n"
										+ "5) Repeat step 4 for each guess until the word is solved.\n"
										+ "\nEnjoy! - Jake Webber",
								"INSTRUCTIONS", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			MAListener = new MenuActionListener();
			menuBar.add(helpMenu, BorderLayout.PAGE_END);
			instructions = new JMenuItem("Usage Instructions");
			instructions.addActionListener(MAListener);
			helpMenu.add(instructions);
			frame.setJMenuBar(menuBar);
			
			//Setting up Find Dictionary button
			finddictionary = new JButton();
			finddictionary.addActionListener(new ButtonActionListener());
			finddictionary.setText("Find Dictionary");

			//Setting up Build Button
			build = new JButton();
			build.addActionListener(new ButtonActionListener());
			build.setPreferredSize(new Dimension(50, 100));
			build.setText("Build!");

			//Setting up Apply Word Button
			applyWord = new JButton();
			applyWord.addActionListener(new ButtonActionListener());
			applyWord.setPreferredSize(new Dimension(20, 50));
			applyWord.setText("Apply Updated Word");
			applyWord.setEnabled(false);

			//Setting up Word Count Text Field
			wordlengthlabel = new JLabel("Word Length:");
			wordlengthlabel.setLabelFor(wordLengthTextField);
			wordLengthTextField = new JFormattedTextField();
			wordLengthTextField.setValue(new Integer(0));
			wordLengthTextField.setColumns(1);
			wordLengthTextField.setSize(22, 100);
			wordLengthTextField.addPropertyChangeListener("value", new FormattedTextFieldListener());
			//Setting up Word String Text Field
			wordStringLabel = new JLabel("Word:");
			wordStringLabel.setLabelFor(wordStringTextField);
			wordStringTextField = new JFormattedTextField();
			wordStringTextField.setValue(new String("-"));
			wordStringTextField.addPropertyChangeListener("value", new FormattedTextFieldListener());

			//Adding components to panels
			
			titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
			titlePanel.add(title);
			titlePanel.add(Box.createRigidArea(new Dimension(50, 50)));

			/*----------PANELS SETUP--------*/
			
			//WORDEDITS Panel
			wordEdits.setLayout(new BoxLayout(wordEdits, BoxLayout.Y_AXIS));
			wordEdits.setPreferredSize(new Dimension(200, 100));
			wordEdits.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.blue),
					dictionaryEdits.getBorder()));
			wordEdits.setBackground(Color.lightGray);
			
			//LETTERBOXES Panel
			letterBoxes.add(wordStringLabel);
			wordStringLabel.setPreferredSize(new Dimension(100, 20));

			//WORDEDITS Panel
			
			wordEdits.add(bestGuessLabel);
			bestGuessLabel.setFont(new Font("Helvetica", Font.PLAIN, 30));
			wordEdits.add(bestGuess);
			wordEdits.add(Box.createRigidArea(new Dimension(0,50)));

			wordEdits.add(applyWord);
			applyWord.setVisible(false);

			//dictionaryEdits Panel
			
			dictionaryEdits.setLayout(new BoxLayout(dictionaryEdits, BoxLayout.Y_AXIS));
			dictionaryEdits.setBackground(Color.lightGray);
			dictionaryEdits.setPreferredSize(new Dimension(150, 100));
			dictionaryEdits.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.black),
					dictionaryEdits.getBorder()));
			dictionaryEdits.add(finddictionary);
			dictionaryEdits.add(Box.createRigidArea(new Dimension(0,40)));
			dictionaryEdits.add(wordlengthlabel);
			dictionaryEdits.add(wordLengthTextField);
			dictionaryEdits.add(Box.createRigidArea(new Dimension(0,70)));

			dictionaryEdits.add(build);
			//Directions Panel
			
			directions.setLayout(new BoxLayout(directions, BoxLayout.Y_AXIS));
			directionsLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
			
			directions.setPreferredSize(new Dimension(400, 200));
			
		


			// Gets the frame ready to be visible
			frame.pack();

			// Makes the frame visible
			frame.setVisible(true);
	}
	//-------------------------------------------------------------------------------------------------
	//END OF MAIN METHOD


	protected static MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (java.text.ParseException exc) {
			System.err.println("formatter is bad: " + exc.getMessage());
			System.exit(-1);
		}
		return formatter;
	}



}