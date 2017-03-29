package fxwindows.bindings;

import fxwindows.core.Area;
import fxwindows.wrapped.container.Container;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ObservableValue;

/**
 * Utility class containing functions to bind a position to a (side of) area.<p/>
 * Sample: shape1.bind(Positions.bottomOf(area1));
 *
 * @author Coen Boelhouwers
 */
public final class PositionBinding {

	private Area child;
	private final DoubleExpression x;
	private final DoubleExpression y;

	protected PositionBinding(Area area, DoubleExpression x, DoubleExpression y) {
		this.child = area;
		this.x = x;
		this.y = y;
	}

	public void toBottomLeft(Area other) {
		child.bindX(other.xProperty().subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty()).subtract(y));
	}

	public void toBottomRight(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty()).subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty()).subtract(y));
	}

	public void toCenter(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty().divide(2)).subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty().divide(2)).subtract(y));
	}

	public void toCenterBottom(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty().divide(2)).subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty()).subtract(y));
	}

	public void toCenterLeft(Area other) {
		child.bindX(other.xProperty().subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty().divide(2)).subtract(y));
	}

	public void toCenterRight(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty()).subtract(x));
		child.bindY(other.yProperty().add(other.heightProperty().divide(2)).subtract(y));
	}

	public void toCenterTop(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty().divide(2)).subtract(x));
		child.bindY(other.yProperty().subtract(y));
	}

	public void toInnerBottomLeft(Area other) {
		child.bindX(x.negate());
		child.bindY(other.innerHeightProperty().subtract(y));
	}

	public void toInnerBottomRight(Area other) {
		child.bindX(other.innerWidthProperty().subtract(x));
		child.bindY(other.innerHeightProperty().subtract(y));
	}

	public void toInnerCenter(Area other) {
		child.bindX(other.innerWidthProperty().divide(2).subtract(x));
		child.bindY(other.innerHeightProperty().divide(2).subtract(y));
	}

	public void toInnerCenterBottom(Area other) {
		child.bindX(other.innerWidthProperty().divide(2).subtract(x));
		child.bindY(other.innerHeightProperty().subtract(y));
	}

	public void toInnerCenterLeft(Area other) {
		child.bindX(x.negate());
		child.bindY(other.innerHeightProperty().divide(2).subtract(y));
	}

	public void toInnerCenterRight(Area other) {
		child.bindX(other.innerWidthProperty().subtract(x));
		child.bindY(other.innerHeightProperty().divide(2).subtract(y));
	}

	public void toInnerCenterTop(Area other) {
		child.bindX(other.innerWidthProperty().divide(2).subtract(x));
		child.bindY(y.negate());
	}

	public void toInnerTopLeft(Area other) {
		child.bindX(x.negate());
		child.bindY(y.negate());
	}

	public void toInnerTopRight(Area other) {
		child.bindX(other.innerWidthProperty().subtract(x));
		child.bindY(y.negate());
	}

	public void toTopLeft(Area other) {
		child.bindX(other.xProperty().subtract(x));
		child.bindY(other.yProperty().subtract(y));
	}

	public void toTopRight(Area other) {
		child.bindX(other.xProperty().add(other.widthProperty()).subtract(x));
		child.bindY(other.yProperty().subtract(y));
	}
}
