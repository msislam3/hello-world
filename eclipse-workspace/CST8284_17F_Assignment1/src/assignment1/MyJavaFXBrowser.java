package assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

public class MyJavaFXBrowser extends Application {

	private static TextField addressBarTextField = new TextField("address here");
	private static HBox addressBar = new HBox();
	private static VBox topPane = new VBox();
	
	@Override
	public void start(Stage primaryStage) {
				
	    WebPage currentPage = new WebPage();
		WebView webView = currentPage.getWebView();
		WebEngine webEngine = currentPage.createWebEngine(primaryStage);
		
		String defaultWebPage = Menus.getDefaultWebPageAddressFromFile();
		webEngine.load(defaultWebPage);
		getAddressBarTextField().setText(defaultWebPage);
				
		topPane.getChildren().add(Menus.getMenuBar(currentPage.getWebView()));
		setAddressBar(topPane, webEngine);
		
		BorderPane root = new BorderPane();
		root.setCenter(webView);
		root.setTop(topPane);
		
		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();	
	}
	
	@Override
	public void stop() {
		Menus.saveBookMarksInFile();
		//https://stackoverflow.com/a/30723892/1841089
		System.exit(0);
	}

	public static TextField getAddressBarTextField() {
		return addressBarTextField;
	}
	
	public static HBox getAddressBar() {
		return addressBar;
	}
	
	public static VBox getTopPane() {
		return topPane;
	}
	
	private void setAddressBar(VBox topVBox, WebEngine engine) {		
		//Add address bar label
		Text label = new Text("Enter address");	
		addressBar.getChildren().add(label);
		
		//Add address bar text field	
		addressBar.getChildren().add(getAddressBarTextField());
		
		//Add address bar button
		Button goButton = new Button("GO");
		addressBar.getChildren().add(goButton);	
		goButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				engine.load(getAddressBarTextField().getText());			
			}
		});
		
		addressBar.setSpacing(1);
		addressBar.setAlignment(Pos.CENTER_LEFT);
		//https://stackoverflow.com/a/31018911/1841089
		HBox.setHgrow(getAddressBarTextField(), Priority.ALWAYS);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
		System.out.println("stop");
	}

}
