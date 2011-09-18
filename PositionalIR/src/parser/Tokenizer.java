package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Document;

public class Tokenizer {
	
	private final Pattern punctuation = Pattern.compile("\\p{Punct}");

	public String[] tokenizeDocument(Document document) {
		String all = document.getAll();
		all = this.removePunctuation(all);
		return all.split(" ");
	}
	
	public String[] tokenizeQuery(String query) {
		query = this.removePunctuation(query);
		return query.split(" ");
	}

	public String removePunctuation(String text) {
		StringBuffer buffer = new StringBuffer();
		Matcher matcher = punctuation.matcher(text);
		while(matcher.find()) {
			matcher.appendReplacement(buffer, " ");
		}
		matcher.appendTail(buffer);
		return buffer.toString().replaceAll("  +|\t+", " ");
	}

}
