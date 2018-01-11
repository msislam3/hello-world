import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Remember: you must first use 'Build Path' to allow Eclipse to 'see' the 
// JavaFX library.  This was covered in lab 1, when you displayed the fractal.

public class MyJavaFXAppA_SimpleButton extends Application{
   @Override
   public void start(Stage primaryStage){
      StackPane pane = new StackPane();
      pane.getChildren().add(new Button("OK"));
      Scene scene =  new Scene(pane, 200, 50);
      primaryStage.setScene(scene);
      primaryStage.show();	   
   }
   
   public static void main(String[] args){
	      Application.launch(args);
   }

}
