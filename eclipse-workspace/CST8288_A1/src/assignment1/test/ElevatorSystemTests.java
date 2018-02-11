package assignment1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import assignment1.elevator.Elevator;
import assignment1.elevator.ElevatorImp;
import assignment1.elevatorsystem.ElevatorSystemImp;

class ElevatorSystemTests {
	private static final int MIN_FLOOR = 0;
	private static final int MAX_FLOOR = 20;
	
	private ElevatorSystemImp system;
	private ElevatorImp elevator;
	
	@Before
	public void testSetup() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(1, system);
	}
	
	@Test
	public void testRequestStop() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(1, system);
		
		try {
			system.requestStop(0, null);
			fail("requestStop should not except a null elevator");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.requestStop(MIN_FLOOR-1, elevator);
			fail("requestStop should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.requestStop(MAX_FLOOR+1, elevator);
			fail("requestStop should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testCallUp() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(1, system);
		system.addElevator(elevator);
		
		try {
			system.callUp(MIN_FLOOR-1);
			fail("callUp should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.callUp(MAX_FLOOR+1);
			fail("callUp should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		//TODO: Check can I call callUp with max floor
		
		Elevator returnedElevator =  system.callUp(MIN_FLOOR);
		
		assertEquals(elevator, returnedElevator, "callUp did not return the correct Elevator");
		
		assertEquals(MIN_FLOOR, system.getCurrentFloor(), "the Elevator did not moved to the correct floor");
	}
	
	@Test
	void testCallDown() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(1, system);
		system.addElevator(elevator);
		
		try {
			system.callDown(MIN_FLOOR-1);
			fail("callDown should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.callDown(MAX_FLOOR+1);
			fail("callDown should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		//TODO: Check can I call callDown with min floor
		
		Elevator returnedElevator =  system.callDown(MAX_FLOOR);
		
		assertEquals(elevator, returnedElevator, "callDown did not return the correct Elevator");
		
		assertEquals(MAX_FLOOR, system.getCurrentFloor(), "the Elevator did not moved to the correct floor");
	}
	
	@Test
	void testFloorCount() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		
		assertEquals(MAX_FLOOR-MIN_FLOOR+1, system.getFloorCount(), "getFloorCount did not return the correct floor count");
	}

}
