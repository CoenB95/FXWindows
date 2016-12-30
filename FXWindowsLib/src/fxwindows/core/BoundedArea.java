package fxwindows.core;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class BoundedArea extends PaddedArea {

	private final DoubleProperty contentHeight = new SimpleDoubleProperty();
	private final DoubleProperty contentWidth = new SimpleDoubleProperty();
	private final DoubleProperty maxHeight = new SimpleDoubleProperty();
	private final DoubleProperty maxWidth = new SimpleDoubleProperty();
	private final ObjectProperty<LayoutBehavior> heightBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	private final ObjectProperty<LayoutBehavior> widthBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	
	private DoubleExpression heightBinding;
	private DoubleExpression widthBinding;

	public BoundedArea() {
		super();
		heightBinding = Bindings.createDoubleBinding(() -> {
			switch(getHeightBehavior()) {
			case FILL_SPACE:
				return getMaxHeight();
			case WRAP_CONTENT:
				return getContentHeight() + 2 * getPaddingY();
			default:
				return 0.0;
			}
		}, contentHeightProperty(), maxHeightProperty(), heightBehaviorProperty(),
				paddingYProperty());
		bindHeight(heightBinding);
		
		widthBinding = Bindings.createDoubleBinding(() -> {
			switch(getWidthBehavior()) {
			case FILL_SPACE:
				return getMaxWidth();
			case WRAP_CONTENT:
				return getContentWidth() + 2 * getPaddingX();
			default:
				return 0.0;
			}
		}, contentWidthProperty(), maxWidthProperty(), widthBehaviorProperty(),
                paddingXProperty());
		bindWidth(widthBinding);
	}
	// 
	protected DoubleProperty contentHeightProperty() {
		return contentHeight;
	}

	protected double getContentHeight() {
		return contentHeightProperty().get();
	}

	protected void setContentHeight(double value) {
		contentHeightProperty().set(value);
	}

	// 
	protected DoubleProperty contentWidthProperty() {
		return contentWidth;
	}

	protected double getContentWidth() {
		return contentWidthProperty().get();
	}

	protected void setContentWidth(double value) {
		contentWidthProperty().set(value);
	}

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
