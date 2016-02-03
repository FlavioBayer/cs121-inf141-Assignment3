/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author flavi
 */
public class Question3 {
    //How many subdomains did you find?
    //Submit the list of subdomains ordered alphabetically and the number of unique pages detected in each subdomain.
    //The file should be called Subdomains.txt, and its content should be lines containing the URL, a comma, a space, and the number.
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Loading Pages...");
        Map<WebURL,HtmlParseData> pages = MyCrawler.readPagesData(MyCrawler.FILE_PAGES_NAME);
        System.out.println("Pages loaded! Processing...");
        Map<String,Integer> aa = new TreeMap<>();
        for(Map.Entry<WebURL,HtmlParseData> up : pages.entrySet()){
            String subdomain = up.getKey().getSubDomain()+"."+up.getKey().getDomain();
            Integer currentSubdomainCount = aa.get(subdomain);
            if(currentSubdomainCount==null){
                currentSubdomainCount = 0;
            }
            currentSubdomainCount++;
            aa.put(subdomain, currentSubdomainCount);
        }
        System.out.printf("Question 3: %d subdomains pages were found:\n", aa.size());
        for(Map.Entry<String,Integer> bb : aa.entrySet()){
            System.out.printf("%s, %d\n", bb.getKey(), bb.getValue());
        }
    }
    
}
