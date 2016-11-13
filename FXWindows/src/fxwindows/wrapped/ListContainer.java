package fxwindows.wrapped;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ListContainer extends Container {
	
	private Rectangle background;
	private Rectangle rect;
	private DoubleProperty scroll;
	private double listHeight;
	private boolean updateRequested = true;
	
	public DoubleProperty scrollProperty() {
		if (scroll == null) {
			scroll = new SimpleDoubleProperty();
			scroll.addListener((a,b,c) -> {
				updateRequested = true;
			});
		}
		return scroll;
	}
	
	public double getScroll() { return scrollProperty().get(); }
	public void setScroll(double value) { scrollProperty().set(value); }

	public ListContainer() {
		super();
		background = new Rectangle();
		background.opacityProperty().bind(alphaProperty());
		background.fillProperty().bind(backgroundColorProperty());
		background.layoutXProperty().bind(transformedXProperty());
		background.layoutYProperty().bind(transformedYProperty());
		background.widthProperty().bind(widthProperty());
		background.heightProperty().bind(heightProperty());
		getChildren().addListener((Change<? extends WrappedNode> c) -> {
			while (c.next()) {
				for (WrappedNode w : c.getAddedSubList()) {
					w.bindX(transformedXProperty());
					w.heightProperty().addListener((a,b,c1) -> {
						updateRequested = true;
					});
					//w.addToPane(pane);
					updateRequested = true;
				}
				for (WrappedNode w : c.getRemoved()) {
					//w.baseXProperty().unbind();
					updateRequested = true;
				}
			}
		});
		//pane = new Pane(rect);
	}
	
	@Override
	public void update(long time) {
		super.update(time);
		if (updateRequested) updateY();
	}
	
	public double getListHeight() {
		return listHeight;
	}
	
	private void updateY() {
		updateRequested = false;
		double height = 0;
		double width = 0;
		for (WrappedNode w : getChildren()) {
			//System.out.println("Child placed at y=" + (getY() + height + 
			//		getScroll()));
			w.setY(getY() + height + getScroll());
			height += w.getHeight();
			if ((height + getScroll() <= 0) ||
					(height - w.getHeight() + getScroll() > 300)) {
				w.setAlpha(0);
			} else w.setAlpha(1);
			
			if (w.getWidth() > width) width = w.getWidth();
		}
		listHeight = height;
		setHeight(Math.min(height,300));
		setWidth(width);
		//System.out.println("Rectangle: x="+getX()+", y="+getY()+", w="+getWidth()+
		//		", h="+getHeight());
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(background);
		super.addToPane(p);
		//super.addToPane(pane);
		//p.getChildren().add(pane);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(background);
		super.removeFromPane(p);
	}
}
