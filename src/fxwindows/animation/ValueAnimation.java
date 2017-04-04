package fxwindows.animation;


import javafx.animation.Interpolator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

import java.time.Duration;

public class ValueAnimation extends Animation {

	private Property<Number> listener;
	private final DoubleProperty fromValue = new SimpleDoubleProperty();
	private final DoubleProperty toValue = new SimpleDoubleProperty();
	private final DoubleProperty value = new SimpleDoubleProperty();
	
	public ValueAnimation(Duration duration) {
		super(duration);
	}

	public ValueAnimation(Property<Number> listener, Duration duration) {
		super(duration);
		this.listener = listener;
	}
	
	public double getTo() {
		return toValue.get();
	}
	
	public ValueAnimation setTo(double newO) {
		toValue.set(newO);
		return this;
	}
	
	public ValueAnimation setTo(ObservableValue<? extends Number> newO) {
		toValue.bind(newO);
		return this;
	}
	
	public ValueAnimation setFrom(double newO) {
		fromValue.set(newO);
		value.set(fromValue.get());
		return this;
	}

	public ValueAnimation setFrom(ObservableValue<? extends Number> newO) {
		fromValue.bind(newO);
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
	public ValueAnimation setInterpolator(Interpolator value) {
		return (ValueAnimation) super.setInterpolator(value);
	}

	@Override
	public ValueAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public ValueAnimation then(Animation other, long millisDelay) {
		return (ValueAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {
		value.set(fromValue.get() + 
				(toValue.get() - fromValue.get()) * progress);
		if (listener != null) listener.setValue(value.get());
	}
}