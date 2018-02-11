package assignment1.elevator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import assignment1.elevatorsystem.ElevatorPanel;

public class ElevatorImp extends Observable implements Elevator{

	private static final int POWER_START_STOP = 2;
	private static final int POWER_CONTINUOUS = 1;
	private static final long SLEEP_START_STOP = 50;
	private static final long SLEEP_CONTINUOUS = 25;
	
	private final double MAX_CAPACITY_PERSONS;
	
	private double powerUsed;
	private int currentFloor;
	private double capacity;
	private ElevatorPanel panel;
	private MovingState state;
	
	public ElevatorImp(double CAPACITY_PERSONS, ElevatorPanel panel) {
		MAX_CAPACITY_PERSONS = CAPACITY_PERSONS;
		this.capacity = CAPACITY_PERSONS;
		this.panel = panel;
		this.currentFloor = 0;
		this.state = MovingState.Idle;
	}

	/**
	 * move {@link Elevator} to a specific floor
	 * @param floor - target floor
	 */
	@Override
	public void moveTo(int targetFloor) {
		if(targetFloor < 0 ) throw new IllegalArgumentException("Floor cannot be negative");
		
		while(currentFloor != targetFloor) {
			if(state == MovingState.Idle) {
				//Change state based on direction. Elevator transition from Idle -> SlowDown or Idle -> SlowUp
				MovingState newState = targetFloor - currentFloor < 0 ? MovingState.SlowDown: MovingState.SlowUp;
				System.out.println("Old State: "+state+" New State: "+newState + " Current Floor: "+currentFloor+" Target Floor: "+targetFloor);
				setState(newState);
			}else if(state == MovingState.SlowUp || state == MovingState.SlowDown || state == MovingState.Up || state == MovingState.Down) {
				processMovingState(targetFloor);
			}else {
				throw new UnsupportedOperationException("Elevator in an invalid state: "+state.toString());
			}
			
			//notify changes to observer about the data changes
			setChanged();
			List<Number> data = Arrays.asList(currentFloor, targetFloor, powerUsed);
			notifyObservers(data);
		}
		
		//Reached destination so go to idle state
		setState(MovingState.Idle);
	}
	
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

	private void setState(MovingState state) {
		this.state = state;
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
		panel.requestStop(floor, this);
		moveTo(floor);
	}

	/**
	 * get current capacity of the elevator.
	 * @return integer for total capacity currently in the {@link Elevator}
	 */
	@Override
	public double getCapacity() {
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
	
	private void calculatePowerUsed() {
		if(state.isGoingSlow()) {
			powerUsed += POWER_START_STOP;
		}else if(state.isGoingNormal()) {
			powerUsed += POWER_CONTINUOUS;
		}
	}
}
