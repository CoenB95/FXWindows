package fxwindows.wrapped;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Texie extends WrappedNode {

	private Group group;
	private Text textNode;
	private Rectangle rectNode;

	// Font
	private ObjectProperty<Font> font;
	public ObjectProperty<Font> fontProperty() {
		if (font == null) {
			font = new SimpleObjectProperty<Font>(Font.getDefault());
			textNode.fontProperty().bind(font);
		}
		return font;
	}
	public void setFont(Font value) { fontProperty().set(value); }
	public Font getFont() { return fontProperty().get(); }


	// TextColor
	private ObjectProperty<Paint> textColor;
	public ObjectProperty<Paint> textColorProperty() {
		if (textColor == null) {
			textColor = new SimpleObjectProperty<Paint>(Color.BLACK);
			textNode.fillProperty().bind(textColor);
		}
		return textColor;
	}
	public void setTextColor(Paint value) { textColorProperty().set(value); }
	public Paint getTextColor() { return textColorProperty().get(); }


	// Text
	private StringProperty text;
	public StringProperty textProperty() {
		if (text == null) {
			text = new SimpleStringProperty();
			textNode.textProperty().bind(text);
		}
		return text;
	}
	public void setText(String value) { textProperty().set(value); }
	public String getText() { return textProperty().get(); }


	// WrappingWidth
	private DoubleProperty wrappingWidth;
	public DoubleProperty wrappingWidthProperty() {
		if (wrappingWidth == null) {
			wrappingWidth = new SimpleDoubleProperty(0);
			textNode.wrappingWidthProperty().bind(wrappingWidth);
		}
		return wrappingWidth;
	}
	public void setWrappingWidth(double value) { wrappingWidthProperty().set(value); }
	public double getWrappingWidth() { return wrappingWidthProperty().get(); }

	public Texie() {
		this("");
	}

	public Texie(String value) {
		this(new Text(value));
	}

	public Texie(Text text) {
		super();
		textNode = text;
		textNode.setCache(true);
		textNode.setCacheHint(CacheHint.SPEED);
		rectNode = new Rectangle();
		group = new Group(rectNode, textNode);
		setupBindings();
	}

	public Texie(String text, Font font) {
		this(text);
		setFont(font);
	}

	public Texie(String text, Font font, Paint color) {
		this(text, font);
		setTextColor(color);
	}

	private void setupBindings() {
		//rectNode.layoutXProperty().bind(transformedXProperty());
		//rectNode.layoutYProperty().bind(transformedYProperty());
		rectNode.widthProperty().bind(widthProperty());
		rectNode.heightProperty().bind(heightProperty());
		group.layoutXProperty().bind(xProperty());
		group.layoutYProperty().bind(yProperty());
		//textNode.layoutXProperty().bind(transformedXProperty());
		//textNode.layoutYProperty().bind(transformedYProperty());
		textNode.setTextOrigin(VPos.TOP);
		group.opacityProperty().bind(alphaProperty());
		group.visibleProperty().bind(alphaProperty().greaterThan(0));
		textNode.textProperty().addListener((a,b,c) -> calculateSize());
		textNode.wrappingWidthProperty().addListener((a,b,c) -> calculateSize());
		rectNode.fillProperty().bind(Bindings.when(
				textNode.hoverProperty())
				.then((Paint) Color.rgb(255, 255, 255, 0.2))
				.otherwise(backgroundColorProperty()));
		calculateSize();
	}

	private void calculateSize() {
		Platform.runLater(() -> {
			setHeight(textNode.getBoundsInLocal().getHeight());
			setWidth(textNode.getBoundsInLocal().getWidth());
		});
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().addAll(group);
	}
	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().removeAll(group);
	}
	@Override
	public void clip(Node n) {
		rectNode.setClip(n);
	}
}
