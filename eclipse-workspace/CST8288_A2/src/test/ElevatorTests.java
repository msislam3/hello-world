package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import elevator.ElevatorImp;
import elevatorsystem.ElevatorSystemImp;

public class ElevatorTests {

	private static final int MIN_FLOOR = 0;
	private static final int MAX_FLOOR = 20;
	private static final int CAPACITY = 1;
	private static final int ID = 0;
	private static final boolean DELAY = false;
	
	private ElevatorSystemImp system;
	private ElevatorImp elevator;
	
	@BeforeEach
	void setup() {
		system = new ElevatorSystemImp(MIN_FLOOR, MAX_FLOOR);
		elevator = new ElevatorImp(CAPACITY, system, ID, DELAY);
	}
	
	@Test
	void testIsEmpty() {
		assertTrue(elevator.isEmpty(), "elevator should be empty");
		
		elevator.addPersons(1);
		
		assertFalse(elevator.isEmpty(), "elevator should not be empty");
	}

	@Test
	void testIsFull() {
		assertFalse(elevator.isFull(), "elevator should not be full");
		
		elevator.addPersons(CAPACITY);
		
		assertTrue(elevator.isFull(), "elevator should be full");
	}
	
	@Test
	void testAddPersons() {
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
		try {
			elevator.requestStop(-1);
			fail("requestStop cannot take negative number");
		}catch(IllegalArgumentException e) {}
		
		//TODO: How to test async operations
		//If elevator.requestStop is called, the elevator will not moved immediately as it is moved using a thread
		//so assert will fail every time
		
		//system.addElevator(elevator);
		//system.start();
		//elevator.requestStop(10);
		
		//assertEquals(10, elevator.getFloor(), "requestStop did not  move the elevator to correct floor");
	}
	
	@Test
	void testMoveTo() {
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
