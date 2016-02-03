/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.assignments.three;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author flavi
 */
public class Question5 {
    //What are the 500 most common words in this domain?
    //(Ignore English stop words, which can be found, for example, at http://www.ranks.nl/stopwords.)
    //Submit the list of common words ordered by frequency (and alphabetically for words with the same frequency) in a file called CommonWords.txt.
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashSet<String> stopwords = new HashSet<>();
        stopwords.add("a");
        stopwords.add("about");
        stopwords.add("above");
        stopwords.add("after");
        stopwords.add("again");
        stopwords.add("against");
        stopwords.add("all");
        stopwords.add("am");
        stopwords.add("an");
        stopwords.add("and");
        stopwords.add("any");
        stopwords.add("are");
        stopwords.add("aren't");
        stopwords.add("as");
        stopwords.add("at");
        stopwords.add("be");
        stopwords.add("because");
        stopwords.add("been");
        stopwords.add("before");
        stopwords.add("being");
        stopwords.add("below");
        stopwords.add("between");
        stopwords.add("both");
        stopwords.add("but");
        stopwords.add("by");
        stopwords.add("can't");
        stopwords.add("cannot");
        stopwords.add("could");
        stopwords.add("couldn't");
        stopwords.add("did");
        stopwords.add("didn't");
        stopwords.add("do");
        stopwords.add("does");
        stopwords.add("doesn't");
        stopwords.add("doing");
        stopwords.add("don't");
        stopwords.add("down");
        stopwords.add("during");
        stopwords.add("each");
        stopwords.add("few");
        stopwords.add("for");
        stopwords.add("from");
        stopwords.add("further");
        stopwords.add("had");
        stopwords.add("hadn't");
        stopwords.add("has");
        stopwords.add("hasn't");
        stopwords.add("have");
        stopwords.add("haven't");
        stopwords.add("having");
        stopwords.add("he");
        stopwords.add("he'd");
        stopwords.add("he'll");
        stopwords.add("he's");
        stopwords.add("her");
        stopwords.add("here");
        stopwords.add("here's");
        stopwords.add("hers");
        stopwords.add("herself");
        stopwords.add("him");
        stopwords.add("himself");
        stopwords.add("his");
        stopwords.add("how");
        stopwords.add("how's");
        stopwords.add("i");
        stopwords.add("i'd");
        stopwords.add("i'll");
        stopwords.add("i'm");
        stopwords.add("i've");
        stopwords.add("if");
        stopwords.add("in");
        stopwords.add("into");
        stopwords.add("is");
        stopwords.add("isn't");
        stopwords.add("it");
        stopwords.add("it's");
        stopwords.add("its");
        stopwords.add("itself");
        stopwords.add("let's");
        stopwords.add("me");
        stopwords.add("more");
        stopwords.add("most");
        stopwords.add("mustn't");
        stopwords.add("my");
        stopwords.add("myself");
        stopwords.add("no");
        stopwords.add("nor");
        stopwords.add("not");
        stopwords.add("of");
        stopwords.add("off");
        stopwords.add("on");
        stopwords.add("once");
        stopwords.add("only");
        stopwords.add("or");
        stopwords.add("other");
        stopwords.add("ought");
        stopwords.add("our");
        stopwords.add("ours ourselves");
        stopwords.add("out");
        stopwords.add("over");
        stopwords.add("own");
        stopwords.add("same");
        stopwords.add("shan't");
        stopwords.add("she");
        stopwords.add("she'd");
        stopwords.add("she'll");
        stopwords.add("she's");
        stopwords.add("should");
        stopwords.add("shouldn't");
        stopwords.add("so");
        stopwords.add("some");
        stopwords.add("such");
        stopwords.add("than");
        stopwords.add("that");
        stopwords.add("that's");
        stopwords.add("the");
        stopwords.add("their");
        stopwords.add("theirs");
        stopwords.add("them");
        stopwords.add("themselves");
        stopwords.add("then");
        stopwords.add("there");
        stopwords.add("there's");
        stopwords.add("these");
        stopwords.add("they");
        stopwords.add("they'd");
        stopwords.add("they'll");
        stopwords.add("they're");
        stopwords.add("they've");
        stopwords.add("this");
        stopwords.add("those");
        stopwords.add("through");
        stopwords.add("to");
        stopwords.add("too");
        stopwords.add("under");
        stopwords.add("until");
        stopwords.add("up");
        stopwords.add("very");
        stopwords.add("was");
        stopwords.add("wasn't");
        stopwords.add("we");
        stopwords.add("we'd");
        stopwords.add("we'll");
        stopwords.add("we're");
        stopwords.add("we've");
        stopwords.add("were");
        stopwords.add("weren't");
        stopwords.add("what");
        stopwords.add("what's");
        stopwords.add("when");
        stopwords.add("when's");
        stopwords.add("where");
        stopwords.add("where's");
        stopwords.add("which");
        stopwords.add("while");
        stopwords.add("who");
        stopwords.add("who's");
        stopwords.add("whom");
        stopwords.add("why");
        stopwords.add("why's");
        stopwords.add("with");
        stopwords.add("won't");
        stopwords.add("would");
        stopwords.add("wouldn't");
        stopwords.add("you");
        stopwords.add("you'd");
        stopwords.add("you'll");
        stopwords.add("you're");
        stopwords.add("you've");
        stopwords.add("your");
        stopwords.add("yours");
        stopwords.add("yourself");
        stopwords.add("yourselves");
        
        System.out.println("Loading Pages...");
        System.out.println("Pages loaded! Processing(1)...");
        Map<String,Integer> tokenCount = new HashMap<>();
        for(Iterator<Map.Entry<WebURL,HtmlParseData>> it=MyCrawler.iteratePagesData(MyCrawler.FILE_PAGES_NAME); it.hasNext(); ){
            Map.Entry<WebURL,HtmlParseData> up = it.next();
            if(up.getValue().getText().length()>1000000)continue;//ignore files bigger than 1MB
            List<String> tokens = ir.assignments.two.a.Utilities.tokenizeString(up.getValue().getText());
            for(String token : tokens){
                if(stopwords.contains(token))continue;
                Integer currentSubdomainCount = tokenCount.get(token);
                if(currentSubdomainCount==null){
                    currentSubdomainCount = 0;
                }
                currentSubdomainCount++;
                tokenCount.put(token, currentSubdomainCount);
            }
        }
        System.out.println("(2)...");
        ArrayList< Map.Entry<String,Integer> > sortedTokens = new ArrayList<>(tokenCount.entrySet());
        tokenCount.clear();
        sortedTokens.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e0, Map.Entry<String, Integer> e1) {
                if(!e0.getValue().equals(e1.getValue())){
                    return -e0.getValue().compareTo(e1.getValue());
                }else{
                    return e0.getKey().compareTo(e1.getKey());
                }
            }
        });
        for(int i=0; i<500 && i<sortedTokens.size(); i++){
            System.out.println(sortedTokens.get(i).getKey() + ", " + sortedTokens.get(i).getValue());
        }
    } 
    
}
