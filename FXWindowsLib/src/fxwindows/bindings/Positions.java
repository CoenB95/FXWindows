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

	public static PositionBinding bindBottomLeft(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), area.heightProperty());
	}

	public static PositionBinding bindBottomRight(Area area) {
		return new PositionBinding(area, area.widthProperty(), area.heightProperty());
	}

	public static PositionBinding bindCenter(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), area.heightProperty().divide(2));
	}

	public static PositionBinding bindCenterBottom(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), area.heightProperty());
	}

	public static PositionBinding bindCenterLeft(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), area.heightProperty().divide(2));
	}

	public static PositionBinding bindCenterRight(Area area) {
		return new PositionBinding(area, area.widthProperty(), area.heightProperty().divide(2));
	}

	public static PositionBinding bindCenterTop(Area area) {
		return new PositionBinding(area, area.widthProperty().divide(2), new SimpleDoubleProperty());
	}

	public static PositionBinding bindTopLeft(Area area) {
		return new PositionBinding(area, new SimpleDoubleProperty(), new SimpleDoubleProperty());
	}

	public static PositionBinding bindTopRight(Area area) {
		return new PositionBinding(area, area.widthProperty(), new SimpleDoubleProperty());
	}
}
