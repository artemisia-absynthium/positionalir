package com.googlecode.positionalir.engine;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import model.Index;
import parser.Indexer;
import search.SearchEngine;
import serializer.IndexSerializer;
import util.LogUtil;

public class Engine {
	
	private final Logger log = LogUtil.getLogger(Engine.class);
	
	private final IndexSerializer indexSerializer;
	
	public Engine() {
		this.indexSerializer = new IndexSerializer("reuters.index");
	}
	
	public static void main(String[] args) throws IOException {
		LogManager.getLogManager().readConfiguration(Engine.class.getResourceAsStream("/logging.properties"));
		
		//l'indice completo in memoria occupa oltre 256 MB ma meno di 512 MB
		//Per il parsing di tutti aggiungere come opzione java -Xmx512m
		boolean rebuildIndex = false;
		
		final Engine engine = new Engine();
		
		final Index index;
		
		if(rebuildIndex) {
			index = engine.buildReutersIndex();
			engine.saveIndex(index);
		} else {
			index = engine.loadIndex();
		}
		
//		System.out.println(index.getWordPositions("on", "397").toString());
//		System.out.println(index.getWordPositions("the", "397").toString());
//		System.out.println(index.getWordPositions("news", "397").toString());
		
		SearchEngine searchEngine = new SearchEngine(index);
		
		String[] trySearch = searchEngine.searchQuery("on the news");
		
		System.out.println(Arrays.toString(trySearch));
	}

	public Index loadIndex() {
		return indexSerializer.loadIndex();
	}
	
	public void saveIndex(Index index) {
		indexSerializer.saveIndex(index);
	}

	public Index buildReutersIndex() {
		log.info(LogUtil.logTaskStart("Build Index"));
		Indexer indexer = new Indexer();
		indexer.index("/reut2-000.sgm"
				, "/reut2-001.sgm", "/reut2-002.sgm", "/reut2-003.sgm", "/reut2-004.sgm"
				, "/reut2-005.sgm", "/reut2-006.sgm", "/reut2-007.sgm", "/reut2-008.sgm"
				, "/reut2-009.sgm", "/reut2-010.sgm", "/reut2-011.sgm", "/reut2-012.sgm"
				, "/reut2-013.sgm", "/reut2-014.sgm", "/reut2-015.sgm", "/reut2-016.sgm"
				, "/reut2-017.sgm", "/reut2-018.sgm", "/reut2-019.sgm", "/reut2-020.sgm"
				, "/reut2-021.sgm"
				);
		log.info(LogUtil.logTaskEnd("Build Index"));
		return indexer.getIndex();
	}

}
