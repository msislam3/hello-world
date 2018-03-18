package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.Elevator;
import elevator.ElevatorImp;
import elevatorsystem.ElevatorSystemImp;

class ElevatorSystemTests {
	private static final int MIN_FLOOR = 0;
	private static final int MAX_FLOOR = 20;
	private static final int ID = 0;
	private static final boolean DELAY = false;
	
	private ElevatorSystemImp system;
	private ElevatorImp elevator;
	
	@BeforeEach
	void testSetup() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(1, system, ID, DELAY);
	}
	
	@Test
	void testRequestStop() {	
		try {
			system.requestStop(null, 0);
			fail("requestStop should not except a null elevator");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.requestStop(elevator, MIN_FLOOR-1);
			fail("requestStop should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.requestStop(elevator, MAX_FLOOR+1);
			fail("requestStop should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testCallUp() {
		system.addElevator(elevator);
		
		try {
			system.callUp(MIN_FLOOR-1);
			fail("callUp should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.callUp(MAX_FLOOR+1);
			fail("callUp should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
			
		Elevator returnedElevator =  system.callUp(MIN_FLOOR);
		
		assertEquals(elevator, returnedElevator, "callUp did not return the correct Elevator");
		
		//Though call up is an async task, it still passes because the elevator starts at MIN_FLOOR
		assertEquals(MIN_FLOOR, system.getCurrentFloor(), "the Elevator did not moved to the correct floor");
	}
	
	@Test
	void testCallDown() {
		system.addElevator(elevator);
		
		try {
			system.callDown(MIN_FLOOR-1);
			fail("callDown should not except a floor lower than MIN_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.callDown(MAX_FLOOR+1);
			fail("callDown should not except a floor higher then MAX_FLOOR");
		}catch(IllegalArgumentException e) {}
		
		Elevator returnedElevator =  system.callDown(MAX_FLOOR);
		
		assertEquals(elevator, returnedElevator, "callDown did not return the correct Elevator");
		
		//TODO: Async task
		//assertEquals(MAX_FLOOR, system.getCurrentFloor(), "the Elevator did not moved to the correct floor");
	}
	
	@Test
	void testFloorCount() {	
		assertEquals(MAX_FLOOR-MIN_FLOOR+1, system.getFloorCount(), "getFloorCount did not return the correct floor count");
	}

	@Test
	void testRequestStops() {
		try {
			system.requestStops(null, 1,2,3);
			fail("cannot request stops to null elevator");
		}catch(IllegalArgumentException e) {}
		
		try {
			system.requestStops(elevator, 1,2,3);
			fail("cannot request stops to a elevator that is not in the system");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testAddObservers() {
		try {
			system.addObserver(null);
			fail("cannot add null observer to the system");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testAddElevator() {
		try {
			system.addElevator(null);
			fail("cannot add null elevator to the system");
		}catch(IllegalArgumentException e) {}
		
		system.addElevator(elevator);
		assertEquals(1,  system.getElevatorCount(), "addElevator did not add an elevator");
		
		try {
			system.addElevator(elevator);
			fail("cannot add the same elevator multiple times");
		}catch(IllegalArgumentException e) {}
	}
}
