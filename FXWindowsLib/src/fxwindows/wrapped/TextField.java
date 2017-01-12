package fxwindows.wrapped;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TextField extends TextBase {

	private javafx.scene.control.TextField field;
	private boolean initialized = false;
	
	private final StringProperty text = new SimpleStringProperty();
	private final StringProperty promptText = new SimpleStringProperty();
	private final ObjectProperty<Runnable> onAction = new SimpleObjectProperty<>();

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
		field.setOnAction(e -> {
			if (onAction.get() != null) onAction.get().run();
		});
		textProperty().bindBidirectional(field.textProperty());
		field.fontProperty().bind(fontProperty());
		promptTextProperty().bindBidirectional(field.promptTextProperty());
	}
	
	public StringProperty promptTextProperty() {
		return promptText;
	}
	
	public String getPromptText() {
		return promptTextProperty().get();
	}
	
	public void setPromptText(String value) {
		promptTextProperty().set(value);
	}
	
	public StringProperty textProperty() {
		return text;
	}
	
	public Runnable getOnAction() {
		return onAction.get();
	}
	
	public String getText() {
		return textProperty().get();
	}
	
	public void setOnAction(Runnable value) {
		onAction.set(value);
	}
	
	public void setText(String value) {
		textProperty().set(value);
	}
	
	@Override
	public Node getNode() {
		return field;
	}

	@Override
	public void clip(Node n) {
		field.setClip(n);
	}
}
