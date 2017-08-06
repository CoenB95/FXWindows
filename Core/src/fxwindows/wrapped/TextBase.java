package fxwindows.wrapped;

import fxwindows.core.ShapeBase;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Base for text-related controls, defining textProperty,
 * fontProperty, textColorProperty and wrappingWidthProperty.
 * 
 * @author CoenB95
 */
public abstract class TextBase extends ShapeBase {

	private final StringProperty text = new SimpleStringProperty();

	// Font
	private ObjectProperty<Font> font = new SimpleObjectProperty<Font>(Font.getDefault());
	
	public ObjectProperty<Font> fontProperty() {
		return font;
	}
	
	public void setFont(Font value) {
		fontProperty().set(value);
	}
	
	public Font getFont() {
		return fontProperty().get();
	}


	// TextColor
	private ObjectProperty<Paint> textColor = new SimpleObjectProperty<Paint>(Color.BLACK);
	
	public ObjectProperty<Paint> textColorProperty() {
		return textColor;
	}
	
	public void setTextColor(Paint value) {
		textColorProperty().set(value);
	}
	
	public Paint getTextColor() {
		return textColorProperty().get();
	}


	// Text
	public StringProperty textProperty() {
		return text;
	}
	
	public void setText(String value) {
		textProperty().set(value);
	}
	
	public String getText() {
		return textProperty().get();
	}


	// WrappingWidth
	private BooleanProperty wrapText = new SimpleBooleanProperty();
	
	public BooleanProperty wrapTextProperty() {
		return wrapText;
	}
	
	public void setWrapText(boolean value) {
		wrapTextProperty().set(value);
	}
	
	public boolean getWrapText() {
		return wrapTextProperty().get();
	}
}
