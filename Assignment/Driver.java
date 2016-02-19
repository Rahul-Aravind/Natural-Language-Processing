import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import Utils.FileUtils;

public class Driver {
	private static String ipFileName;
	private static String opDirectory;
	private static String opFileName;
	private static GetInput getInput;
	private static NgramEnumerator ngram;
	private static HashMap<String, Double>unigramCounter;
	private static HashMap<String, Double>bigramCounter;
	private static TreeMap<String, Double>unigramProbabilities;
	private static TreeMap<String, Double>bigramProbabilities;
	private static Counter cObj;
	private static ProbabilityComputation pObj;
	private static FileUtils fObj;
	private static int totalWordCount;
	
	public static void getInputs() {
		System.out.println("Please Enter the fileName after placing the file in the correct directory");
		getInput = new GetInput();
		ipFileName = getInput.getInputFileName();
		opDirectory = getInput.getOutputFileName();
	}
	
	public static void main(String[] args) {
		
		getInputs();
		
		System.out.println("************************************************");
		System.out.println("Computing the unigram probabilities");
		System.out.println("************************************************");
		
		ngram = NgramEnumerator.UNIGRAM;
		
		cObj = new Counter(ngram.toString(), ipFileName);
		totalWordCount = cObj.getTotalWordCount();
		System.out.println(totalWordCount);
		unigramCounter = cObj.getNGramCounter();
		
		pObj = new ProbabilityComputation(ngram.toString());
		unigramProbabilities = pObj.getUnigramProbabilities(unigramCounter, totalWordCount);
		
		
		System.out.println("************************************************");
		System.out.println("Writing the unigram probabilities into a file");
		System.out.println("************************************************");
		
		fObj = new FileUtils();
		opFileName = ngram.toString() + ".txt";
		fObj.writeContentsIntoFile(unigramProbabilities, opDirectory + opFileName);
		
		System.out.println("************************************************");
		System.out.println("Computing the bigram probabilities");
		System.out.println("************************************************");
		
		ngram = NgramEnumerator.BIGRAM;
		cObj = new Counter(ngram.toString(), ipFileName);
		
		bigramCounter = cObj.getNGramCounter();
		pObj = new ProbabilityComputation(ngram.toString());
		bigramProbabilities = pObj.getBigramProbabilities(unigramCounter, bigramCounter);
		
		System.out.println("************************************************");
		System.out.println("Writing the bigram probabilities into a file");
		System.out.println("************************************************");
		
		opFileName = ngram.toString() + ".txt";
		fObj.writeContentsIntoFile(bigramProbabilities, opDirectory + opFileName); 
		
		System.out.println("***************************************");
		System.out.println("Computing the unigram probabilities with good turing discounting");
		System.out.println("***************************************");
		
		ngram = NgramEnumerator.UNIGRAM_WITH_GOODTURING;
		cObj = new Counter(ngram.toString(), ipFileName);
		
		HashMap<Double, Double>freqBucketFromUnigram = cObj.formBucketsBasedUponFrequencies(unigramCounter);
		pObj = new ProbabilityComputation(ngram.toString());
		unigramProbabilities = pObj.computeProbabilitesBasedOnGoodTuring(unigramCounter, freqBucketFromUnigram, totalWordCount + 1);
		
		System.out.println(freqBucketFromUnigram);
		
		opFileName = ngram.toString() + ".txt";
		fObj.writeContentsIntoFile(unigramProbabilities, opDirectory + opFileName);
		
		System.out.println("***************************************");
		System.out.println("Computing the bigram probabilities with good turing discounting");
		System.out.println("***************************************");
		
		ngram = NgramEnumerator.BIGRAM_WITH_GOODTURING;
		cObj = new Counter(ngram.toString(), ipFileName);
		
		HashMap<Double, Double>freqBucketFromBigram = cObj.formBucketsBasedUponFrequencies(bigramCounter);
		pObj = new ProbabilityComputation(ngram.toString());
		bigramProbabilities = pObj.computeProbabilitesBasedOnGoodTuring(bigramCounter, freqBucketFromBigram, totalWordCount);
		
		System.out.println(freqBucketFromBigram);
		
		opFileName = ngram.toString() + ".txt";
		fObj.writeContentsIntoFile(bigramProbabilities, opDirectory + opFileName);  
	}

}
