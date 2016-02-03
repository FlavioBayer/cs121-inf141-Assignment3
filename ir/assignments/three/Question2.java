/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static ir.assignments.three.MyCrawler.FILE_TOVISIT_NAME;

/**
 *
 * @author flavi
 */
public class Question2 {
    //How many unique pages did you find in the entire domain?
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Loading Pages...");
        Map<WebURL,HtmlParseData> pages = MyCrawler.readPagesData(MyCrawler.FILE_PAGES_NAME);
        Set<WebURL> queue = new HashSet<>();
        System.out.println("Loading Queue...");
        try{
            File f = new File(MyCrawler.FILE_TOVISIT_NAME);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try{
                while(true){
                    WebURL wu = (WebURL)ois.readObject();
                    queue.add(wu);
                }
            }catch(EOFException e){
            }
            ois.close();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        System.out.println("Q: " + queue.size());
        System.out.println("P: " + pages.size());
        queue.removeAll(pages.keySet());
        pages.clear();
        System.out.println("R: " + queue.size());
        System.out.println("Pages loaded!");
        System.out.printf("Question 2: %d unique pages were found in the entire domain\n", pages.size());
    }
    
}
