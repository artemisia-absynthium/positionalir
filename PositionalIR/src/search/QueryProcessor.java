package search;

import parser.Tokenizer;

public class QueryProcessor {
	
	private final Tokenizer tokenizer;
	
	public QueryProcessor() {
		this.tokenizer = new Tokenizer();
	}
	
	public String[] process(String query) {
		return tokenizer.tokenizeQuery(query);
	}

}
