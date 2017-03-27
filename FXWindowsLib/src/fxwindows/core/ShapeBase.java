package fxwindows.core;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public abstract class ShapeBase extends ColoredBoundedArea {

	private final ReadOnlyBooleanWrapper hovered = new ReadOnlyBooleanWrapper();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseClicked = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<MouseEvent>> onMouseDragged = new SimpleObjectProperty<>();
    private final DoubleProperty rotation = new SimpleDoubleProperty();

	public ShapeBase() {

	}

	public void setDraggable(Draggable draggable) {
		getNode().setOnDragDetected(event -> {
			Dragboard board = getNode().startDragAndDrop(TransferMode.COPY);
			draggable.setupDragboard(board);
			event.consume();
		});
	}

	/**
	 * Sets up this Shape in order to receive notifications when some
	 * other Shape is dragged over this one.
	 * @param dropable logic to determine if the data of this drag
	 *                      is a valid candidate.
	 */
	public void setDropable(Dropable dropable) {
		if (dropable == null) {
			getNode().setOnDragOver(null);
			getNode().setOnDragEntered(null);
			getNode().setOnDragExited(null);
			getNode().setOnDragDropped(null);
			return;
		}
		getNode().setOnDragOver(event -> {
			if (event.getGestureSource() != getNode() &&
					dropable.onValidateDrag(event.getDragboard()))
				event.acceptTransferModes(TransferMode.COPY);
			event.consume();
		});
		getNode().setOnDragEntered(event -> {
			if (event.getGestureSource() != getNode())
				dropable.onDragEnter(event.getDragboard(), dropable.onValidateDrag(event.getDragboard()));
			event.consume();

		});
		getNode().setOnDragExited(event -> {
			dropable.onDragExit();
			event.consume();
		});
		getNode().setOnDragDropped(event -> {
			event.setDropCompleted(dropable.onDragDropped(event.getDragboard()));
			event.consume();
		});
	}
	
	public abstract Node getNode();
	public abstract void clip(Node n);

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

	/**
	 * @deprecated use {@link #getNode()}.onMouseClickedProperty() instead.
	 */
	@Deprecated
	public ObjectProperty<EventHandler<MouseEvent>> onMouseClickedProperty() {
		return onMouseClicked;
	}

	/**
	 * @deprecated use {@link #getNode()}.getOnMouseClicked() instead.
	 */
	@Deprecated
	public EventHandler<MouseEvent> getOnMouseClicked() {
		return onMouseClickedProperty().get();
	}

	/**
	 * @deprecated use {@link #getNode()}.setOnMouseClicked() instead.
	 */
	@Deprecated
	public void setOnMouseClicked(EventHandler<MouseEvent> value) {
		onMouseClickedProperty().set(value);
	}

	/**
	 * @deprecated use {@link #getNode()}.onMouseDraggedProperty() instead.
	 */
	@Deprecated
	public ObjectProperty<EventHandler<MouseEvent>> onMouseDraggedProperty() {
		return onMouseDragged;
	}

	/**
	 * @deprecated use {@link #getNode()}.getOnMouseDragged() instead.
	 */
	@Deprecated
	public EventHandler<MouseEvent> getOnMouseDragged() {
		return onMouseDraggedProperty().get();
	}

	/**
	 * @deprecated use {@link #getNode()}.setOnMouseDragged() instead.
	 */
	@Deprecated
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

	/**
	 * Creates bindings for the alpha, background color,
	 * border color and border width of this Shape.
	 *
	 * @param shape the base shape.
	 */
	protected void setupBackgroundBindings(Shape shape) {
		setupBackgroundBindings(shape, true);
	}

	/**
	 * Creates bindings for the background color,
	 * border color and border width of this Shape.
	 *
	 * @param shape the base shape.
	 * @param fill whether fill should be bound (Line bug).
	 */
	protected void setupBackgroundBindings(Shape shape, boolean fill) {
		if (fill) shape.fillProperty().bind(backgroundColorProperty());
		shape.strokeProperty().bind(borderColorProperty());
		shape.strokeWidthProperty().bind(borderWidthProperty());
	}

	/**
	 * Creates bindings for the scale.
	 *
	 * @param node the base node.
	 */
	protected void setupScaleBindings(Node node) {
		Scale scale = Transform.scale(1, 1, 0, 0);
		scale.xProperty().bind(scaleXProperty());
		scale.yProperty().bind(scaleYProperty());
		node.getTransforms().add(scale);
	}

	/**
	 * Creates bindings for the alpha, visibility and rotation.
	 *
	 * @param node the base node.
	 */
	protected void setupTopLevelBindings(Node node) {
		node.opacityProperty().bind(alphaProperty());
		node.rotateProperty().bind(rotationProperty());
	}

	/**
	 * Creates bindings for the mouseClick and mouseDrag.
	 *
	 * @param node the base node.
	 */
	protected void setupMouseBindings(Node node) {
		//node.onMouseClickedProperty().bind(onMouseClicked);
		//node.onMouseDraggedProperty().bind(onMouseDragged);
		hovered.bind(node.hoverProperty());
	}
}
