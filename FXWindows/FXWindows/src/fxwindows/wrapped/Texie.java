package fxwindows.wrapped;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Texie extends WrappedRectangle {

	private Text textNode;
	
	public Texie() {
		this("");
	}
	
	public Texie(String value) {
		this(value, new Text());
	}
	
	public Texie(String value, Text text) {
		super();
		textNode = text;
		setupBindings(value);
	}
	
	public Texie(String text, Font font) {
		this(text);
		textNode.setFont(font);
		textNode.setTextOrigin(VPos.TOP);
	}
	
	public Texie(String text, Font font, Paint color) {
		this(text, font);
		textNode.setFill(color);
		textNode.setTextOrigin(VPos.TOP);
	}
	
	private void setupBindings(String value) {
		textNode.layoutXProperty().bind(baseXProperty());
		textNode.layoutYProperty().bind(baseYProperty());
		textNode.translateXProperty().bind(transformXProperty());
		textNode.translateYProperty().bind(transformYProperty());
//		textNode.setTextOrigin(VPos.TOP);
		textNode.textProperty().addListener((a,b,c) -> {
			Platform.runLater(() -> {
				setCalculatedHeight(textNode.getBoundsInLocal().getHeight());
				setCalculatedWidth(textNode.getBoundsInLocal().getWidth());
			});
		});
		((Rectangle) getNode()).fillProperty().bind(Bindings.when(
				textNode.hoverProperty())
				.then(Color.rgb(255, 255, 255, 0.1)).otherwise(Color.TRANSPARENT));
		textNode.setText(value);
	}
	
	@Override
	public void addToStage(Pane p) {
		super.addToStage(p);
		p.getChildren().add(textNode);
	}
}
