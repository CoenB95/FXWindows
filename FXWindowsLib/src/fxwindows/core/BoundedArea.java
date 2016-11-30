package fxwindows.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class BoundedArea extends Area {
	
	private final DoubleProperty maxHeight = new SimpleDoubleProperty();
	private final DoubleProperty maxWidth = new SimpleDoubleProperty();
	private final ObjectProperty<LayoutBehavior> heightBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	private final ObjectProperty<LayoutBehavior> widthBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	
	public DoubleProperty maxHeightProperty() {
		return maxHeight;
	}
	
	public double getMaxHeight() {
		return maxHeightProperty().get();
	}
	
	public void setMaxHeight(double value) {
		maxHeightProperty().set(value);
	}
	
	public DoubleProperty maxWidthProperty() {
		return maxWidth;
	}
	
	public double getMaxWidth() {
		return maxWidthProperty().get();
	}
	
	public void setMaxWidth(double value) {
		maxWidthProperty().set(value);
	}
	
	public ObjectProperty<LayoutBehavior> heightBehaviorProperty() {
		return heightBehavior;
	}
	
	public LayoutBehavior getHeightBehavior() {
		return heightBehaviorProperty().get();
	}
	
	public void setHeightBehavior(LayoutBehavior value) {
		heightBehaviorProperty().set(value);
	}
	
	public ObjectProperty<LayoutBehavior> widthBehaviorProperty() {
		return widthBehavior;
	}
	
	public LayoutBehavior getWidthBehavior() {
		return widthBehaviorProperty().get();
	}
	
	public void setWidthBehavior(LayoutBehavior value) {
		widthBehaviorProperty().set(value);
	}
	
	public enum LayoutBehavior {
		FILL_SPACE, WRAP_CONTENT
	}
}
