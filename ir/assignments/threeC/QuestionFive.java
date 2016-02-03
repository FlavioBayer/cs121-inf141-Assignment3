package ir.assignments.three;

import ir.assignments.two.a.Frequency;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class QuestionFive {

	public static final String BASE_DIR = "/scratch/pyamanak/crawlerContents/";
	public static final String CRAWL_ROOT = BASE_DIR + "dist/";
	private static final HashSet<String> stopWords = new HashSet<>();
	private static final String STOP_WORDS = CRAWL_ROOT + "stopwords.txt";

	public static void solveQuestion5() {
		try {
			if (!fillStopWords()) {
				return;
			}
			final File crawlLogs = new File("/scratch/pyamanak/crawlerContents/dist/crawl");
			final ArrayList<Frequency> al = new ArrayList<>();
			final HashMap<String, Integer> map = new HashMap<>();
			listFiles(map, crawlLogs);
			addMapToList(map, al);
			sortList(al);
			writeFile(al);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeFile(final ArrayList<Frequency> al) throws IOException {
		final File f = new File("/scratch/pyamanak/crawlerContents/dist/answer.txt");
		final FileWriter fwr = new FileWriter(f, true);
		final StringBuilder sb = new StringBuilder();
		for (final Frequency fr : al) {
			sb.append(String.format("%d %s\n", fr.getFrequency(), fr.getText()));
		}
		fwr.write(sb.toString());
		fwr.close();
	}

	private static void sortList(final ArrayList<Frequency> al) {
		Collections.sort(al, new Comparator<Frequency>() {
			@Override
			public final int compare(final Frequency f1, final Frequency f2) {
				int diff;
				diff = f2.getFrequency() - f1.getFrequency();
				if (diff == 0) {
					diff = f1.getText().compareTo(f2.getText());
				}
				return diff;
			}
		});
	}

	private static void listFiles(final HashMap<String, Integer> map, final File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				for (final File f : file.listFiles()) {
					listFiles(map, f);
				}
			} else {
				final ArrayList<String> tokens = tokenizeFile(file);
				for (final String token : tokens) {
					if (token.length() > 2 && !isNumeric(token)) {
						if (map.containsKey(token)) {
							map.put(token, map.get(token) + 1);
						} else {
							map.put(token, 1);
						}
					}
				}
			}
		} else {
			System.out.println("Cannot list file: " + file.getPath());
		}
	}

	private static boolean isNumeric(final String s) {
	    return s.matches("[-+]?\\d*\\.?\\d+");
	}

	private static void addMapToList(final HashMap<String, Integer> map, final ArrayList<Frequency> al) {
		for (final Map.Entry<String, Integer> entry : map.entrySet()) {
			al.add(new Frequency(entry.getKey(), entry.getValue()));
		}
	}


	public static boolean fillStopWords() throws FileNotFoundException {
		final File f = new File(STOP_WORDS);
		if (f.exists()) {
			final Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				final String s = sc.nextLine();
				stopWords.add(s);
			}
			sc.close();
			return true;
		} else {
			return false;
		}
	}

	public static boolean isStopWord(final String s) {
		return stopWords.contains(s);
	}


	/**
	 * Reads the input text file and splits it into alphanumeric tokens. Returns
	 * an ArrayList of these tokens, ordered according to their occurrence in
	 * the original text file.
	 *
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case.
	 *
	 * Example:
	 *
	 * Given this input string "An input string, this is! (or is it?)"
	 *
	 * The output list of strings should be ["an", "input", "string", "this",
	 * "is", "or", "is", "it"]
	 *
	 * @param input
	 *            The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by
	 *         occurrence.
	 */
	public static ArrayList<String> tokenizeFile(File input) {
		/*
		 * Return null if the File input is null or if the file does not exist
		 * for some reason
		 */
		if (input == null || !input.exists()) {
			return null;
		}

		final ArrayList<String> tokenList = new ArrayList<>();
		try {
			final Scanner scanner = new Scanner(input);
			while (scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				final String[] sanitized = sanitizeString(line);
				if (sanitized != null) {
					for (final String token : sanitized) {
						if (token.length() > 0 && !isStopWord(token)) {
							tokenList.add(token);
						}
					}
				}
			}
			scanner.close();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return tokenList;
	}

	public static String[] sanitizeString(final String string) {
			return string != null ? string.toLowerCase()
					.replaceAll("[^A-Za-z0-9]", " ").split("\\s+") : null;
	}

}

