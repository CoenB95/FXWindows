package fxwindows.view;

import fxwindows.wrapped.Arc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

/**
 * @author Coen Boelhouwers
 */
public class ProgressCircle extends Arc {

	private static final double INC_1 = 3;
	private static final double INC_2 = 5;

	private final DoubleProperty progress = new SimpleDoubleProperty();

	private double angle;
	private double length;
	private boolean invert;

	public ProgressCircle(double size, Color borderColor, double borderWidth, double padding) {
		((javafx.scene.shape.Arc) getNode()).setStrokeLineCap(StrokeLineCap.ROUND);
		setRadiusXY(size/2, size/2);
		setPadding(padding);
		setBorderColor(borderColor);
		setBorderWidth(borderWidth);
		setAngleLength(90);
	}

	public static ProgressCircle small(Color color) {
		return new ProgressCircle(15, color, 2, 5);
	}

	public static ProgressCircle medium(Color color) {
		return new ProgressCircle(30, color, 2, 10);
	}

	public double getProgress() {
		return progress.get();
	}

	public DoubleProperty progressProperty() {
		return progress;
	}

	public void setProgress(double value) {
		progress.set(value);
	}

	@Override
	public void update() {
		super.update();
		angle = (angle + (invert ? INC_1 : INC_1 + INC_2)) % 360;
		length = (length + (invert ? INC_2 : -INC_2)) % 360;
		if (length < 20) {
			length = 20;
			invert = !invert;
		}
		if (length > 270) {
			length = 270;
			invert = !invert;
		}
		setStartAngle(angle);
		setAngleLength(length);
	}
}
