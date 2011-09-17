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
	
	public String[] search(String... queryTokens) {
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
		List<Integer> positions1 = index.getWordPositions(queryTokens[0], docID);
		for (int i = 1; i < queryTokens.length; i++) {
			List<Integer> positions2 = index.getWordPositions(queryTokens[i], docID);
			if(!containsNumbersInARow(positions1, positions2))
				return false;
			positions1 = positions2;
		}
		return true;
	}

	private boolean containsNumbersInARow(List<Integer> positions1, List<Integer> positions2) {
		Iterator<Integer> it1 = positions1.iterator();
		while (it1.hasNext()) {
			int pos1 = it1.next();
			if (positions2.contains(pos1 + 1)) {
				return true;
			}
		}
		return false;
	}

}
