/*SolverEngine.java
 * Author: Jake Webber
 * Last edited: 1/28/2015
 * 
 * Generates letter frequencies for a dictionary file using a
 * letter parsing algorithm.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolverEngine{
	//Stored possible words of wordLength. Created and added to in the constructor. 
	//Should be modified every time the method is run, removing words that contain
	//incorrect letters OR removing words that do not contain correct letters in the correct positions. 
	public ArrayList possibleWords = new ArrayList(0); 

	//Entire dictionary file. Only used to get words of a certain length 
	//in constructor once, irrelevent after.
	public ArrayList dictionary = new ArrayList(0); 

	//Stores the last char guess made by the method so that the method
	//can tell which letter it got correct/incorrect and remove 
	//corresponding words the next time it runs. 
	public char lastGuess = 0;

	//char array to store guessed letters already generated by the method.
	//generateMove cross-checks with this list to ensure that the current letter
	//it is going to guess is not already in this array. 
	public char[] guessedLetters = new char[26];
	//Marks the position for guessedLetters char array, incremented by 1 for each run. 
	public int position = 0;
	public File file;
	public int fileStatus = 1;

	//Stores the wordBoard for comparison tests with possible words. 
	public String InString = "";

	//Important 2D array: Stores alphabet and each letter's frequency, which is
	//incremented by 1 every time it occurs in every word of possibleWords. 
	//Letter frequency is reset each time method is run.
	public String[][] possibleLetters = {
			//First row: Alphabet
			{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", 
				"u", "v", "w", "x", "y", "z"},
				//Second row: Frequency of corresponding letter
				{"0", "0", "0", "0", "0", "0", "0", "0", "0", 
					"0", "0", "0", "0", "0", "0", "0", "0", "0", 
					"0", "0", "0", "0", "0", "0", "0", "0"},
	};

	//--------------------------------------------------------------------------------------------
	//CONSTRUCTOR:
	//Takes input for the wordLength when building PlayFasterComputerPlayer
	//Builds dictionary file in a dictionary ArrayList
	//Adds all words of the the same length as wordLength to possibleWords ArrayList
	public SolverEngine(int wordLength, File dictionaryfile){
		file = dictionaryfile;
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
		//Adds all dictionary.data entries to ArrayList
		while(scanner.hasNext()){
			this.dictionary.add(scanner.next());
		}
		//Adds all words the same length as wordLength to possibleWords arrayList
		for(int i = 0; i < this.dictionary.size(); i++){
			String CurrentWord = (String) this.dictionary.get(i);
			if(CurrentWord.length() == wordLength){
				this.possibleWords.add(CurrentWord);
			}
		}
		for(int i = 0; i < wordLength; i++){
			InString += "-";
		}

	}

	/**----------------------------------------------------------------------------
	 * Generates the next move based on the most frequent letter occuring in the 
	 * list of possibleWords. Removes from possibleWords based on whether the last
	 * guess was correct or not and updates letter frequencies. 
	 * 
	 * @param updatedInString, containing the wordboard of correctly guessed letters 
	 * and corresponding positions. 
	 * already_guessed, list of incorrectly guessed characters
	 * @return the double array with alphabet letter frequencies
	 */
	public String[][] generateMove(String updatedInString, String incorrectGuesses){
		//REMOVING WORDS--------------------------------------------------------
		//remove words that do not contain correct letters in correct positions.
		char[] updatedInStringArray = updatedInString.toCharArray();
		for(int i = 0; i < this.possibleWords.size(); i++){
			String CurrentWord = (String) this.possibleWords.get(i);
			char[] currentWordArray = CurrentWord.toCharArray();
			//check each word in possibleWords
			for(int j = 0; j < currentWordArray.length; j++){
				if(updatedInStringArray[j] != '-'){
					//if the char at position in currentword doesnt match same char in updatedInString, remove word.
					if(updatedInStringArray[j] == currentWordArray[j]){
						continue;
					}else{
						this.possibleWords.remove(i);
						i--; //roll back i, entire ArrayList was shifted to the left. 
						break;							
					}
				}
			}
		}

		//Remove words containing characters from incorrectGuesses
		if(incorrectGuesses != null){
			for(int j = 0; j < incorrectGuesses.length(); j++){
				for(int i = 0; i < this.possibleWords.size(); i++){
					String CurrentWord = (String) this.possibleWords.get(i);
					int foundLetter = CurrentWord.indexOf(incorrectGuesses.charAt(j));
					if(foundLetter >= 0){
						this.possibleWords.remove(i);
						i--;
					}
				}
			}
		}
		//FINISHED REMOVING WORDS--------------------------------------------------------
		InString = updatedInString; //updating InString, sets up for the next guess.
		char nextGuess = 0; //the returned char guess being initialized.

		//Resetting letter frequency each time method is run.
		for(int i = 0; i < 26; i++){
			this.possibleLetters[1][i] = "0";
		}

		//Adds all letter frequencies from words in possibleWords.
		//int i runs for ALL POSSIBLE WORDS OF WORDLENGTH.
		for(int i = 0; i < this.possibleWords.size(); i++){
			String CurrentWord = (String) this.possibleWords.get(i);
			char[] currentWordArray = CurrentWord.toCharArray();
			//int j runs for LENGTH OF CURRENT WORD BEING SCANNED.
			for(int j = 0; j < currentWordArray.length; j++){
				//int x runs for EVERY LETTER OF THE ALPHABET 
				for(int x = 0; x < 26; x++){
					//if the current letter 'j' in the current word = the current 'x' letter, 'x' has frequency increased by 1.
					if(currentWordArray[j] == this.possibleLetters[0][x].charAt(0)){
						int currentInt = Integer.parseInt(this.possibleLetters[1][x]);
						currentInt++;
						String currentIntString = Integer.toString(currentInt);
						this.possibleLetters[1][x] = currentIntString;
					}	
				}
			}
		}
		//Sorting possibleLetters array from highest to lowest frequency.
		int loop = 0;
		while(loop < 2){
			for(int i = 0, j = 1; i < 26 && j < 26; i++, j++){
				for(int x = 0; x < this.guessedLetters.length; x++){
					if(this.possibleLetters[0][i].charAt(0) == this.guessedLetters[x]){
						this.possibleLetters[1][i] = "0";
					}
				}
				int frequency1 = Integer.parseInt(this.possibleLetters[1][i]);
				int frequency2 = Integer.parseInt(this.possibleLetters[1][j]);
				if(frequency1 < frequency2){
					String temp = this.possibleLetters[1][j];
					this.possibleLetters[1][j] = this.possibleLetters[1][i];
					this.possibleLetters[1][i] = temp;
					String temp2 = this.possibleLetters[0][j];
					this.possibleLetters[0][j] = this.possibleLetters[0][i];
					this.possibleLetters[0][i] = temp2;
					i = 0;
					j = 1;
				}

			}
			loop++;
		}
		//Setting nextGuess and its frequency. Adding the letter to guessedletters.
		//If nextGuess happens to be found in guessedLetters, the next highest letter is used.
		//This is a fail-safe loop, nextGuess should always initially generate a new letter.
		nextGuess = this.possibleLetters[0][0].charAt(0);
		int tempposition = 1;
		for(int i = 0; i < this.guessedLetters.length; i++){
			if(nextGuess == this.guessedLetters[i]){
				nextGuess = this.possibleLetters[0][tempposition].charAt(0);
				i = 0;
				tempposition++;
			}
		}
		int highestFrequency = Integer.parseInt(this.possibleLetters[1][tempposition - 1]);
		this.guessedLetters[position] = nextGuess;
		this.position++;
		//Test prints for the highest letter & frequency
		//System.out.print("AI Letter: " + nextGuess);
		//System.out.println("	Frequency: " + highestFrequency);
		this.lastGuess = nextGuess;
		return this.possibleLetters;
	}

	//----------------------------------------------------------------------------
	//TESTING METHOD. Prints the possible letters array and corresponding frequencies. 
	//Not required for this class to work. 
	public void printPossibleLetters(){
		System.out.println();
		System.out.println("Possible letters: ");
		for(int row = 0; row < this.possibleLetters.length; row++){
			System.out.print("{");
			for(int col = 0; col < this.possibleLetters[0].length; col++){
				System.out.print(this.possibleLetters[row][col] + ", ");
			}
			System.out.print("}");
			System.out.println();
		}
	}
}
