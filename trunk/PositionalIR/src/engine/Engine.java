package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import model.Document;
import parser.Indexer;
import parser.Parser;
import search.SearchEngine;

public class Engine {
	
	private static final String INPUT_FILE = "/reut2-000.sgm";
	
	public Engine() {}
	
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		List<Document> documents = parser.parse(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(INPUT_FILE))));
		Indexer indexer = new Indexer();
		indexer.index(documents);
//		System.out.println(indexer.getIndex().toString());
		SearchEngine engine = new SearchEngine(indexer.getIndex());
		final String[] result = engine.search("in", "the");
		System.out.println(Arrays.toString(result));
	}

}
