package search;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Index;

public class SearchEngine {
	
	private final Index index;

	public SearchEngine(Index index) {
		this.index = index;
	}
	
	public String[] search(String... queryTokens) { //TODO: raffinare
		Set<String> candidatedDocuments = new LinkedHashSet<String>(index.getDocumentsIDs(queryTokens[0]));
		for (String token : queryTokens) {
			Set<String> documentsIDs = index.getDocumentsIDs(token);
			// ottengo la lista dei documenti dove occorrono tutte le parole
			candidatedDocuments.retainAll(documentsIDs);
		}
		// ottengo le posizioni delle parole nei documenti
		final Iterator<String> iterator = candidatedDocuments.iterator();
		while (iterator.hasNext()) {
			String docID = iterator.next();
			if(!inARow(docID, queryTokens)) {
				iterator.remove();
			}
		}
		return candidatedDocuments.toArray(new String[0]);
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
		final List<Integer> wordPositions = index.getWordPositions(queryTokens[queryTokenIndex], 
				docID);
		return wordPositions.contains(position) && 
				isInARow(position + 1, docID, queryTokenIndex + 1, queryTokens);
	}

}
