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

/**
 * The GUI for the elevator simulator
 * <p>
 * The interface shows Current Floor, Target Floor and Total Power Used by the {@link Elevator}. 
 * Also shows an animation for the movement of the {@link Elevator}
 * <p>
 * Extends {@link Application}, Implements {@link Observer} 
 * 
 * @author Rifat Shams
 * @version 1.0.0.0
 */
public class ElevatorApplication extends Application implements Observer {
	private static final String TARGET = "target";
	private static final String EMPTY = "empty";
	private static final String ELEVATOR = "elevator";
	
	/**
	 * Number of floors in the system
	 */
	private static final int FLOOR_COUNT = 21;

	/**
	 * Array of labels representing the floors
	 */
	private Label[] floors;
	
	/**
	 * The {@link ElevatorAnimator} used for animation
	 */
	private ElevatorAnimator animator;
	
	/**
	 * The {@link Simulator} that runs the simulation
	 */
	private Simulator simulator;
	
	/**
	 * {@link Text} to show the total power usage
	 */
	private Text powerUsageText = new Text("0");
	
	/**
	 * {@link Text} to show the target floor
	 */
	private Text targetFloorText = new Text("0");
	
	/**
	 * {@link Text} to show the current floor
	 */
	private Text currentFloorText = new Text("0");
	
	/**
	 * {@link Label} for power usage
	 */
	private Label powerUsageLabel = new Label("Power Usage");
	
	/**
	 * {@link Label} for target floor
	 */
	private Label targetFloorLabel = new Label("Target Floor");
	
	/**
	 * {@link Label} for current floor
	 */
	private Label currentFloorLabel = new Label("Current Floor");
	
	//https://docs.oracle.com/javafx/2/layout/style_css.htm
	//https://docs.oracle.com/javafx/2/get_started/css.htm
	
	/**
	 * Builds the GUI of the simulator application and show it to the user
	 */
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
	
	/**
	 * Initializes the components of the application
	 */
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

	/**
	 * Receives the data sent by the {@link Elevator}
	 */
	@Override
	public void update(Observable observable, Object arg) {
		if(observable instanceof ElevatorImp) {
			if(arg instanceof List) {
				animator.addData((List<Number>)arg);
			}
		}
	}

	/**
	 * Main method of the application
	 * @param args - arguments for the application
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	/**
	 * Class used to change the GUI periodically for the simulation
	 * <p>
	 * Extends {@link AnimationTimer}
	 * 
	 * @author Rifat Shams
	 * @version 1.0.0.0
	 *
	 */
	private class ElevatorAnimator extends javafx.animation.AnimationTimer{

		/**
		 * Constant representing one second
		 */
		private static final long SECOND = 1000000001;
		
		/**
		 * Constant used for normal animation speed
		 */
		private static final long NORMAL_SPEED = SECOND/7;
		
		/**
		 * Constant used for slow animation speed
		 */
		private static final long SLOW_SPEED = SECOND/3; 
		
		/**
		 * Target floor of the elevator
		 */
		private int targetFloor;
		
		/**
		 * Current floor of the elevator
		 */
		private int currentFloor;
		
		/**
		 * Power used by the elevator
		 */
		private double powerUsage;
		
		/**
		 * Time when the GUI was last updated
		 */
		private long prevFrame;
		
		/**
		 * Time interval for GUI update
		 */
		private long timeCheck;
		
		/**
		 * Queue to hold the GUI update data
		 */
		private Queue<List<Number>> queue = new LinkedList<>();
		
		/**
		 * Adds GUI update data to the queue
		 * 
		 * @param data - List containing current floor, target floor and power used
		 * @throws IllegalArgumentException when data passed is null
		 */
		public void addData(List<Number> data) {
			if(data == null) throw new IllegalArgumentException("Data cannot be null");
			
			System.out.println(data.get(0) +" "+ data.get(1) +" "+data.get(2));
			queue.add(data);
		}
		
		/**
		 * Updates the GUI using data received from the {@link Elevator} periodically
		 * <p>
		 * The speed of the update is controlled by timeCheck
		 */
		@Override
		public void handle(long now) {
			//If enough time has not passed yet since last update return
			if(now - prevFrame < timeCheck) {
				return;
			}		
			prevFrame = now;
		
			//Get the data from the queue for the next update and update the GUI
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
