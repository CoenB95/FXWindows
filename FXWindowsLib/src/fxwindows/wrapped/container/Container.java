package fxwindows.wrapped.container;

import java.util.ArrayList;

import fxwindows.core.ShapeBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * The Container class defines the basics for a ShapeBase containing
 * other ShapeBases. This class manages child-clipping to the container's
 * size, as well as drawing a background (default transparent).
 * How children should be positioned is left to subclasses.
 *
 * @author Coen Boelhouwers
 * @version 1.1
 */
public abstract class Container extends ShapeBase {

	private Pane pane;
	private Rectangle rect;
	private Rectangle background;

    private final ObservableList<ShapeBase> children =
			FXCollections.observableArrayList(new ArrayList<>());

	public ObservableList<ShapeBase> getChildren() {
		return children;
	}
	
	public Container() {
		super();

        // Important note!
        // The layoutXProperty of a Node is relative to the parent node.
        // So when we changed this code to move the local Pane,
        // we effectively doubled the position of all contents.

        // Setup all things needed to clip the children to the max height;
        pane = new Pane();
        pane.layoutXProperty().bind(xProperty());
        pane.layoutYProperty().bind(yProperty());
        pane.prefWidthProperty().bind(widthProperty());
        pane.prefHeightProperty().bind(heightProperty());

        rect = new javafx.scene.shape.Rectangle();
        // Don't bind position of rect, background and children. See note.
        rect.widthProperty().bind(widthProperty());
        rect.heightProperty().bind(heightProperty());
        pane.setClip(rect);


        // The background of this container.
        background = new javafx.scene.shape.Rectangle();
        background.opacityProperty().bind(alphaProperty());
        background.fillProperty().bind(backgroundColorProperty());
        background.strokeProperty().bind(borderColorProperty());
        background.strokeWidthProperty().bind(borderWidthProperty());
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        pane.getChildren().add(background);

		getChildren().addListener((ListChangeListener<ShapeBase>) c -> {
            while (c.next()) {
                for (ShapeBase w : c.getAddedSubList()) {
                    w.addToPane(pane);
                }
                for (ShapeBase w : c.getRemoved()) {
                    w.removeFromPane(pane);
                }
            }
        });
		setupClickedHandlers(pane);
	}

	public Pane getPane() {
	    return pane;
    }

    /**
     * Passes the update call to its children, as requested.
     */
	@Override
	public void update() {
		for (ShapeBase w : getChildren()) {
			w.update();
		}
	}

	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(pane);
	}

	@Override
	public void removeFromPane(Pane p) {
        p.getChildren().remove(pane);
	}
	
	@Override
	public void clip(Node n) {
	    // TODO Remove this method? Seems unneeded thanks to javafx.scene.Pane's clipping.
		// TODO Implement clip for lists?
	}
}
