package ir.assignments.two.b;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */
/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
         * 
         * The complexity of this algorithm is O(n*log(n)): the word count is done in linear time, but the sort of the output costs O(n*log(n))
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
            if(words==null)return new LinkedList<>();
            //check overload computeWordFrequencies(Iterator<String> words) to see the algorithm
            return computeWordFrequencies(words.iterator());//get the iterator and delegate
	}
        
        /**
	 * Takes a input Iterator of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * See {@link computeWordFrequencies(List<String> words)}  for examples
         * 
         * The complexity of this algorithm is O(n*log(n)): the word count is done in linear time, but the sort of the output costs O(n*log(n))
	 *  
	 * @param words A Iterator of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(Iterator<String> words) {
            //Each word (will)points to a Frequency object in wordsFreq
            HashMap<String,Frequency> wordsFrequencies = new HashMap<>();
            //group words and count the frequency
            while(words.hasNext()){
                String s = words.next();
                Frequency f = wordsFrequencies.get(s);//search for the word frequency
                if(f==null){//word does not exists in the map: create Frequency and insert
                    f = new Frequency(s, 1);
                    wordsFrequencies.put(s, f);
                }else{//word already inserted: update Frequency
                    f.incrementFrequency();
                }
            }
            //get all the Frequency objects generated and put them into a ArrayList
            ArrayList<Frequency> frequencies = new ArrayList<>(wordsFrequencies.values());
            wordsFrequencies.clear();//free, we don't need wordsFreq anymore
            //sort by <getFrequency() DESC,getText() ASC>
            frequencies.sort(new Comparator<Frequency>() {
                @Override
                public int compare(Frequency fa, Frequency fb) {
                    if(fa.getFrequency()!=fb.getFrequency()){//getFrequency() are different, sort descending by getFrequency()
                        return -Integer.compare(fa.getFrequency(), fb.getFrequency());
                    }else{//sort ascending by getText()
                        return fa.getText().compareTo(fb.getText());
                    }
                }
            });
            return frequencies;
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args) {
            //tokenize file
            if(args.length<1){
                System.out.println("Please give me a file name!");
                return;
            }
            File file = new File(args[0]);
            ArrayList<String> words = Utilities.tokenizeFile(file);
            if(words==null){
                System.out.println("Error during tokenization");
                return;
            }
            //compute 1-Gram frequencies
            List<Frequency> frequencies = computeWordFrequencies(words);
            //print
            Utilities.printFrequencies(frequencies);
	}
}
