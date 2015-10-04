import java.util.HashMap;

import Utils.FileUtils;

public class Counter {
	private String ngram;
	private String fileName;
	private HashMap<String, Double> hashMap;
	private HashMap<Double, Double> freqBucket;
	
	public Counter(String ngram, String fileName) {
		this.ngram = ngram;
		this.fileName = fileName;
		hashMap = new HashMap<String, Double>();
		freqBucket = new HashMap<Double, Double>();
	}
	
	public String[] getWords() {
		FileUtils f = new FileUtils(fileName);
		String fileContents = f.readFileContents();
		
		String[] tokens = fileContents.split("\\r?\\n");
		
		return tokens;
	}
	
	public int getTotalWordCount() {
		String[] tokens = getWords();
		return tokens.length;
	}
	
	public HashMap<String, Double> getUnigramCounter() {
		String[] tokens = getWords();
		
		for(String str : tokens) {
			if(hashMap.get(str) == null) {
				hashMap.put(str,  new Double(1));
			}
			else {
				hashMap.put(str, hashMap.get(str) + 1);
			}
		}
		
		return hashMap;
	}
	
	public HashMap<String, Double> getValidBigramFromCorpus() {
		String[] tokens = getWords();
		int len = tokens.length;
		int i;
		
		for(i = 0; i < len - 1; i++) {
			String s = tokens[i] + " " + tokens[i + 1];
			if(hashMap.get(s) == null) {
				hashMap.put(s,  new Double(1));
			}
			else {
				hashMap.put(s, hashMap.get(s) + 1);
			}
		}
		return hashMap;
	}
	
	public HashMap<String, Double> getAllPossibleBigrams() {
		HashMap<String, Double> validBigramCounter = getValidBigramFromCorpus();
		HashMap<String, Double> allBigramCounter = new HashMap<String, Double>();
		String[] tokens = getWords();
		int len = tokens.length;
		int i;
		int j;
		
		for(i = 0; i < len; i++) {
			String s = tokens[i] + " " + tokens[i];
			if(validBigramCounter.get(s) == null)
				allBigramCounter.put(s, new Double(0.0));
			else
				allBigramCounter.put(s, validBigramCounter.get(s));
		}
		
		for(i = 0; i < len; i++) {
			for(j = i + 1; j < len; j++) {
				String s = tokens[i] + " " + tokens[j];
				if(validBigramCounter.get(s) == null)
					allBigramCounter.put(s, new Double(0.0));
				else
					allBigramCounter.put(s, validBigramCounter.get(s));
				
				s = tokens[j] + " " + tokens[i];
				if(validBigramCounter.get(s) == null)
					allBigramCounter.put(s, new Double(0.0));
				else
					allBigramCounter.put(s, validBigramCounter.get(s));
			}
		}
		
		return allBigramCounter;
	}
	
	
	
	public HashMap<String, Double> getNGramCounter() {
		if(this.ngram.equalsIgnoreCase(NgramEnumerator.UNIGRAM.toString()))
			return getUnigramCounter();
		else if(this.ngram.equalsIgnoreCase(NgramEnumerator.BIGRAM.toString()))
			return getAllPossibleBigrams();
		
		return null;
	}
	
	public HashMap<Double, Double> formBucketsBasedUponFrequencies(HashMap<String, Double> hashMap) {
		for(String s : hashMap.keySet()) {
			Double value = hashMap.get(s);
			if(freqBucket.get(value) == null) {
				freqBucket.put(value, new Double(1));
			}
			else {
				freqBucket.put(value,  freqBucket.get(value) + 1);
			}
		}
		return freqBucket;
	}

}
