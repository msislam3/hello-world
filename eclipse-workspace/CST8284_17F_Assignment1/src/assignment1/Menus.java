package assignment1;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Menus {
	private static MenuBar menuBar = new MenuBar();
	
	private static Menu mnuFile = new Menu("File");
	private static Menu mnuSettings = new Menu("Settings");
	private static Menu mnuBookMarks = new Menu("Bookmarks");
	private static Menu mnuHelp = new Menu("Help");
	
	private static MenuItem mnuItmRefresh = new MenuItem("Refresh");;
	private static MenuItem mnuItmExit = new MenuItem("Exit");
	private static MenuItem mnuItmToggleAddressBar = new MenuItem("Toggle AddressBar");
	private static MenuItem mnuItmChangeStartUp = new MenuItem("Change StartUp");
	private static MenuItem mnuItmAddBookmark = new MenuItem("Add Bookmark");
	private static MenuItem mnuItmAbout = new MenuItem("About");
	
	private static FileUtils defaultWebPagePath = new FileUtils();
	private static FileUtils bookMarkPath = new FileUtils();
	
	private static ArrayList<String> bookMarks;
	private static boolean showAddressBar = false;

	static {
		defaultWebPagePath.setFileName("default.web");
		bookMarkPath.setFileName("bookMarks.web");
	}
	
	public static void setBookMarks(ArrayList<String> newBookMarks) {
		bookMarks = newBookMarks;
	}
	
	public static ArrayList<String> getBookMarks(){
		return bookMarks;
	}
	
	public static void saveBookMarksInFile() {
		File bookMarksFile = new File(getBookMarkPath().getFileName());
		FileUtils.saveFileContents(bookMarksFile, bookMarks);
	}
	
	private static void loadBookmarksFromFile(){
		File bookMarksFile = new File(getBookMarkPath().getFileName());
		if(bookMarksFile.exists()) {
			setBookMarks(FileUtils.getFileContentsAsArrayList(bookMarksFile));
		}
		else {
			setBookMarks(new ArrayList<String>());
		}
	}
	
	private static FileUtils getBookMarkPath() {
		return bookMarkPath;
	}

	public static void saveDefaultWebPageAddressInFile(String currentPageAddress) {
		File defaultWebPageFile = new File(getDefaultWebPagePath().getFileName());
		FileUtils.saveFileContents(defaultWebPageFile, currentPageAddress);
	}
	
	public static String getDefaultWebPageAddressFromFile() {
		File defaultWebPageFile = new File(getDefaultWebPagePath().getFileName());
		if(defaultWebPageFile.exists()) {
			return FileUtils.getFileContentsString(defaultWebPageFile);
		}
		else {
			String defaultPage = "http://www.google.ca";
			FileUtils.saveFileContents(defaultWebPageFile, defaultPage);
			return defaultPage;
		}
	}
	
	private static FileUtils getDefaultWebPagePath() {
		return defaultWebPagePath;
	} 
	
	public static MenuBar getMenuBar(WebView webView) {													
		menuBar.getMenus().addAll(getMnuFile(webView), getMnuSettings(webView), getMnuBookMarks(webView), getMnuHelp());		
		return menuBar;
	}
	
	public static Menu getMnuFile(WebView webView) {
		mnuFile.getItems().addAll(getMnuItmRefresh(webView), getMnuItmExit());	
		
		return mnuFile;
	}
	
	public static Menu getMnuSettings(WebView webView) {
		mnuSettings.getItems().addAll(getMnuItemToggleAddressBar(), getMnuItemChangeStartUp(webView.getEngine()));
		
		return mnuSettings;
	}
	
	public static Menu getMnuBookMarks(WebView webView) {
		loadBookmarksFromFile();
		
		MenuItem addBookMark = getMnuItemAddBookMark();		
		ArrayList<String> bookMarks = getBookMarks();

		mnuBookMarks.getItems().add(addBookMark);
		setSavedBookMarksInMenu(webView.getEngine(), mnuBookMarks, bookMarks);
			
		addBookMark.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {			
				//Q - If the same page is added twice, should it appear twice in the book mark menu
				String currentPage = MyJavaFXBrowser.getAddressBarTextField().getText();
				bookMarks.add(currentPage);
				setBookMarksMenuItem(webView.getEngine(), mnuBookMarks, currentPage);
			}
		});
		
		return mnuBookMarks;
	}
	
	public static Menu getMnuHelp() {
		mnuHelp.getItems().add(getMnuItemAbout());	
		
		return mnuHelp;
	}
	
	public static MenuItem getMnuItmRefresh(WebView webView) {
		mnuItmRefresh.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				webView.getEngine().reload();				
			}
		});
		
		return mnuItmRefresh;
	}
	
	public static MenuItem getMnuItmExit() {		
		mnuItmExit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				//Putting System.exit() causes the overridden stop method not called
				//System.exit(0);
			}
		});
		
		return mnuItmExit;
	}
	
	public static MenuItem getMnuItemToggleAddressBar() {			
		mnuItmToggleAddressBar.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				showAddressBar = !showAddressBar;
				if(showAddressBar) {
					MyJavaFXBrowser.getTopPane().getChildren().add(MyJavaFXBrowser.getAddressBar());
				}
				else {
					MyJavaFXBrowser.getTopPane().getChildren().remove(MyJavaFXBrowser.getAddressBar());
				}
			}
		});
		
		return mnuItmToggleAddressBar;
	}
	
	public static MenuItem getMnuItemChangeStartUp(WebEngine webEngine) {			
		mnuItmChangeStartUp.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println(webEngine.getLocation());
				saveDefaultWebPageAddressInFile(MyJavaFXBrowser.getAddressBarTextField().getText());
			}
		});
		
		return mnuItmChangeStartUp;
	}

	public static MenuItem getMnuItemAbout() {		
		mnuItmAbout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("About");
				alert.setHeaderText(null);
				alert.setContentText("Rifat Shams \nStudent Number: 040898113");
				alert.showAndWait();
			}
		});
		
		return mnuItmAbout;
	}
	
	public static MenuItem getMnuItemAddBookMark() {
		return mnuItmAddBookmark;
	}
	
	private static void setSavedBookMarksInMenu(WebEngine webEngine, Menu bookMarkMenu, ArrayList<String> bookMarks) {
		for (String bookMarkAddress : bookMarks) {
			setBookMarksMenuItem(webEngine, bookMarkMenu, bookMarkAddress);
		}
	}

	private static void setBookMarksMenuItem(WebEngine webEngine, Menu bookMarkMenu, String bookMarkAddress) {
		MenuItem item = new MenuItem(bookMarkAddress);
		item.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				webEngine.load(bookMarkAddress);
				MyJavaFXBrowser.getAddressBarTextField().setText(bookMarkAddress);
			}
		});
		bookMarkMenu.getItems().add(item);
	}
}
