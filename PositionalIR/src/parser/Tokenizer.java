package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Document;

public class Tokenizer {
	
	private final Pattern punctuation = Pattern.compile("\\p{Punct}");

	public Tokenizer() {}
	
	public String[] tokenize(Document document) {
		String all = document.getAll();
		all = this.removePunctuation(all);
		return all.split(" ");
	}

	private String removePunctuation(String all) {
		StringBuffer buffer = new StringBuffer();
		String replace = " ";
		Matcher matcher = punctuation.matcher(all);
		while(matcher.find()) {
			matcher.appendReplacement(buffer, replace);
		}
		matcher.appendTail(buffer);
		return buffer.toString().replaceAll("  +|\t+", " ");
	}

}
