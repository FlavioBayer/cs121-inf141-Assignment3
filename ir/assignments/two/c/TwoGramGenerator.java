/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.two.c;

import java.util.Iterator;

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */
/**
 * An iterator that generates 2-Gram words on demand given an 1-Gram iterator Iterator
 * The complexity for the generation of a new 2-Gram word is constant(ignoring the length of the string)
 * Thus the cost of iterating all the content of the generated iterator is O(n*t),
 * Where n is the number of strings in the underlying iterator and t is the cost to fetch an item from the underlying iterator(usually constant)
 */
public class TwoGramGenerator implements Iterator<String>{
    private final Iterator<String> oneGramIterator;//Underlying one-gram iterator
    private String previousString;//Last string given by the underlying one-gram iterator
    
    /**
     * Creates a 2-Gram Iterator given an 1-Gram Iterator
     * @param oneGramIterable The underlying 1-Gram iterator
     */
    public TwoGramGenerator(Iterator<String> oneGramIterable) {
        //initialization
        this.oneGramIterator = oneGramIterable;
        this.previousString = null;
        if(this.oneGramIterator.hasNext()){//initialize previousString
            this.previousString = this.oneGramIterator.next();
        }
    }

    /**
     * Returns true if the iteration has more words. 
     * @return true if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return this.oneGramIterator.hasNext();
    }

    /**
     * Returns the next 2-gram word in the iteration.
     * @return the next 2-gram word in the iteration
     */
    @Override
    public String next() {
        final String currentString = this.oneGramIterator.next();
        final String r = this.previousString + ' ' + currentString;
        this.previousString = currentString;
        return r;
    }
    
}
