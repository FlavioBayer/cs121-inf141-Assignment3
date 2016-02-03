package ir.assignments.three;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LongestPage {
	public static void main(String[] args){
		  try {
              Scanner file = new Scanner(new File(args[0]));
              int largest = Integer.MIN_VALUE;
              String longestPage = "";
              
              while(file.hasNextLine()){
              	String line = file.nextLine();
              	String[] lineContents = line.split("\\s+");
              	
              	int wordCount = Integer.parseInt(lineContents[0]);
              	String htmlPage = lineContents[1];
              	
              	if(wordCount > largest){
              		largest = wordCount;
              		longestPage = htmlPage;
              	}
              }
   
              file.close();
   
              System.out.println("Page with most words is : " + longestPage + " with " + largest + " words");
          } catch(IOException e) {
              System.out.println(e.getMessage());
          }
      }
}
