/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author flavi
 */
public class Question4 {
    //What is the longest page in terms of number of words?
    //(Don't count HTML markup as words.)
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Loading Pages...");
        //Map<WebURL,HtmlParseData> pages = MyCrawler.readPagesData(MyCrawler.pagesFileName);
        System.out.println("Pages loaded! Processing...");
        WebURL longestUrl = null;
        int longestSize = -1;
        int i = 0;
        for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
            Map.Entry<WebURL,HtmlParseData> up = it.next();
            if(up.getValue().getText().length()>1000000)continue;//ignore files bigger than 1MB
            List<String> tokens = ir.assignments.two.a.Utilities.tokenizeString(up.getValue().getText());
            if(tokens.size()>longestSize){
                longestUrl = up.getKey();
                longestSize = tokens.size();
            }
            tokens.clear();
            i++;
        }
        System.out.printf("Question 4: The longest page found(containing %d words) is '%s'\n", longestSize, longestUrl.getURL());
    }    
}
