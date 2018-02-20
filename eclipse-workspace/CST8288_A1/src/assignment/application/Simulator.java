package assignment.application;

import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import assignment1.elevator.Elevator;
import assignment1.elevator.ElevatorImp;
import assignment1.elevatorsystem.ElevatorSystem;
import assignment1.elevatorsystem.ElevatorSystemImp;
import assignment1.elevatorsystem.ElevatorPanel;

public class Simulator {
	private ElevatorSystem system;

	public Simulator( Observer observer){
		system = new ElevatorSystemImp( 0, 20);
		ElevatorImp e = new ElevatorImp( 10.0, (ElevatorPanel) system);
		e.addObserver( observer);
		system.addElevator( e);
	}

	public void start(){
		ExecutorService e = Executors.newSingleThreadExecutor();
		e.execute( () -> {
			try {
				Thread.sleep( 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			Elevator elevator = system.callUp( 0);
			elevator.addPersons( 1);	
			elevator.requestStop( 10);
			elevator.requestStop( 2);
			elevator.requestStop( 5);
			elevator.requestStop( 9);
			elevator.requestStop( 20);
			elevator.requestStop( 0);	
		});
		e.shutdown();
	}
}
