package fxwindows.wrapped;

import fxwindows.wrapped.container.ScrollContainer;
import fxwindows.core.ShapeBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;

/**
 * Base class for a vertical-list-like layout.
 * @author CoenB95
 */
public abstract class VerticalContainer extends ScrollContainer {

	private final DoubleProperty margin = new SimpleDoubleProperty();
	
	public VerticalContainer() {
		super();
		setMaxHeight(250);
		setMaxWidth(100);

		// When the amount of children changes, a vertical re-layout is needed.
		getChildren().addListener((Change<? extends ShapeBase> c) -> {
			while (c.next()) {
				for (ShapeBase w : c.getAddedSubList()) {
					w.maxWidthProperty().bind(maxWidthProperty());
				}
			}
		});
	}
	
	public DoubleProperty marginProperty() {
		return margin;
	}
	
	public double getMargin() {
		return margin.get();
	}
	
	public void setMargin(double value) {
		margin.set(value);
	}

	@Override
	public void update() {
		super.update();
		updateY();
	}

	/**Called to relocate all children.*/
	private void updateY() {
		double height = 0;
		double width = 0;
		for (ShapeBase w : getChildren()) {
			w.setXY(0, height + getScrollY());
			height += w.getHeight() + getMargin();
			if ((height + getScrollY() <= 0) ||
					(height + getScrollY() - w.getHeight()  > getInnerHeight())) {
				w.setAlpha(0);
			} else w.setAlpha(1);

			if (w.getWidth() > width) width = w.getWidth();
		}
		setContentHeight(height);
		setContentWidth(width);
	}
}
