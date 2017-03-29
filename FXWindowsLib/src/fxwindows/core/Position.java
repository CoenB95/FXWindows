package fxwindows.core;

import fxwindows.bindings.XY;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * <b>The positioning system</b>
 * 
 * <p>Positioning is based around the following concept:
 * <ul><li><b>Base:</b> the base position, rotation and size.</li>
 * <li><b>Transformation:</b> position, rotation and size changes,
 * relative to the base.</li></ul>
 * The position of this object should be set by changing the <i>base-position</i>
 * and applying transformations. Reading the position on the other hand,
 * should be done using the <i>transformed position</i>, as this is how
 * the object currently is visible.
 * @author CoenB95
 *
 */
public class Position {

	private final DoubleProperty baseX = new SimpleDoubleProperty();
	private final DoubleProperty baseY = new SimpleDoubleProperty();
	private final ReadOnlyDoubleWrapper transformedX = new ReadOnlyDoubleWrapper();
	private final ReadOnlyDoubleWrapper transformedY = new ReadOnlyDoubleWrapper();
	
	public Position() {
		transformedX.bind(baseXProperty().add(transformXProperty()));
		transformedY.bind(baseYProperty().add(transformYProperty()));
	}

	public Position(double x, double y) {
		this();
		setX(x);
		setY(y);
	}

	// Base-position
	/**
	 * Defines the basis X coordinate of this <code>Drawable</code>.
	 *
	 * @defaultValue 0
	 */
	
	private DoubleProperty baseXProperty() {
		return baseX;
	}
	
	public void setX(double x) { this.baseXProperty().set(x); }
	public void bindX(DoubleExpression x) {
		if (baseXProperty().isBound()) {
			System.out.println("Rebound x of " + this);
			baseXProperty().unbind();
		}
		baseXProperty().bind(x);
	}

	/**
	 * Defines the basis Y coordinate of this <code>Drawable</code>.
	 *
	 * @defaultValue 0
	 */
	private DoubleProperty baseYProperty() {
		return baseY;
	}
	
	public void setY(double y) { this.baseYProperty().set(y); }
	public void bindY(DoubleExpression y) {
		if (baseYProperty().isBound()) {
			System.out.println("Rebound y of " + this);
			baseYProperty().unbind();
		}
		baseYProperty().bind(y);
	}


	// Transforms
	private DoubleProperty transformX;
	public DoubleProperty transformXProperty() {
		if (transformX == null) transformX = new SimpleDoubleProperty(0);
		return transformX;
	}

	/**
	 * Sets the transformed position.
	 * @param x the new value.
	 */
	public void setTransformX(double x) {
		this.transformXProperty().set(x);
	}
	
	public double getTransformX() {
		return transformXProperty().get();
	}

	private DoubleProperty transformY;
	public DoubleProperty transformYProperty() {
		if (transformY == null) transformY = new SimpleDoubleProperty(0);
		return transformY;
	}

	/**
	 * Sets the transformed position.
	 * @param y the new value.
	 */
	public void setTransformY(double y) {
		this.transformYProperty().set(y);
	}
	
	public double getTransformY() {
		return transformYProperty().get();
	}


	// Transformed-position
	public ReadOnlyDoubleProperty xProperty() {
		return transformedX.getReadOnlyProperty();
	}

	public double getX() { return xProperty().get(); }

	
	public ReadOnlyDoubleProperty yProperty() {
		return transformedY.getReadOnlyProperty();
	}

	public double getY() { return yProperty().get(); }
	
	public void bindXY(DoubleExpression x, DoubleExpression y) {
		bindX(x);
		bindY(y);
	}

	/**Sets the base position.
	 * @param x the new x value.
	 * @param y the new y value.
	 */
	public void setXY(double x, double y) {
		setX(x);
		setY(y);
	}
}
