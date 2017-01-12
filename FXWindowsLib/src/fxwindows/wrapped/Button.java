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
		/*Bindings.createDoubleBinding(() -> {
			button.maxWidthProperty().unbind();
			button.maxHeightProperty().unbind();
			button.setMaxWidth(Region.USE_COMPUTED_SIZE);
			button.setMaxHeight(Region.USE_COMPUTED_SIZE);
			setContentHeight(button.prefHeight(1000));
			setContentWidth(button.prefWidth(1000));
			System.out.println("Button pref size: w=" + getContentWidth() +
					", h=" + getContentHeight());
			button.maxHeightProperty().bind(heightProperty().add(10));
			button.maxWidthProperty().bind(widthProperty().add(10));
			return 0.0;
		}, button.textProperty(), button.parentProperty());*/
		contentWidthProperty().bind(button.widthProperty());
		contentHeightProperty().bind(button.heightProperty());
//		button.widthProperty().addListener((v1,v2,v3) -> {
//			if (v3.doubleValue() >= getInnerWidth())
//				button.setMaxWidth(getInnerWidth());
//			else button.setMaxWidth(Region.USE_COMPUTED_SIZE);
//		});
//		button.heightProperty().addListener((v1,v2,v3) -> {
//			if (v3.doubleValue() >= getInnerHeight())
//				button.setMaxHeight(getInnerHeight());
//			else button.setMaxHeight(Region.USE_COMPUTED_SIZE);
//		});
		//button.prefHeightProperty().bind(heightProperty());
		//button.prefWidthProperty().bind(widthProperty());
		button.setOnAction(e -> {
			if (onAction.get() != null) onAction.get().run();
		});
		button.textProperty().bind(textProperty());
		button.fontProperty().bind(fontProperty());
		button.wrapTextProperty().bind(wrapTextProperty());
		button.textFillProperty().bind(textColorProperty());
		styleBinding = Bindings.createStringBinding(() -> {
			return "-fx-base: " + getBackgroundColor().toString()
					.replace("0x", "#");
		}, backgroundColorProperty());
		button.styleProperty().bind(styleBinding);
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
