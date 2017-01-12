package fxwindows.wrapped;

import fxwindows.core.Position;
import fxwindows.core.ShapeBase;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Line extends ShapeBase {

	private javafx.scene.shape.Line line;
	private final DoubleProperty endX = new SimpleDoubleProperty();
	private final DoubleProperty endY = new SimpleDoubleProperty();
	private final DoubleProperty lengthX = new SimpleDoubleProperty();
	private final DoubleProperty lengthY = new SimpleDoubleProperty();
	
	public Line() {
		super();
		line = new javafx.scene.shape.Line();
		line.startXProperty().bind(xProperty());
		line.startYProperty().bind(yProperty());
		line.endXProperty().bind(endX);
		line.endYProperty().bind(endY);
		line.strokeProperty().bind(borderColorProperty());
		line.strokeWidthProperty().bind(borderWidthProperty());
		lengthX.bind(endX.subtract(xProperty()));
		lengthY.bind(endY.subtract(yProperty()));
		setupClickedHandlers(line);
	}
	
	public Line(double startX, double startY) {
		this();
		setXY(startX, startY);
	}
	
	public Line(DoubleExpression startX, DoubleExpression startY) {
		this();
		bindX(startX);
		bindY(startY);
	}
	
	public Line(DoubleExpression startY, DoubleExpression startX,
			DoubleExpression endX, DoubleExpression endY) {
		this(startX, startY);
		bindEndX(endX);
		bindEndY(endY);
	}
	
	public DoubleProperty lengthXProperty() {
		return lengthX;
	}
	
	public DoubleProperty lengthYProperty() {
		return lengthY;
	}
	
	public DoubleProperty endXProperty() {
		return endX;
	}
	
	public DoubleProperty endYProperty() {
		return endY;
	}
	
	public void bindEndX(DoubleExpression value) {
		endX.bind(value);
	}
	
	public void bindEndY(DoubleExpression value) {
		endY.bind(value);
	}
	
	public void bindEndXY(DoubleExpression x, DoubleExpression y) {
		endX.bind(x);
		endY.bind(y);
	}
	
	public void bindEndPosition(Position p) {
		endX.bind(p.xProperty());
		endY.bind(p.yProperty());
	}

	@Override
	public Node getNode() {
		return line;
	}

	@Override
	public void clip(Node n) {
		line.setClip(n);
	}
}
