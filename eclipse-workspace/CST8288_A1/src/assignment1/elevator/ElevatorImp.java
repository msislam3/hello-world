package assignment1.elevator;

import java.util.ArrayList;
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
	}

	/**
	 * move {@link Elevator} to a specific floor
	 * @param floor - target floor
	 */
	@Override
	public void moveTo(int targetFloor) {
		if(targetFloor < 0 ) throw new IllegalArgumentException("Floor cannot be negative");
		
		//move from current floor to requested floor. Calculate power consumed		
		int step = targetFloor - currentFloor < 0 ? -1: 1;
		state = targetFloor - currentFloor < 0 ? MovingState.SlowDown: MovingState.SlowUp;
		
		while(currentFloor != targetFloor) {			
			currentFloor+= step;
			calculatePowerUsed();
			
			setChanged();
			List<Integer> data = new ArrayList<>();
			data.add(currentFloor);
			data.add(targetFloor);
			data.add((int)powerUsed);
			notifyObservers(data);
		}
	}

	/**
	 * add number of persons to {@link Elevator}
	 * @param persons - number of passengers getting on at current floor
	 */
	@Override
	public void addPersons(int persons) {
		if(persons < 1 ) throw new IllegalArgumentException("Persons cannot be less than one");
		
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
		powerUsed += 2*POWER_START_STOP;
	}
}
