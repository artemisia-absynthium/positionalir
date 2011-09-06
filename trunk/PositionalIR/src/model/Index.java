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
		if (this.reverseIndex.containsKey(word)) {
			//Se è già stata trovata almeno una volta nel documento corrente
			if (this.reverseIndex.get(word).containsKey(docid)) {
				List<Integer> positions = new ArrayList<Integer>(1);
				positions = this.reverseIndex.get(word).get(docid);
				positions.add(position);
			}
			else {
				List<Integer> positions = new ArrayList<Integer>(1);
				positions.add(position);
				Map<String, List<Integer>> occurrences = new LinkedHashMap<String, List<Integer>>();
				occurrences.put(docid, positions);
				this.reverseIndex.get(word).put(docid, positions);
			}
		}
		else {
			List<Integer> positions = new ArrayList<Integer>(1);
			positions.add(position);
			Map<String, List<Integer>> occurrences = new LinkedHashMap<String, List<Integer>>();
			occurrences.put(docid, positions);
			this.reverseIndex.put(word, occurrences);
		}
	}

	@Override
	public String toString() {
		return this.reverseIndex.toString().replaceAll("},", "},\n\t");
	}

}
