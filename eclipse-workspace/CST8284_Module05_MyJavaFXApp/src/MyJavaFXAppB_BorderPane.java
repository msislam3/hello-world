import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Remember: you must first use 'Build Path' to allow Eclipse to 'see' the 
// JavaFX library.  This was covered in lab 1, when you displayed the fractal.

public class MyJavaFXAppB_BorderPane extends Application {
	@Override
	public void start(Stage primaryStage) {
	
		// Create text for centerPane
		StackPane centerPane = new StackPane();
		Text text = new Text("JavaFX Programming");
		centerPane.getChildren().add(text);

		// Add buttons to bottomPane
		Button btLeft = new Button("Left");
		Button btRight = new Button("Right");
		HBox bottomPane = new HBox(20);
		bottomPane.getChildren().addAll(btLeft, btRight);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setStyle("-fx-border-color: green");
		
		// Add centerPane and bottomPane to BorderPane
		BorderPane rootPane = new BorderPane();
		rootPane.setCenter(centerPane); // load centre pane
		rootPane.setBottom(bottomPane); // load bottom pane

		// Load the BorderPane into the Scene, and the Scene into the Stage
		Scene scene = new Scene(rootPane, 450, 200);
		primaryStage.setTitle("JavaFX Demo Using BorderPanes");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
