package fxwindows.core;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * Utility class containing functions to bind a position to a (side of) area.<p/>
 * Sample: shape1.bind(Positions.bottomOf(area1));
 *
 * @author Coen Boelhouwers
 */
public final class XY {

	private final ReadOnlyDoubleWrapper x;
	private final ReadOnlyDoubleWrapper y;

	private XY() {
		this.x = new ReadOnlyDoubleWrapper();
		this.y = new ReadOnlyDoubleWrapper();
	}

	private XY(double x, double y) {
		this();
		this.x.set(x);
		this.y.set(y);
	}

	private XY(DoubleExpression x, DoubleExpression y) {
		this();
		this.x.bind(x);
		this.y.bind(y);
	}

	public XY minus(Area other) {
		return new XY(xProperty().subtract(other.widthProperty()),
				yProperty().subtract(other.heightProperty()));
	}

	public XY minusWidth(Area other) {
		return new XY(xProperty().subtract(other.widthProperty()), yProperty());
	}

	public XY minusHeight(Area other) {
		return new XY(xProperty(), yProperty().subtract(other.heightProperty()));
	}

	public static XY bottomLeftOf(Area other) {
		return new XY(other.xProperty(),
				other.yProperty().add(other.heightProperty()));
	}

	public static XY bottomRightOf(Area other) {
		return new XY(other.xProperty().add(other.widthProperty()),
				other.yProperty().add(other.heightProperty()));
	}

	public static XY topLeftOf(Area other) {
		return new XY(other.xProperty(),
				other.yProperty());
	}

	public static XY topRightOf(Area other) {
		return new XY(other.xProperty().add(other.widthProperty()),
				other.yProperty());
	}

	public static XY centerOf(Area other) {
		return new XY(other.xProperty().add(other.widthProperty().divide(2)),
				other.yProperty().add(other.heightProperty().divide(2)));
	}

	public double getX() {
		return x.get();
	}

	public double getY() {
		return y.get();
	}

	public ReadOnlyDoubleProperty xProperty() {
		return x.getReadOnlyProperty();
	}

	public ReadOnlyDoubleProperty yProperty() {
		return y.getReadOnlyProperty();
	}
}
