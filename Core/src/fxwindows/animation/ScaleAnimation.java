package fxwindows.animation;


import fxwindows.core.ShapeBase;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;

import java.time.Duration;

public class ScaleAnimation extends Animation {

	private DoubleExpression fromX;
	private DoubleExpression fromY;
	private DoubleExpression toX;
	private DoubleExpression toY;
	private ShapeBase drawable;

	public ScaleAnimation(ShapeBase drawable, Duration duration) {
		this(drawable, duration, 1, 1, 1, 1);
	}

	public ScaleAnimation(ShapeBase drawable, Duration duration, double fromX, double fromY, double toX, double toY) {
		super(duration);
		this.drawable = drawable;
		this.fromX = new SimpleDoubleProperty(fromX);
		this.fromY = new SimpleDoubleProperty(fromY);
		this.toX = new SimpleDoubleProperty(toX);
		this.toY = new SimpleDoubleProperty(toY);
	}

	public ScaleAnimation(ShapeBase drawable, Duration duration, DoubleExpression fromX, DoubleExpression fromY,
						  DoubleExpression toX, DoubleExpression toY) {
		super(duration);
		this.drawable = drawable;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public ScaleAnimation setShapeBase(ShapeBase value) {
		drawable = value;
		return this;
	}

	public ScaleAnimation setTo(double newX, double newY) {
		setToX(newX);
		setToY(newY);
		return this;
	}

	public ScaleAnimation setTo(DoubleExpression newX, DoubleExpression newY) {
		setToX(newX);
		setToY(newY);
		return this;
	}

	public ScaleAnimation setToX(double newX) {
		toX = new SimpleDoubleProperty(newX);
		return this;
	}

	public ScaleAnimation setToX(DoubleExpression newX) {
		toX = newX;
		return this;
	}

	public ScaleAnimation setToY(double newY) {
		toY = new SimpleDoubleProperty(newY);
		return this;
	}

	public ScaleAnimation setToY(DoubleExpression newY) {
		toY = newY;
		return this;
	}

	public ScaleAnimation setFrom(double newX, double newY) {
		setFromX(newX);
		setFromY(newY);
		return this;
	}

	public ScaleAnimation setFrom(DoubleExpression newX, DoubleExpression newY) {
		setFromX(newX);
		setFromY(newY);
		return this;
	}

	public ScaleAnimation setFromX(double newX) {
		fromX = new SimpleDoubleProperty(newX);
		return this;
	}

	public ScaleAnimation setFromX(DoubleExpression newX) {
		fromX = newX;
		return this;
	}

	public ScaleAnimation setFromY(double newY) {
		fromY = new SimpleDoubleProperty(newY);
		return this;
	}

	public ScaleAnimation setFromY(DoubleExpression newY) {
		fromY = newY;
		return this;
	}

	@Override
	public ScaleAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public ScaleAnimation then(Animation other, long millisDelay) {
		return (ScaleAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {
		drawable.setScaleX(fromX.get() + (toX.get() - fromX.get())*progress);
		drawable.setScaleY(fromY.get() + (toY.get() - fromY.get())*progress);
	}
}