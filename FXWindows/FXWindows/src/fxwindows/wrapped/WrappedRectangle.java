package fxwindows.wrapped;

import javafx.scene.shape.Rectangle;

public class WrappedRectangle extends WrappedNode {

	public WrappedRectangle() {
		super(new Rectangle());
		Rectangle r = (Rectangle) getNode();
		r.widthProperty().bind(transformedWidthProperty());
		r.heightProperty().bind(transformedHeightProperty());
	}
	
	public WrappedRectangle(double w, double h) {
		this();
		setCalculatedHeight(h);
		setCalculatedWidth(w);
	}
}
