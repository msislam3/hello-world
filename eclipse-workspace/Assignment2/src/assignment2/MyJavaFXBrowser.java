/*
 * File name: MyJavaFXBrowser.java
 * Author: Rifat Shams, 040898113
 * Course: CST8284 - OOP
 * Assignment: 2
 * Date: 12 January, 2018
 * Professor: Dave Houtman
 * Purpose: The entry point of the web browser program. Creates a JavaFX application that allows user to browse web pages
 */
package assignment2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * The main class of the application. Creates the web browser application with browsing window, menu bar, address bar, history and code pane
 * @author Rifat Shams
 * @version 1.0.0
 * @see javafx.application.Application
 * @see javafx.scene.Scene
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.Label
 * @see javafx.scene.control.ScrollPane
 * @see javafx.scene.control.ScrollPane.ScrollBarPolicy
 * @see javafx.scene.control.TextArea
 * @see javafx.scene.control.TextField
 * @see javafx.scene.input.KeyCode
 * @see javafx.scene.layout.BorderPane
 * @see javafx.scene.layout.HBox
 * @see javafx.scene.layout.Priority
 * @see javafx.scene.layout.VBox
 * @see javafx.scene.web.WebEngine
 * @see javafx.scene.web.WebView
 * @see javafx.stage.Stage
 * @since 1.0.0
 */
public class MyJavaFXBrowser extends Application {

	/**
	 * The main method of the application.
	 * <p>
	 * Creates a web browser with browsing window, menu bar, address bar, history pane and code pane
	 */
	@Override
	public void start(Stage primaryStage) {
		TextArea codeTextArea = new  TextArea("Source code");	 
		TextField textAddressBar = new TextField("address here");
		
		//This code provided by Dave Houtman (2017) personal communication
		WebPage currentPage = new WebPage();
		WebView webView = currentPage.getWebView();
		WebEngine webEngine = currentPage.createWebEngine(primaryStage, textAddressBar, codeTextArea);

		String defaultWebPage = Menus.getDefaultWebPage();
		webEngine.load(defaultWebPage);

		BorderPane root = new BorderPane();	
		ScrollPane scrollPane = getCodeAreaPane(codeTextArea);
		VBox topPane = new VBox();	
		HBox addressBarPane = getAddressBarPane(webEngine, textAddressBar);
		
		topPane.getChildren().add(Menus.getMenuBar(webView, topPane, addressBarPane, root, scrollPane));
		
		root.setCenter(webView);
		root.setTop(topPane);
		
		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	/**
	 * Method that creates a ScrollPane for showing the HTML/JavaScript code 
	 * @param codeTextArea The text area where the actual HTML/JavaScript code is shown
	 * @return Returns a ScrollPane for the code area
	 */
	private ScrollPane getCodeAreaPane(TextArea codeTextArea) {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(codeTextArea);
		scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToWidth(true);
		scrollPane.prefHeight(200);
		scrollPane.maxHeight(400);
		return scrollPane;
	}
	
	/**
	 * Method that creates the addressbar pane containing the addressbar text box and the go button
	 * <p>
	 * The method also contains the handler for the go button click event and the addressbar text field keypress event 
	 * @param webEngine The web engine that loads the web page in the browser window
	 * @param textAddressBar The text field to right web page URL
	 * @return Returns a HBox for the address bar
	 */
	private HBox getAddressBarPane(WebEngine webEngine, TextField textAddressBar) {
		Label labelAddressBar = new Label("Enter Address");
		Button buttonGo = new Button("Go");
		buttonGo.setOnAction(e->
			{
				String url = textAddressBar.getText();
				Menus.loadAddress(url);
			});
				
		
		textAddressBar.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER) {
				String url = textAddressBar.getText();
				Menus.loadAddress(url);
			}
		});
		
		HBox addressBarPane = new HBox();
		addressBarPane.getChildren().addAll(labelAddressBar, textAddressBar, buttonGo);

		//ItachiUchiha. (2015). JavaFX Resizing TextField with Window [WebPage]. Retrieved from
		//https://stackoverflow.com/a/31018911/911668
		HBox.setHgrow(textAddressBar, Priority.ALWAYS);

		return addressBarPane;
	}
	
	/**
	 * Method that is executed when the application is stopped
	 * <p>
	 * Responsible for saving the bookmarks to a file
	 */
	@Override
	public void stop() {
		Menus.saveBookmarksInFile();
	}

	/**
	 * Tha main method that launches the application
	 * @param args The command line argument passed to the application
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}
