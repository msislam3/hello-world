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
	private AnimationTimer animator;
	
	private Text powerUsageText = new Text("0");
	private Text targetFloorText = new Text("0");
	private Text currentFloorText = new Text("0");
	
	private Label powerUsageLabel = new Label("Power Usage");
	private Label targetFloorLabel = new Label("Target Floor");
	private Label currentFloorLabel = new Label("Current Floor");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane infoPane = new GridPane();
		infoPane.setHgap(2);
		infoPane.setMinWidth(150);
		
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
		
		Scene scene = new Scene(rootPane, 640, 640);
		scene.getStylesheets().add(ElevatorApplication.class.getResource("elevator.css").toExternalForm());
		primaryStage.setTitle("Elevator Simulation");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		animator.start();
		Simulator simulator = new Simulator(this);
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
		animator = new AnimationTimer();
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof ElevatorImp) {
			if(arg instanceof List) {
				animator.setData((List<Integer>)arg);
			}
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	private class AnimationTimer extends javafx.animation.AnimationTimer{

		private static final long SECOND = 1000000001;
		private static final long NORMAL_SPEED = SECOND/7;
		private static final long SLOW_SPEED = SECOND/3; 
		
		private boolean newTurn = true;
		private int targetFloor;
		private int currentFloor=0;
		private int powerUsage=0;
		private long prevFrame;
		private long timeCheck;
		
		private Queue<List<Integer>> queue = new LinkedList<>();
		
		public void setData(List<Integer> data) {
			System.out.println(data.get(0) +" "+ data.get(1) +" "+data.get(2));
			queue.add(data);
		}
		
		@Override
		public void handle(long now) {
			if(now - prevFrame < timeCheck) {
				return;
			}		
			prevFrame = now;
			
			if(newTurn) {				
				List<Integer> data = queue.poll();
				if(data == null) return;
				
				floors[currentFloor].setId(EMPTY);
				
				currentFloor = data.get(0);
				targetFloor = data.get(1);
				powerUsage = data.get(2);
				
				currentFloorText.setText(currentFloor+"");
				powerUsageText.setText(powerUsage+"");	
				targetFloorText.setText(targetFloor+"");
							
				floors[targetFloor].setId(TARGET);
				floors[currentFloor].setId(ELEVATOR);
				timeCheck = SLOW_SPEED;
				newTurn = false;
			}else if(currentFloor==targetFloor) {
				newTurn = true;
			}else {
				floors[currentFloor].setId(EMPTY);
				
				List<Integer> data = queue.poll();
				if(data == null) return;
				
				currentFloor = data.get(0);
				powerUsage = data.get(2);
				
				currentFloorText.setText(currentFloor+"");
				powerUsageText.setText(powerUsage+"");			
				
				floors[currentFloor].setId(ELEVATOR);
				timeCheck = Math.abs(currentFloor-targetFloor) == 1 ? SLOW_SPEED : NORMAL_SPEED;
			}		
		}		
	}
}
