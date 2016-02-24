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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * UCI CS 121 - Assignment 2: Crawling
 * @author Flavio Bayer X0947373, Kevin Ho 30441608, Lance Lee 75935072, Munish Juneja 82377245
 * @version 1.0
 * @date 2016.01.29
 */
public class MyController {
    public static final String WORK_DIRECTORY = "C:/Users/flavi/Desktop/cs121tmp/";
    public static final String ROOT_URL = "http://www.ics.uci.edu";
    public static final String USER_AGENT = "UCI Inf141-CS121 crawler X0947373 30441608 75935072 82377245";
    public static final int NUMBER_OF_CRAWLERS = 16;

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
        config.setPolitenessDelay(2000);
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
        boolean startFromBeginning = false;
        if(startFromBeginning){
            System.out.println("Are you sure you want to delete the files?(y/n)");
            if(System.in.read()!='y'){
                System.out.println("bye");
                return;
            }
            controller.addSeed(ROOT_URL);
            new File(MyCrawler.FILE_PAGES_NAME).delete();
            new File(MyCrawler.FILE_ERRORS_NAME).delete();
            new File(MyCrawler.FILE_TOVISIT_NAME).delete();
        }else{
            //load data
            MyCrawler.errors = MyCrawler.readErrorsData(MyCrawler.FILE_ERRORS_NAME);
            System.out.println("E: " + MyCrawler.errors.size());
            MyCrawler.toVisit = MyCrawler.readToVisitData(MyCrawler.FILE_TOVISIT_NAME);
            System.out.println("Q: " + MyCrawler.toVisit.size());
            MyCrawler.pages = new HashMap<>();//this.pages.size();
            for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
                Map.Entry<WebURL,HtmlParseData> up = it.next();
                MyCrawler.pages.put(up.getKey(), null);//up.getValue();
            }
            System.out.println("P: " + MyCrawler.pages.size());
            int i=0;
            for (WebURL wurl : MyCrawler.toVisit) {
                if(i++%10==0)System.out.printf("\rAdding seeds: %06d/%06d - %05.2f%%", i, MyCrawler.toVisit.size(), 100.*i/MyCrawler.toVisit.size());
                    
                if(MyCrawler.pages.containsKey(wurl))continue;
                if(MyCrawler.errors.containsKey(wurl))continue;
                if(wurl.getSubDomain().toLowerCase().equals("wics.ics"))continue;
                if(wurl.getSubDomain().toLowerCase().equals("djp3-pc2.ics"))continue;
                if(wurl.getSubDomain().toLowerCase().equals("drzaius.ics"))continue;
                if(wurl.getSubDomain().toLowerCase().equals("archive.ics"))continue;
                if(wurl.getSubDomain().toLowerCase().equals("graphmod.ics"))continue;
                    
                controller.addSeed(wurl.getURL());
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
