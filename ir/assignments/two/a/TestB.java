/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.two.a;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * UCI CS 121 - Assignment 1: Text processing functions
 * @author Flavio Bayer
 * @version 1.0
 * @date 2016.01.19
 */
public class TestB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //tokenize file
        if(args.length<1){
            System.out.println("Please give me a file name!");
            //return;
        }
        File file = new File("C:\\Users\\flavi\\Desktop\\uci\\Winter\\cs121\\Assignment1\\fILEtEST3.txt");
        long t0, t1;
        t0 = System.nanoTime();
        List<String> words = Utilities.tokenizeFiles(file);
        List<String> words2 = Utilities.tokenizeFile(file);
        t1 = System.nanoTime();
        System.out.println("Tokenization time: " + (t1-t0)*1e-9 + "s");
        System.out.println(words.equals(words2));
        for(int i=0; i<Math.min(words.size(), words2.size()); i++){
            if(words.get(i).equals(words2.get(i))==false){
                System.out.println(i + "'" + words.get(i) + "' - '" + words2.get(i) + "'");
                break;
            }
        }
        System.out.println(words.size() + "-" + words2.size());
        if(words==null){
            System.out.println("Error during tokenization");
            return;
        }
        //print
        System.out.println(words.toString());
        System.out.println(words2.toString());
    }
    
}
