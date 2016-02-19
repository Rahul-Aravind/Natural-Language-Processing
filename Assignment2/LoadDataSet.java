import java.util.HashMap;

public class LoadDataSet {
	private HashMap<Pair<String, String>, Double> transitionProb;
	private HashMap<Pair<String, String>, Double> likelihoodProb;
	private String[] observationSequences;
	
	public LoadDataSet() {
		transitionProb = new HashMap<Pair<String, String>, Double>();
		likelihoodProb = new HashMap<Pair<String, String>, Double>();
		loadTransitionProb();
		loadLikelihoodProb();
		loadObservationSequences();
	}
	
	public void loadTransitionProb() {
		Pair<String, String> p = new Pair<String, String>("Start", "Hot");
		transitionProb.put(p, 0.8);
		
		p = new Pair<String, String>("Hot", "Hot");
		transitionProb.put(p, 0.7);
		
		p = new Pair<String, String>("Hot", "Cold");
		transitionProb.put(p, 0.3);
		
		p = new Pair<String, String>("Start", "Cold");
		transitionProb.put(p, 0.2);
		
		p = new Pair<String, String>("Cold", "Cold");
		transitionProb.put(p, 0.6);
		
		p = new Pair<String, String>("Cold", "Hot");
		transitionProb.put(p, 0.4);
	}
	
	public void loadLikelihoodProb() {
		String[] lambda = {"Hot", "Cold"};
		String[] observations = {"1", "2", "3"};
		Double[] values = {0.2, 0.4, 0.4, 0.5, 0.4, 0.1};
		
		int i = 0;
		for(String s : lambda) {
			for(String t : observations) {
				Pair<String, String> p = new Pair<String, String>(t, s);
				likelihoodProb.put(p, values[i]);
				i++;
			}
		}
	}
	
	public void loadObservationSequences() {
		observationSequences = new String[2]; // only two input sequences
		observationSequences[0] = "331122313";
		observationSequences[1] = "331123312";
	}
	
	public HashMap<Pair<String, String>, Double> getTransitionProb() {
		return transitionProb;
	}
	
	public HashMap<Pair<String, String>, Double> getLikelihoodProb() {
		return likelihoodProb;
	}
	
	public String[] getObservationSequences() {
		return observationSequences;
	}

}
