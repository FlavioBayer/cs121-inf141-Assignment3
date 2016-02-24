package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
public class Refilter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //load data
        MyCrawler.toVisit = new HashSet<>();
        System.out.println("CP0");
        Set<WebURL> keys = new HashSet<>();
        int i = 0;
        try {
            File f = new File(MyCrawler.FILE_PAGES_NAME + ".j");
            f.delete();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            keys.clear();
            for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
                Map.Entry<WebURL,HtmlParseData> up = it.next();
                if(up.getKey().getSubDomain().equals("duttgroup.ics") || up.getKey().getSubDomain().equals("duttgroup-test.ics") || up.getKey().getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?")){
                    System.out.println("Deleted: " + up.getKey().getURL());
                    i++;
                    continue;
                }
                if(keys.add(up.getKey())==false){i++; continue;}
                oos.writeObject(up.getKey());
                oos.writeObject(up.getValue().getHtml());
                oos.writeObject(up.getValue().getText());
                oos.writeObject(up.getValue().getTitle());
                oos.writeObject(up.getValue().getMetaTags());
                oos.writeObject(up.getValue().getOutgoingUrls());
                oos.reset();
                MyCrawler.toVisit.add(up.getKey());
                MyCrawler.toVisit.addAll(up.getValue().getOutgoingUrls());
            }
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CP1 - " + i);
        i = 0;
        try {
            File f = new File(MyCrawler.FILE_ERRORS_NAME + ".j");
            f.delete();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            MyCrawler.errors = MyCrawler.readErrorsData(MyCrawler.FILE_ERRORS_NAME);
            System.out.println("CP2");
            keys.clear();
            for (Map.Entry<WebURL,Integer> up : MyCrawler.errors.entrySet()) {
                if(up.getKey().getSubDomain().equals("duttgroup.ics")){i++; continue;}
                if(up.getKey().getSubDomain().equals("duttgroup-test.ics")){i++; continue;}
                //if(up.getKey().getSubDomain().equals("archive.ics")){i++; continue;}
                //if(up.getKey().getSubDomain().equals("drzaius.ics")){i++; continue;}
                if(up.getKey().getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?")){i++; continue;}
                if(keys.add(up.getKey())==false){i++; continue;}
                oos.writeObject(up.getKey());
                oos.writeInt(up.getValue());
                oos.reset();
                MyCrawler.toVisit.add(up.getKey());
            }
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CP3 - " + i);
        i = 0;
        try {
            File f = new File(MyCrawler.FILE_TOVISIT_NAME + ".j");
            f.delete();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            MyCrawler.toVisit.addAll( MyCrawler.readToVisitData(MyCrawler.FILE_TOVISIT_NAME));
            System.out.println("CP4");
            keys.clear();
            for (WebURL up : MyCrawler.toVisit) {
                if(up.getSubDomain().equals("duttgroup.ics")){i++; continue;}
                if(up.getSubDomain().equals("duttgroup-test.ics")){i++; continue;}
                if(up.getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?")){i++; continue;}
                if(keys.add(up)==false){i++; continue;}
                oos.writeObject(up);
                oos.reset();
            }
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("CP5 - " + i);
        //free resources
    }
}
