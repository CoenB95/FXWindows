package fxwindows;

import javafx.beans.binding.DoubleExpression;
import javafx.scene.shape.Shape;

public class LayoutUtil {

	private Shape shape;
	private byte side;
	
	private static final byte TOP = 1;
	private static final byte START = 2;
	private static final byte BOTTOM = 3;
	private static final byte END = 4;
	
	private LayoutUtil(Shape base, byte side) {
		shape = base;
	}
	
	public static LayoutUtil bindTopOf(Shape s1) {
		return new LayoutUtil(s1, TOP);
	}
	
	public static LayoutUtil bindStartOf(Shape s1) {
		return new LayoutUtil(s1, START);
	}
	
	public static LayoutUtil bindBottomOf(Shape s1) {
		return new LayoutUtil(s1, BOTTOM);
	}
	
	public static LayoutUtil bindEndOf(Shape s1) {
		return new LayoutUtil(s1, END);
	}
	
	public void toBottomOf(Shape s2) {
		switch (side) {
		case TOP:
			shape.layoutXProperty().bind(
					s2.layoutXProperty().add(s2.getBoundsInLocal().getHeight()));
			break;
		case START:
			
		case BOTTOM:
		case END:
		}
	}
	
	public static void bindBeneath() {
		
	}
}
