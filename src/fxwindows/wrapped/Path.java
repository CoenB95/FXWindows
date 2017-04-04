package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.PathElement;

/**
 * A Circle.
 * For consistency, the decision has been made to - for now -
 * make no use of Circle's centerX and centerY.
 *
 * @author Coen Boelhouwers
 */
public class Path extends ShapeBase {

	private javafx.scene.shape.Path path;

	/**
	 * The implementation of the Path Shape.
	 * For now, the width and height of a Path are not calculated.
	 * This has been decided because there does not seem to be a reason one
	 * wants to know the size, it mostly is calculated using other shape's sizes.
	 */
	public Path() {
		path = new javafx.scene.shape.Path();
		path.layoutXProperty().bind(innerXProperty());
		path.layoutYProperty().bind(innerYProperty());
		setupTopLevelBindings(path);
		setupBackgroundBindings(path);
		setupMouseBindings(path);
	}

	public ObservableList<PathElement> getElements() {
		return path.getElements();
	}

	@Override
	public Node getNode() {
		return path;
	}

	@Override
	public void clip(Node n) {
		path.setClip(n);
	}
}
