package engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import model.Document;
import model.Index;
import parser.Indexer;
import parser.Parser;

public class Engine {
	
	private static final String INPUT_FILE = "/reut2-000.sgm";
	
	public Engine() {}
	
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		List<Document> documents = parser.parse(new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(INPUT_FILE))));
		Indexer indexer = new Indexer();
		indexer.index(documents);
		Index index = new Index();
		System.out.println(index.toString());
	}

}
