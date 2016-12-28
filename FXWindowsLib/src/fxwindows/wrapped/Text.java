package fxwindows.wrapped;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
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

public class Text extends ShapeBase {

	private Group group;
	private javafx.scene.text.Text textNode;
	private Rectangle rectNode;
	private boolean recalculate = true;
	private ObjectBinding<Paint> fillBinding;
	
	private final StringProperty text = new SimpleStringProperty();

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
	public StringProperty textProperty() {
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

	public Text() {
		this("");
	}

	public Text(String value) {
		this(new javafx.scene.text.Text());
		setText(value);
	}

	private Text(javafx.scene.text.Text text) {
		super();
		textNode = text;
		textNode.setCache(true);
		textNode.setCacheHint(CacheHint.SPEED);
		rectNode = new Rectangle();
		//textClip = new Rectangle();
		group = new Group(rectNode, textNode);
		setupBindings();
	}

	public Text(String text, Font font) {
		this(text);
		setFont(font);
	}

	public Text(String text, Font font, Paint color) {
		this(text, font);
		setTextColor(color);
	}

	private void setupBindings() {
		group.setOnMouseClicked((e) -> {
			for (Runnable r : getOnMouseClickedListeners()) r.run();
		});
		rectNode.widthProperty().bind(widthProperty());
		rectNode.heightProperty().bind(heightProperty());
		group.layoutXProperty().bind(xProperty());
		group.layoutYProperty().bind(yProperty());
		textNode.setTextOrigin(VPos.TOP);
		group.opacityProperty().bind(alphaProperty());
		group.visibleProperty().bind(alphaProperty().greaterThan(0));
		textNode.fontProperty().addListener((a,b,c) -> recalculate = true);
		textNode.textProperty().bind(textProperty());
		textNode.textProperty().addListener((a,b,c) -> recalculate = true);
		maxWidthProperty().addListener((a,b,c) -> recalculate = true);
		textNode.wrappingWidthProperty().addListener((a,b,c) -> recalculate = true);
		fillBinding = Bindings.createObjectBinding(() -> {
			if (group.isHover()) {
				Color c = (Color) getBackgroundColor();
				if (c.equals(Color.TRANSPARENT)) return Color.gray(1, 0.2);
				else return ((Color)getBackgroundColor()).brighter();
			}
			return getBackgroundColor();
		}, backgroundColorProperty(), group.hoverProperty());
		rectNode.fillProperty().bind(fillBinding);
		
		recalculate = true;
//		textClip.setLayoutX(0);
//		textClip.setLayoutY(0);
//		textClip.widthProperty().bind(widthProperty());
//		textClip.heightProperty().bind(heightProperty());
//		//textClip.setHeight(100);
//		textNode.setClip(textClip);
	}

	private void calculateSize() {
		textNode.setClip(null);
		setContentHeight(textNode.getBoundsInLocal().getHeight());
		setContentWidth(textNode.getBoundsInLocal().getWidth());
		Rectangle textClip = new Rectangle();
		textClip.setLayoutX(0);
		textClip.setLayoutY(0);
		textClip.setHeight(getHeight());
		textClip.setWidth(getWidth());
		//textClip.setHeight(100);
		textNode.setClip(textClip);
	}

	@Override
	public void update() {
		if (recalculate) {
			recalculate = false;
			calculateSize();
		}
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().addAll(group);
		recalculate = true;
	}
	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().removeAll(group);
	}
	@Override
	public void clip(Node n) {
		group.setClip(n);
	}
}
