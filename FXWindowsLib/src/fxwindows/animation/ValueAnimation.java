package fxwindows.animation;


import java.time.Duration;

import fxwindows.core.Animatable;
import fxwindows.core.AnimatedColorable;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ValueAnimation extends Animation {
	
	private DoubleExpression fromValue;
	private DoubleExpression toValue;
	private final DoubleProperty value = new SimpleDoubleProperty();
	
	public ValueAnimation(Duration duration) {
		super(duration);
	}
	
	public ValueAnimation(Animatable drawable, Duration duration) {
		super(drawable, duration);
		fromValue = new SimpleDoubleProperty();
		toValue = new SimpleDoubleProperty();
	}
	
	public double getTo() {
		return toValue.get();
	}
	
	public ValueAnimation setTo(double newO) {
		toValue = new SimpleDoubleProperty(newO);
		return this;
	}
	
	public ValueAnimation setTo(DoubleExpression newO) {
		toValue = newO;
		return this;
	}
	
	public ValueAnimation setFrom(double newO) {
		fromValue = new SimpleDoubleProperty(newO);
		value.set(fromValue.get());
		return this;
	}
	public ValueAnimation setFrom(DoubleExpression newO) {
		fromValue = newO;
		value.set(fromValue.get());
		return this;
	}
	public DoubleProperty valueProperty() {
		return value;
	}
	public double getValue() {
		return value.get();
	}

	@Override
	public void update(double progress) {
		value.set(fromValue.get() + 
				(toValue.get() - fromValue.get()) * progress);
	}
}