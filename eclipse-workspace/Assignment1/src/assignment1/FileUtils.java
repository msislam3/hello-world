package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtils {

	private String fileName;
	private String path;

	public static void saveFileContents(File f, String s) {
		try(PrintWriter output = new PrintWriter(f);) {
			output.write(s);	
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	public static String getFileContentsAsString(File f) {
		String fileContent="";
		try(Scanner input = new Scanner(f);){
			if(input.hasNext()) {
				fileContent =  input.nextLine();
			}	
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return fileContent;
	}

	public static void saveFileContents(File f, ArrayList<String> ar) {
		try(PrintWriter output = new PrintWriter(f);) {
			for (String writeContent : ar) {
				output.print(writeContent + "\n");
			}			
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getFileContentsAsArrayList(File f) {
		ArrayList<String> fileContent = new ArrayList<String>();
		try(Scanner input = new Scanner(f);){
			while(input.hasNext()) {
				fileContent.add(input.nextLine());
			}	
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return fileContent;
	}

	public static boolean fileExists(File f) {
		return f.exists();
	}

	public static boolean fileExists(String s) {
		File f = new File(s);
		return fileExists(f);
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setPath(String s) {
		this.path = s;
	}

	public void setFileName(String s) {
		this.fileName = s;
	}
}
