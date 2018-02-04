package assignment1.elevatorsystem;

import assignment1.elevator.Elevator;

/**
 * @author MSI
 */
public interface ElevatorSystem {
	/**
	 * when calling up it means the passenger intends to travel to a higher floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reached the requested floor
	 */
	Elevator callUp(int floor);
	
	/**
	 * when calling down it means the passenger intends to travel to a lower floor.
	 * @param floor - passengers current floor when calling for an {@link Elevator}
	 * @return an {@link ElevatorSystem} that has reached the requested floor
	 */
	Elevator callDown(int floor);
	
	/**
	 * add an {@link Elevator} to {@link ElevatorSystem}, if implemented multiple {@link Elevator} can be added
	 * @param elevator - {@link Elevator} object to be added to {@link ElevatorSystem}
	 */
	void addElevator(Elevator elevator);
	
	/**
	 * return current floor of {@link Elevator} in {@link ElevatorSystem}.
	 * @return current floor of only {@link Elevator}
	 */
	int getCurrentFloor();
	
	/**
	 * get total floors to which {@link ElevatorSystem} can send an {@link Elevator}.
	 * @return total floors
	 */
	int getFloorCount();
	
	/**
	 * get maximum floor for this {@link ElevatorSystem}
	 * @return maximum floor for this {@link ElevatorSystem}
	 */
	int getMaxFloor();
	
	/**
	 * get minimum floor for this {@link ElevatorSystem}
	 * @return minimum floor for this {@link ElevatorSystem}
	 */
	int getMinFloor();
	
	/**
	 * return total power consumed by all {@link Elevator} in the {@link ElevatorSystem}
	 * @return total power consumed
	 */
	double getPowerConsumed();
}
