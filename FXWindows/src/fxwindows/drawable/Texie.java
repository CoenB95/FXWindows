package fxwindows.drawable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Texie extends Drawable {
	private Text logic;
	private Image text_image;
	private boolean snapshot_requested = true;
	private boolean use_snapshot = true;


	// Font
	private ObjectProperty<Font> font;
	public ObjectProperty<Font> fontProperty() {
		if (font == null) font = new SimpleObjectProperty<Font>(Font.getDefault());
		return font;
	}
	public void setFont(Font value) { fontProperty().set(value); }
	public Font getFont() { return fontProperty().get(); }


	// TextColor
	private ObjectProperty<Paint> textColor;
	public ObjectProperty<Paint> textColorProperty() {
		if (textColor == null) {
			textColor = new SimpleObjectProperty<Paint>(Color.BLACK);
			textColor.addListener((a,b,c) -> { snapshot_requested = true; });
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
			text.addListener((a,b,c) -> { snapshot_requested = true; });
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
			wrappingWidth.addListener((a,b,c) -> {
				snapshot_requested = true;
			});
		}
		return wrappingWidth;
	}
	public void setWrappingWidth(double value) { wrappingWidthProperty().set(value); }
	public double getWrappingWidth() { return wrappingWidthProperty().get(); }

	// Constructor
	public Texie() {
		super();
		setupText();
		backgroundColorProperty().addListener((a,b,c) -> {
			snapshot_requested = true;
		});
	}

	public Texie(String value) {
		this();
		setText(value);
	}

	private void setupText() {
		logic = new Text();
		logic.wrappingWidthProperty().bind(wrappingWidthProperty());
		logic.textProperty().bind(textProperty());
		logic.fontProperty().bind(fontProperty());
	}

	@Override
	public void click() {
		super.click();
		System.out.println("Texie \""+logic.getText()+"\" clicked.");
	}

	@Override
	public void draw(GraphicsContext g) {
		if (snapshot_requested) {
			System.out.println("Snapshot");
			snapshot_requested = false;
			SnapshotParameters p = new SnapshotParameters();
			p.setFill(getBackgroundColor());
			logic.setOpacity(1);
			logic.setFill(getTextColor());
			text_image = logic.snapshot(p, null);
			logic.setOpacity(0);
			setCalculatedHeight(logic.getBoundsInLocal().getHeight());
			setCalculatedWidth(logic.getBoundsInLocal().getWidth());
		}
		g.setGlobalAlpha(getAlpha());
		g.setStroke(getBorderColor());
		if (!use_snapshot) {
			g.setFont(getFont());
			g.setFill(getTextColor());
			g.fillText(getText(), getX(), getY());
		} else {
			//	g.setFill(getBackgroundColor());
			//	g.strokeRect(getX(), getY(), getWidth(), getHeight());
			//	g.fillRect(getX(), getY(), getWidth(), getHeight());
			g.drawImage(text_image, getX(), getY());
		}
		if (isHovered()) { 
			g.setGlobalAlpha(0.2);
			g.setFill(Color.WHITE);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
		}
		g.setGlobalAlpha(1);
	}

	@Override
	public String toString() {
		return "Texie[text="+logic.getText()+"]";
	}
}