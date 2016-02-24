package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * UCI CS 121 - Assignment 2: Crawling
 * @author Flavio Bayer X0947373, Kevin Ho 30441608, Lance Lee 75935072, Munish Juneja 82377245
 * @version 1.0
 * @date 2016.01.29
 */
public class Question3 {

    /**
     * How many subdomains did you find?
     * Submit the list of subdomains ordered alphabetically and the number of unique pages detected in each subdomain.
     * The file should be called Subdomains.txt, and its content should be lines containing the URL, a comma, a space, and the number.
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Map<String,Integer> subdomainCount = new TreeMap<>();
        int itp=0;
        for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
            Map.Entry<WebURL,HtmlParseData> up = it.next();
            if(itp++%1147==0)System.out.println(itp);//print progress so far
            if(MyCrawler.isConsideredValidHtmlPage(up.getValue())==false)continue;
            //get subdomain
            String subdomain = up.getKey().getSubDomain()+"."+up.getKey().getDomain();
            //update count
            Integer currentSubdomainCount = subdomainCount.get(subdomain);
            if(currentSubdomainCount==null){//there's no entry for the subdamin
                currentSubdomainCount = 0;
            }
            currentSubdomainCount++;
            subdomainCount.put(subdomain, currentSubdomainCount);
        }
        //print results
        System.out.printf("Question 3: %d subdomains pages were found:\n", subdomainCount.size());
        PrintStream ps = new PrintStream(MyController.WORK_DIRECTORY + "Subdomains.txt");
        for(Map.Entry<String,Integer> bb : subdomainCount.entrySet()){
            System.out.printf("%s, %d\n", bb.getKey(), bb.getValue());
            ps.printf("%s, %d\n", bb.getKey(), bb.getValue());
        }
        ps.close();
    }
    
}
