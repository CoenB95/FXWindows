package fxwindows.animation;


import java.time.Duration;

import fxwindows.core.AnimatedColorable;

public class FadeAnimation extends ValueAnimation {
	
	public FadeAnimation(Duration duration) {
		super(duration);
	}
	
	public FadeAnimation(AnimatedColorable drawable, Duration duration) {
		super(drawable, duration);
	}

	@Override
	public void update(double progress) {
		super.update(progress);
		((AnimatedColorable)getDrawable()).setAlpha(getValue());
	}
}