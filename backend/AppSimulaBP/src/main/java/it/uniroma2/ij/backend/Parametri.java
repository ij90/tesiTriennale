package it.uniroma2.ij.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.uniroma2.sel.simlab.ebpmn.analyzers.BaseMeasure;

public class Parametri {
	
	private int replications;
	private int totalTokens;
	private ArrayList<Integer> performers_units;
	private Map<String, Integer> performers = new HashMap<String, Integer>();
	
	public Map<String, Integer> getPerformers() {
		return performers;
	}
	
	public void setPerformers(Map<String, Integer> performers) {
		this.performers = performers;
	}

	public int getTotalTokens() {
		return totalTokens;
	}

	public void setTotalTokens(int totalTokens) {
		this.totalTokens = totalTokens;
	}

	public ArrayList<Integer> getPerformers_units() {
		return performers_units;
	}

	public void setPerformers_units(ArrayList<Integer> performers_units) {
		this.performers_units = performers_units;
	}


	public int getReplications() {
		return replications;
	}

	public void setReplications(int replications) {
		this.replications = replications;
	}
	
	public void stampaArray() {
		for(int i = 0; i < performers_units.size(); i++) {
			System.out.format("Unita: %d\n", performers_units.get(i));
		}
	}
	
	public void stampaMappa() {
		Iterator<Map.Entry<String, Integer>> it = performers.entrySet().iterator();
		
		while(it.hasNext()) {
			Map.Entry<String, Integer> prova = it.next();
			System.out.format("nomePerf: %s", prova.getKey());
			System.out.println();
			System.out.format("unità: %d", prova.getValue());
			System.out.println();
		}
	}

}
