package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Index {
	
	//          Parola      ID doc  Posizioni
	private final Map<String, Map<String, List<Integer>>> reverseIndex;

	public Index() {
		this.reverseIndex = new TreeMap<String, Map<String, List<Integer>>>();
	}
	
	public Map<String, Map<String, List<Integer>>> getReverseIndex() {
		return reverseIndex;
	}

	public void addWord(String word, String docid, int position) {
		//Se la parola è già contenuta nell'indice
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

	@Override
	public String toString() {
		return this.reverseIndex.toString().replaceAll("},", "},\n\t");
	}

}
