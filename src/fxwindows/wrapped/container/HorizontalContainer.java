package fxwindows.wrapped.container;

import fxwindows.core.ShapeBase;
import fxwindows.wrapped.container.ScrollContainer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;

/**
 * Container that lays out its children in a vertical fashion.
 * 
 * @author CoenB95
 */
public class HorizontalContainer extends ScrollContainer {

	private final DoubleProperty margin = new SimpleDoubleProperty();
	private ChildAlignment childAlignment = ChildAlignment.TOP;

	public HorizontalContainer() {
		super();
		setMaxHeight(100);
		setMaxWidth(250);
	}
	
	public DoubleProperty marginProperty() {
		return margin;
	}

	public ChildAlignment getChildAlignment() {
		return childAlignment;
	}

	public void setChildAlignment(ChildAlignment value) {
		childAlignment = value;
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
		updateX();
	}

	/**Called to relocate all children.*/
	private void updateX() {
		double height = 0;
		double width = 0;
		for (ShapeBase w : getChildren()) {
			w.setX(width + getScrollX());
			width += w.getWidth() + getMargin();
			if ((width + getScrollX() <= 0) ||
					(width + getScrollX() - w.getWidth()  > getInnerWidth())) {
				w.getNode().setVisible(false);
			} else w.getNode().setVisible(true);

			if (w.getHeight() > height) height = w.getHeight();
		}
		for (ShapeBase w : getChildren()) {
			switch (childAlignment) {
				case TOP:
					w.setY(0);
					break;
				case CENTER:
					w.setY((height - w.getHeight()) / 2);
					break;
				case BOTTOM:
					w.setY(height - w.getHeight());
					break;
			}
		}
		setContentHeight(height);
		setContentWidth(width);
	}

	public enum ChildAlignment {
		TOP, CENTER, BOTTOM
	}
}
