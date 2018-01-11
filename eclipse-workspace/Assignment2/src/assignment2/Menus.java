/*
 * File name: Menus.java
 * Author: Rifat Shams, 040898113
 * Course: CST8284 - OOP
 * Assignment: 2
 * Date: 12 January, 2018
 * Professor: Dave Houtman
 * Purpose: Contains code to create the menu bar for the web browser
 */

package assignment2;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/**
 * Class to create menu bar for the web browser
 * <p>
 * This class contains separate methods to create different menu and menu items
 * @author Rifat Shams
 * @version 1.0.0
 * @see java.io.File
 * @see java.net.MalformedURLException
 * @see java.net.URL
 * @see java.util.ArrayList
 * @see javafx.application.Platform
 * @see javafx.event.ActionEvent
 * @see javafx.event.EventHandler
 * @see javafx.scene.control.Alert
 * @see javafx.scene.control.Alert.AlertType
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.ContextMenu
 * @see javafx.scene.control.CustomMenuItem
 * @see javafx.scene.control.Label
 * @see javafx.scene.control.Menu
 * @see javafx.scene.control.MenuBar
 * @see javafx.scene.control.MenuItem
 * @see javafx.scene.control.ScrollPane
 * @see javafx.scene.control.ScrollPane.ScrollBarPolicy
 * @see javafx.scene.input.KeyCombination
 * @see javafx.scene.input.MouseButton
 * @see javafx.scene.layout.BorderPane
 * @see javafx.scene.layout.HBox
 * @see javafx.scene.layout.VBox
 * @see javafx.scene.text.Text
 * @see javafx.scene.web.WebEngine
 * @see javafx.scene.web.WebHistory
 * @see javafx.scene.web.WebView
 * @since 1.0.0
 */
public class Menus {
	//Redko, A. (2013). Using JavaFX UI Controls [WebPage]. Retrieved from
	//https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm

	//Jenkov, J. (2016). JavaFX MenuButton [WebPage]. Retrieved from
	//http://tutorials.jenkov.com/javafx/menubutton.html

	/**
	 * Menu bar for the application
	 */
	private static MenuBar menuBar = new MenuBar();

	/**
	 * File menu
	 */
	private static Menu mnuFile;
	/**
	 * Settings menu
	 */
	private static Menu mnuSettings;
	/**
	 * Bookmark menu
	 */
	private static Menu mnuBookmarks;
	/**
	 * Help menu
	 */
	private static Menu mnuHelp;

	/**
	 * Refresh menu item. Lets the user the reload the web page currently showing
	 */
	private static MenuItem mnuItmRefresh;
	/**
	 * Exit menu item. Lets the use exit the application
	 */
	private static MenuItem mnuItmExit;
	/**
	 * Toggle addressbar menu item. Lets the user toggle the visibility of the addressbar pane
	 */
	private static MenuItem mnuItmToggleAddressBar;
	/**
	 * Change startup menu item. Lets the user to change the start up web page 
	 */
	private static MenuItem mnuItmChangeStartup;
	/**
	 * Add bookmark menu item. Lets the user to add the current web page as book marks
	 */
	private static MenuItem mnuItmAddBookmark;
	/**
	 * About menu item. Shows the user the About dialog
	 */
	private static MenuItem mnuItmAbout;
	/**
	 * Display code menu item. Lets the user to see the HTML/JavaScript code of the web page
	 */
	private static MenuItem mnuItmCode;
	/**
	 * Display history menu item. Lets the user to see the web page history
	 */
	private static MenuItem mnuItmHistory;

	/**
	 * The context menu for the bookmarks
	 */
	private static ContextMenu contextMenu;
	/**
	 * Delete bookmark menu item. Lets the user to delete a bookmark
	 */
	private static MenuItem mnuItmDeleteBookMark;
	
	/**
	 * List of bookmarks
	 */
	private static ArrayList<String> bookmarks = new ArrayList<>();
	/**
	 * The bookmark selected to be removed
	 */
	private static CustomMenuItem selectedBookMark;
	
	/**
	 * Webengine that loads web pages to the browser
	 */
	private static WebEngine webEngine;
	/**
	 * Top pane containing menubar and addressbar
	 */
	private static VBox topPane;
	/**
	 * Addressbar pane containing addressbar text field and go button
	 */
	private static HBox addressBarPane;
	/**
	 * Main container for the application
	 */
	private static BorderPane root;
	/**
	 * Scrollpane to show the HTML/JavaScript code
	 */
	private static ScrollPane codePane;
	
	/**
	 * Method to load an URL in the webengine
	 * @param url The URL to load
	 */
	public static void loadAddress(String url) {
		try {
			new URL(url);
			webEngine.load(url);
		} catch (MalformedURLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Wrong URL Format");
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			alert.showAndWait(); 
		} catch ( StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Method that returns the current bookmarks
	 * @return An array list containing the bookmarks
	 */
	public static ArrayList<String> getBookMarks() {
		return bookmarks;
	}

	/**
	 * Method to set bookmarks
	 * @param bookMarks An arraylist containing the bookmarks to set
	 */
	public static void setBookMarks(ArrayList<String> bookMarks) {
		Menus.bookmarks = bookMarks;
	}
	
	/**
	 * Method that creates an return the menu bar for the application
	 * @param view The webview object that shows the web pages to the user
	 * @param topPane The top pane that contains the menubar and addressbar 
	 * @param addressBarPane The pane that contains the addressbar text field and go button
	 * @param root The main container for the application
	 * @param codePane The scrollpane that contains the text area for the code area 
	 * @return The menu bar of the application
	 */
	public static MenuBar getMenuBar(WebView view, VBox topPane, HBox addressBarPane, BorderPane root, ScrollPane codePane) {
		webEngine = view.getEngine();
		Menus.topPane = topPane;
		Menus.addressBarPane = addressBarPane;
		Menus.root = root;
		Menus.codePane = codePane;
		
		createContextMenu();	
		
		menuBar.getMenus().addAll(getMnuFile(), getMnuSettings(), getMnuBookmarks(), getMnuHelp());
		return menuBar;
	}

	/**
	 * Method to create the context menu for the bookmark items
	 */
	private static void createContextMenu() {
		mnuItmDeleteBookMark= new MenuItem("Remove Bookmark");
		mnuItmDeleteBookMark.setOnAction(e->
			{
				if(selectedBookMark != null) {
					mnuBookmarks.getItems().remove(selectedBookMark);
					getBookMarks().remove(((Label)selectedBookMark.getContent()).getText());
				}
			});
		
		contextMenu = new ContextMenu();
		contextMenu.getItems().add(mnuItmDeleteBookMark);
	}
	
	/**
	 * Method to create the file menu
	 * @return The file menu
	 */
	public static Menu getMnuFile() {
		mnuFile = getMnu("_File", getMnuItmRefresh(), getMnuItmExit());
		return mnuFile;
	}

	/**
	 * Method to create the settings menu
	 * @return The settings menu
	 */
	public static Menu getMnuSettings() {		
		mnuSettings = getMnu("_Settings", getMnuItmToggleAddressBar(), getMnuItmChangeStartup(), getMnuItemHistory(), getMnuItemCode());
		return mnuSettings;
	}

	/**
	 * Method to create the bookmark menu
	 * <p>
	 * The method is responsible to load the saved bookmarks from file in the menu
	 * @return The bookmark menu
	 */
	public static Menu getMnuBookmarks() {	
		getBookmarksFromFile();
		
		ArrayList<MenuItem> menuItems = new ArrayList<>();
		
		//Add 'Add BookMark' menu item
		menuItems.add(getMnuItmAddBookmark());
		
		//Add the saved BookMarks as menu items
		for(String URL: getBookMarks()) {
			MenuItem bookmarkItem = getMenuItemForBookMark(URL);
			menuItems.add(bookmarkItem);
		}
		
		mnuBookmarks = getMnu("_Bookmarks", menuItems.toArray(new MenuItem[menuItems.size()]));		
		return mnuBookmarks;
	}

	/**
	 * Method to create the help menu
	 * @return The help menu
	 */
	public static Menu getMnuHelp() {
		mnuHelp = getMnu("_Help", getMnuItmAbout());
		return mnuHelp;
	}
	
	/**
	 * Utility method to create menus using supplied name and menu items
	 * @param name The name of the menu
	 * @param menuItems List of menu items included in the menu
	 * @return The menu which contains the menu items
	 */
	private static Menu getMnu(String name, MenuItem... menuItems) {
		Menu menu = new Menu(name);
		menu.getItems().addAll(menuItems);
		return menu;
	} 
		
	/**
	 * Method to create Refresh menu item
	 * @return The refresh menu item
	 */
	public static MenuItem getMnuItmRefresh() {			
		mnuItmRefresh = getMnuItm("Refresh", "R", e->
			{
				webEngine.reload();
			}) ;		
		return mnuItmRefresh;
	}

	/**
	 * Method to create the Exit menu item
	 * @return The Exit menu item
	 */
	public static MenuItem getMnuItmExit() {		
		mnuItmExit = getMnuItm("Exit", "E", e->
			{
				Platform.exit();
			});
		return mnuItmExit;
	}

	/**
	 * Method to create the Toggle AddressBar menu item
	 * @return The ToggleAddressBar menu item
	 */
	public static MenuItem getMnuItmToggleAddressBar() {
		mnuItmToggleAddressBar = getMnuItm("Toggle Address Bar", "T", e->
			{
				//breaktop. (2017). JavaFX - setVisible doesn't “hide” the element [WebPage]. Retrieved from
				//https://stackoverflow.com/a/28558301/9116680		
				if(topPane.getChildren().contains(addressBarPane)) {
					topPane.getChildren().remove(addressBarPane);
				}
				else {
					topPane.getChildren().add(addressBarPane);
				}
			}); 
		return mnuItmToggleAddressBar;
	}

	/**
	 * Method to create the Change Startup menu item
	 * @return The Change Startup menu item
	 */
	public static MenuItem getMnuItmChangeStartup() {
		mnuItmChangeStartup = getMnuItm("Change Start-up Page", "S", e->
			{
				//Oracle. (2015). Class WebEngine [WebPage]. Retrieved from
				//https://docs.oracle.com/javase/8/javafx/api/javafx/scene/web/WebEngine.html
				String url = webEngine.getLocation();
				File file = new File("default.web");
				FileUtils.saveFileContents(file, url);
			});
		return mnuItmChangeStartup;
	}
	
	/**
	 * Method to create the Display Code menu item
	 * @return The Display Code menu item
	 */
	public static MenuItem getMnuItemCode() {
		mnuItmCode = getMnuItm("Display Code", "C", e->
			{
				if(root.getChildren().contains(codePane)) {
					root.getChildren().remove(codePane);
				}
				else {
					root.setBottom(codePane);
				}
			});
		return mnuItmCode;
	}
	
	/**
	 * Method to create the Show History menu item
	 * @return The Show History menu item
	 */
	public static MenuItem getMnuItemHistory() {
		mnuItmHistory = getMnuItm("Show History", "H", e->
			{
				if(root.getRight() != null) {
					root.setRight(null);;
				}
				else {
					root.setRight(getHistoryPane());
				}
			});
		return mnuItmHistory;
	}
	
	/**
	 * Method to create the Add BookMark menu item
	 * @return The Add BookMark menu item
	 */
	public static MenuItem getMnuItmAddBookmark() {
		mnuItmAddBookmark = getMnuItm("Add Bookmark", "B", e->
			{
				String currentURL = webEngine.getLocation();
				getBookMarks().add(currentURL);			
				MenuItem bookmarkItem = getMenuItemForBookMark(currentURL);
				mnuBookmarks.getItems().add(bookmarkItem);
			}); 
		return mnuItmAddBookmark;
	}
	
	/**
	 * Utility method to create Menu item for a bookmark address
	 * <p>
	 * The method creates a CustomMenuItem for the bookmark. It also sets up handler to handle right and left click on the menu item
	 * @param url The bookmark address
	 * @return The menu item for the bookmark
	 */
	private static MenuItem getMenuItemForBookMark(String url) {
        Label menuLabel = new Label(url); 
        CustomMenuItem menuItem = new CustomMenuItem(menuLabel);

        menuLabel.setOnMousePressed(e->
	        {   	 
				  if(e.getButton() == MouseButton.PRIMARY) {
					  loadAddress(url);
				  }
				  else if (e.getButton() == MouseButton.SECONDARY) {
					  selectedBookMark = menuItem;
					  contextMenu.show(topPane, e.getScreenX(), e.getScreenY());
				  }
	        });

        return menuItem;
    }

	/**
	 * Method to create the About menu item
	 * @return The About menu item
	 */
	public static MenuItem getMnuItmAbout() {
		mnuItmAbout = getMnuItm("About", "A", 
				e->{
					//Jakob, M. (2014). JavaFX Dialogs (official) [Blog post]. Retrieved from
					//Source: http://code.makery.ch/blog/javafx-dialogs-official/
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("Rifat Shams\nStudent Number: 040898113");
					alert.showAndWait(); 				
				});
		
		return mnuItmAbout;
	}
	
	/**
	 * Utility method to create menu items
	 * <p>
	 * The method creats a menu item with the specified name and sets the accelerator key and action handler for the menu item
	 * @param name The name of the menu item
	 * @param accelerator The accelerator key for the menu item
	 * @param handler The handler for the menu item
	 * @return The created menu item
	 */
	private static MenuItem getMnuItm(String name, String accelerator, EventHandler<ActionEvent> handler) {
		MenuItem mnuItm = new MenuItem(name);
		//Java-Buddy. (2012). JavaFX 2.0: Set Accelerator (KeyCombination) for menu items [Blog post]. Retrieved from
		//Source: http://java-buddy.blogspot.ca/2012/02/javafx-20-set-accelerator.html
		mnuItm.setAccelerator(KeyCombination.keyCombination("Ctrl+"+accelerator));
		mnuItm.setOnAction(handler);
		return mnuItm;
	}
	
	/**
	 * Method to create a pane to show the web page history in the browser
	 * <p>
	 * The method creates a VBox for the history pane. It contains a scrollpane that shows the history and two buttons, back and forward
	 * which lets user to navigate through the history list
	 * @return The history pane 
	 */
	private static VBox getHistoryPane() {
		VBox historyPane = new VBox();
		historyPane.setPrefWidth(200);
		historyPane.setMaxWidth(500);
			
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		//James_D. (2015). Fill width in a Pane [WebPage]. Retrieved from
		//https://stackoverflow.com/a/33415353/1841089	
		scrollPane.prefHeightProperty().bind(historyPane.heightProperty());
		
		Text historyText = new Text();
		
		StringBuilder builder = new StringBuilder();
		WebHistory history = webEngine.getHistory();
		for (WebHistory.Entry entry : history.getEntries()) {
			builder.append(entry.getTitle()+"\n");
		}
		historyText.setText(builder.toString());
		
		scrollPane.setContent(historyText);
		
		HBox buttonPane = new HBox();
		
		//Frank. (2013). How to program Back and Forward buttons in JavaFX with WebView and WebEngine? [WebPage]. Retrieved from
		//https://stackoverflow.com/a/18989127/1841089
		Button buttonForward = new Button("\u23F5");
		Button buttonBack = new Button("\u23F4");
		
		buttonForward.setOnAction(e->
			{				
				int currentIndex = history.getCurrentIndex();
				if(currentIndex < history.getEntries().size()-1) {
					Platform.runLater(
						new Runnable() { 
							public void run() { 
								history.go(1); 	
							} 
						});
				
					setButtonForwardState(history.getCurrentIndex() + 1, history.getEntries().size(), buttonForward);
					setButtonBackState(history.getCurrentIndex() + 1, buttonBack);
				}
			});
		
		setButtonForwardState(history.getCurrentIndex(), history.getEntries().size(), buttonForward);
					
		buttonBack.setOnAction(e-> 
			{
					int currentIndex = history.getCurrentIndex();
					if(currentIndex>0) {
						Platform.runLater(
								new Runnable() { 
									public void run() { 
										history.go(-1); 	
									} 
								});
						
						setButtonForwardState(history.getCurrentIndex()-1, history.getEntries().size(), buttonForward);
						setButtonBackState(history.getCurrentIndex()-1, buttonBack);
					}
				
			});
		
		setButtonBackState(history.getCurrentIndex(), buttonBack);
		
		buttonPane.getChildren().addAll(buttonBack,buttonForward);
		
		historyPane.getChildren().addAll(scrollPane, buttonPane);
		
		return historyPane;
	}

	/**
	 * Method to set the state of the Back button based on current position in the history list
	 * <p>
	 * The Back button will be disabled if the first page of the history is currently showing else it will be enabled
	 * @param currentIndex The current position in the history list 
	 * @param buttonBack The Back button
	 */
	private static void setButtonBackState(int currentIndex, Button buttonBack) {
		if(currentIndex == 0) {
			buttonBack.setDisable(true);
		}
		else {
			buttonBack.setDisable(false);
		}
	}

	/**
	 * Method to set the state of the Forward button based on current position in the history list
	 * <p>
	 * The Forward button will be disabled if the last page of the history is currently showing else it will be enabled
	 * @param currentIndex The current position in the history list
	 * @param size The size of the history list
	 * @param buttonForward The forward button
	 */
	private static void setButtonForwardState(int currentIndex, int size, Button buttonForward) {
		if(currentIndex == size-1) {
			buttonForward.setDisable(true);
		}
		else {
			buttonForward.setDisable(false);
		}
	}

	/**
	 * Method to return the default web page address
	 * <p>
	 * The method reads the "default.web" file to read the default web page. If the file does not exist it returns www.google.ca
	 * @return The default web page address
	 */
	public static String getDefaultWebPage() {
		String defaultWebPage="";
		File file = new File("default.web");
		if(file.exists()) {
			defaultWebPage = FileUtils.getFileContentsAsString(file);
		}
		else {
			defaultWebPage = "http://www.google.ca";
			FileUtils.saveFileContents(file, defaultWebPage);
		}	
		return defaultWebPage;
	}
	
	/**
	 * Method to save the bookmarks in the file
	 * <p>
	 * The method saves the bookmarks in "bookmarks.web" file
	 */
	public static void saveBookmarksInFile() {
		File file = new File("bookmarks.web");
		FileUtils.saveFileContents(file, getBookMarks());
	}

	/**
	 * Method to load the saved book marks from the file
	 * <p>
	 * The method reads the bookmarks from "bookmarks.web" file
	 */
	private static void getBookmarksFromFile() {
		File file = new File("bookmarks.web");
		if(file.exists()) {
			setBookMarks(FileUtils.getFileContentsAsArrayList(file));
		}
		else {
			setBookMarks(new ArrayList<>());
		}
	}
}
