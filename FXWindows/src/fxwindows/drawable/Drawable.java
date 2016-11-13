package fxwindows.drawable;

import fxwindows.Animatable;
import javafx.scene.canvas.GraphicsContext;

/**
 * ...
 * <p><b>The positioning system</b>
 * 
 * <p>Positioning is based around the following concept:
 * <ul><li><b>Base:</b> the base position, rotation and size.</li>
 * <li><b>Transformation:</b> position, rotation and size changes,
 * relative to the base.</li></ul>
 * The position of this object should be set by changing the <i>base-position</i>
 * and applying transformations. Reading the position on the other hand,
 * should be done using the <i>transformed position</i>, as this is how
 * the object currently is visible.
 * @author PC1
 *
 */
public abstract class Drawable extends Animatable {

	public abstract void draw(GraphicsContext graphics);
	public Drawable mouse(double x, double y) {
		return (x > getX() && x < getX() + getWidth() &&
				y > getY() && y < getY() + getHeight()) ?
						this : null;
	}
	public void click() {
		if (onMouseClickedProperty().get() != null)
			onMouseClickedProperty().get().run();
	}
}