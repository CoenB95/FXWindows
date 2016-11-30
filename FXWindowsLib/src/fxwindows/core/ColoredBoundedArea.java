package fxwindows.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Base class for a positioned that can be colored.
 * @author CoenB95
 *
 */
public abstract class ColoredBoundedArea extends BoundedArea implements Colorable {
	
	private DoubleProperty alpha;
	
	@Override
	public DoubleProperty alphaProperty() {
		if (alpha == null) alpha = new SimpleDoubleProperty(1);
		return alpha;
	}
	
	@Override
	public void setAlpha(double value) { alphaProperty().set(value); }
	
	@Override
	public double getAlpha() { return alphaProperty().get(); }

	private ObjectProperty<Paint> borderColor;
	
	@Override
	public ObjectProperty<Paint> borderColorProperty() {
		if (borderColor == null)
			borderColor = new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		return borderColor;
	}
	
	@Override
	public void setBorderColor(Paint value) { borderColorProperty().set(value); }
	
	@Override
	public Paint getBorderColor() { return borderColorProperty().get(); }
	
	private DoubleProperty borderWidth;
	
	@Override
	public DoubleProperty borderWidthProperty() {
		if (borderWidth == null)
			borderWidth = new SimpleDoubleProperty(1);
		return borderWidth;
	}
	
	@Override
	public void setBorderWidth(double value) { borderWidthProperty().set(value); }
	
	@Override
	public double getBorderWidth() { return borderWidthProperty().get(); }

	private ObjectProperty<Paint> backgroundColor;
	
	@Override
	public ObjectProperty<Paint> backgroundColorProperty() {
		if (backgroundColor == null)
			backgroundColor = new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		return backgroundColor;
	}
	
	@Override
	public void setBackgroundColor(Paint value) { backgroundColorProperty().set(value); }
	
	@Override
	public Paint getBackgroundColor() { return backgroundColorProperty().get(); }
}
