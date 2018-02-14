package assignment1.elevator;

/**
 * 
 * @author Rifat Shams
 */
public interface Elevator {
	/**
	 * move {@link Elevator} to a specific floor
	 * @param floor - target floor
	 */
	void moveTo(int floor);
	
	/**
	 * add number of persons to {@link Elevator}
	 * @param persons - number of passengers getting on at current floor
	 */
	void addPersons(int persons);
	
	/**
	 * represent the request made by one passenger inside of {@link Elevator}
	 * @param floor - target floor
	 */
	void requestStop(int floor);
	
	/**
	 * get current capacity of the elevator.
	 * @return integer for total capacity currently in the {@link Elevator}
	 */
	double getCapacity();
	
	/**
	 * get current floor of {@link Elevator} at this point
	 * @return current floor
	 */
	int getFloor();
	
	/**
	 * return total amount of power consumed to this point
	 * @return power consumed
	 */
	double getPowerConsumed();
	
	/**
	 * get current {@link MovingState} of the {@link Elevator}
	 * @return current {@link MovingState}
	 */
	MovingState getState();
	
	/**
	 * check if capacity has reached its maximum
	 * @return if {@link Elevator} is full
	 */
	boolean isFull();
	
	/**
	 * check if capacity is zero
	 * @return if {@link Elevator} is empty
	 */
	boolean isEmpty();
}
