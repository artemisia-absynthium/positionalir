package com.googlecode.positionalir.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.positionalir.model.Document;
import com.googlecode.positionalir.util.LogUtil;




public class Parser {
	
	private final Logger log = LogUtil.getLogger(Parser.class);
	
	private final Pattern htmlPattern = Pattern.compile("&#(\\d+);");
	private final Pattern newidPattern = Pattern.compile("NEWID=\"(\\d+)\"");
	private final Pattern titlePattern = Pattern.compile("<TITLE>(.*)</TITLE>");
	private final Pattern bodyPattern = Pattern.compile("<BODY>(.*)</BODY>");
	private final Pattern bodyPattern2 = Pattern.compile("<TEXT.*>(.*)\t(.*)\t(.*)</TEXT>");
	
	private final Map<String, String> entities;
	
	public Parser() {
		this.entities = new HashMap<String, String>();
		this.entities.put("&lt", "<");
		this.entities.put("&amp", "&");
	}

	private String parseSGML(BufferedReader reader) throws IOException {
		StringBuffer buffer = new StringBuffer(1024*1024*2);
		String line;
		reader.readLine();
		while ((line = reader.readLine()) != null) {
			String newLine = this.resolveDTDEntities(line);
			this.resolveHtmlCodes(buffer, newLine);
			buffer.append("\t");
		}
		String result = buffer.toString().replaceAll("> +", ">")
									.replaceAll(" +<", "<")
									.replaceAll("  +", " ")
									.replaceAll("REUTERS>\t?<REUTERS", "REUTERS>\n<REUTERS");
		return result;
	}

	private void resolveHtmlCodes(StringBuffer buffer,
			String line) {
		Matcher matcher = htmlPattern.matcher(line);
		while (matcher.find()) {
			String group = matcher.group(1);
			String replace = String.valueOf((char) Integer.parseInt(group));
			matcher.appendReplacement(buffer, replace);
		}
		matcher.appendTail(buffer);
	}

	private String resolveDTDEntities(String line) {
		final Set<String> entitiesSet = this.entities.keySet();
		for (String entity : entitiesSet) {
			line = line.replaceAll(entity, this.entities.get(entity));
		}
		return line;
	}

	public List<Document> parse(BufferedReader reader) throws IOException {
		log.info(LogUtil.logTaskStart("Parsing SGML"));
		
		log.info(LogUtil.logTaskStart("Reading File"));
		String input = this.parseSGML(reader);
		log.info(LogUtil.logTaskEnd("Reading File"));
		
		log.info(LogUtil.logTaskStart("Tokenizing"));
		BufferedReader reader2 = new BufferedReader(new StringReader(input));
		String line;
		List<Document> documents = new LinkedList<Document>();
		while ((line = reader2.readLine()) != null) {
			Document document = this.tokenize(line);
			documents.add(document);
		}
		log.info(LogUtil.logTaskEnd("Tokenizing"));
		
		log.info(LogUtil.logTaskEnd("Parsing SGML"));
		return documents;
	}

	private Document tokenize(String line) {
		String newid, title, body;
		newid = extractMatcherPatternGroup(line, newidPattern, 1);
		title = extractMatcherPatternGroup(line, titlePattern, 1);
		body = extractMatcherPatternGroup(line, bodyPattern, 1);
		if (body.isEmpty()) {
			title = extractMatcherPatternGroup(line, bodyPattern2, 1);
			body = extractMatcherPatternGroup(line, bodyPattern2, 3);
		}
		return this.createDocument(newid, title, body);
	}

	private Document createDocument(String newid, String title, String body) {
		return new Document(newid, title, body);
	}

	private String extractMatcherPatternGroup(final String line, final Pattern newidPattern, final int i) {
		final Matcher matcher = newidPattern.matcher(line);
		while (matcher.find()) {
			return matcher.group(i);
		}
		return "";
	}
	
}
