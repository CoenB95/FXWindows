package fxwindows.animation;


import fxwindows.core.Colorable;
import javafx.beans.value.ObservableValue;

import java.time.Duration;

public class FadeAnimation extends ValueAnimation {

	private Colorable animatedColorable;

	public FadeAnimation(Duration duration) {
		super(duration);
	}
	
	public FadeAnimation(Colorable drawable, Duration duration) {
		super(duration);
		animatedColorable = drawable;
	}

	public FadeAnimation(Colorable colorable, Duration duration, double from, double to) {
		this(colorable, duration);
		setFrom(from);
		setTo(to);
	}

	public FadeAnimation(Colorable colorable, Duration duration, ObservableValue<Number> from,
						 ObservableValue<Number> to) {
		this(colorable, duration);
		setFrom(from);
		setTo(to);
	}

	@Override
	public FadeAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public FadeAnimation then(Animation other, long millisDelay) {
		return (FadeAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {
		super.update(progress);
		animatedColorable.setAlpha(getValue());
	}
}