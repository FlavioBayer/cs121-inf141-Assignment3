/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author flavi
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            //load data
            System.out.println("CP0");
            try {
                File f = new File(MyCrawler.FILE_PAGES_NAME + ".j");
                f.delete();
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
                    Map.Entry<WebURL,HtmlParseData> up = it.next();
                    System.out.println(up.getKey().getURL().toLowerCase());
                    if(up.getKey().getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?"))continue;
                    oos.writeObject(up.getKey());
                    oos.writeObject(up.getValue().getHtml());
                    oos.writeObject(up.getValue().getText());
                    oos.writeObject(up.getValue().getTitle());
                    oos.writeObject(up.getValue().getMetaTags());
                    oos.writeObject(up.getValue().getOutgoingUrls());
                }
                oos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("CP1");
            try {
                File f = new File(MyCrawler.FILE_ERRORS_NAME + ".j");
                f.delete();
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                MyCrawler.errors = MyCrawler.readErrorsData(MyCrawler.FILE_ERRORS_NAME);
                System.out.println("CP2");
                for (Map.Entry<WebURL,Integer> up : MyCrawler.errors.entrySet()) {
                    if(up.getKey().getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?"))continue;
                    oos.writeObject(up.getKey());
                    oos.writeInt(up.getValue());
                }
                oos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                File f = new File(MyCrawler.FILE_TOVISIT_NAME + ".j");
                f.delete();
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                System.out.println("CP3");
                MyCrawler.toVisit = MyCrawler.readToVisitData(MyCrawler.FILE_TOVISIT_NAME);
                System.out.println("CP4");
                for (WebURL up : MyCrawler.toVisit) {
                    if(up.getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?"))continue;
                    oos.writeObject(up);
                }
                System.out.println("CP5");
                oos.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //free resources
    }
        
    
}
