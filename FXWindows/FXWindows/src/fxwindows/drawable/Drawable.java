package fxwindows.drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import fxwindows.animation.Animation;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.GraphicsContext;
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
public abstract class Drawable {
	private List<Animation> animations;
	
	
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
	public DoubleProperty baseYProperty() {
		if (baseY == null) baseY = new SimpleDoubleProperty();
		return baseY;
	}
	public void setY(double y) { this.baseYProperty().set(y); }
	public void bindY(DoubleExpression y) { this.baseYProperty().bind(y); }
	
	private DoubleProperty baseHeight;
	private DoubleProperty baseHeightProperty() {
		if (baseHeight == null) baseHeight = new SimpleDoubleProperty();
		return baseHeight;
	}
	protected void setCalculatedHeight(double value) { baseHeightProperty().set(value); }
	
	private DoubleProperty baseWidth;
	private DoubleProperty baseWidthProperty() {
		if (baseWidth == null) baseWidth = new SimpleDoubleProperty();
		return baseWidth;
	}
	protected void setCalculatedWidth(double value) { baseWidthProperty().set(value); }
	
	
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
			transformedHeight.bind(baseHeightProperty());
		}
		return transformedHeight.getReadOnlyProperty();
	}
	public double getHeight() { return transformedHeightProperty().get(); }
	
	private ReadOnlyDoubleWrapper transformedWidth;
	public ReadOnlyDoubleProperty transformedWidthProperty() {
		if (transformedWidth == null) {
			transformedWidth = new ReadOnlyDoubleWrapper();
			transformedWidth.bind(baseWidthProperty());
		}
		return transformedWidth.getReadOnlyProperty();
	}
	public double getWidth() { return transformedWidthProperty().get(); }
	
	
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
	
	public Drawable() {
		animations = new ArrayList<>();
	}
	public abstract void draw(GraphicsContext graphics);
	public Drawable mouse(double x, double y) {
		return (x > getX() && x < getX() + getWidth() &&
				y > getY() && y < getY() + getHeight()) ?
						this : null;
	}
	public void click() {
		if (onMouseClickedProperty().get() != null)
			onMouseClickedProperty().get().run();
	}
	public void update() {
		ListIterator<Animation> it = animations.listIterator(0);
		while (it.hasNext()) {
			Animation a = it.next();
			a.update();
			if (a.hasEnded()) it.remove();
		}
	}
	public void addAnimation(Animation anim) {
		animations.add(anim);
	}
	
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