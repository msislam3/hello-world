package simulator;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import elevator.ElevatorImp;
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
	 * Array of labels representing the floors
	 */
	private Label[][] floors;
	
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
	private Text[] powerUsageText;
	
	/**
	 * {@link Text} to show the target floor
	 */
	private Text[] targetFloorText;
	
	/**
	 * {@link Text} to show the current floor
	 */
	private Text[] currentFloorText;
	
	/**
	 * {@link Label} for power usage
	 */
	private Label[] powerUsageLabel;
	
	/**
	 * {@link Label} for target floor
	 */
	private Label[] targetFloorLabel;
	
	/**
	 * {@link Label} for current floor
	 */
	private Label[] currentFloorLabel;
	
	private Label[] elevatorNumberLabel;
	
	//https://docs.oracle.com/javafx/2/layout/style_css.htm
	//https://docs.oracle.com/javafx/2/get_started/css.htm
	
	/**
	 * Builds the GUI of the simulator application and show it to the user
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane rootPane = new GridPane();
		
		for (int i = 0; i < simulator.getElevatorCount(); i++) {

			GridPane infoPane = new GridPane();
			infoPane.setMinWidth(150);
			infoPane.getStyleClass().add("grid");
			
			powerUsageText[i].setId("info-text");
			targetFloorText[i].setId("info-text");
			currentFloorText[i].setId("info-text");
			
			infoPane.add(elevatorNumberLabel[i], 0, 0);
			
			infoPane.add(powerUsageLabel[i], 0, 1);
			infoPane.add(targetFloorLabel[i], 0, 2);
			infoPane.add(currentFloorLabel[i], 0, 3);
			
			infoPane.add(powerUsageText[i], 1, 1);
			infoPane.add(targetFloorText[i], 1, 2);
			infoPane.add(currentFloorText[i], 1, 3);
			
			GridPane floorPane = new GridPane();
			for (int j = 0; j < simulator.getFloorCount(); j++) {
				floorPane.add(floors[i][j], 0, simulator.getFloorCount() - j);
			}
			
			BorderPane elevatorPane = new BorderPane();
			elevatorPane.setCenter(floorPane);
			elevatorPane.setLeft(infoPane);
			
			rootPane.add(elevatorPane, i, 0);
		}
		
		Scene scene = new Scene(rootPane, 1600, 600);
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
	
		simulator = new Simulator(this);
		animator = new ElevatorAnimator();
		
		floors = new Label[simulator.getElevatorCount()][];
		for (int i = 0; i < floors.length; i++) {
			floors[i] = new Label[simulator.getFloorCount()];
			for(int j = 0; j<simulator.getFloorCount();j++) {
				floors[i][j] = new Label();
				floors[i][j].setId("empty");
			}
		}
		
		elevatorNumberLabel = new Label[simulator.getElevatorCount()];
		for (int i = 0; i < elevatorNumberLabel.length; i++) {
			elevatorNumberLabel[i] = new Label("Elevator: "+i);
		}
		
		currentFloorLabel = new Label[simulator.getElevatorCount()];
		for (int i = 0; i < currentFloorLabel.length; i++) {
			currentFloorLabel[i] = new Label("Current Floor");
		}
		
		targetFloorLabel = new Label[simulator.getElevatorCount()];
		for (int i = 0; i < targetFloorLabel.length; i++) {
			targetFloorLabel[i] = new Label("Target Floor");
		}
		
		powerUsageLabel = new Label[simulator.getElevatorCount()];
		for (int i = 0; i < powerUsageLabel.length; i++) {
			powerUsageLabel[i] = new Label("Power Usage");
		}
		
		currentFloorText = new Text[simulator.getElevatorCount()];
		for (int i = 0; i < currentFloorText.length; i++) {
			currentFloorText[i] = new Text("0");
		}
		
		targetFloorText = new Text[simulator.getElevatorCount()];
		for (int i = 0; i < targetFloorText.length; i++) {
			targetFloorText[i] = new Text("0");
		}
		
		powerUsageText = new Text[simulator.getElevatorCount()];
		for (int i = 0; i < powerUsageText.length; i++) {
			powerUsageText[i] = new Text("0");
		}
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
		 * Current floor of the elevator
		 */
		private int[] currentFloors;
		
		
		/**
		 * Queue to hold the GUI update data
		 */
		private Queue<List<Number>> queue = new LinkedList<>();
		
		public ElevatorAnimator() {
			currentFloors = new int[simulator.getElevatorCount()];
		}
		
		/**
		 * Adds GUI update data to the queue
		 * 
		 * @param data - List containing current floor, target floor and power used
		 * @throws IllegalArgumentException when data passed is null
		 */
		public void addData(List<Number> data) {
			if(data == null) throw new IllegalArgumentException("Data cannot be null");
			
			//System.out.println(data.get(0) +" "+ data.get(1) +" "+data.get(2));
			queue.add(data);
		}
		
		/**
		 * Updates the GUI using data received from the {@link Elevator} periodically
		 */
		@Override
		public void handle(long now) {
			//If queue is empty
			if(queue.isEmpty()) {
				return;
			}		
		
			//Get the data from the queue for the next update and update the GUI		
			List<Number> data = queue.poll();
			//a thread getting queue.isEmpty() false in line 254 can stil get null data if another thread have already polled the last data
			//from the queue before it grabing the data
			if(data != null) {
				int id = data.get(0).intValue();
				
				floors[id][currentFloors[id]].setId(EMPTY);
				
				int targetFloor = data.get(2).intValue();
				double powerUsage = data.get(3).doubleValue();
				currentFloors[id] = data.get(1).intValue();
				
				
				floors[id][targetFloor].setId(TARGET);
				floors[id][currentFloors[id]].setId(ELEVATOR);
				
				currentFloorText[id].setText(Integer.toString(currentFloors[id]));
				targetFloorText[id].setText(Integer.toString(targetFloor));
				powerUsageText[id].setText(Double.toString(powerUsage));	
			}	
		}		
	}
}
