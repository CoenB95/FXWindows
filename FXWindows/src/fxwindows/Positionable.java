package fxwindows;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * ...
 * <p><b>The positioning system</b>
 * 
 * <p>Positioning is based around the following concept:
 * <ul><li><b>Base:</b> the base position, rotation and size.</li>
 * <li><b>Transformation:</b> position, rotation and size changes,
 * relative to the base.</li></ul>
 * The position of this object should be set by changing the <i>base-position</i>
 * and applying transformations. Reading the position on the other hand,
 * should be done using the <i>transformed position</i>, as this is how
 * the object currently is visible.
 * @author PC1
 *
 */
public abstract class Positionable {

	// Base-position
	/**
	 * Defines the basis X coordinate of this <code>Drawable</code>.
	 *
	 * @defaultValue 0
	 */
	private DoubleProperty baseX;
	private DoubleProperty baseXProperty() {
		if (baseX == null) baseX = new SimpleDoubleProperty();
		return baseX;
	}
	public void setX(double x) { this.baseXProperty().set(x); }
	public void bindX(DoubleExpression x) { this.baseXProperty().bind(x); }

	/**
	 * Defines the basis Y coordinate of this <code>Drawable</code>.
	 *
	 * @defaultValue 0
	 */
	private DoubleProperty baseY;
	private DoubleProperty baseYProperty() {
		if (baseY == null) baseY = new SimpleDoubleProperty();
		return baseY;
	}
	public void setY(double y) { this.baseYProperty().set(y); }
	public void bindY(DoubleExpression y) { this.baseYProperty().bind(y); }

	
	// Width and Height
	private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper();
	public ReadOnlyDoubleProperty heightProperty() {
		return height.getReadOnlyProperty();
	}
	/**@deprecated use {@link #setHeight(double)} instead.*/
	protected void setCalculatedHeight(double value) {}
	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setHeight(double value) { height.set(value); }
	protected void bindHeight(DoubleExpression value) { height.bind(value); }
	public double getHeight() { return heightProperty().get(); }

	private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper();
	public ReadOnlyDoubleProperty widthProperty() {
		return width.getReadOnlyProperty();
	}
	/**@deprecated use {@link #setWidth(double)} instead.*/
	protected void setCalculatedWidth(double value) {}
	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setWidth(double value) { width.set(value); }
	protected void bindWidth(DoubleExpression value) { width.bind(value); }
	public double getWidth() { return widthProperty().get(); }


	// Transforms
	private DoubleProperty transformX;
	public DoubleProperty transformXProperty() {
		if (transformX == null) transformX = new SimpleDoubleProperty();
		return transformX;
	}
	private DoubleProperty transformY;
	public DoubleProperty transformYProperty() {
		if (transformY == null) transformY = new SimpleDoubleProperty();
		return transformY;
	}


	// Transformed-position
	private ReadOnlyDoubleWrapper transformedX;
	public ReadOnlyDoubleProperty transformedXProperty() {
		if (transformedX == null) {
			transformedX = new ReadOnlyDoubleWrapper();
			transformedX.bind(baseXProperty().add(transformXProperty()));
		}
		return transformedX.getReadOnlyProperty();
	}
	public double getX() { return transformedXProperty().get(); }

	private ReadOnlyDoubleWrapper transformedY;
	public ReadOnlyDoubleProperty transformedYProperty() {
		if (transformedY == null) {
			transformedY = new ReadOnlyDoubleWrapper();
			transformedY.bind(baseYProperty().add(transformYProperty()));
		}
		return transformedY.getReadOnlyProperty();
	}
	public double getY() { return transformedYProperty().get(); }

	private ReadOnlyDoubleWrapper transformedHeight;
	public ReadOnlyDoubleProperty transformedHeightProperty() {
		if (transformedHeight == null) {
			transformedHeight = new ReadOnlyDoubleWrapper();
			transformedHeight.bind(heightProperty());
		}
		return transformedHeight.getReadOnlyProperty();
	}
	//public double getHeight() { return transformedHeightProperty().get(); }

	private ReadOnlyDoubleWrapper transformedWidth;
	public ReadOnlyDoubleProperty transformedWidthProperty() {
		if (transformedWidth == null) {
			transformedWidth = new ReadOnlyDoubleWrapper();
			transformedWidth.bind(widthProperty());
		}
		return transformedWidth.getReadOnlyProperty();
	}
	//public double getWidth() { return transformedWidthProperty().get(); }


	// Other properties
	private DoubleProperty alpha;
	public DoubleProperty alphaProperty() {
		if (alpha == null) alpha = new SimpleDoubleProperty(1);
		return alpha;
	}
	public void setAlpha(double value) { alphaProperty().set(value); }
	public double getAlpha() { return alphaProperty().get(); }

	private ObjectProperty<Paint> borderColor;
	public ObjectProperty<Paint> borderColorProperty() {
		if (borderColor == null)
			borderColor = new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		return borderColor;
	}
	public void setBorderColor(Paint value) { borderColorProperty().set(value); }
	public Paint getBorderColor() { return borderColorProperty().get(); }

	private ObjectProperty<Paint> backgroundColor;
	public ObjectProperty<Paint> backgroundColorProperty() {
		if (backgroundColor == null)
			backgroundColor = new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		return backgroundColor;
	}
	public void setBackgroundColor(Paint value) { backgroundColorProperty().set(value); }
	public Paint getBackgroundColor() { return backgroundColorProperty().get(); }

	//=========================

	private ReadOnlyBooleanWrapper hovered;
	public ReadOnlyBooleanProperty hoveredProperty() {
		if (hovered == null) hovered = new ReadOnlyBooleanWrapper();
		return hovered.getReadOnlyProperty();
	}
	public void setHovered(boolean value) { 
		if (hovered == null) hovered = new ReadOnlyBooleanWrapper();
		hovered.set(value); }
	public boolean isHovered() { return hoveredProperty().get(); }

	private ObjectProperty<Runnable> onMouseClicked;
	public ObjectProperty<Runnable> onMouseClickedProperty() {
		if (onMouseClicked == null) onMouseClicked = new SimpleObjectProperty<>();
		return onMouseClicked;
	}
	public void setOnMouseClicked(Runnable r) { onMouseClickedProperty().set(r); }
	public Runnable getOnMouseClicked() { return onMouseClickedProperty().get(); }

	/**Sets the base position.
	 * @param x the new x value.
	 * @param y the new y value.*/
	public void setXY(double x, double y) {
		setX(x);
		setY(y);
	}
	/**Sets the transformed position.
	 * @param x the new value.*/
	public void transformX(double x) {
		this.transformXProperty().set(x);
	}
	/**Sets the transformed position.
	 * @param x the new value.*/
	public void transformY(double y) {
		this.transformYProperty().set(y);
	}
}
