package cst8284.shape;

public class Circle extends BasicShape {

	public Circle() {
		this(1.0);
	}
	
	public Circle(double width) {
		setWidth(width);
	}
	
	public Circle(Circle circle) {
		this(circle.getWidth());
	}
	
	@Override
	public double getArea() {
		return Math.PI * Math.pow(getWidth()/2, 2);
	}

	@Override
	public double getPerimeter() {
		return 2 * Math.PI * (getWidth()/2);
	}
	
	@Override
	public String toString(){
		return ("Circle Overrides " + super.toString());
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof Circle) {
			Circle circle = (Circle)object;
			return getWidth() == circle.getWidth();		
		}
		
		return false;
	}

}
