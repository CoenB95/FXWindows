package fxwindows.bindings;

import fxwindows.core.Area;
import fxwindows.wrapped.container.Container;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ObservableValue;

/**
 * Utility class containing functions to bind a position to a (side of) area.<p/>
 * Sample: shape1.bind(Positions.bottomOf(area1));
 *
 * @author Coen Boelhouwers
 */
public final class Positions {

	private Positions() {

	}

	/**
	 * Centers one Area relative to another. Strategy depends on whether the Areas are related
	 * (parent-child).
	 * @param area
	 * @param other
	 */
	public static void center(Area area, Area other) {
		if (other instanceof Container && ((Container) other).getChildren().contains(other))
			centerInto(area, (Container) other);
		else centerOnto(area, other);
	}
	/**
	 * Binds the child's position to stay in the center of its parent.
	 * The parent must be a Container that contains the child. If not, use {@link #centerOnto(Area, Area)}
	 * instead.
	 *
	 * @param child the child that wants to be positioned.
	 * @param parent the wrapping container.
	 */
	private static void centerInto(Area child, Container parent) {
		child.bindX(parent.widthProperty().divide(2).subtract(child.widthProperty().divide(2)));
		child.bindY(parent.heightProperty().divide(2).subtract(child.heightProperty().divide(2)));
	}

	/**
	 * Binds the child's position to stay in the center of the other Area.
	 * Assumes both Areas are at the same level in the parent tree.
	 *
	 * @param child the child that wants to be positioned.
	 * @param other the other area the child wants to align to.
	 */
	private static void centerOnto(Area child, Area other) {
		child.bindX(other.innerXProperty().add(other.widthProperty().divide(2))
				.subtract(child.widthProperty().divide(2)));
		child.bindY(other.innerYProperty().add(other.heightProperty().divide(2))
				.subtract(child.heightProperty().divide(2)));
	}
}
