package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * UCI CS 121 - Assignment 2: Crawling
 * @author Flavio Bayer X0947373, Kevin Ho 30441608, Lance Lee 75935072, Munish Juneja 82377245
 * @version 1.0
 * @date 2016.01.29
 */
public class Question2 {
    /**
     * How many unique pages did you find in the entire domain?
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Set<WebURL> pagesURLs = new HashSet<>();
        //add urls to the set
        for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
            Map.Entry<WebURL,HtmlParseData> up = it.next();
            if(MyCrawler.isConsideredValidHtmlPage(up.getValue())==false)continue;
            pagesURLs.add(up.getKey());
        }
        //print how any pages where found
        System.out.printf("Question 2: %d unique pages were found in the entire domain\n", pagesURLs.size());
    }
    
}
