package cst8284.shape;

public class SolidObject <T extends BasicShape> {
	
	private T shape;
	private double depth;
	
	protected SolidObject(T shape, double depth) {
		setShape(shape);
		setDepth(depth);
	}
	
	protected SolidObject(T shape) {
		this(shape, 1.0);
	}
	
	public String getName() {
		if(getShape() instanceof Circle) {
			return "Cylinder";
		}
		else if(getShape() instanceof Rectangle) {
			return "Block";
		}
		else {
			return "Cube";
		}
	}
	
	public double getVolume() {
		return getShape().getArea() * getDepth();
	}
	
	public double getSurfaceArea() {
		return (2 * getShape().getArea()) + (getShape().getPerimeter() * getDepth());
	}
	
	public T getShape() {
		return shape;
	}
	
	public void setShape(T shape) {
		this.shape = shape;
	}
	
	public double getDepth() {
		return depth;
	}
	
	public void setDepth(double depth) {
		this.depth = depth;
	}
}
