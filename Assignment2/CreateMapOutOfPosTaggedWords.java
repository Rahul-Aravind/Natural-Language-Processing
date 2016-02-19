import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CreateMapOutOfPosTaggedWords {
	private HashMap<Pair<String, String>, Double> wordTagHashMap;
	private HashMap<String, Double> sortedMapOnValues;
	private HashMap<String, Double> heap; 
	private HashMap<String, Double> wordFreqMap;
	private HashMap<String, Pair<String, Double>> wordTagMax;
	private HashMap<Pair<String, String>, Boolean> visitedWordTagPair;
	private String fileContents;
	private Double errorRateInCorpus;
	
	public CreateMapOutOfPosTaggedWords(String fileContents) {
		this.fileContents = fileContents;
		heap = new HashMap<String, Double>();
		wordTagHashMap = new HashMap<Pair<String, String>, Double>();
		wordFreqMap = new HashMap<String, Double>();
		wordTagMax = new HashMap<String, Pair<String, Double>>();
		visitedWordTagPair = new HashMap<Pair<String, String>, Boolean>();
	}
	
	public void loadTagsAndWordsIntoHashMaps() {
		String[] tokens = fileContents.split(" ");
		
		for(String token : tokens) {
			String[] temp = token.split("_");
			if(temp.length == 2) {
				Pair<String, String> p = new Pair<String, String>(temp[0], temp[1]);
				
				if(wordTagHashMap.get(p) == null) {
					wordTagHashMap.put(p, new Double(1.0));
				}
				else {
					wordTagHashMap.put(p, wordTagHashMap.get(p) + 1);
				}
				
				String word = temp[0];
				
				if(wordFreqMap.get(word) == null) {
					wordFreqMap.put(word, new Double(1.0));
				}
				else {
					wordFreqMap.put(word, wordFreqMap.get(word) + 1);
				}
			}
		}
	}
	
	public void computeMaxWordTagPair() {
		String[] tokens = fileContents.split(" ");
		
		for(String token : tokens) {
			String[] temp = token.split("_");
			if(temp.length == 2) {
				Pair<String, String> wordTagPair = new Pair<String, String>(temp[0], temp[1]);
				if(visitedWordTagPair.get(wordTagPair) == null) {
					if(wordTagMax.get(temp[0]) == null) {
						Double freq = wordTagHashMap.get(wordTagPair);
						wordTagMax.put(temp[0], new Pair<String, Double>(temp[1], freq));
					}
					else {
						Double prevValue = wordTagMax.get(temp[0]).getSecond();
						Double newValue = wordTagHashMap.get(wordTagPair);
						if(newValue > prevValue) {
							wordTagMax.put(temp[0], new Pair<String, Double>(temp[1], newValue));
						}
						else { /* do nothing */ }
					}
					visitedWordTagPair.put(wordTagPair, true);
				}
			}
		}
	}
	
	public void differentiateErroneousTaggedWordsFromProperlyTaggedWords() {
		for(String s : wordTagMax.keySet()) {
			String tag = wordTagMax.get(s).getFirst();
			visitedWordTagPair.put(new Pair<String, String>(s, tag), false);
			// false - indicates correctly tagged based on tag frequency
			//true - indicates erroneously tagged based on tag frequency
		}
	}
	
	
	private static HashMap<String, Double> sortByComparator(Map<String, Double> unsortMap) {
		List<Map.Entry<String, Double>> list = 
			new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
                                           Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public void findTopFiveErroneouslyTaggedWords() {
		
		Double total;
		for(Pair<String, String> p : visitedWordTagPair.keySet()) {
			if(visitedWordTagPair.get(p) == false) {
				total = wordFreqMap.get(p.getFirst()) - wordTagHashMap.get(p);
				heap.put(p.getFirst(), total);
			}
		}
		//System.out.println(heap);
		sortedMapOnValues = sortByComparator(heap);
		
		System.out.println("Top five Erroneously Tagged words");
		int i = 0;
		for(String s : sortedMapOnValues.keySet()) {
			if(i > 4) break;
			System.out.println(s + " " + sortedMapOnValues.get(s));
			i++;
		} 
		
	}
	
	
	public Double computeErrorRate() {
		differentiateErroneousTaggedWordsFromProperlyTaggedWords();
		Double erroneousCount = 0.0;
		for(Pair<String, String> p : visitedWordTagPair.keySet()) {
			if(visitedWordTagPair.get(p) == true) {
				erroneousCount += wordTagHashMap.get(p);
			}
		}
		
		Double totalWordCount = 0.0;
		for(Pair<String, String> p : wordTagHashMap.keySet()) {
			totalWordCount += wordTagHashMap.get(p);
		}
		
		findTopFiveErroneouslyTaggedWords();
		
		errorRateInCorpus = ( erroneousCount / totalWordCount );
		
		return errorRateInCorpus;
		
	}
	
	public List<String> getAllPosTagsOfWords(String fileContents, String Word) {
		String[] tokens = fileContents.split(" ");
		List<String> allTagsOfWord = new ArrayList<String>();
		for(String t : tokens) {
			String[] splits = t.split("_");
			if(splits.length == 2) {
				if(splits[0].equals(Word)) allTagsOfWord.add(t);
			}
		}
		return allTagsOfWord;
	}
	
	public void ApplyRuleAndComputeErrorRate(List<String> preceedingTags, String word, String correctTag, String fileContents) {
		List<String> originalWordTags = getAllPosTagsOfWords(fileContents, word);
		List<String> modifiedAsPerRules = new ArrayList<String>();
		String[] tokens = fileContents.split(" ");
		int len = tokens.length;
		for(int i = 0; i < len; i++) {
			boolean precedingFlag = false;
			if(i - 1 >= 0) {
				String[] t = tokens[i - 1].split("_");
				String pTag = (t.length == 2) ? t[1] : null;
				if(pTag != null && preceedingTags.contains(pTag)) precedingFlag = true;
			}
			String[] current = tokens[i].split("_");
			if(current.length == 2) {
				String currentWord = current[0];
				if(currentWord.equals(word) == false) continue;
				String currentTag = current[1];
				if(precedingFlag == true) { 
					currentTag = correctTag;
				}
				modifiedAsPerRules.add(currentWord + "_" + currentTag);
			}
		}
		
		int i = 0;
		int listlen = modifiedAsPerRules.size();
		int correctCountWrtRule = 0;
		
		while(i < listlen) {
			//System.out.println(originalWordTags.get(i) + " " + modifiedAsPerRules.get(i));
			if(originalWordTags.get(i).equals(modifiedAsPerRules.get(i)) == false)
				correctCountWrtRule++;
			
			i++;
		}
		
		
		if(correctCountWrtRule > sortedMapOnValues.get(word))
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) + correctCountWrtRule));
		else
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) - correctCountWrtRule));
		
		
		
	}
	
	public void ApplySucceedingRuleAndComputeErrorRate(List<String> succeedingTags, String word, String correctTag, String fileContents) {
		List<String> originalWordTags = getAllPosTagsOfWords(fileContents, word);
		List<String> modifiedAsPerRules = new ArrayList<String>();
		String[] tokens = fileContents.split(" ");
		int len = tokens.length;
		for(int i = 0; i < len; i++) {
			boolean precedingFlag = false;
			if(i + 1 < len) {
				String[] t = tokens[i + 1].split("_");
				String pTag = (t.length == 2) ? t[1] : null;
				if(pTag != null && succeedingTags.contains(pTag)) precedingFlag = true;
			}
			String[] current = tokens[i].split("_");
			if(current.length == 2) {
				String currentWord = current[0];
				if(currentWord.equals(word) == false) continue;
				String currentTag = current[1];
				if(precedingFlag == true) { 
					currentTag = correctTag;
				}
				modifiedAsPerRules.add(currentWord + "_" + currentTag);
			}
		}
		
		int i = 0;
		int listlen = modifiedAsPerRules.size();
		int correctCountWrtRule = 0;
		
		while(i < listlen) {
			//System.out.println(originalWordTags.get(i) + " " + modifiedAsPerRules.get(i));
			if(originalWordTags.get(i).equals(modifiedAsPerRules.get(i)) == false)
				correctCountWrtRule++;
			
			i++;
		}
		
		if(correctCountWrtRule > sortedMapOnValues.get(word))
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) + correctCountWrtRule));
		else
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) - correctCountWrtRule));
		
	}
	
	public void ApplyRuleAndComputeErrorRate(List<String> preceedingTags, String word, String correctTag, String fileContents, List<String> succeedingTags) {
		List<String> originalWordTags = getAllPosTagsOfWords(fileContents, word);
		List<String> modifiedAsPerRules = new ArrayList<String>();
		
		String[] tokens = fileContents.split(" ");
		int len = tokens.length;
		for(int i = 0; i < len; i++) {
			boolean precedingFlag = false;
			boolean succeedingFlag = false;
			if(i - 1 >= 0) {
				String[] t = tokens[i - 1].split("_");
				String pTag = (t.length == 2) ? t[1] : null;
				if(pTag != null && preceedingTags.contains(pTag)) precedingFlag = true;
			}
			if(i + 1 < len) {
				String[] t = tokens[i + 1].split("_");
				String sTag = (t.length == 2) ? t[1] : null;
				if(sTag != null && succeedingTags.contains(sTag)) succeedingFlag = true;
			}
			String[] current = tokens[i].split("_");
			if(current.length == 2) {
				String currentWord = current[0];
				if(currentWord.equals(word) == false) continue;
				String currentTag = current[1];
				if(precedingFlag == true && succeedingFlag == true) { 
					currentTag = correctTag;
				}
				modifiedAsPerRules.add(currentWord + "_" + currentTag);
			}
		}
		
		int i = 0;
		int listlen = modifiedAsPerRules.size();
		int correctCountWrtRule = 0;
		
		while(i < listlen) {
			//System.out.println(originalWordTags.get(i) + " " + modifiedAsPerRules.get(i));
			if(originalWordTags.get(i).equals(modifiedAsPerRules.get(i)) == false)
				correctCountWrtRule++;
			
			i++;
		}
		
		if(correctCountWrtRule > sortedMapOnValues.get(word))
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) + correctCountWrtRule));
		else
			System.out.println("Error count for word " + word + " after applying the rule : " + (sortedMapOnValues.get(word) - correctCountWrtRule));
		
	}
	
	public HashMap<String, Pair<String, Double>> getWordTagMaxMap() {
		return wordTagMax;
	}
	
	public HashMap<Pair<String, String>, Double> getWordTagHashMap() {
		return wordTagHashMap;
	}
	
	public HashMap<String, Double> getWordFreqHashMap() {
		return wordFreqMap;
	}

}


