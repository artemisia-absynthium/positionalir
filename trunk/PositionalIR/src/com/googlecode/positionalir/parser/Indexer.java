package com.googlecode.positionalir.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.positionalir.model.Document;
import com.googlecode.positionalir.model.Index;
import com.googlecode.positionalir.util.IOUtil;
import com.googlecode.positionalir.util.LogUtil;


public class Indexer {
	
	private final Logger log = LogUtil.getLogger(Indexer.class);
	
	private final Index index;
	
	private final Tokenizer tokenizer;
	
	private final Parser parser;
	
	public Indexer() {
		this(new Index());
	}
	
	public Indexer(Index index) {
		this(index, new Tokenizer(), new Parser());
	}
	
	public Indexer(Index index, Tokenizer tokenizer, Parser parser) {
		this.index = index;
		this.parser = parser;
		this.tokenizer = tokenizer;
	}

	public Index getIndex() {
		return index;
	}
	
	public void index(String... files) {
		log.info(LogUtil.logTaskStart("Indexing files"));
		for (String file : files) {
			log.info(LogUtil.logTaskStart("Processing File(" + file + ")"));
			
			final BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(file)));
			try {
				List<Document> parseDoc = this.parser.parse(reader);
				this.index(parseDoc);
			} catch (IOException e) {
				log.log(Level.SEVERE, LogUtil.log("Errore nel parsing del file: " + file + "."), e);
				continue;
			} finally {
				log.info(LogUtil.logTaskEnd("Processing File(" + file + ")"));
				IOUtil.close(reader);
			}
		}
		log.info(LogUtil.logTaskEnd("Indexing files"));
	}
	

	public void index(List<Document> documents) {
		log.info(LogUtil.logTaskStart("Indexing Documents"));
		for(Document document : documents) {
			this.createInverseIndex(document);
		}
		log.info(LogUtil.logTaskEnd("Indexing Documents"));
	}

	private void createInverseIndex(Document document) {
		String[] bagOfWords = tokenizer.tokenizeDocument(document);
		int i = 1;
		for (String word : bagOfWords) {
			if (word.matches("\\w+")) {
				this.index.addWord(word, document.getNewid(), i);
				i++;
			}
		}
	}
}
