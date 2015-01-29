import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

public class WordSolver {
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
	public static String incorrectGuesses = "";
	public static String wordStringTemp = "";
	public static String incorrectGuessesTemp = "";
	//GRAPHIC DISPLAY VARIABLES
	public static String[] alphabet =  new String[26];
	//{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
	//"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	public static double[] letterFrequencies = new double[26];

	//panels
	private static JPanel panel;
	private static JPanel titlePanel;
	private static JPanel dictionaryEdits;
	private static JPanel wordEdits;
	private static JPanel wordEditsSub;
	private static JPanel wordEditsButtons;
	private static JPanel graphPanel;
	private static JPanel letterBoxes;
	//labels
	public static JLabel step1 = new JLabel ("Step 1");
	public static JLabel wordStringLabel;
	public static JLabel incorrectLabel = new JLabel ("Incorrect: ");
	public static JLabel incorrectGuessesLabel;
	public static JLabel bestGuess = new JLabel("");
	public static JLabel bestGuessLabel = new JLabel("Most Common Letter: ");
	public static JFormattedTextField[] letterInputs = new JFormattedTextField[0];
	public static JFormattedTextField incorrectGuessesField;
	public static JFormattedTextField letterField = new JFormattedTextField(createFormatter("?"));
	public static JLabel graphPanelLabel = new JLabel("graphPanel");
	//borders
	public static Border blackline = BorderFactory.createLineBorder(Color.black);
	public static TitledBorder incorrectGuessesTitle;
	public static TitledBorder wordLengthTitle;
	public static TitledBorder dictionaryEditsTitle;
	public static TitledBorder wordEditsTitle;

	public static JFileChooser dictionarychooser;
	public static SolverEngine engine;
	public static FileNameExtensionFilter filter;
	public static File file = null;

	public static String[] wordLengthArray = {"2", "3", "4", "5", "6", "7", "8", "9", "10", 
		"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",};


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JComboBox wordLengthComboBox = new JComboBox(wordLengthArray);


	//STARTING MAIN METHOD
	//-------------------------------------------------------------------------------------------------
	public static void main(String[] args) throws IOException {

		//Initialize panels
		panel = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, null);
			} };
		
			background = ImageIO.read(ResourceLoader.load("background.png"));
			GridBagConstraints c = new GridBagConstraints();
			titlePanel = new JPanel();
			dictionaryEdits = new JPanel();
			wordEdits = new JPanel();
			wordEditsSub = new JPanel();
			wordEditsButtons = new JPanel();
			graphPanel = new JPanel();
			letterBoxes = new JPanel();

			menuBar = new JMenuBar();
			helpMenu = new JMenu("Help");

			// Create a new JFrame
			frame = new JFrame("WORD Solver");
			// Set a minimum size for the JFrame
			frame.setMaximumSize(new Dimension(1000, 400));
			frame.setMinimumSize(new Dimension(1000, 400));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.getContentPane().add(panel);
			
			titlePanel.setOpaque(false);
			dictionaryEdits.setOpaque(false);
			wordEdits.setOpaque(false);
			letterBoxes.setOpaque(false);
			graphPanel.setOpaque(false);
			
			panel.setLayout(new GridBagLayout());

			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx = 0.5;
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 3;
			panel.add(titlePanel, c);

			c.gridy = 1;
			c.gridwidth = 1;
			panel.add(dictionaryEdits, c);
			
			c.gridx = 1;
			c.insets = new Insets(0,10,0,10); 

			panel.add(wordEdits, c);
			c.insets = new Insets(0,0,0,0); 

			c.gridx = 2;
			panel.add(graphPanel,c);
			
			c.gridy = 2;
			c.gridx = 0;
			c.gridwidth = 3;
			panel.add(letterBoxes, c);
			frame.setBackground(new Color(240, 100, 100));

			incorrectGuessesTitle = BorderFactory.createTitledBorder("Incorrect Update");
			
			incorrectGuessesTitle.setTitleJustification(TitledBorder.LEFT);
			incorrectGuessesField = new JFormattedTextField(createFormatter("?"));
			incorrectGuessesField.setValue(null);
			incorrectGuessesField.setVisible(false);
			incorrectGuessesField.setEnabled(false);
			incorrectGuessesField.setBackground(new Color(245, 250, 252));
			incorrectLabel.setVisible(false);
			bestGuess.setVisible(false); //Enable the apply word button
			bestGuessLabel.setVisible(false);
			incorrectGuessesLabel = new JLabel();
			

			wordLengthTitle = BorderFactory.createTitledBorder("Word Length");
			wordLengthTitle.setTitleJustification(TitledBorder.CENTER);
			dictionaryEditsTitle = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1, Color.gray, Color.DARK_GRAY), 
					"Dictionary Edits", TitledBorder.CENTER, TitledBorder.TOP, new Font("ARIAL", Font.BOLD, 15));
			dictionaryEditsTitle.setTitleJustification(TitledBorder.CENTER);
			wordEditsTitle = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1, Color.gray, Color.DARK_GRAY), 
					"Word Updates", TitledBorder.CENTER, TitledBorder.TOP, new Font("ARIAL", Font.BOLD, 15));
			wordEditsTitle.setTitleJustification(TitledBorder.CENTER);

			//Setting up title
		

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

			wordLengthComboBox.setSelectedIndex(0);
			wordLengthComboBox.setBorder(wordLengthTitle);
			wordLengthComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent arg0) {
					String length = (String) wordLengthComboBox.getSelectedItem();
					System.out.println(length);
					wordlength = Integer.parseInt(length);
				}
			});
			if(file != null){
				step1.setText("Step 1 (Complete)");
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
						if(buildCondition == true){  //The user is able to build the dictionary & wordlength is not 0
							System.out.println("BUILDING..");
							build.setText("Build!  (Reset)");
							//build.setEnabled(false);
							engine = new SolverEngine(wordlength, file); //Start engine passing wordlength and dictionary file.
							letterInputs = new JFormattedTextField[wordlength];

							/* Setting uptext field space for each letter in the word */
							letterBoxes.removeAll();
							wordString = "";
							incorrectGuesses = "";
							incorrectGuessesLabel.setText("");

							letterBoxes.setLayout(new GridLayout(2, wordlength));
							for(int i = 0; i < wordlength; i++){
								wordString+= "-";
								letterField = new JFormattedTextField(createFormatter("?"));
								letterInputs[i] = letterField;
								letterBoxes.add(letterInputs[i], BorderLayout.AFTER_LAST_LINE);								
							}
							letterBoxes.add(new JLabel("  "));
							for(int i = 1; i < wordlength + 1; i++){
								String x = Integer.toString(i);
								JLabel letterFieldLabel = new JLabel(x);
								letterBoxes.add(letterFieldLabel); //add number to letter boxes
							}
							letterBoxes.updateUI();
							/* Generating first guess based on word length */
							String[][] guess = engine.generateMove(wordString, null);
							for(int i = 0; i < 26; i++){
								alphabet[i] = guess[0][i];
							}
							for(int j = 0; j < 26; j++){
								letterFrequencies[j] = Integer.parseInt(guess[1][j]);
							}
							bestGuess.setText(guess[0][0].toUpperCase());
							graphPanel.removeAll();
							graphPanel.add(new graphDisplay(letterFrequencies, alphabet, "Letter Frequencies"));
							graphPanel.updateUI(); //RESET AND UPDATE graph UI. 
							engine.printPossibleLetters();
							bestGuess.setVisible(true); //Enable the apply word button
							bestGuessLabel.setVisible(true); //Enable the apply word button
							incorrectLabel.setVisible(true);
							applyWord.setVisible(true); //Enable the apply word button
							applyWord.setEnabled(true);
							incorrectGuessesField.setVisible(true);
							incorrectGuessesField.setEnabled(true);

						}
					}
					if(event.getSource() == applyWord){ //USER PRESED "UPDATE WORD"
						String x = "";
						String y = "";
						boolean validUpdate = true;
						for(int i = 0; i < wordlength; i++){
							if(letterInputs[i].getText().equalsIgnoreCase(" ")){ //Letterbox was empty
								x+= "-";
							}else{
								x += letterInputs[i].getText().toLowerCase(); //Letterbox contained letters
								letterInputs[i].setText(letterInputs[i].getText().toUpperCase());
								letterInputs[i].setEnabled(false);
							}
						}
						//updating incorrect guesses
						y = incorrectGuessesField.getText();
						y = y.replaceAll("[^a-zA-Z]", "");
						y = y.toLowerCase();

						System.out.println("INCORRECT: " + incorrectGuesses);
						//Checking for duplicate characters in found and incorrect
						for(int j = 0;  j < y.length(); j++){ 
							for(int i = 0; i < x.length(); i++){
								if(y.charAt(j) == x.charAt(i)){
									JOptionPane.showMessageDialog(frame, "The letter '" + Character.toUpperCase(x.charAt(i)) + 
											"' cannot be both in the word and incorrect. \n"
											+ "Please remove one instance of this letter.",
											"FOUND DUPLICATE LETTERS", JOptionPane.PLAIN_MESSAGE);
									validUpdate = false;
								}
							}
						}
						//Checking for duplicate characters incorrect
						for(int j = 0; j < incorrectGuesses.length(); j++){
							for(int i = 0; i < y.length(); i++){
								if(incorrectGuesses.charAt(j) == y.charAt(i)){
									JOptionPane.showMessageDialog(frame, "The letter '" + Character.toUpperCase(incorrectGuesses.charAt(j)) + 
											"' was already marked incorrect. \n"
											+ "Please enter a different letter.",
											"FOUND DUPLICATE LETTERS", JOptionPane.PLAIN_MESSAGE);
									validUpdate = false;
								}
							}
						}
						if(validUpdate){
							incorrectGuesses += y;
							incorrectGuessesLabel.setText(incorrectGuesses.toUpperCase());
						}
						if( x.equalsIgnoreCase(wordStringTemp) && incorrectGuesses.equalsIgnoreCase(incorrectGuessesTemp) ){ //User has not updated any letters
							JOptionPane.showMessageDialog(frame, "Please update the word with found letters or update the incorrect letters.",
									"NOTHING TO UPDATE", JOptionPane.PLAIN_MESSAGE);
						}else if(validUpdate){
							String[][] guess = engine.generateMove(wordString, y);
							for(int i = 0; i < 26; i++){
								alphabet[i] = guess[0][i];
							}
							for(int i = 0; i < 26; i++){
								letterFrequencies[i] = Integer.parseInt(guess[1][i]);
							}
							
							graphPanel.removeAll();
							graphPanel.add(new graphDisplay(letterFrequencies, alphabet, "Letter Frequencies"));
							bestGuess.setText(guess[0][0].toUpperCase());
							System.out.println("applying update..."); 
							engine.printPossibleLetters();
							graphPanel.updateUI(); //RESET AND UPDATE graph UI. 
						}
						incorrectGuessesField.setValue(null);
						incorrectGuessesTemp = incorrectGuesses;
						wordStringTemp = x;
						wordString = x;
						System.out.println(wordString);
						
						//Check if word has been completed
						if(wordString.indexOf("-") < 0){
							JOptionPane.showMessageDialog(frame, "The word has been found! Your word is " + wordString.toUpperCase() + ". \n"
									+ "Press 'build' to rebuild the dictionary and find another wordn",
									"WORD FOUND", JOptionPane.PLAIN_MESSAGE);
								applyWord.setEnabled(false);
						}else if(letterFrequencies[0] == 0){
							JOptionPane.showMessageDialog(frame, "The word was not found in the dictionary. Make sure that all of the "
									+ "incorrect letters and correct letter positions were entered correctly. \n"
									+ "Press 'build' to rebuild the dictionary and find another word.",
									"WORD NOT FOUND", JOptionPane.PLAIN_MESSAGE);
							applyWord.setEnabled(false);
						}
					}
				}				
			}//END of button action listener

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
								"Welcome to the Word Solver\n\n"
										+ "Word Solver takes input for a word of a specified length, generating the best possible guess\n"
										+ " each time it is updated.\n"
										+ "\n1) 		If you are not using the default dictionary, select a dictionary file (.data or .txt).\n"
										+ "	Make sure  words in the dictionary are separated by new lines only.\n"
										+ "\n2) Specify the length of the word you are trying to solve, then select 'build'. 'build' resets the \n"
										+ "dictionary and letters in order to solve a new word, while narrowing down to the words of the \n"
										+ "specified length.\n"
										+ "\n3) A graph is generated containing the frequencies of the letters in the remaining possible word\n"
										+ "list. The best guess is highlighted on the graph. After guessing a letter, enter solved letters for\n"
										+ "the word in the corresponding textbox. Make sure the letters that have been solved are in the correct\n"
										+ "position. If the  guess was incorrect, enter the letter into the 'incorrect guess' field.\n"
										+ "This indicates to the solver that the word does not contain that letter.\n"
										+ "\n4) Press 'Apply Updated Word' once you have put any correctly guessed letters in their place.\n"
										+ "\n5) Repeat for each guess until the word is solved, or all possibilities have been exhausted.\n"
										+ "\nEnjoy!\n Jacob Webber",
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
			finddictionary.setToolTipText("Search computer for a new dictionary file.");
			
			//Setting up Build Button
			build = new JButton();
			build.addActionListener(new ButtonActionListener());
			build.setText("Build!");
			build.setToolTipText("Trims dictionary file to contain only words of the specified length.");

			//Setting up Apply Word Button
			applyWord = new JButton();
			applyWord.addActionListener(new ButtonActionListener());
			applyWord.setText("Apply Word Updates");
			applyWord.setToolTipText("Applies the found letter(s) or incorrect letter to the search algorithm");
			applyWord.setEnabled(false);

			//Setting up Word Count Text Field
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
			titlePanel.add(Box.createRigidArea(new Dimension(50, 50)));

			/*----------PANELS SETUP--------*/

			//WORDEDITS Panel
			wordEdits.setLayout(new GridBagLayout());
			wordEdits.setBorder(wordEditsTitle);
			//incorrectGuessesField.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
			incorrectGuessesField.setBorder(incorrectGuessesTitle);
			incorrectLabel.setFont(new Font("ARIAL", Font.PLAIN, 20));
			incorrectGuessesLabel.setFont(new Font("ARIAL", Font.PLAIN, 20));
			incorrectGuessesLabel.setForeground(Color.red);
			bestGuess.setFont(new Font("ARIAL", Font.BOLD, 20));
			bestGuess.setForeground(new Color(0,100,0));
			bestGuessLabel.setFont(new Font("ARIAL", Font.PLAIN, 20));
			
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.weightx = 0.5;
			c.gridx = 0;
			c.gridy = 0;
			wordEdits.add(bestGuessLabel, c);
			c.gridy = 1;
			wordEdits.add(bestGuess, c);
			c.gridy = 2;
			incorrectGuessesField.setPreferredSize(new Dimension(120, 40));
			wordEdits.add(incorrectGuessesField, c);
			
			c.gridx = 0;
			c.gridy = 3;
			wordEdits.add(incorrectLabel, c);
			c.weightx = 0.5;
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth = 3;
			incorrectGuessesLabel.setPreferredSize(new Dimension(120, 40));
			wordEdits.add(incorrectGuessesLabel, c);
		
			c.gridx = 0;
			c.gridy = 5;
			wordEdits.add(applyWord, c);

			applyWord.setVisible(false);


			//LETTERBOXES Panel

			letterBoxes.add(wordStringLabel);
			wordStringLabel.setPreferredSize(new Dimension(100, 20));
			
			
			JLabel step2 = new JLabel("Step 2");
			JLabel step3 = new JLabel("Step 3");
			step1.setFont(new Font("Arial", Font.BOLD, 12));
			step1.setForeground(Color.gray);
			step2.setFont(new Font("Arial", Font.BOLD, 12));
			step2.setForeground(Color.gray);
			step3.setFont(new Font("Arial", Font.BOLD, 12));
			step3.setForeground(Color.gray);
			
			//dictionaryEdits Panel
			dictionaryEdits.setLayout(new GridBagLayout());
			dictionaryEdits.setBorder(dictionaryEditsTitle);
		//	wordLengthComboBox.setPreferredSize(new Dimension(50, 40));
			wordLengthComboBox.setOpaque(false);
			
			//finddictionary.setPreferredSize(new Dimension(120, 30));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 1;
			c.gridy = 1;
			dictionaryEdits.add(step1,c);
			c.gridy = 2;
			dictionaryEdits.add(finddictionary, c);
			
			
			c.gridy = 3;
			dictionaryEdits.add(Box.createRigidArea(new Dimension(0,20)), c);
			c.gridy = 4;
			dictionaryEdits.add(step2,c);
			c.gridy = 5;
			dictionaryEdits.add(wordLengthComboBox, c);

			
			
			c.gridy = 6;
			dictionaryEdits.add(Box.createRigidArea(new Dimension(0,20)), c);
			c.gridy = 7;
			dictionaryEdits.add(step3, c);
			c.gridy = 8;
			dictionaryEdits.add(build, c);
			dictionaryEdits.add(Box.createGlue());

			//graphPanel Panel
			graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
			graphPanelLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
			graphPanel.setPreferredSize(new Dimension(400, 200));

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
