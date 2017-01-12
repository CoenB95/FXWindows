package fxwindows.core;

import fxwindows.core.ColoredBoundedArea;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class ShapeBase extends ColoredBoundedArea {

	private final ReadOnlyBooleanWrapper hovered = new ReadOnlyBooleanWrapper();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseClicked = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseDragged = new SimpleObjectProperty<>();

	public ShapeBase() {
		
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
}
