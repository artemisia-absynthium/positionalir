package search;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import util.LogUtil;

import model.Index;

public class SearchEngine {
	
	private final Logger log = LogUtil.getLogger(SearchEngine.class);
	
	private final Index index;
	
	private final QueryProcessor queryProcessor;

	public SearchEngine(Index index) {
		this(index, new QueryProcessor());
	}
	
	public SearchEngine(Index index, QueryProcessor queryProcessor) {
		this.index = index;
		this.queryProcessor = queryProcessor;
	}
	
	public String[] searchQuery(String query) {
		log.info(LogUtil.logTaskStart("Search Query"));
		final String[] queryTokens = this.queryProcessor.process(query);
		final String[] result = this.search(queryTokens);
		log.info(LogUtil.logTaskEnd("Search Query"));
		return result;
	}
	
	public String[] search(String... queryTokens) { //TODO: raffinare nomi variabili
		log.info(LogUtil.logTaskStart("Search DocID"));
		
		log.info(LogUtil.logTaskStart("Filtering Doc with all token"));
		final Set<String> candidateDocuments = new LinkedHashSet<String>(index.getDocumentsIDs(queryTokens[0]));
		for (String token : queryTokens) {
			candidateDocuments.retainAll(index.getDocumentsIDs(token));
		}
		log.info(LogUtil.logTaskEnd("Filtering Doc with all token"));
		log.info(LogUtil.log("Found " + candidateDocuments.size() + " docs with all token " + candidateDocuments + "."));
		log.info(LogUtil.logTaskStart("Filtering Doc with consecutive token"));
		final Iterator<String> iterator = candidateDocuments.iterator();
		while (iterator.hasNext()) {
			final String docID = iterator.next();
			log.info(LogUtil.logTaskStart("Checking Doc(" + docID + ")"));
			if(!inARow(docID, queryTokens)) {
				iterator.remove();
			}
			log.info(LogUtil.logTaskEnd("Checking Doc(" + docID + ")"));
		}
		log.info(LogUtil.logTaskEnd("Filtering Doc with consecutive token"));
		
		final String[] result = candidateDocuments.toArray(new String[0]);
		log.info(LogUtil.logTaskEnd("Search DocID"));
		return result;
	}

	private boolean inARow(String docID, String[] queryTokens) {
		final List<Integer> wordPositions = index.getWordPositions(queryTokens[0], docID);
		for (int position : wordPositions) {
			if (this.isInARow(position + 1, docID, 1, queryTokens)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isInARow(int position, String docID, int queryTokenIndex, String[] queryTokens) {
		if (queryTokenIndex >= queryTokens.length) {
			return true;
		}
		final List<Integer> wordPositions = index.getWordPositions(queryTokens[queryTokenIndex], docID);
		return wordPositions.contains(position) && 
				isInARow(position + 1, docID, queryTokenIndex + 1, queryTokens);
	}

}
