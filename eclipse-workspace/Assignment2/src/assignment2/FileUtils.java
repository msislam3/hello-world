/*
 * File name: FileUtils.java
 * Author: Rifat Shams, Student ID# 040898113
 * Course: CST8284 - OOP
 * Assignment: 2
 * Date: 12 January, 2018
 * Professor: Dave Houtman
 * Purpose: Contain methods to perform File I/O operations
 */

package assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class contains utility methods to perform basic File I/O operations. 
 * 
 * @author Rifat Shams
 * @version 1.0.0
 * @see java.io.File 
 * @see java.io.FileNotFoundException
 * @see java.io.PrintWriter
 * @see java.util.ArrayList
 * @see java.util.Scanner
 * @since 1.0.0
 */
public class FileUtils {

	/**
	 * Saves a string passed in s, in the file passed in f
	 * <p>
	 * The method will create the file if it does not exist. The method will return without 
	 * writing any content if the file is not a writable file (directory) or if it can not create the file 
	 * 
	 * @param f The file object where the string has to be written
	 * @param s The string that has to be written in the file
	 */
	public static void saveFileContents(File f, String s) {
		try(PrintWriter output = new PrintWriter(f);) {
			output.write(s);	
		}
		catch(FileNotFoundException e){
			
		}
	}

	/**
	 * Gets the content of the file f and returns as string
	 * <p>
	 * The method reads one line from the file in a string and returns string. 
	 * If the file does not exist, it returns a empty string.
	 * 
	 * @param f The file to be read
	 * @return A string that are read from the file or empty string if the file does not exist
	 */
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

	/**
	 * Saves the list of strings passed in list ar, in the file passed in file f
	 * <p>
	 * The method will create the file if it does not exist. The method will return without 
	 * writing any content if the file is not a writable file (directory) or if it can not create the file 
	 * 
	 * @param f The file object where the strings has to be written
	 * @param ar The array list containing the list of strings
	 */
	public static void saveFileContents(File f, ArrayList<String> ar) {
		try(PrintWriter output = new PrintWriter(f);) {
			for (String writeContent : ar) {
				output.print(writeContent + "\n");
			}			
		}
		catch(FileNotFoundException e){
		
		}
	}

	/**
	 * Gets the content of the file f and returns as an array list
	 * <p>
	 * The method reads each line from the file in a string and returns the list of strings. 
	 * If the file does not exist, it returns a empty array list
	 * 
	 * @param f The file to be read
	 * @return An array list containing the strings that are read from the file or empty list if the file does not exist
	 */
	public static ArrayList<String> getFileContentsAsArrayList(File f) {
		ArrayList<String> fileContent = new ArrayList<String>();
		try(Scanner input = new Scanner(f);){
			while(input.hasNext()) {
				fileContent.add(input.nextLine());
			}	
		}
		catch(FileNotFoundException e){
			
		}
		return fileContent;
	}

	/**
	 * Checks if the file f exists or not
	 * 
	 * @param f The file to check
	 * @return Returns true if the file exists or else returns false
	 */
	public static boolean fileExists(File f) {
		return f.exists();
	}

	/**
	 * Checks if a file exists or not in path s
	 * 
	 * @param s The path of the file
	 * @return Returns true if the file exits or else returns false
	 */
	public static boolean fileExists(String s) {
		File f = new File(s);
		return fileExists(f);
	}
}
