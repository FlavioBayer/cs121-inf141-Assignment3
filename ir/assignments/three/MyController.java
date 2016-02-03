package ir.assignments.three;

import java.io.File;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyController {

    public static final String WORK_DIRECTORY = "C:/Users/flavi/Desktop/cs121tmp/";
    public static final String ROOT_URL = "http://www.ics.uci.edu";
    public static final String USER_AGENT = "UCI Inf141-CS121 crawler X0947373 30441608 75935072 82377245";
    public static final int NUMBER_OF_CRAWLERS = 4;

    public static void main(final String[] args) throws Exception {
        //create working directory if it does not exists
        File rootFolder = new File(WORK_DIRECTORY);
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        //Create configs
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(WORK_DIRECTORY);
        config.setUserAgentString(USER_AGENT);
        config.setPolitenessDelay(600);
        //config.setResumableCrawling(true);// I will take care of that by myself
        config.setMaxDepthOfCrawling(-1);
        config.setMaxPagesToFetch(-1);
        
        //Instantiate the controller for this crawl.
        System.out.println("Setting up CrawlController");
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName(USER_AGENT);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        System.out.println("CrawlController OK");
        
        //Start crawling from rootUrl
        boolean startFromBeginning = true;
        if(startFromBeginning){
            controller.addSeed(ROOT_URL);
            new File(MyCrawler.FILE_PAGES_NAME).delete();
            new File(MyCrawler.FILE_ERRORS_NAME).delete();
            new File(MyCrawler.FILE_TOVISIT_NAME).delete();
        }else{
            //load data
            MyCrawler.pages = new HashMap<>();//this.pages.size();
            MyCrawler.totalVisited = MyCrawler.pages.size();
            for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
                Map.Entry<WebURL,HtmlParseData> up = it.next();
                MyCrawler.totalVisited++;
                controller.addSeenUrl(up.getKey().getURL(), up.getKey().getDocid());
            }
            System.out.println("P: " + MyCrawler.totalVisited);
            MyCrawler.errors = MyCrawler.readErrorsData(MyCrawler.FILE_ERRORS_NAME);
            MyCrawler.totalError = MyCrawler.errors.size();
            for (Map.Entry<WebURL,Integer> wurli : MyCrawler.errors.entrySet()) {
                controller.addSeenUrl(wurli.getKey().getURL(), wurli.getKey().getDocid());
            }
            System.out.println("E: " + MyCrawler.totalError);
            MyCrawler.toVisit = MyCrawler.readToVisitData(MyCrawler.FILE_TOVISIT_NAME);
            MyCrawler.totalToVisit = MyCrawler.toVisit.size();
            System.out.println("Q: " + MyCrawler.totalToVisit);
            int i=0;
            for (WebURL wurl : MyCrawler.toVisit) {
                controller.addSeed(wurl.getURL());
                if(i++%10==0){
                    System.out.printf("\rAdding seeds: %06d/%06d - %05.2f%%", i, MyCrawler.toVisit.size(), 100.*i/MyCrawler.toVisit.size());
                    System.out.flush();
                }
            }
            //free resources
            System.out.println("\nOK!");
        }

        //crawl
        long t0, t1;
        System.out.println("Starting crawler");
        t0 = System.nanoTime();
        controller.start(MyCrawler.class, NUMBER_OF_CRAWLERS);
        t1 = System.nanoTime();
        System.out.println("Crawler has finished the work");
        System.out.printf("Question 1: The crawler took %.6fs to crawl the entire domain\n", (t1-t0)*1e-9);
    }
}
