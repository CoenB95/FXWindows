package fxwindows.animation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SequentialAnimation extends Animation {

	private List<Animation> animations;

	public SequentialAnimation(Animation... anims) {
		super(Duration.ofMillis(0));
		animations = new ArrayList<>();
		for (Animation a : anims) add(a);
	}

	public SequentialAnimation add(Animation a) {
		animations.add(a);
		setDuration(getDuration().plus(a.getDuration()));
		return this;
	}

	@Override
	public void jumpTo(double newProgress) {
		super.jumpTo(newProgress);
		jump(newProgress);
	}

	@Override
	public void pause(boolean value, long millisTimeout) {
		super.pause(value, millisTimeout);
		for (Animation a : animations) a.pause(value, millisTimeout);
	}

	@Override
	public void startAt(long milisDelay) {
		super.startAt(milisDelay);
		long delay = 0;
		for (Animation a : animations) {
			a.startAt(milisDelay + delay);
			delay += a.getDuration().toMillis();
		}
	}

	@Override
	public void stop() {
		super.stop();
		for (Animation a : animations) a.stop();
	}

	@Override
	public SequentialAnimation then(Animation other) {
		return then(other, 0);
	}

	@Override
	public SequentialAnimation then(Animation other, long millisDelay) {
		return (SequentialAnimation) super.then(other, millisDelay);
	}

	@Override
	public void update(double progress) {

	}

	/*@Override
	public void update(double progress) {*/
	private void jump(double progress) {
		long time = (long) (progress * getDuration().toMillis());
		long prev = 0;
		long cur = 0;
		for (Animation a : animations) {
			cur += a.getDuration().toMillis();
			if (cur > time && prev < time) {
				a.update((time - prev) / (double) a.getDuration().toMillis());
			} else if (cur < time) a.update(1.0);
			else if (prev > time) a.update(0.0);
			prev = cur;
		}
	}
}