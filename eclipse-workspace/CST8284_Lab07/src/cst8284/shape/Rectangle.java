package cst8284.shape;

public class Rectangle extends Square {
	private double height;
	
	public Rectangle() {
		this(1.0,1.0);
	}
	
	public Rectangle(double width, double height) {
		setWidth(width);
		this.height = height;
	}
	
	public Rectangle(Rectangle rectangle){
		this(rectangle.getWidth(), rectangle.getHeight());
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getHeight() {
		return height;
	}
	
	@Override
	public double getArea() {
		return getWidth()*getHeight();
	}

	@Override
	public double getPerimeter() {
		return 2*(getWidth()+getHeight());
	}
	
	@Override
	public String toString(){
		return ("Rectangle Overrides " + super.toString());
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof Rectangle) {
			if(super.equals(object)) {
				Rectangle rectangle = (Rectangle)object;
				return getHeight() == rectangle.getHeight();
			}
		}
		
		return false;
	}
}
