Word-Solver
==============

Solves for words of a specified length in a given dictionary file based on letter frequencies. Optimized for solving word-based games such as Hangman or Wheel of Fortune.
![Alt text](https://raw.githubusercontent.com/jakewebber/Word-Solver/master/wordSolverScreenshot.png?raw=true "Word Solver")
<h2>How To Run</h2>
Download the ZIP file - this includes an executable JAR file, an English dictionary file, and the background image for the GUI. 
Instructions are provided inside the program. You will need the length of the single word you are trying to solve for. 

<h2>How It Works (In a Short Summary)</h2>
Every word that is the same length as the one you are trying to solve for is taken from the dictionary file and stored in an array list as separate objects. A double array containing every letter of the alphabet tracks of the number of times every letter appears in each word in the array list and stores its frequency. As this double array is sorted from highest to lowest frequency, the initial best guess is produced: the most frequently occurring letter out of every word matching your word length. 

The next step involves two possible cases:

<b> CASE 1 </b>
The best guess that was produced is CORRECT. You would then add this letter to the corresponding spot(s) provided in the solver. The solver handles this updated word by removing all words stored in the array list that do not contain the letter in those specific spot(s), updating the possible words. The double array containing every letter's frequency is then updated for the shortened array list, generating a new most frequently occurring letter to be the best guess.

<b> CASE 2 </b>
The best guess that was produced is INCORRECT. You would then simply submit this to the solver by not updating the letter spots for the word at all. The solver adds this letter to an array of incorrect letters and shortens the array list by removing all words containing this letter, updating the possible words. The double array containing every letter's frequency is then updated for the shortened array list, generating a new most frequently occurring letter to be the best guess. 


This process continues until either the word is solved or the maximum number of incorrect guesses has been reached. 

<h2>What Other Useful Data Can Be Obtained?</h2>
Sorting through a massive dictionary file containing nearly every single word in the English language letter by letter brings out some interesting conclusions that would be hard to obtain otherwise. For instance, vowels in general are by far the most frequent first guess generated by the solver for a word of any length. This suggests that the sounds vowels produce are far more important and diverse than consonants, and they are necessary for chaining the distinct sounds in each word.
Out of the vowels, the letter E is the most common first guess for words between 4 and 14 letters in length. This is likely because of the silent property E holds to distinguish between the different sounds vowels can make (such as the O sound in woke vs the O sound in block), in addition to the common suffixes it is in (es, ed, er, est), making it the most common letter in use. Words of letter length 15 or greater often use I the most frequently. Again, this is likely due to the suffixes associated with longer words such as -ion, -ias, -ist, -ing, -ism, -ies.
