package fxwindows.animation;


import java.time.Duration;

import fxwindows.wrapped.WrappedNode;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;

public class MoveAnimation extends Animation {
	private DoubleExpression fromX;
	private DoubleExpression fromY;
	private DoubleExpression toX;
	private DoubleExpression toY;
	public MoveAnimation(Duration duration) {
		super(duration);
	}
	public MoveAnimation(WrappedNode drawable, Duration duration) {
		super(drawable, duration);
		fromX = new SimpleDoubleProperty();
		fromY = new SimpleDoubleProperty();
		toX = new SimpleDoubleProperty();
		toY = new SimpleDoubleProperty();
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
	public void update(double progress) {
		((WrappedNode)getDrawable()).setTransformX(fromX.get() + (toX.get() - 
				fromX.get())*progress);
		((WrappedNode)getDrawable()).setTransformY(fromY.get() + (toY.get() -
				fromY.get())*progress);
	}
}