package fxwindows.wrapped;

import javafx.beans.binding.DoubleExpression;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class WrappedRectangle extends WrappedNode {

	private Rectangle rect;
	
	public WrappedRectangle() {
		super();
		rect = new Rectangle();
		rect.layoutXProperty().bind(transformedXProperty());
		rect.layoutYProperty().bind(transformedYProperty());
		rect.widthProperty().bind(widthProperty());
		rect.heightProperty().bind(heightProperty());
		rect.fillProperty().bind(backgroundColorProperty());
	}
	
	public WrappedRectangle(double w, double h) {
		this();
		setHeight(h);
		setWidth(w);
	}
	
	public WrappedRectangle(DoubleExpression w, DoubleExpression h) {
		this();
		bindWidth(w);
		bindHeight(h);
	}

	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(rect);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(rect);
	}

	@Override
	public void clip(Node n) {
		rect.setClip(n);
	}
}
