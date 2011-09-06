package parser;

import java.util.List;

import model.Document;
import model.Index;

public class Indexer {
	
	public Indexer() {}

	public void index(List<Document> documents) {
		for(Document document : documents) {
			this.createInverseIndex(document);
		}
	}

	private void createInverseIndex(Document document) {
		Tokenizer tokenizer = new Tokenizer();
		String[] bagOfWords = tokenizer.tokenize(document);
		Index index = new Index();
		int i = 1;
		for (String word : bagOfWords) {
			if (word.matches("\\w+")) {
				index.addWord(word, document.getNewid(), i);
				i++;
			}
		}
	}
}
