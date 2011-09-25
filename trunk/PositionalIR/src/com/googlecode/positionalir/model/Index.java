package com.googlecode.positionalir.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Index implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9006197481888423582L;
	
	/**
	 * Parola -> ID doc -> Posizioni.
	 */
	//            Parola      ID doc      Posizioni
	private final Map<String, Map<String, List<Integer>>> reverseIndex;
	
	private final Map<Integer, String> docID2File;

	public Index() {
		this.reverseIndex = new HashMap<String, Map<String, List<Integer>>>();
		this.docID2File = new HashMap<Integer, String>();
	}
	
	public Map<String, Map<String, List<Integer>>> getReverseIndex() {
		return reverseIndex;
	}
	
	public Set<String> getDocumentsIDs(String word) {
		final Map<String, List<Integer>> docId2Positions = this.reverseIndex.get(word);
		if (docId2Positions == null) {
			return Collections.emptySet();
		}
		return docId2Positions.keySet();
	}
	
	public List<Integer> getWordPositions(String word, String docID) {
		return this.reverseIndex.get(word).get(docID);
	}

	public void addWord(String word, String docid, int position) {
		Map<String, List<Integer>> occurrences = this.reverseIndex.get(word);
		if(occurrences == null) {
			occurrences = new LinkedHashMap<String, List<Integer>>();
			this.reverseIndex.put(word, occurrences);
		}
		List<Integer> positions = occurrences.get(docid);
		if(positions == null) {
			positions = new ArrayList<Integer>(1);
			occurrences.put(docid, positions);
		}
		positions.add(position);
	}
	
	public void addIndexedFileEntry(File file, List<Document> parseDoc) {
		final String absolutePath = file.getAbsolutePath();
		for (Document document : parseDoc) {
			this.docID2File.put(Integer.parseInt(document.getNewid()), absolutePath);
		}
	}
	
	public String getFilePath(Integer docID) {
		return this.docID2File.get(docID);
	}
	
	@Override
	public String toString() {
		return new TreeMap<String, Map<String, List<Integer>>>(this.reverseIndex).toString().replaceAll("},", "},\n\t");
	}

}
