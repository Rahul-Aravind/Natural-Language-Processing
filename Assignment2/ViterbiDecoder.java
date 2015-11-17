import java.util.HashMap;

public class ViterbiDecoder {
	private String weatherSequences;
	private String observationSequences;
	private HashMap<Pair<String, String>, Double> transitionProb;
	private HashMap<Pair<String, String>, Double> likelihoodProb;
	private String[] lambda;
	
	public ViterbiDecoder(String observationSequences, HashMap<Pair<String, String>, Double> tp, HashMap<Pair<String, String>, Double> lp) {
		weatherSequences = "";
		this.observationSequences = observationSequences;
		this.transitionProb = tp;
		this.likelihoodProb = lp;
		lambda = new String[2]; // total possible weather conditions
		lambda[0] = "Hot";
		lambda[1] = "Cold";
	}
	
	public String computeWeatherSequences() {
		Double[][] viterbiPathProb = new Double[2][observationSequences.length()];
		
		Double transProb = transitionProb.get(new Pair<String, String>("Start", "Hot"));
		Double likeliProb = likelihoodProb.get(new Pair<String, String>(String.valueOf(observationSequences.charAt(0)), "Hot"));
		
		viterbiPathProb[0][0] = transProb * likeliProb;
		
		transProb = transitionProb.get(new Pair<String, String>("Start", "Cold"));
		likeliProb = likelihoodProb.get(new Pair<String, String>(String.valueOf(observationSequences.charAt(0)), "Cold"));
		
		viterbiPathProb[1][0] = transProb * likeliProb;
		Double maxStateProb = 0.0;
		
		if(viterbiPathProb[0][0] > viterbiPathProb[1][0]) weatherSequences += lambda[0] + " ";
		else weatherSequences += lambda[1] + " ";
		
		for(int i = 1; i < observationSequences.length(); i++) {
			maxStateProb = 0.0;
			for(int j = 0; j < lambda.length; j++) {
				Double[] prd = new Double[lambda.length];
				Double maxPrd = 0.0;
				for(int k = 0; k < lambda.length; k++) {
					//product of viterbiProb, transition and likelihood
					//System.out.println(k + " " + j);
					//System.out.println(lambda[k] + " " + lambda[j]);
					transProb = transitionProb.get(new Pair<String, String>(lambda[k], lambda[j]));
					likeliProb = likelihoodProb.get(new Pair<String, String>(String.valueOf(observationSequences.charAt(i)), lambda[j]));
					//System.out.println(transProb);
					//System.out.println(likeliProb);
					//System.out.println(viterbiPathProb[k][i - 1]);
					prd[k] = viterbiPathProb[k][i - 1]* transProb * likeliProb;
					//System.out.println(prd[k]);
				}
				//computing the max
				for(int x = 0; x < lambda.length; x++) {
					if(prd[x] > maxPrd) {
						viterbiPathProb[j][i] = prd[x];
						maxPrd = prd[x];
					}
				}
			}
			//computing the max at the hot or cold state for determining the weather sequence for the input.
			int idx = -1;
			for(int x = 0; x < lambda.length; x++) {
				if(viterbiPathProb[x][i] > maxStateProb) {
					idx = x;
					maxStateProb = viterbiPathProb[x][i];
				}
			}
			weatherSequences += lambda[idx] + " ";
		}
		
		return weatherSequences;
	}

}
