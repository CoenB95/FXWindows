package fxwindows.bindings;

import fxwindows.core.Area;
import fxwindows.core.Position;
import fxwindows.wrapped.container.Container;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
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

	public static PositionBinding bottomLeftOf(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), area.heightProperty());
	}

	public static PositionBinding bottomRightOf(Area area) {
		return new PositionBinding(area, area.widthProperty(), area.heightProperty());
	}

	public static PositionBinding centerOf(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), area.heightProperty().divide(2));
	}

	public static PositionBinding centerBottomOf(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), area.heightProperty());
	}

	public static PositionBinding centerLeftOf(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), area.heightProperty().divide(2));
	}

	public static PositionBinding centerRightOf(Area area) {
		return new PositionBinding(area, area.widthProperty(), area.heightProperty().divide(2));
	}

	public static PositionBinding centerTopOf(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), new SimpleDoubleProperty());
	}

	public static PositionBinding topLeftOf(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), new SimpleDoubleProperty());
	}

	public static PositionBinding topRightOf(Area area) {
		return new PositionBinding(area, area.widthProperty(), new SimpleDoubleProperty());
	}
}
