package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

/**
 * @author Coen Boelhouwers
 */
public class Canvas extends ShapeBase {

	private javafx.scene.canvas.Canvas canvas;

	public Canvas(double w, double h) {
		canvas = new javafx.scene.canvas.Canvas(w, h) {
			@Override
			public boolean isResizable() {
				return true;
			}

			@Override
			public double prefWidth(double height) {
				return getWidth();
			}

			@Override
			public double prefHeight(double width) {
				return getHeight();
			}
		};
		canvas.widthProperty().bind(widthProperty());
		canvas.heightProperty().bind(heightProperty());
	}

	public GraphicsContext getGraphics() {
		return canvas.getGraphicsContext2D();
	}

	@Override
	public Node getNode() {
		return canvas;
	}

	@Override
	public void clip(Node node) {
		canvas.setClip(node);
	}
}
