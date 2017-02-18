package fxwindows.core;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

/**
 * The base class for all 'nodes' in FXWindows.
 * @author CoenB95
 *
 */
public abstract class Area extends Position {

	private final DoubleProperty contentHeight = new SimpleDoubleProperty();
	private final DoubleProperty contentWidth = new SimpleDoubleProperty();
	private final ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper();
	private final ObjectProperty<LayoutBehavior> heightBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	private final DoubleProperty maxHeight = new SimpleDoubleProperty();
	private final DoubleProperty maxWidth = new SimpleDoubleProperty();
	private final DoubleProperty paddingX = new SimpleDoubleProperty();
	private final DoubleProperty paddingY = new SimpleDoubleProperty();
	private final ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper();
	private final ObjectProperty<LayoutBehavior> widthBehavior =
			new SimpleObjectProperty<>(LayoutBehavior.WRAP_CONTENT);
	private ReadOnlyDoubleWrapper innerX;
	private ReadOnlyDoubleWrapper innerY;
	private ReadOnlyDoubleWrapper innerWidth;
	private ReadOnlyDoubleWrapper innerHeight;

	public Area() {
		super();
		height.bind(Bindings.createDoubleBinding(() -> {
					switch(getHeightBehavior()) {
						case FILL_SPACE:
							return getMaxHeight();
						case WRAP_CONTENT:
							return getContentHeight() + 2 * getPaddingY();
						default:
							return 0.0;
					}
				}, contentHeightProperty(), maxHeightProperty(), heightBehaviorProperty(),
				paddingYProperty()));

		width.bind(Bindings.createDoubleBinding(() -> {
					switch(getWidthBehavior()) {
						case FILL_SPACE:
							return getMaxWidth();
						case WRAP_CONTENT:
							return getContentWidth() + 2 * getPaddingX();
						default:
							return 0.0;
					}
				}, contentWidthProperty(), maxWidthProperty(), widthBehaviorProperty(),
				paddingXProperty()));
	}

	//Properties
	protected DoubleProperty contentHeightProperty() {
		return contentHeight;
	}

	protected DoubleProperty contentWidthProperty() {
		return contentWidth;
	}

	public ObjectProperty<LayoutBehavior> heightBehaviorProperty() {
		return heightBehavior;
	}

	public ReadOnlyDoubleProperty heightProperty() {
		return height.getReadOnlyProperty();
	}

	public ReadOnlyDoubleProperty innerHeightProperty() {
		if (innerHeight == null) {
			innerHeight = new ReadOnlyDoubleWrapper();
			innerHeight.bind(heightProperty().subtract(paddingYProperty().multiply(2)));
		}
		return innerHeight.getReadOnlyProperty();
	}

	public ReadOnlyDoubleProperty innerWidthProperty() {
		if (innerWidth == null) {
			innerWidth = new ReadOnlyDoubleWrapper();
			innerWidth.bind(widthProperty().subtract(paddingXProperty().multiply(2)));
		}
		return innerWidth.getReadOnlyProperty();
	}

	public ReadOnlyDoubleProperty innerXProperty() {
		if (innerX == null) {
			innerX = new ReadOnlyDoubleWrapper();
			innerX.bind(xProperty().add(paddingXProperty()));
		}
		return innerX.getReadOnlyProperty();
	}

	public ReadOnlyDoubleProperty innerYProperty() {
		if (innerY == null) {
			innerY = new ReadOnlyDoubleWrapper();
			innerY.bind(yProperty().add(paddingYProperty()));
		}
		return innerY.getReadOnlyProperty();
	}

	public DoubleProperty maxHeightProperty() {
		return maxHeight;
	}

	public DoubleProperty maxWidthProperty() {
		return maxWidth;
	}

	public DoubleProperty paddingXProperty() {
		return paddingX;
	}

	public DoubleProperty paddingYProperty() {
		return paddingY;
	}

	public ObjectProperty<LayoutBehavior> widthBehaviorProperty() {
		return widthBehavior;
	}

	public ReadOnlyDoubleProperty widthProperty() {
		return width.getReadOnlyProperty();
	}

	//Getters
	protected double getContentHeight() {
		return contentHeightProperty().get();
	}

	protected double getContentWidth() {
		return contentWidthProperty().get();
	}

	public double getHeight() {
		return heightProperty().get();
	}

	public LayoutBehavior getHeightBehavior() {
		return heightBehaviorProperty().get();
	}

	public double getInnerHeight() {
		return innerHeightProperty().get();
	}

	public double getInnerWidth() {
		return innerWidthProperty().get();
	}

	public double getInnerX() {
		return innerXProperty().get();
	}

	public double getInnerY() {
		return innerYProperty().get();
	}

	public double getMaxHeight() {
		return maxHeightProperty().get();
	}

	public double getMaxWidth() {
		return maxWidthProperty().get();
	}

	public double getPaddingX() {
		return paddingXProperty().get();
	}

	public double getPaddingY() {
		return paddingYProperty().get();
	}

	public double getWidth() {
		return widthProperty().get();
	}

	public LayoutBehavior getWidthBehavior() {
		return widthBehaviorProperty().get();
	}

	//Setters
	protected void setContentHeight(double value) {
		contentHeightProperty().set(value);
	}

	protected void setContentWidth(double value) {
		contentWidthProperty().set(value);
	}

	public void setHeightBehavior(LayoutBehavior value) {
		heightBehaviorProperty().set(value);
	}

	/**
	 * Sets the LayoutBehavior of both the height and the width of
	 * this BoundedArea.
	 * @param value the new LayoutBehavior, defaults to WRAP_CONTENT.
	 */
	public void setLayoutBehavior(LayoutBehavior value) {
		widthBehaviorProperty().set(value);
		heightBehaviorProperty().set(value);
	}

	public void setMaxHeight(double value) {
		maxHeightProperty().set(value);
	}

	public void setMaxWidth(double value) {
		maxWidthProperty().set(value);
	}

	public void setPadding(double value) {
		setPaddingXY(value, value);
	}

	public void setPaddingX(double value) {
		paddingX.set(value);
	}

	public void setPaddingXY(double x, double y) {
		setPaddingX(x);
		setPaddingY(y);
	}

	public void setPaddingY(double value) {
		paddingY.set(value);
	}

	public void setWidthBehavior(LayoutBehavior value) {
		widthBehaviorProperty().set(value);
	}
}
