package ir.assignments.three;

import java.io.File;
import java.io.FileWriter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyCrawler extends WebCrawler {
    public static final String FILE_PAGES_NAME = MyController.WORK_DIRECTORY + "/pages.j";
    public static final String FILE_ERRORS_NAME = MyController.WORK_DIRECTORY + "/errors.j";
    public static final String FILE_TOVISIT_NAME = MyController.WORK_DIRECTORY + "/toVisit.j";
    public static HashMap<WebURL,HtmlParseData> pages;
    public static HashMap<WebURL,Integer> errors;
    public static HashSet<WebURL> toVisit;
    public static int totalToVisit, totalVisited, totalError;

    public MyCrawler() {
        super();
        if(pages==null || errors==null || toVisit==null){
            System.out.println("MyCrawler Says: Overriting default containers");
            pages = new HashMap<>();
            errors = new HashMap<>();
            toVisit = new HashSet<>();
            totalToVisit = 0;
            totalVisited = 0;
            totalError = 0;
        }
    }
    
    @Override
    public boolean shouldVisit(Page refer, WebURL wurl) { 
        try{
            final String[] inavalidEndings = new String[]{
                "pdf", "ppt", "pptx", "pps", "doc", "docx", "csv", "xls", "xlsx",//docs
                "webm", "mp4", "avi", "wmv", "flv", "mpeg", "mpg", "mov", "m4v",//video
                "wav", "mp3", "m4a", "wma",//audio
                "png", "jpg", "jpeg", "bmp", "gif", "tiff",//images
                "tar", "zip", "7z", "rar", "dmg", "gz", "xz", "bz", "lz", "jar",//containers
                "js", "css", "swf", "exe", "ps", "bg", "db"//other 
            };
            //check if it's invalid
            if(wurl.getDomain().toLowerCase().equals("uci.edu") && (wurl.getSubDomain().toLowerCase().equals("ics") || wurl.getSubDomain().toLowerCase().endsWith(".ics"))){
                //filter traps
                if(wurl.getSubDomain().toLowerCase().endsWith("calendar.ics"))return false;
                if(wurl.getURL().toLowerCase().startsWith("http://archive.ics.uci.edu/ml/datasets.html?"))return false;
                //filter unwanted file extensions
                for(String ending : inavalidEndings){
                    if(wurl.getPath().toLowerCase().endsWith("." + ending))return false;
                }
                //put in the toVisit queue
                if(toVisit.add(wurl)==false){//ignore if it's in the toVisit queue
                    return false;
                }else{
                    totalToVisit++;
                    System.out.print("\r\rA.");
                    System.out.flush();
                    System.out.print(appendToVisitData(FILE_TOVISIT_NAME, wurl)?"\r\rA+":"\r\rA-");
                    System.out.flush();
                }
                return true;
            }else{
                //System.out.println("Page not visited: '" + wurl.getURL() + "'");
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private String getProgressText(){
        return String.format("%06d/%06d - %05.2f%% - ", this.totalVisited+this.totalError, this.totalToVisit, 100.*(this.totalVisited+this.totalError)/this.totalToVisit);
    }

    @Override
    public void visit(Page page) {
        try{
            WebURL url = page.getWebURL();
            if (page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParsedPage = (HtmlParseData)page.getParseData();
                totalVisited++;
                //pages.put(url, htmlParsedPage);
                System.out.println(getProgressText() + "OK: '" + url.getURL() + "' fetched! Storing html '" + urlToLocalFileName(url) + "'");
                System.out.print("\r\rC.");
                System.out.flush();
                System.out.print(appendPagesData(FILE_PAGES_NAME, url, htmlParsedPage)?"\r\rC+":"\r\rC-");
                System.out.flush();
                writeTextToFile(MyController.WORK_DIRECTORY + "/html/" + urlToLocalFileName(url), htmlParsedPage.getHtml());
            }else if (page.getParseData() instanceof TextParseData) {
                TextParseData textParsedPage = (TextParseData)page.getParseData();
                HtmlParseData htmlParsedPage = new HtmlParseData();
                //htmlParsedPage.setHtml();
                htmlParsedPage.setText(textParsedPage.getTextContent());
                htmlParsedPage.setOutgoingUrls(textParsedPage.getOutgoingUrls());
                totalVisited++;
                //pages.put(url, htmlParsedPage);
                System.out.println(getProgressText() + "OK: '" + url.getURL() + "' fetched! Storing html '" + urlToLocalFileName(url) + "'");
                System.out.print("\r\rD.");
                System.out.flush();
                System.out.print(appendPagesData(FILE_PAGES_NAME, url, htmlParsedPage)?"\r\rD+":"\r\rD-");
                System.out.flush();
                writeTextToFile(MyController.WORK_DIRECTORY + "/html/" + urlToLocalFileName(url), textParsedPage.getTextContent());
            }else{
                totalError++;
                errors.put(url, -4);
                System.out.println(getProgressText() + "NOK: InavelidClass, '" + url.getURL() + "' fetched, but it's an instance of " + page.getParseData().getClass().getName());
                System.out.print("\r\rE.");
                System.out.flush();
                System.out.print(appendErrorData(FILE_ERRORS_NAME, url, -4)?"\r\rE+":"\r\rE-");
                System.out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
        try{
            WebURL webUrl = new WebURL();
            webUrl.setURL(urlStr);
            System.out.println(getProgressText() + "NOK: onUnexpectedStatusCode '" + urlStr + "':" + statusCode + " ("+contentType+"):" + description);
            totalError++;
            errors.put(webUrl, statusCode);
            System.out.print("\r\rG.");
            System.out.flush();
            System.out.print(appendErrorData(FILE_ERRORS_NAME, webUrl, statusCode)?"\r\rG+":"\r\rG-");
            System.out.flush();
            super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
        try{
            WebURL webUrl = new WebURL();
            webUrl.setURL(urlStr);
            System.out.println(getProgressText() + "NOK: onPageBiggerThanMaxSize: '" + webUrl.getURL() + "', " + pageSize);
            totalError++;
            errors.put(webUrl, -1);
            System.out.print("\r\rH.");
            System.out.flush();
            System.out.print(appendErrorData(FILE_ERRORS_NAME, webUrl, -1)?"\r\rH+":"\r\rH-");
            System.out.flush();
            super.onPageBiggerThanMaxSize(urlStr, pageSize);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onContentFetchError(WebURL webUrl) {
        try{
            System.out.println(getProgressText() + "NOK: onContentFetchError: '" + webUrl.getURL() + "'");
            totalError++;
            errors.put(webUrl, -2);
            System.out.print("\r\rI.");
            System.out.flush();
            System.out.print(appendErrorData(FILE_ERRORS_NAME, webUrl, -2)?"\r\rI+":"\r\rI-");
            System.out.flush();
            super.onContentFetchError(webUrl);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onParseError(WebURL webUrl) {
        try{
            System.out.println(getProgressText() + "NOK: onParseError: '" + webUrl.getURL() + "'");
            totalError++;
            errors.put(webUrl, -3);
            System.out.print("\r\rJ.");
            System.out.flush();
            System.out.print(appendErrorData(FILE_ERRORS_NAME, webUrl, -3)?"\r\rI+":"\r\rJ-");
            System.out.flush();
            super.onParseError(webUrl);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private String urlToLocalFileName(final WebURL url){
        String posFileName = "";
        int pqm = url.getURL().indexOf("?");
        if(pqm!=-1){
            posFileName += "__" + url.getURL().substring(pqm+1).replaceAll("^[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "") + ".html";//add posQuestionMark
            //OK: remove invalid characters, like ':', http://stackoverflow.com/questions/754307/regex-to-replace-characters-that-windows-doesnt-accept-in-a-filename
        }if(url.getPath().endsWith("/")){
            posFileName = ".html";
        }
        File f = new File(url.getPath());
        return url.getSubDomain() + '.' + url.getDomain() + f.getParent() + "/file_" + f.getName() + posFileName;
    }

    private synchronized static boolean writeTextToFile(String fileName, String text) {
        try {
            File file = new File(fileName);
            //create parent directories
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            //write html to file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public synchronized static boolean appendPagesData(String fileName, WebURL key, HtmlParseData value) {
        try {
            File f = new File(fileName);
            FileOutputStream fos;
            ObjectOutputStream oos;
            if(!f.exists()){
                f.createNewFile();
                fos = new FileOutputStream(f);
                oos = new ObjectOutputStream(fos);
            }else{
                fos = new FileOutputStream(f, true);
                oos = new AppendingObjectOutputStream(fos);
            }
            
            oos.writeObject(key);
            oos.writeObject(value.getHtml());
            oos.writeObject(value.getText());
            oos.writeObject(value.getTitle());
            oos.writeObject(value.getMetaTags());
            oos.writeObject(value.getOutgoingUrls());
            
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static HashMap<WebURL, HtmlParseData> readPagesData(String fileName) {
        HashMap<WebURL, HtmlParseData> r = new HashMap<>();
        try {
            File f = new File(fileName);
            FileInputStream fos = new FileInputStream(f);
            ObjectInputStream oos = new ObjectInputStream(fos);
            
            while(true){
                WebURL url = (WebURL)oos.readObject();
                HtmlParseData hpd = new HtmlParseData();
                hpd.setHtml((String)oos.readObject());
                hpd.setText((String)oos.readObject());
                hpd.setTitle((String)oos.readObject());
                hpd.setMetaTags((Map<String,String>)oos.readObject());
                hpd.setOutgoingUrls((Set<WebURL>)oos.readObject());
                r.put(url, hpd);
            }
        } catch (EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return r;
    }
    public static Iterator<Map.Entry<WebURL, HtmlParseData>> iteratePagesData(String fileName) {
        try {
            return new Iterator<Map.Entry<WebURL, HtmlParseData>>() {
                final File f = new File(fileName);
                final FileInputStream fos = new FileInputStream(f);
                final ObjectInputStream oos = new ObjectInputStream(fos);
                Map.Entry<WebURL, HtmlParseData> next = null;
                @Override
                public boolean hasNext() {
                    next = null;
                    try{
                        WebURL url = (WebURL)oos.readObject();
                        HtmlParseData hpd = new HtmlParseData();
                        hpd.setHtml((String)oos.readObject());
                        hpd.setText((String)oos.readObject());
                        hpd.setTitle((String)oos.readObject());
                        hpd.setMetaTags((Map<String,String>)oos.readObject());
                        hpd.setOutgoingUrls((Set<WebURL>)oos.readObject());
                        next = new AbstractMap.SimpleEntry<>(url, hpd);
                        return true;
                    }catch(EOFException e){
                        return false;
                    } catch (IOException ex) {
                        Logger.getLogger(MyCrawler.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MyCrawler.class.getName()).log(Level.SEVERE, null, ex);
                        return false;
                    }
                }
                @Override
                public Map.Entry<WebURL, HtmlParseData> next() {
                    if(next==null && !hasNext())throw new NoSuchElementException();
                    Map.Entry<WebURL, HtmlParseData> r = next;
                    next = null;
                    return r;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized static boolean appendErrorData(String fileName, WebURL wurl, Integer errorCode) {
        try {
            File f = new File(fileName);
            FileOutputStream fos;
            ObjectOutputStream oos;
            if(!f.exists()){
                f.createNewFile();
                fos = new FileOutputStream(f);
                oos = new ObjectOutputStream(fos);
            }else{
                fos = new FileOutputStream(f, true);
                oos = new AppendingObjectOutputStream(fos);
            }
            oos.writeObject(wurl);
            oos.writeInt(errorCode);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static HashMap<WebURL, Integer> readErrorsData(String fileName) {
        HashMap<WebURL, Integer> r = new HashMap<>();
        try {
            File f = new File(fileName);
            FileInputStream fos = new FileInputStream(f);
            ObjectInputStream oos = new ObjectInputStream(fos);
            
            while(true){
                WebURL wurl = (WebURL)oos.readObject();
                int errorCode = oos.readInt();
                r.put(wurl, errorCode);
            }
        } catch (EOFException e) {
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return r;
    }
    
    public synchronized static boolean appendToVisitData(String fileName, WebURL wurl){
        try{
            File f = new File(fileName);
            FileOutputStream fos;
            ObjectOutputStream oos;
            if(!f.exists()){
                f.createNewFile();
                fos = new FileOutputStream(f);
                oos = new ObjectOutputStream(fos);
            }else{
                fos = new FileOutputStream(f, true);
                oos = new AppendingObjectOutputStream(fos);
            }
            oos.writeObject(wurl);
            oos.close();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static HashSet<WebURL> readToVisitData(String fileName){
        HashSet<WebURL> r = new HashSet<>();
            int er = 0;
        try{
            File f = new File(fileName);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while(true){
                WebURL wu = (WebURL)ois.readObject();
                r.add(wu);
            }
        }catch(EOFException e){
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return r;
    }
}

//http://stackoverflow.com/questions/1194656/appending-to-an-objectoutputstream?rq=1
class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // do not write a header, but reset:
        // this line added after another question
        // showed a problem with the original
        reset();
    }
}