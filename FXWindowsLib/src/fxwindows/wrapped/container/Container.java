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
	private Pane innerPane;
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

		pane = new Pane();
		pane.layoutXProperty().bind(xProperty());
		pane.layoutYProperty().bind(yProperty());
		pane.prefWidthProperty().bind(widthProperty());
        pane.prefHeightProperty().bind(heightProperty());
        pane.opacityProperty().bind(alphaProperty());
        
        // Setup all things needed to clip the children to the max height;
        innerPane = new Pane();
        innerPane.layoutXProperty().bind(paddingXProperty());
        innerPane.layoutYProperty().bind(paddingYProperty());
        innerPane.prefWidthProperty().bind(innerWidthProperty());
        innerPane.prefHeightProperty().bind(innerHeightProperty());

        rect = new javafx.scene.shape.Rectangle();
        // Don't bind position of rect, background and children. See note.
        rect.widthProperty().bind(innerWidthProperty());
        rect.heightProperty().bind(innerHeightProperty());
        innerPane.setClip(rect);


        // The background of this container.
        // Due to padding considerations, the background is no longer added to the
        // panel. Instead it should be drawn separately at the back. This way
        // children don't need to calculate padding.
        background = new javafx.scene.shape.Rectangle();
        background.fillProperty().bind(backgroundColorProperty());
        background.strokeProperty().bind(borderColorProperty());
        background.strokeWidthProperty().bind(borderWidthProperty());
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        pane.getChildren().addAll(background, innerPane);

		getChildren().addListener((ListChangeListener<ShapeBase>) c -> {
            while (c.next()) {
                for (ShapeBase w : c.getAddedSubList()) {
                    w.addToPane(innerPane);
                }
                for (ShapeBase w : c.getRemoved()) {
                    w.removeFromPane(innerPane);
                }
            }
        });
		setupClickedHandlers(innerPane);
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
		p.getChildren().addAll(pane);
	}

	@Override
	public void removeFromPane(Pane p) {
        p.getChildren().removeAll(pane);
	}
	
	@Override
	public void clip(Node n) {
	    // TODO Remove this method? Seems unneeded thanks to javafx.scene.Pane's clipping.
		// TODO Implement clip for lists?
	}
}
