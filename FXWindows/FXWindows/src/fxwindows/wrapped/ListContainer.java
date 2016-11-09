package fxwindows.wrapped;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ListContainer extends Container {
	
	private DoubleProperty scroll;
	private double listHeight;
	
	public DoubleProperty scrollProperty() {
		if (scroll == null) {
			scroll = new SimpleDoubleProperty();
			scroll.addListener((a,b,c) -> {
				updateY();
			});
		}
		return scroll;
	}
	
	public double getScroll() { return scrollProperty().get(); }
	public void setScroll(double value) { scrollProperty().set(value); }

	public ListContainer() {
		getNode().setOpacity(1);
		((Rectangle) getNode()).setFill(Color.DARKSLATEBLUE);
		getChildren().addListener((Change<? extends WrappedNode> c) -> {
			while (c.next()) {
				for (WrappedNode w : c.getAddedSubList()) {
					w.baseXProperty().bind(transformedXProperty());
					w.transformedHeightProperty().addListener((a,b,c1) -> {
						updateY();
					});
					updateY();
				}
				for (WrappedNode w : c.getRemoved()) {
					w.baseXProperty().unbind();
					updateY();
				}
			}
		});
	}
	
	public double getListHeight() {
		return listHeight;
	}
	
	private void updateY() {
		double height = 0;
		double width = 0;
		for (WrappedNode w : getChildren()) {
			System.out.println("Child placed at y=" + (getY() + height + 
					getScroll()));
			w.setY(getY() + height + getScroll());
			height += w.getHeight();
			if (w.getWidth() > width) width = w.getWidth();
		}
		listHeight = height;
		setCalculatedHeight(Math.min(height,300));
		setCalculatedWidth(width);
		System.out.println("Rectangle: x="+getX()+", y="+getY()+", w="+getWidth()+
				", h="+getHeight());
	}
}
