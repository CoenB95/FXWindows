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
		setupBasicBindings(rect);
	}
	
	public Rectangle(double w, double h) {
		this();
		setHeight(h);
		setWidth(w);
	}
	
	public Rectangle(DoubleExpression w, DoubleExpression h) {
		this();
		bindWidth(w);
		bindHeight(h);
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
