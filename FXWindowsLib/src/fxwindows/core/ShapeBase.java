package fxwindows.core;

import fxwindows.core.ColoredBoundedArea;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

public abstract class ShapeBase extends ColoredBoundedArea {

	private final ReadOnlyBooleanWrapper hovered = new ReadOnlyBooleanWrapper();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseClicked = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseDragged = new SimpleObjectProperty<>();
    private final DoubleProperty rotation = new SimpleDoubleProperty();

	public ShapeBase() {
		
    }

    /**Alpha, Rotation*/
    protected void setupGeneralBindings(Node node) {
		node.opacityProperty().bind(alphaProperty());
		node.visibleProperty().bind(alphaProperty().greaterThan(0));
		node.rotateProperty().bind(rotationProperty());
	}

	/**Background and Border color, border width*/
	protected void setupFillBindings(Shape node) {
		node.fillProperty().bind(backgroundColorProperty());
		node.strokeProperty().bind(borderColorProperty());
		node.strokeWidthProperty().bind(borderWidthProperty());
	}

    protected void setupClickedHandlers(Node node) {
        node.onMouseClickedProperty().bind(onMouseClicked);
        node.onMouseDraggedProperty().bind(onMouseDragged);
        hovered.bind(node.hoverProperty());
	}
	
	public abstract Node getNode();
	public abstract void clip(Node n);
	//public abstract void removeFromPane(Pane p);

    /**
     * Called once every frame so that components can update their state.
     * Default does nothing.
     * Subclasses may or may not implement this method, though subclasses
     * with children is advised to at least pass calls to their children.
     */
	public void update() {}

	// Other properties

	public ReadOnlyBooleanProperty hoveredProperty() {
		return hovered.getReadOnlyProperty();
	}

	public boolean isHovered() {
		return hoveredProperty().get();
	}

	public ObjectProperty<EventHandler<MouseEvent>> onMouseClickedProperty() {
	    return onMouseClicked;
    }

	public EventHandler<MouseEvent> getOnMouseClicked() {
		return onMouseClickedProperty().get();
	}

	public void setOnMouseClicked(EventHandler<MouseEvent> value) {
	    onMouseClickedProperty().set(value);
    }

    public ObjectProperty<EventHandler<MouseEvent>> onMouseDraggedProperty() {
        return onMouseDragged;
    }

    public EventHandler<MouseEvent> getOnMouseDragged() {
        return onMouseDraggedProperty().get();
    }

    public void setOnMouseDragged(EventHandler<MouseEvent> value) {
	    onMouseDraggedProperty().set(value);
    }

    public DoubleProperty rotationProperty() {
		return rotation;
	}

	public double getRotation() {
		return rotation.get();
	}

	public void setRotation(double value) {
		rotation.set(value);
	}
}
