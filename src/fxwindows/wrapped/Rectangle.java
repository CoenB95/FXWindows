package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.beans.binding.DoubleExpression;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Rectangle extends ShapeBase {

	private javafx.scene.shape.Rectangle rect;
	
	public Rectangle() {
		super();
		rect = new javafx.scene.shape.Rectangle();
		rect.layoutXProperty().bind(xProperty());
		rect.layoutYProperty().bind(yProperty());
		rect.widthProperty().bind(widthProperty());
		rect.heightProperty().bind(heightProperty());
		setupTopLevelBindings(rect);
		setupBackgroundBindings(rect);
		setupMouseBindings(rect);
	}
	
	public Rectangle(double w, double h) {
		this();
		setContentHeight(h);
		setContentWidth(w);
	}

	public Rectangle(double x, double y, double w, double h) {
		this(w, h);
		setXY(x, y);
	}
	
	public Rectangle(DoubleExpression w, DoubleExpression h) {
		this();
		contentWidthProperty().bind(w);
		contentHeightProperty().bind(h);
	}

	public Rectangle(DoubleExpression x, DoubleExpression y, DoubleExpression w, DoubleExpression h) {
		this(w, h);
		bindXY(x, y);
	}

	@Override
	public Node getNode() {
		return rect;
	}

	@Override
	public void clip(Node n) {
		rect.setClip(n);
	}
}
