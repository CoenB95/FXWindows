package fxwindows.animation;

import java.time.Duration;

public class NoopAnimation extends Animation {

	private NoopAnimation(Duration duration) {
		super(duration);
	}

	@Override
	public NoopAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public NoopAnimation then(Animation other, long millisDelay) {
		return (NoopAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {

	}
}