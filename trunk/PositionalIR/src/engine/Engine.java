package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import model.Document;
import parser.Indexer;
import parser.Parser;
import search.QueryProcessor;
import search.SearchEngine;

public class Engine {
	
	private static final String INPUT_FILE = "/reut2-000.sgm";
	
	public Engine() {}
	
	public static void main(String[] args) throws IOException {
		final long start = System.currentTimeMillis();
		Parser parser = new Parser();
		List<Document> documents = parser.parse(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(INPUT_FILE))));
		Indexer indexer = new Indexer();
		indexer.index(documents);
		System.out.println(indexer.getIndex().getWordPositions("on", "397").toString());
		System.out.println(indexer.getIndex().getWordPositions("the", "397").toString());
		System.out.println(indexer.getIndex().getWordPositions("news", "397").toString());
		SearchEngine engine = new SearchEngine(indexer.getIndex());
		QueryProcessor processor = new QueryProcessor();
		final String[] result = engine.search(processor.process("on the news"));
		final long end = System.currentTimeMillis();
		System.out.println(Arrays.toString(result));
		System.out.println(end - start + " millisecondi");
	}

}
