package assignment1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class MyJavaFXBrowser extends Application {

	@Override
	public void start(Stage primaryStage) {
		//This code provided by Dave Houtman (2017) personal communication
		WebPage currentPage = new WebPage();
		WebView webView = currentPage.getWebView();
		WebEngine webEngine = currentPage.createWebEngine(primaryStage);

		String defaultWebPage = Menus.getDefaultWebPage();
		TextField textAddressBar = new TextField("address here");
		textAddressBar.setText(defaultWebPage);
		webEngine.load(defaultWebPage);

		VBox topPane = new VBox();
		HBox addressBarPane = getAddressBarPane(webEngine, textAddressBar);
		topPane.getChildren().add(Menus.getMenuBar(webView, topPane, addressBarPane, textAddressBar));

		BorderPane root = new BorderPane();
		root.setCenter(webView);
		root.setTop(topPane);

		Scene scene = new Scene(root, 800, 500);
		primaryStage.setScene(scene);
		primaryStage.show();	
	}

	private HBox getAddressBarPane(WebEngine webEngine, TextField textAddressBar) {
		Label labelAddressBar = new Label("Enter Address");
		Button buttonGo = new Button("Go");
		buttonGo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String URL = textAddressBar.getText();
				webEngine.load(URL);
			}
		});

		HBox addressBarPane = new HBox();
		addressBarPane.getChildren().addAll(labelAddressBar, textAddressBar, buttonGo);

		//ItachiUchiha. (2015). JavaFX Resizing TextField with Window [WebPage]. Retrieved from
		//https://stackoverflow.com/a/31018911/911668
		HBox.setHgrow(textAddressBar, Priority.ALWAYS);

		return addressBarPane;
	}

	@Override
	public void stop() {
		Menus.saveBookmarksInFile();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
