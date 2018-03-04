package elevator;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import elevatorsystem.ElevatorPanel;

/**
 * Class to represent the Elevator. 
 * <p>
 * Extends {@link Observable}, Implements {@link Elevator}
 * 
 * @author Rifat Shams
 * @version 1.0.0.0
 * @since February 15, 2018
 */
public class ElevatorImp extends Observable implements Elevator{

	/**
	 * Power consumed during start and stop 
	 */
	private static final int POWER_START_STOP = 2;
	
	/**
	 * Power consumed during continuous traveling
	 */
	private static final int POWER_CONTINUOUS = 1;
	
	private static final long SLEEP_START_STOP = 500;
	
	private static final long SLEEP_CONTINUOUS = 250;
	
	/**
	 * Maximum capacity of the Elevator
	 */
	private final double MAX_CAPACITY_PERSONS;
	
	private final boolean delay;
	
	private final int ID; 
	
	/**
	 * Total power used by the Elevator
	 */
	private double powerUsed;
	
	/**
	 * Current floor of the Elevator
	 */
	private int currentFloor;
	
	/**
	 * Current capacity of the Elevator
	 */
	private int capacity;
	
	/**
	 * The panel of the Elevator System
	 */
	private ElevatorPanel panel;
	
	/**
	 * Current state of the Elevator
	 */
	private MovingState state;
	
	public ElevatorImp(double CAPACITY_PERSONS, ElevatorPanel panel, int ID, boolean delay) {
		MAX_CAPACITY_PERSONS = CAPACITY_PERSONS;
		this.capacity = (int) CAPACITY_PERSONS;
		this.panel = panel;
		this.currentFloor = 0;
		this.state = MovingState.Idle;
		this.ID = ID;
		this.delay = delay;
	}
	
	/**
	 * Construct an ElevatorImp with specified capacity which uses a specific {@link ElevatorPanel}
	 * @param CAPACITY_PERSONS - The capacity of the elevator
	 * @param panel - The {@link ElevatorPanel} used by the elevator
	 */
	public ElevatorImp(double CAPACITY_PERSONS, ElevatorPanel panel, int ID) {
		this(CAPACITY_PERSONS, panel, ID, false);
	}

	/**
	 * Move {@link Elevator} to a specific floor
	 * @param floor - target floor
	 */
	@Override
	public void moveTo(int targetFloor) {
		if(targetFloor < 0 ) throw new IllegalArgumentException("Floor cannot be negative");
		
		while(currentFloor != targetFloor) {
			if(state == MovingState.Idle) {
				//Change state based on direction. Elevator transition from Idle -> SlowDown or Idle -> SlowUp
				MovingState newState = targetFloor - currentFloor < 0 ? MovingState.SlowDown: MovingState.SlowUp;
				setState(newState);
			}else if(state == MovingState.SlowUp || state == MovingState.SlowDown || state == MovingState.Up || state == MovingState.Down) {
				processMovingState(targetFloor);
			}else {
				throw new UnsupportedOperationException("Elevator in an invalid state: "+state.toString());
			}
			
			//notify changes to observer about the data changes
			setChanged();
			List<Number> data = Arrays.asList(ID, currentFloor, targetFloor, powerUsed);
			notifyObservers(data);
		}
		
		//Reached destination so go to idle state
		setState(MovingState.Idle);
	}
	
	/**
	 * Method to update current floor, power used and state of a moving elevator
	 * @param targetFloor
	 */
	private void processMovingState(int targetFloor) {
		//Going up or down? 
		int step = targetFloor - currentFloor < 0 ? -1: 1;		
		currentFloor+= step;
		
		//Got into new floor should calculate power
		calculatePowerUsed();
		
		//Change state based on position. If the target floor is just one floor away we should go to slow state or else we will go to normal state  
		int diff = Math.abs(currentFloor-targetFloor);
		MovingState newState = diff <= 1 ? state.slow() : state.normal();	
		System.out.println("Old State: "+state+" New State: "+newState + " Current Floor: "+currentFloor+" Target Floor: "+targetFloor);
		setState(newState);
	}

	/**
	 * Set state of {@link Elevator}
	 * @param state - new state
	 */
	private void setState(MovingState state) {
		this.state = state;
		if(delay && !state.isIdle() && !state.isOff()) {
			long sleepDuration = state.isGoingSlow() ? SLEEP_START_STOP : SLEEP_CONTINUOUS; 
			try {
				Thread.sleep(sleepDuration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * add number of persons to {@link Elevator}
	 * @param persons - number of passengers getting on at current floor
	 */
	@Override
	public void addPersons(int persons) {
		if(persons < 1 ) throw new IllegalArgumentException("Persons cannot be less than one");
		if(capacity < persons) throw new IllegalArgumentException("Elevator does not have enough capacity to add these persons");
		
		//decrease the capacity
		capacity = capacity - persons;
	}

	/**
	 * represent the request made by one passenger inside of {@link Elevator}
	 * @param floor - target floor
	 */
	@Override
	public void requestStop(int floor) {
		if(floor < 0 ) throw new IllegalArgumentException("Floor cannot be negative");
		
		//move from current floor to requested floor. Calculate power consumed
		panel.requestStop(this, floor);
	}

	@Override
	public void requestStops(int... floors) {
		if(floors == null || floors.length == 0) throw new IllegalArgumentException();
		panel.requestStops(this, floors);
	}
	
	/**
	 * get current capacity of the elevator.
	 * @return integer for total capacity currently in the {@link Elevator}
	 */
	@Override
	public int getCapacity() {
		return capacity;
	}

	/**
	 * get current floor of {@link Elevator} at this point
	 * @return current floor
	 */
	@Override
	public int getFloor() {
		return currentFloor;
	}

	/**
	 * return total amount of power consumed to this point
	 * @return power consumed
	 */
	@Override
	public double getPowerConsumed() {
		return powerUsed;
	}

	/**
	 * get current {@link MovingState} of the {@link Elevator}
	 * @return current {@link MovingState}
	 */
	@Override
	public MovingState getState() {
		return state;
	}

	/**
	 * check if capacity has reached its maximum
	 * @return if {@link Elevator} is full
	 */
	@Override
	public boolean isFull() {
		return capacity == 0;
	}

	/**
	 * check if capacity is zero
	 * @return if {@link Elevator} is empty
	 */
	@Override
	public boolean isEmpty() {
		return capacity == MAX_CAPACITY_PERSONS;
	}
	
	/**
	 * calculate the power used by the Elevator during movement
	 */
	private void calculatePowerUsed() {
		if(state.isGoingSlow()) {
			powerUsed += POWER_START_STOP;
		}else if(state.isGoingNormal()) {
			powerUsed += POWER_CONTINUOUS;
		}
	}

	@Override
	public boolean isIdle() {
		return state.isIdle();
	}

	@Override
	public int id() {
		return ID;
	}

	@Override
	public int hashCode() {
		return ID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ElevatorImp) {
			ElevatorImp elevator = (ElevatorImp)obj;
			return ID == elevator.id();
		}else {
			return false;
		}
	}
}
