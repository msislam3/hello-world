package elevatorsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import elevator.Elevator;
import elevator.ElevatorImp;
import elevator.MovingState;

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
	 * object used for synchronization 
	 */
	private final Object REQUEST_LOCK = new Object();
	
	/**
	 * maximum floor for the elevator system
	 */
	private final int MAX_FLOOR;
	
	/**
	 * minimum floor for the elevator system
	 */
	private final int MIN_FLOOR;
	
	/**
	 * map of elevators in the system and the floors they have to stop
	 */
	private Map<Elevator, List<Integer>> stops;
	
	
	private Map<Elevator, AtomicBoolean> elevatorUsed;
	
	private ExecutorService service;
	
	private boolean shutdown;
	
	private Runnable run;
	
	private MovingState callDirecton;
	
	private ScheduledExecutorService executorService;
	
	/**
	 * Construct an ElevatorSystemImp with minimum and maximum floor
	 * @param MIN_FLOOR - The minimum floor for the elevator system
	 * @param MAX_FLOOR - The maximum floor for the elevator system
	 */
	public ElevatorSystemImp(int MIN_FLOOR, int MAX_FLOOR) {
		this.MAX_FLOOR = MAX_FLOOR;
		this.MIN_FLOOR = MIN_FLOOR;
		
		stops = new HashMap<Elevator, List<Integer>>();
		elevatorUsed = new HashMap<Elevator, AtomicBoolean>();
		
		executorService = Executors.newScheduledThreadPool( 4);
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
	public void requestStop(final Elevator elevator, final int floor) {
		if(floor < MIN_FLOOR ) throw new IllegalArgumentException("Cannot go below the minimum floor");
		if(floor > MAX_FLOOR ) throw new IllegalArgumentException("Cannot go higher than maximum floor");
		if(elevator == null) throw new IllegalArgumentException("Elevator cannot be null");
		
		requestStops(elevator, new int[] {floor});
	}
	
	/**
	 * called from {@link Elevator} to inform {@link ElevatorSystem} of multiple stop requests.
	 * @param elevator - reference to the calling elevator.
	 * @param floors - new stops to which {@link Elevator} will travel.
	 * @throws IllegalArgumentException when elevator is null
	 */
	@Override
	public void requestStops(Elevator elevator, int... floors) {
		if(elevator == null) throw new IllegalArgumentException("Elevator cannot be null");
		
		synchronized (REQUEST_LOCK) {
			if (!stops.containsKey(elevator)) {
				throw new IllegalArgumentException("Elevator is not present in the system");
			} else {
				ArrayList<Integer> stopList = (ArrayList<Integer>) stops.get(elevator);

				for (int floor : floors) {
					if (floor >= MIN_FLOOR && floor <= MAX_FLOOR && !stopList.contains(floor)) {
						stopList.add(floor);
					}
				}

				// TODO Use quicksort
				// TODO How to get direction
				Collections.sort(stopList);
			}
		}
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
		
		//TODO How to select elevator, should I move the elevator or add to stop
		Elevator result = null;
		return result;
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
		//TODO How to select elevator, should I move the elevator or add to stop
		Elevator result = null;
		return result;
	}

	/**
	 * add an {@link Elevator} to {@link ElevatorSystem}, if implemented multiple {@link Elevator} can be added
	 * @param elevator - {@link Elevator} object to be added to {@link ElevatorSystem}
	 * @throws IllegalArgumentException when elevator is null
	 */
	@Override
	public void addElevator(Elevator elevator) {
		if(elevator == null) throw new IllegalArgumentException();
		if(stops.containsKey(elevator)) throw new IllegalArgumentException();
		
		synchronized (REQUEST_LOCK) {
			stops.put(elevator, new ArrayList<Integer>());
			elevatorUsed.put(elevator, new AtomicBoolean(false));
		}
	}

	/**
	 * return current floor of {@link Elevator} in {@link ElevatorSystem}.
	 * @return current floor of only {@link Elevator}
	 */
	@Override
	public int getCurrentFloor() {
		// TODO What to return?
		return 0;
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
		double totalPowerConsumed = 0.0;
		
		synchronized (REQUEST_LOCK) {
			for (Elevator elevator : stops.keySet()) {
				totalPowerConsumed += elevator.getPowerConsumed();
			}
		}
		
		return totalPowerConsumed;
	}
	
	private void floorCheck(int floor) {
		
	}
	
	private Elevator call(int floor, MovingState direction) {
		return null;
	}
	
	private Elevator getAvailableElevatorIndex(int floor) {
		// TODO 
		return null;
	}
	
	private boolean checkForElevator() {
		return false;
	}

	/**
	 * total number of elevators regardless of their states
	 * @return total number of elevators
	 */
	@Override
	public int getElevatorCount() {
		synchronized (REQUEST_LOCK) {
			return stops.keySet().size();
		}
	}

	/**
	 * add an {@link Observer} to be attached to all {@link Elevator} objects
	 * @param observer - to be added to all {@link Elevator}, cannot be null
	 * @throws IllegalArgumentException when observer is null
	 */
	@Override
	public void addObserver(Observer observer) {
		if(observer == null) throw new IllegalArgumentException();
		
		synchronized (REQUEST_LOCK) {
			for (Elevator elevator : stops.keySet()) {
				elevator.addObserver(observer);
			}
		}
	}

	/**
	 * shutdown {@link ExecutorService} which handles the threads
	 */
	@Override
	public void shutdown() {
		shutdown = true;
		
		//http://www.baeldung.com/java-executor-service-tutorial
		executorService.shutdown();
		try {
			if(!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
				executorService.shutdownNow();
			}
		}catch(InterruptedException e) {
			executorService.shutdownNow();
		}
	}

	/**
	 * start the main thread controlling {@link ElevatorSystem}
	 */
	@Override
	public void start() {
		Runnable elevatorSystemTask = () ->{
			//Continue to run until shutdown method is called
			while(!shutdown) {
				// TODO Check if its a big lock or not
				synchronized (REQUEST_LOCK) {
					//for each elevator in the system, if the elevator is currently idle and it has stops stored 
					//then move the elevator to the next stop
					for (Elevator elevator : stops.keySet()) {
						if(elevator.isIdle()) {
							ArrayList<Integer> stopList = (ArrayList<Integer>) stops.get(elevator);
							if(!stopList.isEmpty()) {
								
								Runnable task = () ->{
									//check if the elevator's move to is already called by another thread
									//if not move the elevator and remove the stop from the list
									AtomicBoolean value = elevatorUsed.get(elevator);
									Integer nextStop = stopList.get(0);
									if(value.compareAndSet(false, true)) {
										elevator.moveTo(nextStop);
										stopList.remove(0);
										value.set(false);
									}
								};
								
								Future<?> result = executorService.submit(task);
							}
						}
					}
				}			
			}
		};
		
		executorService.execute(elevatorSystemTask);
	}
}
