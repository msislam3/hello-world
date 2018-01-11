package cst8284.shape;

public class Square extends BasicShape {

	public Square() {
		this(1.0);
	}
	
	public Square(double width) {
		setWidth(width);
	}
	
	public Square(Square square) {
		this(square.getWidth());
	}
	
	
	@Override
	public double getArea() {
		return Math.pow(getWidth(), 2);
	}

	@Override
	public double getPerimeter() {
		return 4*getWidth();
	}
	
	@Override
	public String toString(){
		return ("Square Overrides " + super.toString());
	}
	
	@Override
	public boolean equals(Object object){
		if(object instanceof Square) {
			Square square = (Square)object;
			return getWidth() == square.getWidth();
		}
		
		return false;
	}
}
