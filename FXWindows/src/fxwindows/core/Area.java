package fxwindows.core;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleObjectProperty;

/**
 * The base class for all 'nodes' in FXWindows.
 * @author CoenB95
 *
 */
public abstract class Area extends Position {

	

	
	// Width and Height
	private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper();
	public ReadOnlyDoubleProperty heightProperty() {
		return height.getReadOnlyProperty();
	}
	
	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setHeight(double value) { height.set(value); }
	protected void bindHeight(DoubleExpression value) { height.bind(value); }
	public double getHeight() { return heightProperty().get(); }

	private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper();
	public ReadOnlyDoubleProperty widthProperty() {
		return width.getReadOnlyProperty();
	}

	/**Sets the calculated width of the object. To be used internal only.*/
	protected void setWidth(double value) { width.set(value); }
	protected void bindWidth(DoubleExpression value) { width.bind(value); }
	public double getWidth() { return widthProperty().get(); }

	
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

	private ObjectProperty<Runnable> onMouseClicked;
	public ObjectProperty<Runnable> onMouseClickedProperty() {
		if (onMouseClicked == null) onMouseClicked = new SimpleObjectProperty<>();
		return onMouseClicked;
	}
	public void setOnMouseClicked(Runnable r) { onMouseClickedProperty().set(r); }
	public Runnable getOnMouseClicked() { return onMouseClickedProperty().get(); }

	
}
