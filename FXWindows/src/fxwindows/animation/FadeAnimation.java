package fxwindows.animation;

import java.time.Duration;

import fxwindows.drawable.Drawable;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.SimpleDoubleProperty;

public class FadeAnimation extends Animation {
	
	private DoubleExpression fromOpacity;
	private DoubleExpression toOpacity;
	
	public FadeAnimation(Duration duration) {
		super(duration);
	}
	
	public FadeAnimation(Drawable drawable, Duration duration) {
		super(drawable, duration);
		fromOpacity = new SimpleDoubleProperty();
		toOpacity = new SimpleDoubleProperty();
	}
	
	public FadeAnimation setTo(double newO) {
		toOpacity = new SimpleDoubleProperty(newO);
		return this;
	}
	
	public FadeAnimation setTo(DoubleExpression newO) {
		toOpacity = newO;
		return this;
	}
	
	public FadeAnimation setFrom(double newO) {
		fromOpacity = new SimpleDoubleProperty(newO);
		return this;
	}
	public FadeAnimation setFrom(DoubleExpression newO) {
		fromOpacity = newO;
		return this;
	}

	@Override
	public void update(double progress) {
		getDrawable().setAlpha(fromOpacity.get() + 
				(toOpacity.get() - fromOpacity.get()) * progress);
	}
}