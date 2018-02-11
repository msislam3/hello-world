package assignment.application;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import assignment1.elevator.ElevatorImp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ElevatorApplication extends Application implements Observer {
	private static final String TARGET = "target";
	private static final String EMPTY = "empty";
	private static final String ELEVATOR = "elevator";
	private static final int FLOOR_COUNT = 21;

	private Label[] floors;
	private ElevatorAnimator animator;
	private Simulator simulator;
	
	private Text powerUsageText = new Text("0");
	private Text targetFloorText = new Text("0");
	private Text currentFloorText = new Text("0");
	
	private Label powerUsageLabel = new Label("Power Usage");
	private Label targetFloorLabel = new Label("Target Floor");
	private Label currentFloorLabel = new Label("Current Floor");
	
	//https://docs.oracle.com/javafx/2/layout/style_css.htm
	//https://docs.oracle.com/javafx/2/get_started/css.htm
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane infoPane = new GridPane();
		infoPane.setMinWidth(150);
		infoPane.getStyleClass().add("grid");
		
		powerUsageText.setId("info-text");
		targetFloorText.setId("info-text");
		currentFloorText.setId("info-text");
		
		infoPane.add(powerUsageLabel, 0, 0);
		infoPane.add(targetFloorLabel, 0, 1);
		infoPane.add(currentFloorLabel, 0, 2);
		
		infoPane.add(powerUsageText, 1, 0);
		infoPane.add(targetFloorText, 1, 1);
		infoPane.add(currentFloorText, 1, 2);
		
		GridPane floorPane = new GridPane();
		for(int i = 0; i < FLOOR_COUNT; i++) {
			floorPane.add(floors[i], 0, FLOOR_COUNT-i);
		}
		
		BorderPane rootPane = new BorderPane();
		rootPane.setCenter(floorPane); 
		rootPane.setLeft(infoPane);
		
		Scene scene = new Scene(rootPane, 350, 600);
		scene.getStylesheets().add(ElevatorApplication.class.getResource("elevator.css").toExternalForm());
		primaryStage.setTitle("Elevator Simulation");
		primaryStage.setScene(scene);
		
		animator.start();
		primaryStage.show();
		simulator.start();
	}
	
	@Override
	public void init() throws Exception {
		super.init();		
		floors = new Label[FLOOR_COUNT];
		for(int i = 0; i<FLOOR_COUNT;i++) {
			floors[i] = new Label();
			floors[i].setId("empty");
		}	
		
		animator = new ElevatorAnimator();
		simulator = new Simulator(this);
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof ElevatorImp) {
			if(arg instanceof List) {
				animator.addData((List<Number>)arg);
			}
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private class ElevatorAnimator extends javafx.animation.AnimationTimer{

		private static final long SECOND = 1000000001;
		private static final long NORMAL_SPEED = SECOND/7;
		private static final long SLOW_SPEED = SECOND/3; 
		
		private int targetFloor;
		private int currentFloor;
		private double powerUsage;
		private long prevFrame;
		private long timeCheck;
		
		private Queue<List<Number>> queue = new LinkedList<>();
		
		public void addData(List<Number> data) {
			System.out.println(data.get(0) +" "+ data.get(1) +" "+data.get(2));
			queue.add(data);
		}
		
		@Override
		public void handle(long now) {
			if(now - prevFrame < timeCheck) {
				return;
			}		
			prevFrame = now;
			
			floors[currentFloor].setId(EMPTY);
			
			List<Number> data = queue.poll();
			if(data == null) return;
			
			currentFloor = data.get(0).intValue();
			targetFloor = data.get(1).intValue();
			powerUsage = data.get(2).doubleValue();
			
			floors[targetFloor].setId(TARGET);
			floors[currentFloor].setId(ELEVATOR);
			
			currentFloorText.setText(Integer.toString(currentFloor));
			targetFloorText.setText(Integer.toString(targetFloor));
			powerUsageText.setText(Double.toString(powerUsage));	
			
			timeCheck = Math.abs(currentFloor-targetFloor) == 1 ? SLOW_SPEED : NORMAL_SPEED;
		}		
	}
}
