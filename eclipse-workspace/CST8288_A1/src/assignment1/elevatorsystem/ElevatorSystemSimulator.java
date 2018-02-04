package assignment1.elevatorsystem;

import assignment1.elevator.Elevator;
import assignment1.elevator.ElevatorImp;

public class ElevatorSystemSimulator {

	public static void main(String[] args) {
		final int MIN_FLOOR = 0;
		final int MAX_FLOOR = 20;
		ElevatorSystemImp system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		ElevatorImp elevator = new ElevatorImp(5, system);
		
		system.addElevator(elevator);
		
		//Ask the elevator to come to floor 12 and we will go down
		Elevator elevator1 =  system.callDown(12);
		//1 person enters the elevator
		elevator1.addPersons(1);
		//Request the elevator to stop at floor 0
		elevator1.requestStop(0);
	}

}
