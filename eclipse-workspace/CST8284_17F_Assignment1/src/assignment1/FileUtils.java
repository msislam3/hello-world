package assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtils {
	
	private String fileName;
	private String path;
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	
	
	public static void saveFileContents(File f, ArrayList<String> ar) {
		
		try (PrintWriter writer = new PrintWriter(f)){
			for (String string : ar) {
				writer.write(string+"\n");
			}
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getFileContentsAsArrayList(File f){
		
		ArrayList<String> ar = new ArrayList<String>();
		
		try(Scanner input = new Scanner(f)){
			while(input.hasNext()) {
				ar.add(input.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ar;
	}
	
	public static boolean fileExists(File f) {
		return f.exists();
	}
	
	public static boolean fileExists(String s) {
		File file = new File(s);
		return file.exists();
	}
	
	public static void saveFileContents(File f, String content) {		
		try (PrintWriter writer = new PrintWriter(f)){
				writer.write(content);			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}

	public static String getFileContentsString(File f) 
	{
		String result = "";
		try(Scanner input = new Scanner(f)){
			if(input.hasNext()) {
				result = input.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
