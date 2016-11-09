package fxwindows;

import javafx.animation.TranslateTransition;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class AnimUtil {

	public static final void move(Shape shape, double x, double y, 
			double millis) {
		TranslateTransition t = 
				new TranslateTransition(Duration.millis(millis), shape);
		t.setFromX(shape.getLayoutX());
		t.setFromY(shape.getLayoutY());
		t.setToX(x);
		t.setToY(y);
		t.playFromStart();
	}
}
