package fxwindows.animation;


import java.time.Duration;

import fxwindows.core.ShapeBase;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;

public class MoveAnimation extends Animation {

	private DoubleExpression fromX;
	private DoubleExpression fromY;
	private DoubleExpression toX;
	private DoubleExpression toY;
	private ShapeBase drawable;

	public MoveAnimation(ShapeBase drawable, Duration duration) {
		this(drawable, duration, 0, 0, 0, 0);
	}

	public MoveAnimation(ShapeBase drawable, Duration duration, double fromX, double fromY, double toX, double toY) {
		super(duration);
		this.drawable = drawable;
		this.fromX = new SimpleDoubleProperty(fromX);
		this.fromY = new SimpleDoubleProperty(fromY);
		this.toX = new SimpleDoubleProperty(toX);
		this.toY = new SimpleDoubleProperty(toY);
	}

	public MoveAnimation(ShapeBase drawable, Duration duration, DoubleExpression fromX, DoubleExpression fromY,
						 DoubleExpression toX, DoubleExpression toY) {
		super(duration);
		this.drawable = drawable;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}

	public MoveAnimation setShapeBase(ShapeBase value) {
		drawable = value;
		return this;
	}

	public MoveAnimation setTo(double newX, double newY) {
		setToX(newX);
		setToY(newY);
		return this;
	}

	public MoveAnimation setTo(DoubleExpression newX, DoubleExpression newY) {
		setToX(newX);
		setToY(newY);
		return this;
	}

	public MoveAnimation setToX(double newX) {
		toX = new SimpleDoubleProperty(newX);
		return this;
	}

	public MoveAnimation setToX(DoubleExpression newX) {
		toX = newX;
		return this;
	}

	public MoveAnimation setToY(double newY) {
		toY = new SimpleDoubleProperty(newY);
		return this;
	}

	public MoveAnimation setToY(DoubleExpression newY) {
		toY = newY;
		return this;
	}

	public MoveAnimation setFrom(double newX, double newY) {
		setFromX(newX);
		setFromY(newY);
		return this;
	}

	public MoveAnimation setFrom(DoubleExpression newX, DoubleExpression newY) {
		setFromX(newX);
		setFromY(newY);
		return this;
	}

	public MoveAnimation setFromX(double newX) {
		fromX = new SimpleDoubleProperty(newX);
		return this;
	}

	public MoveAnimation setFromX(DoubleExpression newX) {
		fromX = newX;
		return this;
	}

	public MoveAnimation setFromY(double newY) {
		fromY = new SimpleDoubleProperty(newY);
		return this;
	}

	public MoveAnimation setFromY(DoubleExpression newY) {
		fromY = newY;
		return this;
	}

	@Override
	public MoveAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public MoveAnimation then(Animation other, long millisDelay) {
		return (MoveAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {
		drawable.setTransformX(fromX.get() + (toX.get() -
				fromX.get())*progress);
		drawable.setTransformY(fromY.get() + (toY.get() -
				fromY.get())*progress);
	}
}