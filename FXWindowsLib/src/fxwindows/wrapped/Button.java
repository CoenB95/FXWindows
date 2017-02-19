package fxwindows.wrapped;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class Button extends TextBase {

	private javafx.scene.control.Button button;
	
	private final ObjectProperty<Runnable> onAction = new SimpleObjectProperty<>();
	private StringBinding styleBinding;

	public Button() {
		this(new javafx.scene.control.Button());
	}
	
	public Button(String value) {
		this(new javafx.scene.control.Button());
		setText(value);
	}
	
	private Button(javafx.scene.control.Button iView) {
		super();
		button = iView;
		button.layoutXProperty().bind(xProperty());
		button.layoutYProperty().bind(yProperty());
		button.visibleProperty().bind(alphaProperty().greaterThan(0));
		contentWidthProperty().bind(button.widthProperty());
		contentHeightProperty().bind(button.heightProperty());
		button.setOnAction(e -> {
			if (onAction.get() != null) onAction.get().run();
		});
		button.textProperty().bind(textProperty());
		button.fontProperty().bind(fontProperty());
		button.wrapTextProperty().bind(wrapTextProperty());
		button.textFillProperty().bind(textColorProperty());
		styleBinding = Bindings.createStringBinding(() -> "-fx-base: " + getBackgroundColor().toString()
				.replace("0x", "#"), backgroundColorProperty());
		button.styleProperty().bind(styleBinding);
		setupTopLevelBindings(button);
	}
	
	public Runnable getOnAction() {
		return onAction.get();
	}
	
	public void setOnAction(Runnable value) {
		onAction.set(value);
	}
	
	@Override
	public Node getNode() {
		return button;
	}

	@Override
	public void clip(Node n) {
		button.setClip(n);
	}
}
