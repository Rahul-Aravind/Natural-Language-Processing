import java.util.List;
import java.util.Vector;

import edu.smu.tspell.wordnet.WordNetDatabase;
import utils.WordNetUtils;

public class FeatureExtraction {
	private static PosTagger pt;
	private static Vector<String>features;
	private static WordNetDatabase wordNetDatabase;
	private static WordNetUtils wordnetUtils;
	private static Lemmatizer lemmatizer;
	
	public static void main(String args[]) {
		String question = "List all the dell laptops?";
		pt = new PosTagger(question);
		String TaggedText = pt.PosTag();
		
		System.out.println(TaggedText);
		
		features = new Vector<String>();
		String[] textTags = TaggedText.split(" ");
		for(String s : textTags) {
			String[] tag = s.split("_");
			if(tag[1].contains("NN") || tag[1].contains("CD")) {
				features.add(tag[0]);
			}
		}
		
		//System.out.println(features);
		
		wordnetUtils = new WordNetUtils();
		wordnetUtils.getSynsets("specs");
		
		lemmatizer = new Lemmatizer();
		List<String> lemmaSet = lemmatizer.lemmatize(question);
		
		System.out.println(lemmaSet);
		
	}
}
