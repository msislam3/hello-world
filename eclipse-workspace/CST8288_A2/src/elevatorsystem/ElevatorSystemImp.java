package elevatorsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import elevator.Elevator;
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
	
	
	private AtomicBoolean[] elevatorUsed;
	private AtomicBoolean alive = new AtomicBoolean(true);
	
	private Runnable run;
	
	private MovingState callDirecton;
	
	private ExecutorService executorService;
	
	private QuickSort sorter = new QuickSort();
	
	/**
	 * Construct an ElevatorSystemImp with minimum and maximum floor
	 * @param MIN_FLOOR - The minimum floor for the elevator system
	 * @param MAX_FLOOR - The maximum floor for the elevator system
	 */
	public ElevatorSystemImp(int MIN_FLOOR, int MAX_FLOOR) {
		this.MAX_FLOOR = MAX_FLOOR;
		this.MIN_FLOOR = MIN_FLOOR;
		
		stops = new HashMap<Elevator, List<Integer>>();
		elevatorUsed = new AtomicBoolean[stops.size()];
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
		
		requestStops(elevator, floor);
	}
	
	/**
	 * called from {@link Elevator} to inform {@link ElevatorSystem} of multiple stop requests.
	 * @param elevator - reference to the calling elevator.
	 * @param floors - new stops to which {@link Elevator} will travel.
	 * @throws IllegalArgumentException when elevator is null
	 */
	@Override
	public void requestStops(Elevator elevator, int... floors) {
		//Q - why are we using optional argument rather than array. It makes clear code in the calling function
		
		if(elevator == null) throw new IllegalArgumentException("Elevator cannot be null");
		
		if (!stops.containsKey(elevator)) {
			throw new IllegalArgumentException("Elevator is not present in the system");
		} else {
			LinkedList<Integer> stopList = (LinkedList<Integer>) stops.get(elevator);
		
			synchronized (REQUEST_LOCK) {	
				//When callUp is called, sort in ascending order, when callDown is called sort in descending order
				// in callUp method set movingDirection to MovingState.Up, in callDown set to MovingState.Down
				// here sort based on movingDirection
				
				//Because callUp/Down and requestStops are called sequentially from the same thread we can use this way
				//Because different threads calling callUp/callDown calls after a break, multiple threads don't collide
				//each other for the direction 
				
				//What is the advantage of sorting before adding and after adding - 46 min
				
				if(callDirecton == MovingState.Up) {
					sorter.sort(floors,true);
				}
				else if(callDirecton == MovingState.Down)
				{
					sorter.sort(floors,false);
				}
				
				for (int floor : floors) {
					if (floor >= MIN_FLOOR && floor <= MAX_FLOOR && !stopList.contains(floor)) {
						stopList.add(floor);
					}
				}
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
		
		Elevator result = call(floor, MovingState.Up);
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
		
		Elevator result = call(floor, MovingState.Down);
		return result;
	}
	
	private Elevator call(int floor, MovingState direction) {
		callDirecton = direction;

		Future<Elevator> f = executorService.submit(() ->{
			Elevator elevator = getAvailableElevatorIndex(floor);
			elevator.moveTo(floor);
			return elevator;
		});
			
		Elevator selectedElevator = null;
		try {
			selectedElevator = f.get();
		} catch (InterruptedException | ExecutionException e) {} 
		
		return selectedElevator;
	}
	
	private Elevator getAvailableElevatorIndex(int floor) {
		Elevator selectedElevator = null;
		int distance = Integer.MAX_VALUE;
		
		//Select an elevator which is idle, whose list of stops is empty and closest to the destination floor
		//inUse for elevator id is false - ??
		for (Elevator elevator : stops.keySet()) {
			if(elevator.isIdle()  && stops.get(elevator).size() == 0 && Math.abs(elevator.getFloor() - floor) < distance) {
				selectedElevator = elevator; 
				distance = Math.abs(elevator.getFloor() - floor) + 1;
			}
		}
		
		return selectedElevator;
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
		
		// Use LinkedList - we will remove a lot of data, each time we remove a data in a Array it will have
		// to move all the data after it so operation is expensive O(n). In linked list this operation is O(1)
		stops.put(elevator, new LinkedList<Integer>());
		
		//It would be much more efficient if map is used
		elevatorUsed = new AtomicBoolean[stops.size()];
		for (int i=0; i<elevatorUsed.length; i++) {
			elevatorUsed[i] = new AtomicBoolean(false);
		}
		
		executorService = Executors.newFixedThreadPool(stops.size()+1); 
		//Fixed thread pool. Fixed number of threads created. If all the threads are running, new work will wait.
		//Q - How many threads running stops.size()+1
		//Q - Why fixed thread pool better - we know how many threads we need so we can create exactly that number of 
		//threads. We don't need to kill any thread
		
		//Creating a new thread is expensive. Executor service creates a pool of threads and reuses it
	}

	/**
	 * return current floor of {@link Elevator} in {@link ElevatorSystem}.
	 * @return current floor of only {@link Elevator}
	 */
	@Override
	public int getCurrentFloor() {
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
		
		for (Elevator elevator : stops.keySet()) {
			totalPowerConsumed += elevator.getPowerConsumed();
		}
			
		return totalPowerConsumed;
	}
	
	private void floorCheck(int floor) {
		
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
		return stops.keySet().size();
	}

	/**
	 * add an {@link Observer} to be attached to all {@link Elevator} objects
	 * @param observer - to be added to all {@link Elevator}, cannot be null
	 * @throws IllegalArgumentException when observer is null
	 */
	@Override
	public void addObserver(Observer observer) {
		if(observer == null) throw new IllegalArgumentException();
		
		for (Elevator elevator : stops.keySet()) {
			elevator.addObserver(observer);
		}	
	}

	/**
	 * shutdown {@link ExecutorService} which handles the threads
	 */
	@Override
	public void shutdown() {
		System.out.println( "System Stopping");
		
		alive.set(false);
		
		//http://www.baeldung.com/java-executor-service-tutorial
		executorService.shutdown();
		//this kills only inactive thread not inactive threads
		
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

		run = () ->{	
			System.out.println( "System Starting");
			while(alive.get()) {
				for (Elevator elevator : stops.keySet()) {
					if(elevator.isIdle()) {
						LinkedList<Integer> stopList = (LinkedList<Integer>) stops.get(elevator);
						//use atomic integer and arrays as prof
						AtomicBoolean value = elevatorUsed[elevator.id()];
						if(!stopList.isEmpty() && !value.get()) {
							synchronized (REQUEST_LOCK) {
								if(value.compareAndSet(false, true)) {
									Integer nextStop = stopList.remove(0);
									executorService.submit(() ->{
										elevator.moveTo(nextStop);
										value.compareAndSet(true, false);
									});
								}
							}
						}
					}
				}
			}
		};
		
		executorService.execute(run);
		//why use submit instead of execute. Check networking sample
	}
}
