package ir.assignments.three;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class SubdomainsOutgoingLinks {

	public static void main(String[] args) {
		Scanner s;

		Map<String, Integer> subdomainOutgoingLinks = new HashMap<String, Integer>();
		
		try{
			s = new Scanner(new File(args[0]));
			//Gets 
			while(s.hasNextLine()){
				String nextLine = s.nextLine();
				String[] lineContents = nextLine.split("\\s+");
				int outgoingLinks = lineContents.length - 1;
				
				//gets subdomain of link
				String subdomain = lineContents[0].substring(lineContents[0].indexOf("http://") + 7, lineContents[0].indexOf(".edu") - lineContents[0].indexOf("http://") + 4);
				
					if(subdomainOutgoingLinks.get(subdomain) == null){
						subdomainOutgoingLinks.put(subdomain, outgoingLinks);
					}
					else{
						subdomainOutgoingLinks.put(subdomain,  subdomainOutgoingLinks.get(subdomain) + outgoingLinks);
					}
				
			}
			
			s.close();
			
			subdomainOutgoingLinks = sortByKey(subdomainOutgoingLinks);
			
			for(Map.Entry<String, Integer> e : subdomainOutgoingLinks.entrySet()){
				System.out.println(e.getKey() + ", " + e.getValue());
			}
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
		
	}

	private static Map<String, Integer> sortByKey(
			Map<String, Integer> unsortedMap) {
		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortedMap.entrySet());
		 
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
					int keyComparable = ((Comparable<String>) ((Map.Entry<String, Integer>) (o1)).getKey())
					.compareTo(((Map.Entry<String, Integer>) (o2)).getKey());
				
				return keyComparable;
			}
		});
	 
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String,Integer> entry = (Map.Entry<String,Integer>) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
