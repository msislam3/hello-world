/*
 * File name: Menus.java
 * Author: Rifat Shams, 040898113
 * Course: CST8284 - OOP
 * Assignment: 2
 * Date: 12 January, 2018
 * Professor: Dave Houtman
 * Purpose:
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
 * @author Rifat Shams
 * @version 1.0.0
 * @since 1.0.0
 */
public class Menus {
	//Redko, A. (2013). Using JavaFX UI Controls [WebPage]. Retrieved from
	//https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm

	//Jenkov, J. (2016). JavaFX MenuButton [WebPage]. Retrieved from
	//http://tutorials.jenkov.com/javafx/menubutton.html
	private static MenuBar menuBar = new MenuBar();

	private static Menu mnuFile;
	private static Menu mnuSettings;
	private static Menu mnuBookmarks;
	private static Menu mnuHelp;

	private static MenuItem mnuItmRefresh;
	private static MenuItem mnuItmExit;
	private static MenuItem mnuItmToggleAddressBar;
	private static MenuItem mnuItmChangeStartup;
	private static MenuItem mnuItmAddBookmark;
	private static MenuItem mnuItmAbout;
	private static MenuItem mnuItmCode;
	private static MenuItem mnuItmHistory;

	private static ContextMenu contextMenu;
	private static MenuItem mnuItmDeleteBookMark;
	
	private static ArrayList<String> bookmarks = new ArrayList<>();
	private static CustomMenuItem selectedBookMark;
	
	private static WebEngine webEngine;
	private static VBox topPane;
	private static HBox addressBarPane;
	private static BorderPane root;
	private static ScrollPane codePane;
	
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
	
	public static ArrayList<String> getBookMarks() {
		return bookmarks;
	}

	public static void setBookMarks(ArrayList<String> bookMarks) {
		Menus.bookmarks = bookMarks;
	}
	
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
	
	public static Menu getMnuFile() {
		mnuFile = getMnu("_File", getMnuItmRefresh(), getMnuItmExit());
		return mnuFile;
	}

	public static Menu getMnuSettings() {		
		mnuSettings = getMnu("_Settings", getMnuItmToggleAddressBar(), getMnuItmChangeStartup(), getMnuItemHistory(), getMnuItemCode());
		return mnuSettings;
	}

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

	public static Menu getMnuHelp() {
		mnuHelp = getMnu("_Help", getMnuItmAbout());
		return mnuHelp;
	}
	
	private static Menu getMnu(String name, MenuItem... menuItems) {
		Menu menu = new Menu(name);
		menu.getItems().addAll(menuItems);
		return menu;
	} 
		
	public static MenuItem getMnuItmRefresh() {	
		//http://java-buddy.blogspot.ca/2012/02/javafx-20-set-accelerator.html
		mnuItmRefresh = getMnuItm("Refresh", "R", e->
			{
				webEngine.reload();
			}) ;		
		return mnuItmRefresh;
	}

	public static MenuItem getMnuItmExit() {		
		mnuItmExit = getMnuItm("Exit", "E", e->
			{
				Platform.exit();
			});
		return mnuItmExit;
	}

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
	
	private static MenuItem getMenuItemForBookMark(String menuName) {
        Label menuLabel = new Label(menuName); 
        CustomMenuItem menuItem = new CustomMenuItem(menuLabel);

        menuLabel.setOnMousePressed(e->
	        {   	 
				  if(e.getButton() == MouseButton.PRIMARY) {
					  loadAddress(menuName);
				  }
				  else if (e.getButton() == MouseButton.SECONDARY) {
					  selectedBookMark = menuItem;
					  contextMenu.show(topPane, e.getScreenX(), e.getScreenY());
				  }
	        });

        return menuItem;
    }

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
	
	private static MenuItem getMnuItm(String name, String accelerator, EventHandler<ActionEvent> handler) {
		MenuItem mnuItm = new MenuItem(name);
		mnuItm.setAccelerator(KeyCombination.keyCombination("Ctrl+"+accelerator));
		mnuItm.setOnAction(handler);
		return mnuItm;
	}
	
	private static VBox getHistoryPane() {
		VBox historyPane = new VBox();
		historyPane.setPrefWidth(200);
		historyPane.setMaxWidth(500);
			
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		//https://stackoverflow.com/questions/33414194/fill-width-in-a-pane?rq=1
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
		
		//https://stackoverflow.com/questions/18928333/how-to-program-back-and-forward-buttons-in-javafx-with-webview-and-webengine
		Button buttonForward = new Button("\u23F5");
		Button buttonBack = new Button("\u23F4");
		
		buttonForward.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
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
			}
		});
		
		setButtonForwardState(history.getCurrentIndex(), history.getEntries().size(), buttonForward);
					
		buttonBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
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
			}
		});
		
		setButtonBackState(history.getCurrentIndex(), buttonBack);
		
		buttonPane.getChildren().addAll(buttonBack,buttonForward);
		
		historyPane.getChildren().addAll(scrollPane, buttonPane);
		
		return historyPane;
	}

	private static void setButtonBackState(int currentIndex, Button buttonBack) {
		//Set back button disable if at the first page
		if(currentIndex == 0) {
			buttonBack.setDisable(true);
		}
		else {
			buttonBack.setDisable(false);
		}
	}

	private static void setButtonForwardState(int currentIndex, int size, Button buttonForward) {
		//Set forward button disable if at the last page
		if(currentIndex == size-1) {
			buttonForward.setDisable(true);
		}
		else {
			buttonForward.setDisable(false);
		}
	}

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
	
	public static void saveBookmarksInFile() {
		File file = new File("bookmarks.web");
		FileUtils.saveFileContents(file, getBookMarks());
	}

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
