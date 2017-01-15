package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;

/**
 * A Circle.
 * For consistency, the decision has been made to - for now -
 * make no use of Circle's centerX and centerY.
 *
 * @author Coen Boelhouwers
 */
public class Circle extends ShapeBase {

	private javafx.scene.shape.Circle circle;
	private final DoubleProperty radius = new SimpleDoubleProperty();

	public Circle() {
		circle = new javafx.scene.shape.Circle();
		circle.layoutXProperty().bind(innerXProperty().add(radiusProperty()));
		circle.layoutYProperty().bind(innerYProperty().add(radiusProperty()));
		circle.radiusProperty().bind(radiusProperty());
		circle.opacityProperty().bind(alphaProperty());
		circle.fillProperty().bind(backgroundColorProperty());
		circle.strokeProperty().bind(borderColorProperty());
		circle.strokeWidthProperty().bind(borderWidthProperty());
		contentWidthProperty().bind(radiusProperty().multiply(2));
		contentHeightProperty().bind(radiusProperty().multiply(2));
	}

	public DoubleProperty radiusProperty() {
		return radius;
	}

	public double getRadius() {
		return radiusProperty().get();
	}

	public void setRadius(double value) {
		radiusProperty().set(value);
	}

	@Override
	public Node getNode() {
		return circle;
	}

	@Override
	public void clip(Node n) {
		circle.setClip(n);
	}
}
