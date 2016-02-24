package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import static ir.assignments.two.a.Utilities.tokenizeString;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * UCI CS 121 - Assignment 2: Crawling
 * @author Flavio Bayer X0947373, Kevin Ho 30441608, Lance Lee 75935072, Munish Juneja 82377245
 * @version 1.0
 * @date 2016.01.29
 */
public class Question4 {
    /**
     * What is the longest page in terms of number of words?
     * (Don't count HTML markup as words.)
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WebURL longestUrl = null;
        int longestSize = -1;
        int itp=0;
        //iterate over the pages and find the longest page
        for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
            Map.Entry<WebURL,HtmlParseData> up = it.next();
            if(itp++%1147==0)System.out.println(itp);//print progress so far
            if(MyCrawler.isConsideredValidHtmlPage(up.getValue())==false)continue;
            List<String> tokens = tokenizeString(up.getValue().getText());
            if(tokens.size()>longestSize){//found a bigger page
                longestUrl = up.getKey();
                longestSize = tokens.size();
            }
            tokens.clear();
        }
        System.out.printf("Question 4: The longest page found(containing %d words) is '%s'\n", longestSize, longestUrl.getURL());
    }    
}
