package ir.assignments.threeC;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {

	public static Collection<String> crawl(final String seedURL) {
		QuestionFive.solveQuestion5();
		return null;
	}

	// Do not find any of these patterns, ignore them all
	public final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf"
					+ "|webm|tar|wma|zip|rar|gz|xz|bz|lz|7z|dmg))$");

	// URLs must conform to this domain
	// In this regex the DOT '.' character GLOBS to an ANY character
	// Start with 'http://' contains ics uci and edu
	public final static Pattern SUFFIX = Pattern.compile("^http://.*\\.ics\\.uci\\.edu/.*");

	// TRAP URLs which will be difficult to crawl or impossible to crawl
	// Start with 'http://' contains ics uci and edu
	// Subdomain is either ftp | archive, etc
	public final static Pattern TRAPS = Pattern.compile("^http://(archive|calendar)\\.ics\\.uci\\.edu/.*");

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(final Page referringPage, final WebURL url) {
		final String href = url.getURL().toLowerCase();
		// Make sure that the file is not any of the file types we do not want,
		// comes from the ics.uci.edu domain, is not a query of any kind,
		// and does not come from any of the TRAP URL domains specified above.
		return !FILTERS.matcher(href).matches()
				// && !TRAPS.matcher(href).matches()
				&& SUFFIX.matcher(href).matches();
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(final Page page) {
		final WebURL url = page.getWebURL();

		if (page.getParseData() instanceof HtmlParseData) {
			final HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			final String title = htmlParseData.getTitle();
			final String text = htmlParseData.getText();
			final Set<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("URL: " + url.getURL());

			writeLog(url, title, text);
			writeWordCounts(url, text);
			writeOutgoingLinks(url, links);
		}
	}

	private int wordCount(final String pageText) {
		return pageText.split("\\s+").length;
	}

	private void writeWordCounts(final WebURL url, final String text) {
		try {
			// Write out a single line which contains the word count and the URL
			final StringBuilder builder = new StringBuilder(String.valueOf(wordCount(text)));
			builder.append("   ");
			builder.append(url.getURL());
			builder.append("\n");

			// Write the file
			final FileWriter fWriter = new FileWriter(Controller.WORDCOUNT_FILE, true);
			fWriter.write(builder.toString());
			fWriter.close();
		} catch (final Exception e) {
			System.err.println("Error when writing " + url.getURL());
		}
	}

	private void writeOutgoingLinks(final WebURL url, final Set<WebURL> links){
		try {
			// Write out a single line which contains all of the outgoing links found on the given URL
			final StringBuilder builder = new StringBuilder(url.getURL());
			builder.append("   ");
			for (final WebURL link : links) {
				builder.append(link.getURL());
				builder.append(" ");
			}
			builder.append("\n");

			// Write the file
			final FileWriter fWriter = new FileWriter(Controller.LINKS_FILE, true);
			fWriter.write(builder.toString());
			fWriter.close();
		} catch (final Exception e) {
			System.err.println("Error when writing " + url.getURL());
		}
	}

	private void writeLog(final WebURL url, final String title, final String text) {
		try {
			// Long way of basically mirroring the webserver

			// The remaining that will be mirrored into the LOG_DIR
			final int STRIP_HTTP = "http://".length();
			final String path = url.getURL().substring(STRIP_HTTP);
			final String logPath = Controller.LOG_DIR + path;

			File file = new File(logPath);
			final String urlPath = url.getPath();
			// If the retrieved URL is a directory, store it as index.txt
			if (file.isDirectory()) {
				file = new File(logPath + "index.txt");
			} else if (urlPath.endsWith("/")) {
				// This is a top level domain index file.
				// Append the / to show that it is a folder
				file = new File(logPath + "/index.txt");
			}
			// Make sure that parent directories exist before attempting to write data
			if (!file.exists()) {
				// This will mirror the webserver by creating all parent directories
				final File parent = new File(file.getParent());
				if (!parent.exists()){
					parent.mkdirs();
				}
			}

			// Finally write the contents to the file, including title and text
			final FileWriter fWriter = new FileWriter(file);
			if (title != null) {
				fWriter.write(title);
			}
			if (text != null) {
				fWriter.write(text);
			}
			fWriter.close();
		} catch (final Exception e) {
			System.err.println("Error when writing " + url.getURL());
		}
	}

}
