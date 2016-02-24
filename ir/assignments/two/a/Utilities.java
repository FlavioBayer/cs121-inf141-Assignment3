package ir.assignments.two.a;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */
/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
         * 
         * The file must be in ASCII format.
         * 
         * Any set of adjacent characters [a-zA-Z0-9] are grouped in a word.
         * Since ' is a non-alphanumeric character, the string "don't" produce ["don","t"] as output
         * 
         * The complexity is linear on the length of the file.
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
        
	public static ArrayList<String> tokenizeFiles(File input) {
            //try (FileInputStream fis = new FileInputStream(input)) {//open the file
            try{
                ArrayList<String> words = new ArrayList<>();
                Scanner s = new Scanner(input);//open the file
                s.useDelimiter("[^a-zA-Z0-9]+");
                //iterate over the characters in the file
                while(s.hasNext()){
                    String ss = s.next();
                    //System.out.println("'" + ss +"'");
                    words.add(ss.toLowerCase());
                }
                //return the words
                return words;
            }catch(FileNotFoundException ex){//File not found!
                System.out.println("Error(UT01): File '" + input.getAbsolutePath() + "' could not be found!");
            }catch(Exception ex){//Error while reading the file!
                System.out.println("Error(UT02): IOException while reading file '" + input.getAbsolutePath() + "', check the file!");
            }
            //null is returned when an error occurs
            return null;
	}
	public static ArrayList<String> tokenizeFile(File input) {
            StringBuilder tmpWord = new StringBuilder();
            //try (FileInputStream fis = new FileInputStream(input)) {//open the file
            try (BufferedReader fis = new BufferedReader(new FileReader(input))) {//open the file
                ArrayList<String> words = new ArrayList<>();
                //iterate over the characters in the file
                for(int c=fis.read(); c!=-1; c=fis.read()){
                    if(Character.isAlphabetic(c) || Character.isDigit(c) || c=='\''){//found one word character
                        tmpWord.append(Character.toLowerCase((char)c));
                    }else if(tmpWord.length()>0){//character does not belong to a word, it's a delimiter, add the string on sb to the words read
                        words.add(tmpWord.toString());
                        tmpWord.setLength(0);
                        //if(words.size()%1000000==0){System.out.println(words.size() + tmpWord.toString()); System.out.flush();}
                    }
                }
                //check if a word was being created when the file ends
                if(tmpWord.length()>0){
                    words.add(tmpWord.toString());
                }
                //return the words
                return words;
            }catch(FileNotFoundException ex){//File not found!
                System.out.println("Error(UT01): File '" + input.getAbsolutePath() + "' could not be found!");
            }catch(IOException ex){//Error while reading the file!
                System.out.println("Error(UT02): IOException while reading file '" + input.getAbsolutePath() + "', check the file!");
            }
            //null is returned when an error occurs
            return null;
	}
        
	/**
	 * 
         * 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
         * 
         * Any set of adjacent characters [a-zA-Z0-9] are grouped in a word.
         * Since ' is a non-alphanumeric character, the string "don't" produce ["don","t"] as output
         * 
         * The complexity is linear on the length of the string s.
	 * 
	 * @param s The string to tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeString(String s) {
            ArrayList<String> words = new ArrayList<>();
            StringBuilder tmpWord = new StringBuilder(s.length());
            //iterate over the characters in the file
            if(s.length()>1000000)System.out.println(s.length());
            for(int i=0; i<s.length(); i++){
                char c = s.charAt(i);
                if(Character.isAlphabetic(c) || Character.isDigit(c) || c=='\''){//found one word character
                    tmpWord.append((char)c);
                }else if(tmpWord.length()>0){//character does not belong to a word, it's a delimiter, add the string on sb to the words read
                    words.add(tmpWord.toString().toLowerCase());
                    tmpWord.setLength(0);//clear stringBuilder
                }
            }
            //check if a word was being created when the file ends
            if(tmpWord.length()>0){
                words.add(tmpWord.toString().toLowerCase());
            }
            //return the words
            return words;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
         * 
         * The complexity is linear on the length of the frequencies input
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
            //get Total Count and Unique Count 
            int totalCount=0, uniqueCount=0;
            for(Frequency fr : frequencies){
                totalCount += fr.getFrequency();
                uniqueCount += 1;
            }
            //Print generic header(counts)
            System.out.println("Total item count: " + totalCount);
            System.out.println("Unique item count: " + uniqueCount);
            /*
            //Print item or n-gram header
            //check how many grams there is in a string
            int gramCount=1;
            if(totalCount>0){
                String s = frequencies.get(0).getText();
                for(int i=s.indexOf(' '); i!=-1; i=s.indexOf(' ', i+1)){
                    gramCount++;
                }
            }
            //Print header(counts)
            if(gramCount==1){
                System.out.println("Total item count: " + totalCount);
                System.out.println("Unique item count: " + uniqueCount);
            }else{
                System.out.println("Total " + gramCount + "-gram count: " + totalCount);
                System.out.println("Unique " + gramCount + "-gram count: " + uniqueCount);
            }*/
            //Print the words and frequencies
            System.out.println();
            for(Frequency fr : frequencies){
                System.out.println(fr.getText() + '\t' + fr.getFrequency());
            }
	}
}
