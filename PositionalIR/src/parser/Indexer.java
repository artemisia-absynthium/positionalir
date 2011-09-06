package parser;

import java.util.List;

import model.Document;
import model.Index;

public class Indexer {
	
	private final Index index;
	
	public Indexer() {
		this.index = new Index();
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
		Tokenizer tokenizer = new Tokenizer();
		String[] bagOfWords = tokenizer.tokenize(document);
		int i = 1;
		for (String word : bagOfWords) {
			if (word.matches("\\w+")) {
				this.getIndex().addWord(word, document.getNewid(), i);
				i++;
			}
		}
	}
}
