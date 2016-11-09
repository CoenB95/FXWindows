package fxwindows.wrapped;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class WrappedNode {

	private Node node;
	
	public WrappedNode(Node node) {
		this.node = node;
		setupBindings();
	}
	
	private void setupBindings() {
		node.layoutXProperty().bind(baseXProperty());
		node.layoutYProperty().bind(baseYProperty());
		node.translateXProperty().bind(transformXProperty());
		node.translateYProperty().bind(transformYProperty());
	}
	
	// Base-position
	private DoubleProperty baseX;
	
	public DoubleProperty baseXProperty() {
		if (baseX == null) {
			baseX = new SimpleDoubleProperty();
			node.layoutXProperty().bind(baseX);
		}
		return baseX;
	}
	
	private DoubleProperty baseY;
	
	public DoubleProperty baseYProperty() {
		if (baseY == null) baseY = new SimpleDoubleProperty();
		return baseY;
	}
	
	private DoubleProperty baseHeight;
	
	protected DoubleProperty baseHeightProperty() {
		if (baseHeight == null) baseHeight = new SimpleDoubleProperty();
		return baseHeight;
	}
	
	private DoubleProperty baseWidth;
	
	protected DoubleProperty baseWidthProperty() {
		if (baseWidth == null) baseWidth = new SimpleDoubleProperty();
		return baseWidth;
	}
	
	
	// Transforms
	private DoubleProperty transformX;
	public DoubleProperty transformXProperty() {
		if (transformX == null) transformX = new SimpleDoubleProperty();
		return transformX;
	}
	private DoubleProperty transformY;
	public DoubleProperty transformYProperty() {
		if (transformY == null) transformY = new SimpleDoubleProperty();
		return transformY;
	}
	
	
	// Transformed-position
	private ReadOnlyDoubleWrapper transformedX;
	
	public ReadOnlyDoubleProperty transformedXProperty() {
		if (transformedX == null) {
			transformedX = new ReadOnlyDoubleWrapper();
			transformedX.bind(baseXProperty().add(transformXProperty()));
		}
		return transformedX.getReadOnlyProperty();
	}
	
	private ReadOnlyDoubleWrapper transformedY;
	
	public ReadOnlyDoubleProperty transformedYProperty() {
		if (transformedY == null) {
			transformedY = new ReadOnlyDoubleWrapper();
			transformedY.bind(baseYProperty().add(transformYProperty()));
		}
		return transformedY.getReadOnlyProperty();
	}
	
	private ReadOnlyDoubleWrapper transformedHeight;
	
	public ReadOnlyDoubleProperty transformedHeightProperty() {
		if (transformedHeight == null) {
			transformedHeight = new ReadOnlyDoubleWrapper();
			transformedHeight.bind(baseHeightProperty());
		}
		return transformedHeight.getReadOnlyProperty();
	}
	
	private ReadOnlyDoubleWrapper transformedWidth;
	
	public ReadOnlyDoubleProperty transformedWidthProperty() {
		if (transformedWidth == null) {
			transformedWidth = new ReadOnlyDoubleWrapper();
			transformedWidth.bind(baseWidthProperty());
		}
		return transformedWidth.getReadOnlyProperty();
	}
	
	
	// Other
	private DoubleProperty opacity;
	
	public DoubleProperty opacityProperty() {
		if (opacity == null) opacity = new SimpleDoubleProperty(1);
		return opacity;
	}
	
	private ReadOnlyBooleanWrapper hovered;
	
	public ReadOnlyBooleanProperty hoveredProperty() {
		if (hovered == null) hovered = new ReadOnlyBooleanWrapper();
		return hovered.getReadOnlyProperty();
	}
	
	public void setHovered(boolean value) { hovered.set(value); }
	public boolean isHovered() { return hoveredProperty().get(); }
	
	
	
	/**@return the transformed position.*/
	public double getX() {
		return transformedXProperty().get();
	}
	/**@return the transformed position.*/
	public double getY() {
		return transformedYProperty().get();
	}
	/**Sets the base position.
	 * @param x the new value.*/
	public void setX(double x) {
		this.baseXProperty().set(x);
	}
	/**Sets the base position.
	 * @param y the new value.*/
	public void setY(double y) {
		this.baseYProperty().set(y);
	}
	/**Sets the base position.
	 * @param x the new x value.
	 * @param y the new y value.*/
	public void setXY(double x, double y) {
		setX(x);
		setY(y);
	}
	/**Sets the transformed position.
	 * @param x the new value.*/
	public void transformX(double x) {
		this.transformXProperty().set(x);
	}
	/**Sets the transformed position.
	 * @param x the new value.*/
	public void transformY(double y) {
		this.transformYProperty().set(y);
	}
	public final double getHeight() {
		return transformedHeightProperty().get();
	}
	public final double getWidth() {
		return transformedWidthProperty().get();
	}
	protected void setCalculatedHeight(double value) {
		baseHeightProperty().set(value);
	}
	protected void setCalculatedWidth(double value) {
		baseWidthProperty().set(value);
	}
	public void setOpacity(double value) {
		opacityProperty().set(value);
	}
	public double getOpacity() {
		return opacityProperty().get();
	}
	public Node getNode() {
		return node;
	}
	public void addToStage(Pane p) {
		p.getChildren().add(getNode());
	}
}
