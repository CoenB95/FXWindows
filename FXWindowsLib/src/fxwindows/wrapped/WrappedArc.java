package fxwindows.wrapped;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

public class WrappedArc extends WrappedNode {

	private Arc arc;
	private final DoubleProperty radiusX = new SimpleDoubleProperty();
	private final DoubleProperty radiusY = new SimpleDoubleProperty();
	private final DoubleProperty startAngle = new SimpleDoubleProperty();
	private final DoubleProperty angleLength = new SimpleDoubleProperty(90);
	private final DoubleProperty rotation = new SimpleDoubleProperty();
	private final ObjectProperty<ArcType> type = new SimpleObjectProperty<>(
			ArcType.OPEN);
	
	public WrappedArc() {
		super();
		arc = new Arc();
		arc.centerXProperty().bind(xProperty());
		arc.centerYProperty().bind(yProperty());
		arc.radiusXProperty().bind(radiusX);
		arc.radiusYProperty().bind(radiusY);
		arc.startAngleProperty().bind(startAngle);
		arc.lengthProperty().bind(angleLength);
		arc.rotateProperty().bind(rotation);
		arc.fillProperty().bind(backgroundColorProperty());
		arc.strokeProperty().bind(borderColorProperty());
		arc.strokeWidthProperty().bind(borderWidthProperty());
		arc.typeProperty().bind(type);
	}
	
	public WrappedArc(double startX, double startY) {
		this();
		setXY(startX, startY);
	}
	
	public WrappedArc(DoubleExpression startY, DoubleExpression startX) {
		this();
		bindX(startX);
		bindY(startY);
	}
	
	public WrappedArc(DoubleExpression startY, DoubleExpression startX,
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
	public void addToPane(Pane p) {
		p.getChildren().add(arc);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(arc);
	}

	@Override
	public void clip(Node n) {
		arc.setClip(n);
	}
}
