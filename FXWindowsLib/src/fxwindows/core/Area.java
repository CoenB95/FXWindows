package fxwindows.core;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.*;

/**
 * The base class for all 'nodes' in FXWindows.
 * @author CoenB95
 *
 */
public abstract class Area extends Position {

	// Width and Height
	private final DoubleProperty baseHeight = new SimpleDoubleProperty();
	private final ReadOnlyDoubleWrapper transformedHeight =
            new ReadOnlyDoubleWrapper();

	public ReadOnlyDoubleProperty heightProperty() {
		return transformedHeight.getReadOnlyProperty();
	}
	
	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setHeight(double value) {
	    baseHeight.set(value);
	}

	protected void unbindHeight() {
	    baseHeight.unbind();
	}

	protected void bindHeight(DoubleExpression value) {
	    baseHeight.bind(value);
	}

	public double getHeight() {
	    return heightProperty().get();
	}

	private final DoubleProperty baseWidth = new SimpleDoubleProperty();
	private final ReadOnlyDoubleWrapper transformedWidth =
            new ReadOnlyDoubleWrapper();

	public ReadOnlyDoubleProperty widthProperty() {
		return transformedWidth.getReadOnlyProperty();
	}

	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setWidth(double value) {
	    baseWidth.set(value);
	}

	protected void unbindWidth() {
	    baseWidth.unbind();
	}

	protected void bindWidth(DoubleExpression value) {
	    baseWidth.bind(value);
	}

	public double getWidth() {
	    return transformedWidth.get();
	}


	public Area() {
	    transformedHeight.bind(baseHeight);
        transformedWidth.bind(baseWidth);
    }

	// Other properties
	private ReadOnlyBooleanWrapper hovered;
	public ReadOnlyBooleanProperty hoveredProperty() {
		if (hovered == null) hovered = new ReadOnlyBooleanWrapper();
		return hovered.getReadOnlyProperty();
	}
	public void setHovered(boolean value) { 
		if (hovered == null) hovered = new ReadOnlyBooleanWrapper();
		hovered.set(value); }
	public boolean isHovered() { return hoveredProperty().get(); }

	private final List<Runnable> onMouseClicked = new ArrayList<>();
	public void addOnMouseClicked(Runnable r) {
		onMouseClicked.add(r);
	}
	public List<Runnable> getOnMouseClickedListeners() {
		return onMouseClicked;
	}

	
}
