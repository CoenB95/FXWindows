package fxwindows.wrapped;

import fxwindows.core.ColoredBoundedArea;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public abstract class ShapeBase extends ColoredBoundedArea {
	
	public ShapeBase() {

	}
	
	public abstract void addToPane(Pane p);
	public abstract void clip(Node n);
	public abstract void removeFromPane(Pane p);

    /**
     * Called once every frame so that components can update their state.
     * Default does nothing.
     * Subclasses may or may not implement this method, though subclasses
     * with children is advised to at least pass calls to their children.
     */
	public void update() {}
}
