package assignment1;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Menus {
	//Redko, A. (2013). Using JavaFX UI Controls [WebPage]. Retrieved from
	//https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm

	//Jenkov, J. (2016). JavaFX MenuButton [WebPage]. Retrieved from
	//http://tutorials.jenkov.com/javafx/menubutton.html
	private static MenuBar menuBar = new MenuBar();

	private static Menu mnuFile = new Menu("File");
	private static Menu mnuSettings = new Menu("Settings");
	private static Menu mnuBookmarks = new Menu("Bookmarks");
	private static Menu mnuHelp = new Menu("Help");

	private static MenuItem mnuItmRefresh = new MenuItem("Refresh");
	private static MenuItem mnuItmExit = new MenuItem("Exit");
	private static MenuItem mnuItmToggleAddressBar = new MenuItem("Toggle Address Bar");
	private static MenuItem mnuItmChangeStartup = new MenuItem("Change Start-up Page");
	private static MenuItem mnuItmAddBookmark = new MenuItem("Add Bookmark");
	private static MenuItem mnuItmAbout = new MenuItem("About");

	private static ArrayList<String> bookmarks = new ArrayList<>();

	public static ArrayList<String> getBookMarks() {
		return bookmarks;
	}

	public static void setBookMarks(ArrayList<String> bookMarks) {
		Menus.bookmarks = bookMarks;
	}
	
	public static MenuBar getMenuBar(WebView view, VBox topPane, HBox addressBarPane, TextField textAddressBar) {
		menuBar.getMenus().addAll(getMnuFile(view), getMnuSettings(topPane, addressBarPane, view.getEngine()), getMnuBookmarks(view.getEngine(), textAddressBar), getMnuHelp());
		return menuBar;
	}

	public static Menu getMnuFile(WebView view) {
		mnuFile.getItems().addAll(getMnuItmRefresh(view), getMnuItmExit());
		return mnuFile;
	}

	public static Menu getMnuSettings(VBox topPane, HBox addressBarPane, WebEngine webEng) {
		mnuSettings.getItems().addAll(getMnuItmToggleAddressBar(topPane, addressBarPane), getMnuItmChangeStartup(webEng));
		return mnuSettings;
	}

	public static Menu getMnuBookmarks(WebEngine webEng, TextField textAddressBar) {
		getBookmarksFromFile();
		mnuBookmarks.getItems().add(getMnuItmAddBookmark(webEng, textAddressBar));

		for(String URL: getBookMarks()) {
			MenuItem bookmarkItem = new MenuItem(URL);

			bookmarkItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					webEng.load(URL);
					textAddressBar.setText(URL);
				}	
			});
			mnuBookmarks.getItems().add(bookmarkItem);
		}
		return mnuBookmarks;
	}

	public static Menu getMnuHelp() {
		mnuHelp.getItems().add(getMnuItmAbout());
		return mnuHelp;
	}

	public static MenuItem getMnuItmRefresh(WebView wv) {	
		mnuItmRefresh.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				wv.getEngine().reload();
			}
		});
		return mnuItmRefresh;
	}

	public static MenuItem getMnuItmExit() {		
		mnuItmExit.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				Platform.exit();
			}
		});
		return mnuItmExit;
	}

	public static MenuItem getMnuItmToggleAddressBar(VBox topPane, HBox addressBarPane) {
		mnuItmToggleAddressBar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//breaktop. (2017). JavaFX - setVisible doesn't “hide” the element [WebPage]. Retrieved from
				//https://stackoverflow.com/a/28558301/9116680		
				if(topPane.getChildren().contains(addressBarPane)) {
					topPane.getChildren().remove(addressBarPane);
				}
				else {
					topPane.getChildren().add(addressBarPane);
				}
			}	
		});
		return mnuItmToggleAddressBar;
	}

	public static MenuItem getMnuItmChangeStartup(WebEngine webEng) {
		mnuItmChangeStartup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				//Oracle. (2015). Class WebEngine [WebPage]. Retrieved from
				//https://docs.oracle.com/javase/8/javafx/api/javafx/scene/web/WebEngine.html
				String url = webEng.getLocation();
				File file = new File("default.web");
				FileUtils.saveFileContents(file, url);
			}	
		});
		return mnuItmChangeStartup;
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

	public static MenuItem getMnuItmAddBookmark(WebEngine webEng, TextField textAddressBar) {
		mnuItmAddBookmark.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String currentURL = webEng.getLocation();
				getBookMarks().add(currentURL);

				MenuItem bookmarkItem = new MenuItem(currentURL);				
				bookmarkItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						webEng.load(currentURL);
						textAddressBar.setText(currentURL);
					}	
				});
				mnuBookmarks.getItems().add(bookmarkItem);
			}
		});
		return mnuItmAddBookmark;
	}

	public static MenuItem getMnuItmAbout() {
		mnuItmAbout.setOnAction(new EventHandler<ActionEvent>() {

			//Jakob, M. (2014). JavaFX Dialogs (official) [Blog post]. Retrieved from
			//Source: http://code.makery.ch/blog/javafx-dialogs-official/
			@Override
			public void handle(ActionEvent e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information");
				alert.setHeaderText(null);
				alert.setContentText("Rifat Shams\nStudent Number: 040898113");
				alert.showAndWait(); 
			}
		});
		return mnuItmAbout;
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
