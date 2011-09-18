package parser;

import java.util.List;

import model.Document;
import model.Index;

public class Indexer {
	
	private final Index index;
	
	private final Tokenizer tokenizer;
	
	public Indexer() {
		this(new Index());
	}
	
	public Indexer(Index index) {
		this.index = index;
		this.tokenizer = new Tokenizer();
	}

	public Index getIndex() {
		return index;
	}

	public void index(List<Document> documents) {
		for(Document document : documents) {
			this.createInverseIndex(document);
		}
	}

	private void createInverseIndex(Document document) {
		String[] bagOfWords = tokenizer.tokenizeDocument(document);
		int i = 1;
		for (String word : bagOfWords) {
			if (word.matches("\\w+")) {
				this.getIndex().addWord(word, document.getNewid(), i);
				i++;
			}
		}
	}
}
