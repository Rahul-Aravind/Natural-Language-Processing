import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Driver2 {
	private static String fileName;
	private static String outputFileName;
	private static Scanner s;
	private static FileUtils fileUtils;
	private static String fileContents;
	private static HashMap<Pair<String, String>, Double> wordTagHashMap;
	private static HashMap<String, Double> wordFreqMap;
	private static CreateMapOutOfPosTaggedWords createMapOutofPosTaggedWordsObj;
	private static HashMap<String, Pair<String, Double>> wordTagMax;
	private static Double errorRateInCorpus;
	
	public static void getInput() {
		s = new Scanner(System.in);
		System.out.println("Enter the input file with full absolute path!!!");
		fileName = s.next();
		System.out.println("Enter the output file with full absolute path!!!");
		outputFileName = s.next();
	}
	
	public static void giveClarity() {
		System.out.println("***************************************");
	}
	
	public static void main(String[] args) {
		getInput();
		fileUtils = new FileUtils(fileName);
		fileContents = fileUtils.readFileContents();
		
		createMapOutofPosTaggedWordsObj = new CreateMapOutOfPosTaggedWords(fileContents);
		createMapOutofPosTaggedWordsObj.loadTagsAndWordsIntoHashMaps();
		
		wordTagHashMap = createMapOutofPosTaggedWordsObj.getWordTagHashMap();
		wordFreqMap = createMapOutofPosTaggedWordsObj.getWordFreqHashMap();
		
		createMapOutofPosTaggedWordsObj.computeMaxWordTagPair();
		wordTagMax = createMapOutofPosTaggedWordsObj.getWordTagMaxMap();
		
		fileUtils.writeContentsIntoFile(wordTagMax, fileContents, outputFileName);
		
		errorRateInCorpus = createMapOutofPosTaggedWordsObj.computeErrorRate();
		
		System.out.println("Error Rate in Corpus : " + errorRateInCorpus);
		
		giveClarity();
		
		System.out.println("We will apply the rule for the word \"that\" .......................");
		
		System.out.println("If the preceding tag belongs to one of the VBZ/NNS/NNP, then tag IN for \" that \" ");
		
		String word = "that";
		String tag = "IN";
		
		List<String> precedingTags = new ArrayList<String>();
		precedingTags.add("VBZ");
		precedingTags.add("NNS");
		precedingTags.add("NNP");
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents);
		
		giveClarity();
		
		System.out.println();
		System.out.println("If the preceding tag belongs to VBZ and"
				+ " if the succeeding tag belongs to one of the JJ/DT, then tag \" that \" as IN");
		
		word = "that";
		tag = "IN";
		
		precedingTags = new ArrayList<String>();
		precedingTags.add("VBZ");
		
		
		List<String> succeedingTags = new ArrayList<String>();
		succeedingTags.add("JJ");
		succeedingTags.add("DT");
		
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents, succeedingTags);
		
		giveClarity();
		
		System.out.println();
		System.out.println("If the preceding tag belongs to one of the RB/DT, then tag \" have \" as VBP");
		
		word = "have";
		tag = "VBP";
		
		precedingTags = new ArrayList<String>();
		precedingTags.add("RB");
		precedingTags.add("DT");
		
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents);
		
		giveClarity();
		
		System.out.println("We ll apply rule for word more");
		System.out.println("If the preceding tag belongs to one of the IN/RB/CD/VB and "
				+ "if the succeeding tag belongs to one of the IN/NN/NNS, then tag \" more \" as JJR");
		
		word = "more";
		tag = "JJR";
		
		precedingTags = new ArrayList<String>();
		precedingTags.add("IN");
		precedingTags.add("RB");
		precedingTags.add("CD");
		precedingTags.add("VB");
		
		succeedingTags = new ArrayList<String>();
		succeedingTags.add("IN");
		succeedingTags.add("NN");
		succeedingTags.add("NNS");
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents, succeedingTags);
		
		giveClarity();
		
		System.out.println("We ll apply rule for word 's");
		System.out.println("If the preceding tag belongs to NN, then tag \" 's \" as VBZ");
		
		word = "'s";
		tag = "VBZ";
		
		precedingTags = new ArrayList<String>();
		precedingTags.add("NN");
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents);
		
		giveClarity();
		
		System.out.println("We ll apply rule for word plans");
		System.out.println("If the preceding tag belongs to one of the VBZ/IN/JJ and"
				+ " if the succeeding tag belongs to one of the TO/IN, then tag \" plans \" as NNS");
		
		word = "plans";
		tag = "NNS";
		
		precedingTags = new ArrayList<String>();
		precedingTags.add("VBZ");
		precedingTags.add("IN");
		precedingTags.add("JJ");
		
		succeedingTags = new ArrayList<String>();
		succeedingTags.add("TO");
		succeedingTags.add("IN");
		
		createMapOutofPosTaggedWordsObj.ApplyRuleAndComputeErrorRate(precedingTags, word, tag, fileContents, succeedingTags);
		
		giveClarity();
		
	}
}
