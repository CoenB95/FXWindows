package fxwindows.core;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Paint;

/**
 * Interface for objects that can be colored.
 * Background color, border color, border width and alpha.
 * @author CoenB95
 *
 */
public interface Colorable {
	
	DoubleProperty alphaProperty();
	void setAlpha(double value);
	double getAlpha();

	ObjectProperty<Paint> borderColorProperty();
	void setBorderColor(Paint value);
	Paint getBorderColor();
	
	DoubleProperty borderWidthProperty();
	void setBorderWidth(double value);
	double getBorderWidth();

	ObjectProperty<Paint> backgroundColorProperty();
	void setBackgroundColor(Paint value);
	Paint getBackgroundColor();
}
