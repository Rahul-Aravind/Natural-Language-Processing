package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	public void writeContentsIntoFile(TreeMap<String, Double> ngramProbability, String fName) {
		try {
			File f = new File(fName);
			if(f.exists()) {
				f.delete();
			}
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			
			for(String key : ngramProbability.keySet()) {
				String temp = key + " " + ngramProbability.get(key);
				bw.write(temp);
				bw.newLine();
			}
			
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
