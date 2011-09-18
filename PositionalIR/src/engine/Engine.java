package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import model.Document;
import model.Index;
import parser.Indexer;
import parser.Parser;
import search.QueryProcessor;
import search.SearchEngine;
import serializer.IndexSerializer;
import util.LogUtil;

public class Engine {
	
	private static final Logger log = LogUtil.getLogger(Engine.class);
	
	private static final String INPUT_FILE = "/reut2-000.sgm";
	
	public Engine() {}
	
	public static void main(String[] args) throws IOException {
		LogManager.getLogManager().readConfiguration(Engine.class.getResourceAsStream("/logging.properties"));
		final IndexSerializer indexSerializer = new IndexSerializer("reuters.index");
		
		Index index;
		
		log.info(LogUtil.logTaskStart("Build Index"));
		index = buildIndexFromFile();
		log.info(LogUtil.logTaskEnd("Build Index"));
		
		indexSerializer.saveIndex(index);
		
		index = indexSerializer.loadIndex();
		
//		System.out.println(index.getWordPositions("on", "397").toString());
//		System.out.println(index.getWordPositions("the", "397").toString());
//		System.out.println(index.getWordPositions("news", "397").toString());
		
		String[] trySearch = trySearch(index, "on the news");
		System.out.println(Arrays.toString(trySearch));
	}

	private static String[] trySearch(Index index, String query) {
		log.info(LogUtil.logTaskStart("Search"));
		SearchEngine engine = new SearchEngine(index);
		QueryProcessor processor = new QueryProcessor();
		final String[] result = engine.search(processor.process(query));
		log.info(LogUtil.logTaskEnd("Search"));
		return result;
	}

	private static Index buildIndexFromFile() throws IOException {
		Parser parser = new Parser();//TODO: introdurre il parser nell'indexer
		List<Document> documents = parser.parse(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(INPUT_FILE))));
		Indexer indexer = new Indexer();
		indexer.index(documents);
		return indexer.getIndex();
	}

}
