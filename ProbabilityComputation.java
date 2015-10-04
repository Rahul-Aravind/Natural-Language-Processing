import java.util.HashMap;
import java.util.TreeMap;

public class ProbabilityComputation {
	
	private TreeMap<String, Double> probability;
	
	public ProbabilityComputation(String ngram) {
		probability = new TreeMap<String, Double>();
	}
	
	public TreeMap<String, Double> getUnigramProbabilities(HashMap<String, Double> hashMap, int totalWordCount) {
		
		for(String s : hashMap.keySet()) {
			Double probab = hashMap.get(s) / totalWordCount;
			probability.put(s, probab);
		}
		
		return probability;
	}
	
	public TreeMap<String, Double> getBigramProbabilities(HashMap<String, Double>uniGram, HashMap<String, Double>biGram) {
		
		for(String s : biGram.keySet()) {
			String[] temp = s.split(" ");
			Double probab = biGram.get(s) / uniGram.get(temp[0]);
			probability.put(s,  probab);
		}
		
		return probability;
	}
	
	public TreeMap<String, Double> computeProbabilitesBasedOnGoodTuring(HashMap<String, Double> hashMap, HashMap<Double, Double>freqBucket, int totalWordCount) {
		
		Double probab = 0.0;
		Double cSmoothed = 0.0;
		for(String s : hashMap.keySet()) {
			Double c = hashMap.get(s);
			if(freqBucket.get(c + 1) == null) {
				probab = new Double(0);
			}
			else {
				if(c == 0) {
					
					Double nC_plus_1 = freqBucket.get(c + 1);
					probab = nC_plus_1 / (totalWordCount - 1);
				}
				else {
					Double nC_plus_1 = freqBucket.get(c + 1);
					Double nC = freqBucket.get(c);
					cSmoothed = (c + 1) * nC_plus_1 / nC;
					probab = cSmoothed / (totalWordCount - 1);
				}
			}
			probability.put(s,  probab);
		}
		return probability;
	}

}
