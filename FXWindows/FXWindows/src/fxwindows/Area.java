package fxwindows;

import javafx.beans.property.DoubleProperty;
import javafx.scene.shape.Shape;

public abstract class Area {

	private DoubleProperty x;
	private DoubleProperty y;
	private DoubleProperty height;
	private DoubleProperty width;
	private Shape shape;
	
	public Area() {
		
	}
	
	public void bindPosition() {
		
	}
	
	public boolean contains(Area other) {
		return false;
	}
}
