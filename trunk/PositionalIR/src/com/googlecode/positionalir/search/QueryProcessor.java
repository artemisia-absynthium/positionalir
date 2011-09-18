package com.googlecode.positionalir.search;

import java.util.logging.Logger;

import com.googlecode.positionalir.parser.Tokenizer;
import com.googlecode.positionalir.util.LogUtil;


public class QueryProcessor {
	
	private final Logger log = LogUtil.getLogger(QueryProcessor.class);
	
	private final Tokenizer tokenizer;
	
	public QueryProcessor() {
		this.tokenizer = new Tokenizer();
	}
	
	public String[] process(String query) {
		log.info(LogUtil.logTaskStart("Processing Query"));
		final String[] result = tokenizer.tokenizeQuery(query);
		log.info(LogUtil.logTaskEnd("Processing Query"));
		return result;
	}

}
