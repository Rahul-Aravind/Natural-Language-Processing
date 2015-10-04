import java.util.Scanner;

public class GetInput {
	private static String ipFileName;
	private static String opFileName;
	private static Scanner s;
	
	public GetInput() {
		s = new Scanner(System.in);
	}
	
	public static String getInputFileName() {
		System.out.println("Enter the Input File Name with full directory: ");
		ipFileName = s.next();
		
		return ipFileName;
	}
	
	public static String getOutputFileName() {
		System.out.println("Enter the output directory path alone in which the result files will be stored.. ");
		opFileName = s.next();
		
		return opFileName;
	}

}
