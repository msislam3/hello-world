package test.cst8284.shape;

import static org.junit.Assert.*;

import org.junit.Test;

import cst8284.shape.*;

public class TestShape {

	@Test
	public void testInstanceOf() {
		Rectangle rect = new Rectangle();
		assertTrue(rect instanceof Square);
	}
	
	@Test
	public void testCircleNotSquare() {
		Square square = new Square();
		Circle circle = new Circle();	
		assertFalse(circle.equals(square));
	}
	
	@Test
	public void testCircleEquals() {
		Circle circle1 = new Circle(2.0);
		Circle circle2 = new Circle(circle1);
		assertTrue(circle1.equals(circle2));
	}

}
