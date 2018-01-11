import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Remember: you must first use 'Build Path' to allow Eclipse to 'see' the 
// JavaFX library.  This was covered in lab 1, when you displayed the fractal.

public class MyJavaFXAppC_GridPane extends Application {
	public void start(Stage pStage) {

		// Define and set the defaults for the three TextFields
		TextField tfName = new TextField();
		tfName.setMaxWidth(120);
		TextField tfPhone = new TextField();
		tfPhone.setMaxWidth(120);
		TextField tfEmail = new TextField();
		tfEmail.setMaxWidth(120);

		// Define and load the three Labels
		Label lblName = new Label("Enter your name:");
		Label lblPhone = new Label("Enter your phone #:");
		Label lblEmail = new Label("Enter your email:");

		// Define the GridPane and set the internal spacing between nodes
		GridPane rootNode = new GridPane();
		rootNode.setPadding(new Insets(10, 10, 10, 10));
		rootNode.setVgap(10);
		rootNode.setHgap(20);

		// Load the Labels and TextFields into the GridPane
		rootNode.add(lblName, 0, 0);
		rootNode.add(lblPhone, 0, 1);
		rootNode.add(lblEmail, 0, 2);

		rootNode.add(tfName, 1, 0);
		rootNode.add(tfPhone, 1, 1);
		rootNode.add(tfEmail, 1, 2);

		// Load the GridPane into the Scene, and the Scene into the Stage
		Scene myScene = new Scene(rootNode, 300, 140);
		pStage.setScene(myScene);
		pStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
