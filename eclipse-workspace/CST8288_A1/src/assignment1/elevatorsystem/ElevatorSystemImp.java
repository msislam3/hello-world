package assignment1.elevatorsystem;

import assignment1.elevator.Elevator;
import assignment1.elevator.MovingState;

/**
 * Class to represent the Elevator System. 
 * <p>
 * Implements {@link ElevatorSystem} {@link ElevatorPanel}
 * 
 * @author Rifat Shams
 * @version 1.0.0.0
 * @since February 15, 2018
 */
public class ElevatorSystemImp implements ElevatorSystem, ElevatorPanel{

	/**
	 * maximum floor for the elevator system
	 */
	private final int MAX_FLOOR;
	
	/**
	 * minimum floor for the elevator system
	 */
	private final int MIN_FLOOR;
	
	/**
	 * elevator included in the elevator system
	 */
	private Elevator elevator;
	
	/**
	 * Construct an ElevatorSystemImp with minimum and maximum floor
	 * @param MIN_FLOOR - The minimum floor for the elevator system
	 * @param MAX_FLOOR - The maximum floor for the elevator system
	 */
	public ElevatorSystemImp(int MIN_FLOOR, int MAX_FLOOR) {
		this.MAX_FLOOR = MAX_FLOOR;
		this.MIN_FLOOR = MIN_FLOOR;
	}
	
	/**
	 * called from {@link Elevator} to inform {@link ElevatorSystem} of new floor.
	 * @param floor - new floor to which {@link Elevator} is moving.
	 * @param elevator - reference to the calling elevator.
	 * @throws IllegalArgumentException when floor is less than MIN_FLOOR
	 * @throws IllegalArgumentException when floor is higher than MAX_FLOOR
	 * @throws IllegalArgumentException when elevator is null
	 */
	@Override
	public void requestStop(int floor, Elevator elevator) {
		if(floor < MIN_FLOOR ) throw new IllegalArgumentException("Cannot go below the minimum floor");
		if(floor > MAX_FLOOR ) throw new IllegalArgumentException("Cannot go higher than maximum floor");
		if(elevator == null) throw new IllegalArgumentException("elevator cannot be null");
		
		elevator.moveTo(floor);
	}

	/**
	 * when calling up it means the passenger intends to travel to a higher floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 * @throws IllegalArgumentException when floor is less than MIN_FLOOR
	 * @throws IllegalArgumentException when floor is higher than MAX_FLOOR
	 */
	@Override
	public Elevator callUp(int floor) {
		if(floor < MIN_FLOOR ) throw new IllegalArgumentException("Cannot go below the minimum floor");
		if(floor > MAX_FLOOR ) throw new IllegalArgumentException("Cannot go higher than maximum floor");
		
		//move the elevator to the requested floor
		elevator.moveTo(floor);
		return elevator;
	}

	/**
	 * when calling down it means the passenger intends to travel to a lower floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reach the requested floor
	 * @throws IllegalArgumentException when floor is less than MIN_FLOOR
	 * @throws IllegalArgumentException when floor is higher than MAX_FLOOR
	 */
	@Override
	public Elevator callDown(int floor) {
		if(floor < MIN_FLOOR ) throw new IllegalArgumentException("Cannot go below the minimum floor");
		if(floor > MAX_FLOOR ) throw new IllegalArgumentException("Cannot go higher than maximum floor");
		
		//move the elevator to the requested floor
		elevator.moveTo(floor);
		return elevator;
	}

	/**
	 * add an {@link Elevator} to {@link ElevatorSystem}, if implemented multiple {@link Elevator} can be added
	 * @param elevator - {@link Elevator} object to be added to {@link ElevatorSystem}
	 * @throws IllegalArgumentException when elevator is null
	 */
	@Override
	public void addElevator(Elevator elevator) {
		if(elevator == null) throw new IllegalArgumentException();
		this.elevator = elevator;
	}

	/**
	 * return current floor of {@link Elevator} in {@link ElevatorSystem}.
	 * @return current floor of only {@link Elevator}
	 */
	@Override
	public int getCurrentFloor() {
		return elevator.getFloor();
	}

	/**
	 * get total floors to which {@link ElevatorSystem} can send an {@link Elevator}.
	 * @return total floors
	 */
	@Override
	public int getFloorCount() {
		return MAX_FLOOR - MIN_FLOOR + 1;
	}
	
	/**
	 * get maximum floor for this {@link ElevatorSystem}
	 * @return maximum floor for this {@link ElevatorSystem}
	 */
	@Override
	public int getMaxFloor() {
		return MAX_FLOOR;
	}

	/**
	 * get minimum floor for this {@link ElevatorSystem}
	 * @return minimum floor for this {@link ElevatorSystem}
	 */
	@Override
	public int getMinFloor() {
		return MIN_FLOOR;
	}

	/**
	 * return total power consumed by all {@link Elevator} in the {@link ElevatorSystem}
	 * @return total power consumed
	 */
	@Override
	public double getPowerConsumed() {
		return elevator.getPowerConsumed();
	}
	
	private void floorCheck(int floor) {
		
	}
	
	private Elevator call(int floor, MovingState direction) {
		return null;
	}
	
	private boolean checkForElevator() {
		return false;
	}
}
