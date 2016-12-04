package fxwindows.wrapped;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TextField extends ShapeBase {

	private javafx.scene.control.TextField field;
	private boolean initialized = false;
	private final StringProperty text = new SimpleStringProperty();

	public TextField() {
		this(new javafx.scene.control.TextField());
	}
	
	public TextField(String value) {
		this(new javafx.scene.control.TextField());
		setText(value);
	}
	
	private TextField(javafx.scene.control.TextField iView) {
		super();
		field = iView;
		field.layoutXProperty().bind(xProperty());
		field.layoutYProperty().bind(yProperty());
		contentWidthProperty().bind(field.widthProperty());
		contentHeightProperty().bind(field.heightProperty());
		// Workaround: since the preferred size is not known till later,
		// we'll wait for it.
		field.widthProperty().addListener((v1,v2,v3) -> {
			if (!initialized) {
				initialized = true;
				field.maxWidthProperty().bind(widthProperty());
				field.maxHeightProperty().bind(heightProperty());
			}
		});
		textProperty().bindBidirectional(field.textProperty());
		Rectangle rect = new Rectangle(10,10);
		field.setShape(rect);
		rect.setFill(Color.RED);
	}
	
	public StringProperty textProperty() {
		return text;
	}
	
	public String getText() {
		return textProperty().get();
	}
	
	public void setText(String value) {
		textProperty().set(value);
	}
	
	@Override
	public void addToPane(Pane p) {
		p.getChildren().add(field);
	}

	@Override
	public void clip(Node n) {
		field.setClip(n);
	}

	@Override
	public void removeFromPane(Pane p) {
		p.getChildren().remove(field);
	}
}
