

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.TreeMap;

public class FileUtils {
	private String fileName;
	private BufferedReader br;
	private StringBuilder sb;
	private BufferedWriter bw;
	
	public FileUtils() {
		br = null;
		bw = null;
		sb = new StringBuilder();
	}
	
	public FileUtils(String fileName) {
		this.fileName = fileName;
		br = null;
		bw = null;
		sb = new StringBuilder();
	}
	
	public String readFileContents() {
		File f = new File(fileName);
		if(f.exists() == false) {
			System.out.println("File : " + fileName +" Not Found in the current directory");
		}
		
		try {
			br = new BufferedReader(new FileReader(f));
			String temp;
			while((temp = br.readLine()) != null) {
				//System.out.println(temp);
				sb.append(temp);
				sb.append("\n");
			}
			br.close();
		}
		
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public void writeContentsIntoFile(HashMap<String, Pair<String, Double>> wordTagMax, String inputFileContents, String OutputFile) {
		try {
			File oFile = new File(OutputFile);
			if(oFile.exists()) {
				oFile.delete();
			}
			
			
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(oFile)));
			String tokens[] = inputFileContents.split(" ");
			
			for(String t : tokens) {
				String[] word_tag = t.split("_");
				if(word_tag.length == 2) {
					String word = word_tag[0];
					String tag = wordTagMax.get(word).getFirst();
					bw.write(word + "_" + tag + " ");
				}
				else bw.write(t);
			}
			
			
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}

