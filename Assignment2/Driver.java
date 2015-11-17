import java.util.HashMap;

public class Driver {
	private static HashMap<Pair<String, String>, Double> transitionProb;
	private static HashMap<Pair<String, String>, Double> likelihoodProb;
	private static String[] observationSequences;
	private static LoadDataSet loadDataSet;
	private static ViterbiDecoder viterbiDecoder;
	
	public static void main(String args[]) {
		loadDataSet = new LoadDataSet();
		
		transitionProb = loadDataSet.getTransitionProb();
		likelihoodProb = loadDataSet.getLikelihoodProb();
		observationSequences = loadDataSet.getObservationSequences();
		viterbiDecoder = new ViterbiDecoder(observationSequences[0], transitionProb, likelihoodProb);
		
		//System.out.println(transitionProb);
		//System.out.println(likelihoodProb);
		String weatherSequence = viterbiDecoder.computeWeatherSequences();
		System.out.println("Observation sequence : " + observationSequences[0] + "Weather Sequence : " +weatherSequence);
		
		viterbiDecoder = new ViterbiDecoder(observationSequences[1], transitionProb, likelihoodProb);
		weatherSequence = viterbiDecoder.computeWeatherSequences();
		System.out.println("Observation sequence : " + observationSequences[1] + "Weather Sequence : " +weatherSequence);
		
		
		
	}

}
