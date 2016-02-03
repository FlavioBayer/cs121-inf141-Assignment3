package ir.assignments.two.d;

import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */
public class PalindromeFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private PalindromeFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["do", "geese", "see", "god", "abba", "bat", "tab"]
	 * 
	 * The output list of palindromes should be 
	 * ["do geese see god:1", "bat tab:1", "abba:1"]
         * 
         * This method calls computePalindromeFrequenciesSlow or computePalindromeFrequenciesFast
         * computePalindromeFrequenciesSlow is simpler, but computePalindromeFrequenciesFast is usually faster
         * You can modify this method to decide which algorithm should be used
         * Check the underlying method description for complexity
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
        private static List<Frequency> computePalindromeFrequencies(ArrayList<String> words){
            return computePalindromeFrequenciesFast(words);
        }
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * This simple pseudocode of the algorithm:
         * for i=0..words.length
         *   for j=i..words.length
         *     if string words[i..j](inclusive) is a palindrome
         *       add words[i..j](inclusive) to the palindrome set and update frequency
         * sort the palindrome set and return
         * 
         * In the worst case all the words have only the same character and the algorithm has cost O((n^3)*m), where n is the number of words and m is the average length of the words:
         *      The outer loop(i) runs n times, thus the cost is O(n)
         *      The inner loop(j) runs n/2 times(on average for each iteration of the outer loop), thus the cost is O(n)
         *      The test of palindrome(check whether words[i..j](inclusive) is a palindrome) also costs O(n*m) in the worst case(all the words are palindromes), where m is the cost of a string comparison
         *      The sort of the output list costs O(n*log(n)*m), where n is the number of words and m is the average length of the word
         *      Thus, the total cost of the algorithm in the worst case is O((n^3)*m), where n is the number of words and m is the average length of the word
         * In the best case there's no palindrome to be found, thus each palindrome test fails at the first character comparison, thus having cost O(1).
         *   In this case only the two loops have non constant cost, thus the cost in the best case is O(n^2), where n is the number of words
         * The real running time of the algorithm depends on the input.
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
	private static List<Frequency> computePalindromeFrequenciesSlow(ArrayList<String> words) {
            HashMap<String,Frequency> palindromeFrequencies = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            //check if words between i(inclusive) and j(inclusive) is a palindrome and insert into palindromeFrequencies
            for(int i=0; i<words.size(); i++){
                //if(i%1000==0)System.out.println(i);//debug print
                for(int j=i; j<words.size(); j++){
                    //Old approach: create a string containing all the words, reverse and 
                    /*sb.setLength(0);
                    for(int k=i; k<=j; k++){//concatenate all the words between i(inclusive) and j(inclusive)
                        sb.append(words.get(k));
                    }
                    if(sb.toString().equals(sb.reverse().toString())){//sb.toString() must be in the stack before calling sb.reverse().toString(): OK
                    */
                    //cehck if the between i(inclusive) and j(inclusive) create a palindrome
                    //no new string is created, 
                    boolean isPalindrome = true;
                    int w1w=i, w1p=0;//position iterator for the beggining of the word(goind towards the end)
                    int w2w=j, w2p=words.get(j).length()-1;//position iterator for the end of the word(goind towards the beginning)
                    while(isPalindrome && (w1w<=w2w || (w1w==w2w && w1p<w2p))){//while is still a palindrome and both iterators have not collided yet
                        isPalindrome = words.get(w1w).charAt(w1p)==words.get(w2w).charAt(w2p);//check if 
                        //advance the foward iterator to the next character
                        w1p++;
                        if(w1p>=words.get(w1w).length()){//reached the end of the word:go to the beginning of the next word
                            w1w++;
                            w1p = 0;
                        }
                        //advance the backward iterator to the next(previus) character
                        w2p--;
                        if(w2p<0){//reached the beginning of the word:go to the end of the next(previous) word
                            w2w--;
                            if(w2w>=0)w2p=words.get(w2w).length()-1;//make sure that w2w is a valid word index
                        }
                    }
                    //if the word is a palindrome: insert into(or update) palindromeFrequencies
                    if(isPalindrome){
                        //create the palindrome string
                        sb.setLength(0);
                        for(int k=i; k<=j; k++){
                            if(k!=i)sb.append(' ');
                            sb.append(words.get(k));
                        }
                        String palindrome = sb.toString();
                        //insert the palindrome on palindromeFrequencies or update the frequency
                        Frequency f = palindromeFrequencies.get(palindrome);
                        if(f==null){//there's no entry for the palindrome: create a new
                            f = new Frequency(palindrome, 1);
                            palindromeFrequencies.put(palindrome, f);
                        }else{//palindrome already exists, just update the frequency
                            f.incrementFrequency();
                        }
                    }
                    
                }
            }
            //get all the Frequency objects generated and put them into a ArrayList
            ArrayList<Frequency> lf = new ArrayList<>(palindromeFrequencies.values());
            palindromeFrequencies.clear();//free, we don't need wordsFreq anymore
            //sort by <getText().length() DESC,getFrequency() DESC,getText() ASC>
            lf.sort(new Comparator<Frequency>() {
                @Override
                public int compare(Frequency fa, Frequency fb) {
                    if(fa.getText().length()!=fb.getText().length()){
                        return -Integer.compare(fa.getText().length(), fb.getText().length());
                    }else if(fa.getFrequency()!=fb.getFrequency()){
                        return -Integer.compare(fa.getFrequency(), fb.getFrequency());
                    }else{
                        return fa.getText().compareTo(fb.getText());
                    }
                }
            });
            return lf;
	}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique palindrome found in the original list. The frequency of each palindrome
	 * is equal to the number of times that palindrome occurs in the original list.
	 * 
	 * Palindromes can span sequential words in the input list.
	 * 
	 * The returned list is ordered by decreasing size, with tied palindromes sorted
	 * by frequency and further tied palindromes sorted alphabetically. 
	 * 
	 * The original list is not modified.
	 * 
	 * This algorithm is a modification of computePalindromeFrequenciesSlow, it tries to avoid some unnecessary comparison
         * Store all the words reversed, then iterate over the words and try to find one that match in the reversed word
         * When a possible match is found the algorithm naively check if the words create a palindrome
         * This algorithm helps to find words in which the border match, but not the inner part(that's why we use the naive technique to make sure)
         * Thus the algorithm avoid some unnecessary comparison, but still need to perform a lot of comparison
         * The final complexity is the same, but by removing some of the comparison the overall speed is better
         * This technique might create overhead for a small number of words, but is usually faster for a large set of words
	 *  
	 * @param words A list of words.
	 * @return A list of palindrome frequencies, ordered by decreasing frequency.
	 */
        private static List<Frequency> computePalindromeFrequenciesFast(ArrayList<String> words) {
            //Create a sorted tree containing <String,Integer>(using the class Frequency, does not really represent a frequency)
            //The first field is the word reversed and the second field is the second field is the index of the word
            TreeSet<Frequency> reversedWords = new TreeSet<>(new Comparator<Frequency>() {
                @Override
                public int compare(Frequency f1, Frequency f2) {
                    if(!f1.getText().equals(f2.getText())){
                        return f1.getText().compareTo(f2.getText());
                    }else{
                        return Integer.compare(f1.getFrequency(), f2.getFrequency());
                    }
                };
            });
            //add all words reversed
            for(int i=0; i<words.size(); i++){
                reversedWords.add(new Frequency(new StringBuilder(words.get(i)).reverse().toString(), i));
            }
            //try to find the palindromes and add them to palindromeFrequencies
            HashMap<String,Frequency> palindromeFrequencies = new HashMap<>();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<words.size(); i++){//iterate over all the words
                //if(i%1000==0)System.out.println(i);//debug
                String si = words.get(i);
                for(int m=1; m<=si.length(); m++){//test the match for each length of si(the reversed word might have length smaller than si.length())
                    String sim = si.substring(0, m);
                    Frequency flb = new Frequency(sim, i);//create a lower bound pair
                    for(Frequency fe : reversedWords.tailSet(flb, true)){//iterate over the set, beggining from the lower bound
                        if(!(m==si.length()?fe.getText().startsWith(sim):fe.getText().equals(sim)))break;//check for upper bound: if we are for si then the word must begin with si, otherwise the word must begin with si[0..m]
                        int j = fe.getFrequency();
                        if(j<i)continue;//by definition of the algorithm: i<j is required
                        //this code is the same as the previous method
                        //check if the word is really a palindrome
                        boolean isPalindrome = true;
                        int w1w=i, w1p=0;//position iterator for the beggining of the word(goind towards the end)
                        int w2w=j, w2p=words.get(j).length()-1;//position iterator for the end of the word(goind towards the beginning)
                        while(isPalindrome && (w1w<=w2w || (w1w==w2w && w1p<w2p))){//while is still a palindrome and both iterators have not collided yet
                            isPalindrome = words.get(w1w).charAt(w1p)==words.get(w2w).charAt(w2p);//check if 
                            //advance the foward iterator to the next character
                            w1p++;
                            if(w1p>=words.get(w1w).length()){//reached the end of the word:go to the beginning of the next word
                                w1w++;
                                w1p = 0;
                            }
                            //advance the backward iterator to the next(previus) character
                            w2p--;
                            if(w2p<0){//reached the beginning of the word:go to the end of the next(previous) word
                                w2w--;
                                if(w2w>=0)w2p=words.get(w2w).length()-1;//make sure that w2w is a valid word index
                            }
                        }
                        //if the word is a palindrome: insert into(or update) palindromeFrequencies
                        if(isPalindrome){
                            //create the palindrome string
                            sb.setLength(0);
                            for(int k=i; k<=j; k++){
                                if(k!=i)sb.append(' ');
                                sb.append(words.get(k));
                            }
                            String palindrome = sb.toString();
                            //insert the palindrome on palindromeFrequencies or update the frequency
                            Frequency f = palindromeFrequencies.get(palindrome);
                            if(f==null){//there's no entry for the palindrome: create a new
                                f = new Frequency(palindrome, 1);
                                palindromeFrequencies.put(palindrome, f);
                            }else{//palindrome already exists, just update the frequency
                                f.incrementFrequency();
                            }
                        }
                    }
                }
            }
            //this code is the same as the previous method
            //get all the Frequency objects generated and put them into a ArrayList
            ArrayList<Frequency> lf = new ArrayList<>(palindromeFrequencies.values());
            palindromeFrequencies.clear();//free, we don't need wordsFreq anymore
            //sort by <getText().length() DESC,getFrequency() DESC,getText() ASC>
            lf.sort(new Comparator<Frequency>() {
                @Override
                public int compare(Frequency fa, Frequency fb) {
                    if(fa.getText().length()!=fb.getText().length()){
                        return -Integer.compare(fa.getText().length(), fb.getText().length());
                    }else if(fa.getFrequency()!=fb.getFrequency()){
                        return -Integer.compare(fa.getFrequency(), fb.getFrequency());
                    }else{
                        return fa.getText().compareTo(fb.getText());
                    }
                }
            });
            return lf;
	}
	
	/**
	 * Runs the 2-gram counter. The input should be the path to a text file.
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
            //compute palindrome frequencies
            long t0,t1;
            t0 = System.nanoTime();
            List<Frequency> frequencies = computePalindromeFrequencies(words);
            t1 = System.nanoTime();
            System.out.println("compute palindrome took " + ((t1-t0)*1e-9) + "s");
            //print
            Utilities.printFrequencies(frequencies);
	}
        
        
}
