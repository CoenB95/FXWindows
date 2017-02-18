package fxwindows.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 * Base class for a positioned that can be colored.
 * @author CoenB95
 *
 */
public abstract class ColoredBoundedArea extends Area implements Colorable {
	
	private DoubleProperty alpha = new SimpleDoubleProperty(1);
	private ObjectProperty<Paint> backgroundColor = new SimpleObjectProperty<>(Color.TRANSPARENT);
	private ObjectProperty<Paint> borderColor = new SimpleObjectProperty<>(Color.TRANSPARENT);
	private DoubleProperty borderWidth = new SimpleDoubleProperty(1);
	
	@Override
	public DoubleProperty alphaProperty() {
		return alpha;
	}

	@Override
	public ObjectProperty<Paint> backgroundColorProperty() {
		return backgroundColor;
	}

	@Override
	public ObjectProperty<Paint> borderColorProperty() {
		return borderColor;
	}

	@Override
	public DoubleProperty borderWidthProperty() {
		return borderWidth;
	}
	
	@Override
	public double getAlpha() {
		return alphaProperty().get();
	}

	@Override
	public Paint getBackgroundColor() {
		return backgroundColorProperty().get();
	}

	@Override
	public Paint getBorderColor() {
		return borderColorProperty().get();
	}

	@Override
	public double getBorderWidth() {
		return borderWidthProperty().get();
	}

	@Override
	public void setAlpha(double value) {
		alphaProperty().set(value);
	}

	@Override
	public void setBackgroundColor(Paint value) {
		backgroundColorProperty().set(value);
	}

	@Override
	public void setBorderColor(Paint value) {
		borderColorProperty().set(value);
	}
	
	@Override
	public void setBorderWidth(double value) {
		borderWidthProperty().set(value);
	}

	/**
	 * Creates bindings for the alpha, background color,
	 * border color border width and effect of this Shape.
	 *
	 * @param shape the base shape.
	 */
	protected void setupBasicBindings(Shape shape) {
		setupBasicBindings(shape, true);
	}

	/**
	 * Creates bindings for the alpha, background color,
	 * border color border width and effect of this Shape.
	 *
	 * @param shape the base shape.
	 * @param fill whether fill should be bound (Line bug).
	 */
	protected void setupBasicBindings(Shape shape, boolean fill) {
		if (fill) shape.fillProperty().bind(backgroundColorProperty());
		shape.strokeProperty().bind(borderColorProperty());
		shape.strokeWidthProperty().bind(borderWidthProperty());
		setupBasicBindings((Node) shape);
	}

	/**
	 * Creates a limited set of bindings for the alpha and effect
	 * of this Node.
	 *
	 * @param node the base node.
	 */
	protected void setupBasicBindings(Node node) {
		node.opacityProperty().bind(alphaProperty());
		node.visibleProperty().bind(alphaProperty().greaterThan(0));
	}
}
