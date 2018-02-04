package assignment1.elevatorsystem;

import assignment1.elevator.Elevator;

/**
 * <p>
 * this interface is implemented by {@link ElevatorSystem}.
 * it is used by {@link Elevator} to call back to {@link ElevatorSystem}
 * regarding the the floor to which {@link Elevator} intends to travel.
 * </p>
 * 
 * @author MSI
 */
public interface ElevatorPanel {
	/**
	 * called from {@link Elevator} to inform {@link ElevatorSystem} of new floor.
	 * @param floor - new floor to which {@link Elevator} is moving.
	 * @param elevator - reference to the calling elevator.
	 */
	void requestStop(int floor, Elevator elevator);
}
