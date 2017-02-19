package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.shape.ArcType;

public class Arc extends ShapeBase {

	private javafx.scene.shape.Arc arc;
	private final DoubleProperty radiusX = new SimpleDoubleProperty();
	private final DoubleProperty radiusY = new SimpleDoubleProperty();
	private final DoubleProperty startAngle = new SimpleDoubleProperty();
	private final DoubleProperty angleLength = new SimpleDoubleProperty(90);
	private final DoubleProperty rotation = new SimpleDoubleProperty();
	private final ObjectProperty<ArcType> type = new SimpleObjectProperty<>(
			ArcType.OPEN);
	
	public Arc() {
		arc = new javafx.scene.shape.Arc();
		arc.centerXProperty().bind(xProperty());
		arc.centerYProperty().bind(yProperty());
		arc.radiusXProperty().bind(radiusX);
		arc.radiusYProperty().bind(radiusY);
		arc.startAngleProperty().bind(startAngle);
		arc.lengthProperty().bind(angleLength);
		arc.typeProperty().bind(type);
		setupTopLevelBindings(arc);
		setupBackgroundBindings(arc);
		setupMouseBindings(arc);
	}
	
	public Arc(double startX, double startY) {
		this();
		setXY(startX, startY);
	}
	
	public Arc(DoubleExpression startX, DoubleExpression startY) {
		this();
		bindX(startX);
		bindY(startY);
	}
	
	public Arc(DoubleExpression startX, DoubleExpression startY,
			DoubleExpression radiusX, DoubleExpression radiusY) {
		this(startX, startY);
		bindRadiusX(radiusX);
		bindRadiusY(radiusY);
	}
	
	public ObjectProperty<ArcType> typeProperty() {
		return type;
	}
	
	public void setType(ArcType value) {
		typeProperty().set(value);
	}
	
	public void setRotation(double value) {
		rotation.set(value);
	}
	public void bindRotation(DoubleExpression value) {
		rotation.bind(value);
	}
	
	public void bindStartAngle(DoubleExpression value) {
		startAngle.bind(value);
	}
	
	public void bindAngleLength(DoubleExpression value) {
		angleLength.bind(value);
	}
	
	public double getStartAngle() {
		return startAngle.get();
	}
	
	public double getAngleLength() {
		return angleLength.get();
	}
	
	public void setStartAngle(double value) {
		startAngle.set(value);
	}
	
	public void setAngleLength(double value) {
		angleLength.set(value);
	}
	
	public void bindRadiusX(DoubleExpression value) {
		radiusX.bind(value);
	}
	
	public void bindRadiusY(DoubleExpression value) {
		radiusY.bind(value);
	}
	
	public void bindRadiusXY(DoubleExpression x, DoubleExpression y) {
		radiusX.bind(x);
		radiusY.bind(y);
	}
	
	public void setRadiusX(double value) {
		radiusX.set(value);
	}
	
	public void setRadiusY(double value) {
		radiusY.set(value);
	}
	
	public void setRadiusXY(double x, double y) {
		radiusX.set(x);
		radiusY.set(y);
	}

	@Override
	public Node getNode() {
		return arc;
	}

	@Override
	public void clip(Node n) {
		arc.setClip(n);
	}
}
