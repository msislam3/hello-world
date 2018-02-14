package assignment1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import assignment1.elevator.ElevatorImp;
import assignment1.elevatorsystem.ElevatorSystemImp;

class ElevatorTests {

	private static final int MIN_FLOOR = 0;
	private static final int MAX_FLOOR = 20;
	private static final int CAPACITY = 1;
	
	private ElevatorSystemImp system;
	private ElevatorImp elevator;
	
	@Test
	void testIsEmpty() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system);
		
		assertTrue(elevator.isEmpty(), "elevator should be empty");
		
		elevator.addPersons(1);
		
		assertFalse(elevator.isEmpty(), "elevator should not be empty");
	}

	@Test
	void testIsFull() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system);
		
		assertFalse(elevator.isFull(), "elevator should not be full");
		
		elevator.addPersons(CAPACITY);
		
		assertTrue(elevator.isFull(), "elevator should be full");
	}
	
	@Test
	void testAddPersons() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system);
		
		try {
			elevator.addPersons(-1);
			fail("addPerson cannot take negative number");
		}catch(IllegalArgumentException e) {}
		
		
		try {
			elevator.addPersons(CAPACITY+1);
			fail("addPerson cannot take person higher than capacity");
		}catch(IllegalArgumentException e) {}
	}
	
	@Test
	void testRequestStop() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system);
		
		try {
			elevator.requestStop(-1);
			fail("requestStop cannot take negative number");
		}catch(IllegalArgumentException e) {}
		
		elevator.requestStop(10);
		
		assertEquals(10, elevator.getFloor(), "requestStop did not  move the elevator to correct floor");
	}
	
	@Test
	void testMoveTo() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system);
		
		try {
			elevator.moveTo(-1);
			fail("moveTo cannot take negative number");
		}catch(IllegalArgumentException e) {}
		
		elevator.moveTo(1);	
		assertEquals(1, elevator.getFloor(), "moveTo did not  move the elevator to correct floor");
		assertEquals(2, elevator.getPowerConsumed(), "moveTo did not calculate power consumed correctly");
		
		elevator.moveTo(1+2);	
		assertEquals(3, elevator.getFloor(), "moveTo did not  move the elevator to correct floor");
		assertEquals(2+4, elevator.getPowerConsumed(), "moveTo did not calculate power consumed correctly");
		
		elevator.moveTo(1+2+3);	
		assertEquals(6, elevator.getFloor(), "moveTo did not  move the elevator to correct floor");
		assertEquals(2+4+5, elevator.getPowerConsumed(), "moveTo did not calculate power consumed correctly");
		
		elevator.moveTo(1+2+3+4);	
		assertEquals(10, elevator.getFloor(), "moveTo did not  move the elevator to correct floor");
		assertEquals(2+4+5+6, elevator.getPowerConsumed(), "moveTo did not calculate power consumed correctly");
	}
}
