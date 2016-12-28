package fxwindows.animation;


import fxwindows.core.Colorable;

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

	@Override
	public void update(double progress) {
		super.update(progress);
		animatedColorable.setAlpha(getValue());
	}
}